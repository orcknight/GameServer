package com.tian.server.bll;


import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.dao.*;
import com.tian.server.entity.*;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.model.PlayerLocation;
import com.tian.server.model.RoomObjects;
import com.tian.server.util.CharUtil;
import com.tian.server.util.UserCacheUtil;
import com.tian.server.util.ZjMudUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/6/9.
 */
public class UserBll extends BaseBll {

    private UserDao userDao = new UserDao();
    private PlayerDao playerDao = new PlayerDao();
    private PlayerInfoDao playerInfoDao = new PlayerInfoDao();

    public UserBll(SocketIOClient socketIOClient) {
        super(socketIOClient);
    }

    public void login(String name, String password){

        UserEntity user =  userDao.getByNameAndPassword(name, password);
        if(user.getId() < 1){
            sendEmpty();
            return;
        }

        //检查用户是否已经登陆
        Map<Integer, Living> cache = UserCacheUtil.getPlayers();
        //如果已经登陆了，先踢用户下线，并重新设置用户信息
        if(cache.containsKey(user.getId())){

            //发送断开连接信息,并且断开连接，并重新缓存用户数据
            Player player = (Player)cache.get(user.getId());
            sendKickOffMsg(player.getSocketClient());
            player.getSocketClient().disconnect();
            cache.remove(user.getId());
        }

        //存储用户信息
        setSocketCache(user.getId());
        Player playerCache = new Player();
        playerCache.setUser(user);
        playerCache.setSocketClient(socketIOClient);
        cache.put(user.getId(), playerCache);

        PlayerEntity player = playerDao.getById(user.getId());
        if(player.getId() < 1){

            //如果没有角色，转到角色创建窗口。
            sendMsg(ZjMudUtil.getEmptyLine() + ZjMudUtil.getCreatePlayerLine());
            return;
        }
        //缓存玩家信息
        playerCache.initInfo(player);
        initPlayerAttr(playerCache);
        initPlayerFamily(playerCache);

        //获取玩家辅助信息并缓存
        PlayerInfoEntity playerInfo = playerInfoDao.getByPlayerId(player.getId());
        playerCache.setPlayerInfo(playerInfo);

        //载入玩家技能
        loadUserSkill(playerCache);
        UserCacheUtil.getAllObjects().put(playerCache.getUuid(), playerCache);

        //缓存玩家信息
        Map<String, RoomEntity> roomMap = UserCacheUtil.getAllMaps();
        playerCache.setLocation(roomMap.get(playerInfo.getRoomName()));
        socketIOClient.getNamespace().getRoomOperations(playerInfo.getRoomName()).sendEvent("stream", ZjMudUtil.getLoginBoradcastLine(player));
        socketIOClient.joinRoom(playerInfo.getRoomName());

        //显示登陆信息以及房间信息
        sendMsg(ZjMudUtil.getEmptyLine() +
                ZjMudUtil.getMainMenuLine() +
                ZjMudUtil.getLoginSuccessLine() +
                ZjMudUtil.getScreenLine("目前权限：(player)") +
                "你连线进入了金庸立志传[立志传一线]。\r\n" +
                ZjMudUtil.getLocationLine(getLocation(playerInfo.getRoomName())));

        //获取房间物品等信息
        Map<String, RoomObjects> roomObjectsMap = UserCacheUtil.getRoomObjectsCache();
        //获取当前房间的物品
        RoomObjects roomObjects = roomObjectsMap.get(playerInfo.getRoomName());
        //检查自己是否在房间内，如果不在添加进去
        if(roomObjects == null) {

            roomObjects = new RoomObjects();
            roomObjects.setPlayers(new ArrayList<Player>());
        }

        if(!roomObjects.getPlayers().contains(playerCache)){

            roomObjects.getPlayers().add(playerCache);
            roomObjectsMap.put(playerInfo.getRoomName(), roomObjects);
        }

        String msg = ZjMudUtil.getObjectsLine(roomObjects, playerCache);
        sendMsg(msg);
    }

    public void createRole(String name, String sex){

        //先检查用户登陆没有
        if(this.userId < 1){

            sendMsg(ZjMudUtil.getEmptyLine() +
                    ZjMudUtil.getScreenLine("您还没有登陆，请先登陆。"));
            return;
        }

        //检查名字是否全部是中文
        if(!CharUtil.isChinese(name)){

            sendMsg(ZjMudUtil.getEmptyLine() +
                    ZjMudUtil.getScreenLine("对不起，请您用「中文」取名字(2-6个字)。"));
            return;
        }

        //检查名字的长度
        if(name.length() < 2 || name.length() > 6){

            sendMsg(ZjMudUtil.getEmptyLine() +
                    ZjMudUtil.getScreenLine("对不起，你的中文姓名不能太长或太短(2-6个字)。"));
            return;
        }

        //创建角色
        PlayerEntity player = new PlayerEntity();
        player.setName(name);
        player.setSex(sex);
        player.setUserId(this.userId);
        playerDao.add(player);

        PlayerInfoEntity playerInfo = new PlayerInfoEntity();
        playerInfo.setCityName("register");
        playerInfo.setRoomName("register/shengmingzhigu");
        playerInfo.setPlayerId(player.getId());
        playerInfoDao.add(playerInfo);
        socketIOClient.getNamespace().getRoomOperations(playerInfo.getRoomName()).sendEvent("stream", ZjMudUtil.getLoginBoradcastLine(player));
        socketIOClient.joinRoom(playerInfo.getRoomName()); //玩家加入当前房间群组，为广播消息

        //存储用户的角色辅助信息
        Map<Integer, Living> playerCacheMap = UserCacheUtil.getPlayers();
        if(playerCacheMap.containsKey(this.userId)){

            Player playerCache = (Player)playerCacheMap.get(this.userId);
            playerCache.setPlayerInfo(playerInfo);

            //缓存玩家信息
            Map<String, RoomEntity> roomMap = UserCacheUtil.getAllMaps();
            playerCache.setLocation(roomMap.get(playerInfo.getRoomName()));
        }

        //先发送刷新界面命令
        sendMsg(ZjMudUtil.getMainMenuLine());
        //发送登陆信息
        sendMsg(ZjMudUtil.getEmptyLine() +
                ZjMudUtil.getScreenLine("时间过得真快，不知不觉你已经十四岁了，今年的运气不知道怎么样。") +
                ZjMudUtil.getScreenLine("───────────────────────────────") +
                ZjMudUtil.getScreenLine("你可以进入不同的方向选择品质和先天属性，然后就投胎做人了。") +
                ZjMudUtil.getScreenLine("───────────────────────────────") +
                ZjMudUtil.getScreenLine("你连线进入了金庸立志传[立志传一线]。") +
                ZjMudUtil.getLocationLine(getLocation(playerInfo.getRoomName())));
    }

