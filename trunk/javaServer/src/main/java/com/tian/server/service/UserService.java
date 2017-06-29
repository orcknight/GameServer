package com.tian.server.service;


import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.dao.PlayerDao;
import com.tian.server.dao.PlayerInfoDao;
import com.tian.server.dao.UserDao;
import com.tian.server.entity.PlayerEntity;
import com.tian.server.entity.PlayerInfoEntity;
import com.tian.server.entity.RoomEntity;
import com.tian.server.entity.UserEntity;
import com.tian.server.model.PlayerCache;
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
        Map<Integer, PlayerCache> cache = UserCacheUtil.getPlayerCache();
        //如果已经登陆了，先踢用户下线，并重新设置用户信息
        if(cache.containsKey(user.getId())){

            //发送断开连接信息,并且断开连接，并重新缓存用户数据
            PlayerCache playerCache = cache.get(user.getId());
            sendKickOffMsg(playerCache.getSocketClient());
            playerCache.getSocketClient().disconnect();
            cache.remove(user.getId());
        }

        //存储用户信息
        setSocketCache(user.getId());
        PlayerCache playerCache = new PlayerCache();
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

        //缓存玩家信息
        Map<String, RoomEntity> roomMap = UserCacheUtil.getMapCache();
        playerCache.setRoom(roomMap.get(playerInfo.getRoomName()));
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
        Map<Integer, PlayerCache> playerCacheMap = UserCacheUtil.getPlayerCache();
        if(playerCacheMap.containsKey(this.userId)){

            PlayerCache playerCache = playerCacheMap.get(this.userId);
            playerCache.setPlayerInfo(playerInfo);

            //缓存玩家信息
            Map<String, RoomEntity> roomMap = UserCacheUtil.getMapCache();
            playerCache.setRoom(roomMap.get(playerInfo.getRoomName()));
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
        Map<Integer, PlayerCache> playerCacheMap = UserCacheUtil.getPlayerCache();
        PlayerCache playerCache = playerCacheMap.get(this.userId);

        if(playerCache != null) {

            //广播玩家离开的信息
            socketIOClient.getNamespace().getRoomOperations(playerCache.getPlayerInfo().getRoomName()).sendEvent("stream",
                    CmdUtil.getLogoutBoradcastLine(playerCache.getPlayer()));

            //清理缓存数据
            UserCacheUtil.getSocketCache().remove(socketIOClient);
            playerCacheMap.remove(this.userId);
            UserCacheUtil.delPlayerFromRoom(playerCache.getPlayerInfo().getRoomName(), playerCache.getPlayer());
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

        Map<SocketIOClient, Integer> socketCache = UserCacheUtil.getSocketCache();
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

        if(location.getNname().length() > 0){

            RoomEntity north = roomMap.get(location.getNname());
            if(north != null){

                playerLocation.setNorth(north);
            }
        }

        if(location.getSname().length() > 0){

            RoomEntity south = roomMap.get(location.getSname());
            if(south != null){

                playerLocation.setSouth(south);
            }
        }

        if(location.getEname().length() > 0){

            RoomEntity east = roomMap.get(location.getEname());
            if(east != null){

                playerLocation.setEast(east);
            }
        }

        if(location.getWname().length() > 0){

            RoomEntity west = roomMap.get(location.getWname());
            if(west != null){

                playerLocation.setWest(west);
            }
        }

        if(location.getNename().length() > 0){

            RoomEntity northEast = roomMap.get(location.getNename());
            if(northEast != null){

                playerLocation.setNorthEast(northEast);
            }
        }

        if(location.getNwname().length() > 0){

            RoomEntity northWest = roomMap.get(location.getNwname());
            if(northWest != null){

                playerLocation.setNorthWest(northWest);
            }
        }

        if(location.getSename().length() > 0){

            RoomEntity southEast = roomMap.get(location.getSename());
            if(southEast != null){

                playerLocation.setSouthEast(southEast);
            }
        }

        if(location.getSwname().length() > 0){

            RoomEntity southWest = roomMap.get(location.getSwname());
            if(southWest != null){

                playerLocation.setSouthWest(southWest);
            }
        }

        if(location.getInname().length() > 0){

            RoomEntity in = roomMap.get(location.getInname());
            if(in != null){

                playerLocation.setIn(in);
            }
        }

        if(location.getOutname().length() > 0){

            RoomEntity out = roomMap.get(location.getInname());
            if(out != null){

                playerLocation.setOut(out);
            }
        }

        return playerLocation;
    }

    private void broadcastLogin(){


    }
}
