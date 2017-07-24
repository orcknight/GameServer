package com.tian.server.service;


import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.dao.PlayerDao;
import com.tian.server.dao.PlayerInfoDao;
import com.tian.server.dao.PlayerSkillDao;
import com.tian.server.dao.UserDao;
import com.tian.server.entity.*;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.model.PlayerLocation;
import com.tian.server.model.RoomObjects;
import com.tian.server.util.CharUtil;
import com.tian.server.util.CmdUtil;
import com.tian.server.util.UserCacheUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/6/9.
 */
public class UserService extends  BaseService{

    private UserDao userDao = new UserDao();
    private PlayerDao playerDao = new PlayerDao();
    private PlayerInfoDao playerInfoDao = new PlayerInfoDao();

    public UserService(SocketIOClient socketIOClient) {
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
            sendMsg(CmdUtil.getEmptyLine() + CmdUtil.getCreatePlayerLine());
            return;
        }
        //缓存玩家信息
        playerCache.setPlayer(player);

        //获取玩家辅助信息并缓存
        PlayerInfoEntity playerInfo = playerInfoDao.getByPlayerId(player.getId());
        playerCache.setPlayerInfo(playerInfo);

        //载入玩家技能
        loadUserSkill(playerCache);

        //缓存玩家信息
        Map<String, RoomEntity> roomMap = UserCacheUtil.getMapCache();
        playerCache.setLocation(roomMap.get(playerInfo.getRoomName()));
        socketIOClient.getNamespace().getRoomOperations(playerInfo.getRoomName()).sendEvent("stream", CmdUtil.getLoginBoradcastLine(player));
        socketIOClient.joinRoom(playerInfo.getRoomName());

        //显示登陆信息以及房间信息
        sendMsg(CmdUtil.getEmptyLine() +
                CmdUtil.getMainMenuLine() +
                CmdUtil.getLoginSuccessLine() +
                CmdUtil.getScreenLine("目前权限：(player)") +
                "你连线进入了金庸立志传[立志传一线]。\r\n" +
                CmdUtil.getLocationLine(getLocation(playerInfo.getRoomName())));

        //获取房间物品等信息
        Map<String, RoomObjects> roomObjectsMap = UserCacheUtil.getRoomObjectsCache();
        //获取当前房间的物品
        RoomObjects roomObjects = roomObjectsMap.get(playerInfo.getRoomName());
        //检查自己是否在房间内，如果不在添加进去
        if(roomObjects == null) {

            roomObjects = new RoomObjects();
            roomObjects.setPlayers(new ArrayList<PlayerEntity>());
        }

        if(!roomObjects.getPlayers().contains(player)){

            roomObjects.getPlayers().add(player);
            roomObjectsMap.put(playerInfo.getRoomName(), roomObjects);
        }

        String msg = CmdUtil.getObjectsLine(roomObjects, player);
        sendMsg(msg);
    }

    public void createRole(String name, String sex){

        //先检查用户登陆没有
        if(this.userId < 1){

            sendMsg(CmdUtil.getEmptyLine() +
                    CmdUtil.getScreenLine("您还没有登陆，请先登陆。"));
            return;
        }

        //检查名字是否全部是中文
        if(!CharUtil.isChinese(name)){

            sendMsg(CmdUtil.getEmptyLine() +
                    CmdUtil.getScreenLine("对不起，请您用「中文」取名字(2-6个字)。"));
            return;
        }

        //检查名字的长度
        if(name.length() < 2 || name.length() > 6){

            sendMsg(CmdUtil.getEmptyLine() +
                    CmdUtil.getScreenLine("对不起，你的中文姓名不能太长或太短(2-6个字)。"));
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
        socketIOClient.getNamespace().getRoomOperations(playerInfo.getRoomName()).sendEvent("stream", CmdUtil.getLoginBoradcastLine(player));
        socketIOClient.joinRoom(playerInfo.getRoomName()); //玩家加入当前房间群组，为广播消息

        //存储用户的角色辅助信息
        Map<Integer, Living> playerCacheMap = UserCacheUtil.getPlayers();
        if(playerCacheMap.containsKey(this.userId)){

            Player playerCache = (Player)playerCacheMap.get(this.userId);
            playerCache.setPlayerInfo(playerInfo);

            //缓存玩家信息
            Map<String, RoomEntity> roomMap = UserCacheUtil.getMapCache();
            playerCache.setLocation(roomMap.get(playerInfo.getRoomName()));
        }

        //先发送刷新界面命令
        sendMsg(CmdUtil.getMainMenuLine());
        //发送登陆信息
        sendMsg(CmdUtil.getEmptyLine() +
                CmdUtil.getScreenLine("时间过得真快，不知不觉你已经十四岁了，今年的运气不知道怎么样。") +
                CmdUtil.getScreenLine("───────────────────────────────") +
                CmdUtil.getScreenLine("你可以进入不同的方向选择品质和先天属性，然后就投胎做人了。") +
                CmdUtil.getScreenLine("───────────────────────────────") +
                CmdUtil.getScreenLine("你连线进入了金庸立志传[立志传一线]。") +
                CmdUtil.getLocationLine(getLocation(playerInfo.getRoomName())));
    }

    public void logout(){

        //如果已经登陆了，发送下线消息然后
        Map<Integer, Living> playerCacheMap = UserCacheUtil.getPlayers();
        Player player = (Player)playerCacheMap.get(this.userId);

        if(player != null) {

            //广播玩家离开的信息
            socketIOClient.getNamespace().getRoomOperations(player.getPlayerInfo().getRoomName()).sendEvent("stream",
                    CmdUtil.getLogoutBoradcastLine(player.getPlayer()));

            //清理缓存数据
            UserCacheUtil.getPlayerSockets().remove(socketIOClient);
            playerCacheMap.remove(this.userId);
            UserCacheUtil.delPlayerFromRoom(player.getPlayerInfo().getRoomName(), player.getPlayer());
        }

        //关闭连接
        socketIOClient.disconnect();
    }

    private void sendKickOffMsg(SocketIOClient socketIOClient){

        String msg = CmdUtil.getEmptyLine() +
                CmdUtil.getScreenLine("你的账号在别处登录，你被迫下线了！") +
                CmdUtil.getScreenLine("与服务器断开连接。");

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

        Map<String, RoomEntity> roomMap = UserCacheUtil.getMapCache();

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
        List<PlayerSkillEntity> playerSkillEntitiesList = playerSkillDao.getListByPlayerId(player.getPlayer().getId());

        player.initSkills(playerSkillEntitiesList);
    }
}