    public void logout(){

        //如果已经登陆了，发送下线消息然后
        Map<Integer, Living> playerCacheMap = UserCacheUtil.getPlayers();
        Player player = (Player)playerCacheMap.get(this.userId);

        if(player != null) {

            //广播玩家离开的信息
            socketIOClient.getNamespace().getRoomOperations(player.getPlayerInfo().getRoomName()).sendEvent("stream",
                    ZjMudUtil.getLogoutBoradcastLine(player));

            //清理缓存数据
            UserCacheUtil.getPlayerSockets().remove(socketIOClient);
            playerCacheMap.remove(this.userId);
            UserCacheUtil.delPlayerFromRoom(player.getPlayerInfo().getRoomName(), player);
        }

        //关闭连接
        socketIOClient.disconnect();
    }

    private void sendKickOffMsg(SocketIOClient socketIOClient){

        String msg = ZjMudUtil.getEmptyLine() +
                ZjMudUtil.getScreenLine("你的账号在别处登录，你被迫下线了！") +
                ZjMudUtil.getScreenLine("与服务器断开连接。");

        socketIOClient.sendEvent("stream", msg);
    }

    private void setSocketCache(Integer userId){

        Map<SocketIOClient, Integer> socketCache = UserCacheUtil.getPlayerSockets();
        socketCache.put(this.socketIOClient, userId);
    }

    private void sendEmpty(){

        socketIOClient.sendEvent("stream", "");
    }

    private PlayerLocation getLocation(String roomName){

        PlayerLocation playerLocation = new PlayerLocation();

        Map<String, RoomEntity> roomMap = UserCacheUtil.getAllMaps();

        RoomEntity location = roomMap.get(roomName);
        playerLocation.setLocation(location);

        if(location.getNorth().length() > 0){

            RoomEntity north = roomMap.get(location.getNorth());
            if(north != null){

                playerLocation.setNorth(north);
            }
        }

        if(location.getSouth().length() > 0){

            RoomEntity south = roomMap.get(location.getSouth());
            if(south != null){

                playerLocation.setSouth(south);
            }
        }

        if(location.getEast().length() > 0){

            RoomEntity east = roomMap.get(location.getEast());
            if(east != null){

                playerLocation.setEast(east);
            }
        }

        if(location.getWest().length() > 0){

            RoomEntity west = roomMap.get(location.getWest());
            if(west != null){

                playerLocation.setWest(west);
            }
        }

        if(location.getNortheast().length() > 0){

            RoomEntity northEast = roomMap.get(location.getNortheast());
            if(northEast != null){

                playerLocation.setNorthEast(northEast);
            }
        }

        if(location.getNorthwest().length() > 0){

            RoomEntity northWest = roomMap.get(location.getNorthwest());
            if(northWest != null){

                playerLocation.setNorthWest(northWest);
            }
        }

        if(location.getSoutheast().length() > 0){

            RoomEntity southEast = roomMap.get(location.getSoutheast());
            if(southEast != null){

                playerLocation.setSouthEast(southEast);
            }
        }

        if(location.getSouthwest().length() > 0){

            RoomEntity southWest = roomMap.get(location.getSouthwest());
            if(southWest != null){

                playerLocation.setSouthWest(southWest);
            }
        }

        if(location.getEnter().length() > 0){

            RoomEntity in = roomMap.get(location.getEnter());
            if(in != null){

                playerLocation.setEnter(in);
            }
        }

        if(location.getOut().length() > 0){

            RoomEntity out = roomMap.get(location.getOut());
            if(out != null){

                playerLocation.setOut(out);
            }
        }

        return playerLocation;
    }

    private void broadcastLogin(){


    }

    private void loadUserSkill(Player player){

        PlayerSkillDao playerSkillDao = new PlayerSkillDao();
        List<PlayerSkillEntity> playerSkillEntitiesList = playerSkillDao.getListByPlayerId(player.getPlayerId());

        player.initSkills(playerSkillEntitiesList);
    }

    private void initPlayerAttr(Player player){

        PlayerAttrDao dao = new PlayerAttrDao();
        List<PlayerAttrEntity> attrs = dao.getListByPlayerId(player.getPlayerId());

        for(PlayerAttrEntity attr : attrs){

            if(attr.getType().equals("brother")){

                player.set("brothers/" + attr.getValue(), attr.getValue());
            }else if(attr.getType().equals("league")){

                player.set("league/leagueName", attr.getValue());
            }
        }
    }

    private void initPlayerFamily(Player player){

        PlayerFamilyDao dao = new PlayerFamilyDao();
        PlayerFamilyEntity family = dao.getByPlayerId(player.getPlayerId());
        if(family == null){
            return;
        }

        player.setFamily(family);
    }
}
