package com.tian.server.bll;


import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import com.tian.server.common.TaskActionType;
import com.tian.server.dao.*;
import com.tian.server.entity.*;
import com.tian.server.model.*;
import com.tian.server.service.PlayerService;
import com.tian.server.service.RoomService;
import com.tian.server.service.TaskService;
import com.tian.server.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.Transaction;

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
    private ServerInfoDao serverInfoDao = new ServerInfoDao();

    public UserBll(SocketIOClient socketIOClient) {
        super(socketIOClient);
    }

    public void login(String name, String password){

        JSONArray jsonArray = new JSONArray();

        UserEntity user =  userDao.getByNameAndPassword(name, password);
        if(user.getId() < 1){
            jsonArray.add(UnityCmdUtil.getEmptyRet());
            sendMsg(jsonArray);
            return;
        }

        //检查用户是否已经登陆
        Map<Integer, Living> cache = UserCacheUtil.getPlayers();
        //如果已经登陆了，先踢用户下线，并重新设置用户信息
        if(cache.containsKey(user.getId())){

            //发送断开连接信息,并且断开连接，并重新缓存用户数据
            Player player = (Player)cache.get(user.getId());
            sendKickOffMsg(player.getSocketClient());
            player.getSocketClient().leaveRoom(player.getPlayerInfo().getRoomName());
            player.getSocketClient().disconnect();
            Packet packet = new Packet(PacketType.CLOSE);
            packet.setSubType(PacketType.DISCONNECT);
            player.getSocketClient().send(packet);
            cache.remove(user.getId());

            UserCacheUtil.getUserSockets().remove(player.getSocketClient());
        }

        //存储用户信息
        setSocketCache(user.getId());
        Player playerCache = new Player();
        playerCache.setUser(user);
        playerCache.setSocketClient(socketIOClient);
        cache.put(user.getId(), playerCache);

        PlayerEntity player = playerDao.getByUserId(user.getId());
        if(player.getId() < 1){

            //如果没有角色，转到角色创建窗口。
            jsonArray.add(UnityCmdUtil.getCreateRoleRet(""));
            sendMsg(jsonArray);
            return;
        }
        //缓存玩家信息
        playerCache.initInfo(player);
        initPlayerAttr(playerCache);
        initPlayerFamily(playerCache);

        //获取玩家辅助信息并缓存
        PlayerInfoEntity playerInfo = playerInfoDao.getByPlayerId(player.getId());
        playerCache.setPlayerInfo(playerInfo);

        //载入玩家的任务信息
        TaskService taskService = new TaskService();
        loadTaskInfo(playerCache);

        //载入玩家技能
        loadUserSkill(playerCache);
        UserCacheUtil.getAllObjects().put(playerCache.getUuid(), playerCache);

        //缓存玩家信息
        Map<String, RoomEntity> roomMap = UserCacheUtil.getAllMaps();
        playerCache.setLocation(roomMap.get(playerInfo.getRoomName()));
        PlayerService playerService = new PlayerService();
        socketIOClient.getNamespace().getRoomOperations(playerInfo.getRoomName()).sendEvent("stream", playerService.getLoginBoradcastLine(player));
        socketIOClient.joinRoom(playerInfo.getRoomName());

        //显示登陆信息
        jsonArray.add(UnityCmdUtil.getClearScreenRet()); //首先清屏
        jsonArray.add(UnityCmdUtil.getInfoWindowRet(serverInfoDao.getServerInfo().getName()));
        //显示房间信息
        RoomService roomService = new RoomService();
        PlayerLocation playerLocation = getLocation(playerInfo.getRoomName());
        JSONArray roomJsonArray = roomService.getRoomDesc(playerLocation);
        jsonArray.addAll(JSONArray.toCollection(roomJsonArray));
        sendMsg(jsonArray);
                //ZjMudUtil.getLocationLine(getLocation(playerInfo.getRoomName());

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

        //String msg = ZjMudUtil.getObjectsLine(roomObjects, playerCache);
        sendMsg(loadObjects(roomObjects, playerCache));
    }

    public void createRole(String name, String sex) {

        JSONArray jsonArray = new JSONArray();
        //先检查用户登陆没有
        if (this.userId < 1) {
            jsonArray.add(UnityCmdUtil.getPopWindowRet("您还没有登陆，请先登陆。"));
            sendMsg(jsonArray);
            return;
        }

        //检查名字是否全部是中文
        if (!CharUtil.isChinese(name)) {

            jsonArray.add(UnityCmdUtil.getPopWindowRet("对不起，请您用「中文」取名字(2-6个字)。"));
            sendMsg(jsonArray);
            return;
        }

        //检查名字的长度
        if (name.length() < 2 || name.length() > 6) {

            jsonArray.add(UnityCmdUtil.getPopWindowRet("对不起，你的中文姓名不能太长或太短(2-6个字)。"));
            sendMsg(jsonArray);
            return;
        }

        //检查名字是否被别人占用
        PlayerService playerService = new PlayerService();
        if (isNameBeUsed(name)) {

            jsonArray.add(UnityCmdUtil.getPopWindowRet("对不起，这个名字已经被其他人占用了。"));
            sendMsg(jsonArray);
            return;
        }


        //创建角色
        Transaction transaction = getSession().getTransaction();

        PlayerEntity player = new PlayerEntity();
        PlayerInfoEntity playerInfo = new PlayerInfoEntity();

        try {
            transaction.begin();
            player.setName(name);
            player.setSex(sex);
            player.setCmdName(getUserName());
            player.setUserId(this.userId);
            player.setUuid(IdUtil.getUUID());
            playerDao.add(player);
            player = playerDao.getById(player.getId());

            playerInfo.setCityName("xinghuacun");
            playerInfo.setRoomName("xinghuacun/guangchang");
            playerInfo.setPlayerId(player.getId());
            playerInfoDao.add(playerInfo);
            transaction.commit();
        }catch(Exception e){
            transaction.rollback();
        }

        //登陆成功，先发送刷新界面命令
        jsonArray.add(UnityCmdUtil.getRoleCreatedRet(""));
        //发送清屏信息
        jsonArray.add(UnityCmdUtil.getClearScreenRet());
        //然后发送欢迎信息
        jsonArray.add(UnityCmdUtil.getInfoWindowRet(serverInfoDao.getServerInfo().getName()));

        socketIOClient.getNamespace().getRoomOperations(playerInfo.getRoomName()).sendEvent("stream", playerService.getLoginBoradcastLine(player));
        socketIOClient.joinRoom(playerInfo.getRoomName()); //玩家加入当前房间群组，为广播消息

        //存储用户的角色辅助信息
        Map<Integer, Living> playerCacheMap = UserCacheUtil.getPlayers();
        Player playerCache = null;
        if (playerCacheMap.containsKey(this.userId)) {

            playerCache = (Player) playerCacheMap.get(this.userId);
            playerCache.initInfo(player);
            playerCache.setPlayerInfo(playerInfo);

            //缓存玩家信息
            Map<String, RoomEntity> roomMap = UserCacheUtil.getAllMaps();
            playerCache.setLocation(roomMap.get(playerInfo.getRoomName()));
        }

        //显示房间信息
        RoomService roomService = new RoomService();
        PlayerLocation playerLocation = getLocation(playerInfo.getRoomName());
        JSONArray roomJsonArray = roomService.getRoomDesc(playerLocation);
        jsonArray.addAll(JSONArray.toCollection(roomJsonArray));
        //发送登陆信息
        sendMsg(jsonArray);

        //载入房间内的玩家和物品
        Map<String, RoomObjects> roomObjectsMap = UserCacheUtil.getRoomObjectsCache();
        //获取当前房间的物品
        RoomObjects roomObjects = roomObjectsMap.get(playerInfo.getRoomName());
        //检查自己是否在房间内，如果不在添加进去
        if(roomObjects == null) {

            roomObjects = new RoomObjects();
            roomObjects.setPlayers(new ArrayList<Player>());
        }
        if(playerCache != null) {
            sendMsg(loadObjects(roomObjects, playerCache));
        }
    }

    public void logout(){

        //如果已经登陆了，发送下线消息然后
        Map<Integer, Living> playerCacheMap = UserCacheUtil.getPlayers();
        Player player = (Player)playerCacheMap.get(this.userId);

        if(player != null) {

            //广播玩家离开的信息
            PlayerService playerService = new PlayerService();

            socketIOClient.getNamespace().getRoomOperations(player.getPlayerInfo().getRoomName()).sendEvent("stream",
                    playerService.getLogoutBoradcastLine(player));

            //清理缓存数据
            UserCacheUtil.getUserSockets().remove(socketIOClient);
            playerCacheMap.remove(this.userId);
            UserCacheUtil.delPlayerFromRoom(player.getPlayerInfo().getRoomName(), player);
        }

        //关闭连接
        socketIOClient.disconnect();
    }

    private void sendKickOffMsg(SocketIOClient socketIOClient){

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = UnityCmdUtil.getInfoWindowRet("你的账号在别处登录，你被迫下线了！\n与服务器断开连接。");
        jsonArray.add(jsonObject);

        socketIOClient.sendEvent("stream", jsonArray);
    }

    private void setSocketCache(Integer userId){

        Map<SocketIOClient, Integer> socketCache = UserCacheUtil.getUserSockets();
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

    private String getUserName(){

        Integer userId = UserCacheUtil.getUserSockets().get(socketIOClient);
        UserEntity userEntity = userDao.getById(userId);
        if(userEntity == null){

            return "";
        }

        return userEntity.getName();
    }

    private JSONArray loadObjects(RoomObjects roomObjects, Player me){

        JSONArray jsonArray = new JSONArray();
        PlayerService playerService = new PlayerService();
        List<Living> npcs = roomObjects.getNpcs();
        if(npcs.size() > 0){

            JSONObject npcObject = playerService.getLookLivingProto(npcs, "npc");
            jsonArray.add(npcObject);
        }

        List<Player> players = roomObjects.getPlayers();
        if(players.size() > 1){

            List<Living> excludeMe = new ArrayList<Living>();
            for(Player player : players){
                if(player.getUuid() == me.getUuid()){
                    continue;
                }
                excludeMe.add(player);
            }

            JSONObject userObject = playerService.getLookLivingProto(excludeMe, "user");
            jsonArray.add(userObject);
        }

        Map<String, RoomGateEntity> roomGates = roomObjects.getGates();
        if(roomGates.size() > 0){

            JSONObject userObject = playerService.getLookLivingProto(roomGates, "gate");
            jsonArray.add(userObject);
        }

        return jsonArray;
    }

    private Boolean isNameBeUsed(String name) {

        if(playerDao.getByName(name) == null){

            return false;
        }

        return true;
    }

    public void loadTaskInfo(Player player){

        PlayerTrackDao playerTrackDao = new PlayerTrackDao();
        PlayerTrackActionDao playerTrackActionDao = new PlayerTrackActionDao();

        List<PlayerTask> playerTasks = player.getTaskList();
        List<PlayerTrackEntity> playerTrackEntityList = playerTrackDao.getDoingTaskByPlayerId(player.getPlayerId());
        for(PlayerTrackEntity playerTrackEntity : playerTrackEntityList){

            TaskTrack taskTrack = UserCacheUtil.getTaskTrackMap().get(playerTrackEntity.getTrackId());

            if(taskTrack.getTrackActions().get(playerTrackEntity.getStep()-1).getActionType().equals(TaskActionType.TALK)){

                player.setTalkTaskCount(player.getTalkTaskCount() + 1);
            }else if(taskTrack.getTrackActions().get(playerTrackEntity.getStep()-1).getActionType().equals(TaskActionType.KILL)){

                player.setKillTaskCount(player.getKillTaskCount() + 1);
            }

            PlayerTask playerTask = new PlayerTask();
            playerTask.setTrack(playerTrackEntity);
            playerTask.setAction(playerTrackActionDao.getDoingTrackActionByTrackId(playerTrackEntity.getId()));
            playerTasks.add(playerTask);
        }
    }



}
