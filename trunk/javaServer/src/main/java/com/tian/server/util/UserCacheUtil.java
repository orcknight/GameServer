package com.tian.server.util;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.common.LivingStatus;
import com.tian.server.entity.*;
import com.tian.server.model.*;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/6/14.
 */
public class UserCacheUtil {

    //一个链接表，游戏中所有生物的链接，主键是唯一标识uuid
    private static Map<Long, MudObject> allObjects = new HashMap<Long, MudObject>(); //所有物体的uuid和物体对象的映射表
    private static Map<Integer, Living> players = new HashMap<Integer, Living>(); //用户id和玩家对象的映射表
    private static Map<SocketIOClient, Integer> userSockets = new HashMap<SocketIOClient, Integer>(); //用户id和socketId的对应关系
    private static Map<String, RoomEntity> allMaps = new HashMap<String, RoomEntity>();
    private static Map<String, RoomObjects> roomObjectsCache = new HashMap<String, RoomObjects>(); //房间名称和房间物品对象的映射表
    private static Map<String, CityEntity> allCitys = new HashMap<String, CityEntity>();
    private static Map<String, Map<String, RoomEntity>> cityedRooms = new HashMap<String, Map<String, RoomEntity>>();
    private static Map<Integer, TaskTrack> taskTrackMap = new HashMap<Integer, TaskTrack>(); //任务列表，任务id
    private static Map<Integer, TaskReward> taskRewardMap = new HashMap<Integer, TaskReward>(); //任务奖励，奖励id
    private static Map<Integer, Long> roomContentMap = new HashMap<Integer, Long>(); //room_content id和对应的uuid的映射表
    private static List<Living> allLivings = new ArrayList<Living>();

    public static Map<Long, MudObject> getAllObjects() {
        return allObjects;
    }

    public static void setAllObjects(Map<Long, Living> MudObject) {
        UserCacheUtil.allObjects = allObjects;
    }

    public static Map<Integer, Living> getPlayers(){

        return players;
    }

    public static Map<SocketIOClient, Integer> getUserSockets(){

        return userSockets;
    }

    public static Map<String, RoomEntity> getAllMaps(){

        return allMaps;
    }

    public static Map<Integer, TaskTrack> getTaskTrackMap() {
        return taskTrackMap;
    }

    public static void setTaskTrackMap(Map<Integer, TaskTrack> taskTrackMap) {
        UserCacheUtil.taskTrackMap = taskTrackMap;
    }

    public static Map<Integer, TaskReward> getTaskRewardMap() {
        return taskRewardMap;
    }

    public static void setTaskRewardMap(Map<Integer, TaskReward> taskRewardMap) {
        UserCacheUtil.taskRewardMap = taskRewardMap;
    }

    public static void initMapCache(List<RoomEntity> list){

        for(RoomEntity entity : list){

            allMaps.put(entity.getName(), entity);
        }
    }

    public static void initTaskTrackMap(List<TaskTrack> taskTrackList){

        for(TaskTrack taskTrack : taskTrackList){

            taskTrackMap.put(taskTrack.getId(), taskTrack);
        }
    }

    public static Map<String, RoomObjects> getRoomObjectsCache(){

        return roomObjectsCache;
    }

    public static Map<String, CityEntity> getAllCitys() {
        return allCitys;
    }

    public static void setAllCitys(Map<String, CityEntity> allCitys) {
        UserCacheUtil.allCitys = allCitys;
    }

    public static Map<String, Map<String, RoomEntity>> getCityedRooms() {
        return cityedRooms;
    }

    public static void setCityedRooms(Map<String, Map<String, RoomEntity>> cityedRooms) {
        UserCacheUtil.cityedRooms = cityedRooms;
    }

    public static Map<Integer, Long> getRoomContentMap() {
        return roomContentMap;
    }

    public static void setRoomContentMap(Map<Integer, Long> roomContentMap) {
        UserCacheUtil.roomContentMap = roomContentMap;
    }

    public static List<Living> getAllLivings() {
        return allLivings;
    }

    public static void setAllLivings(List<Living> allLivings) {
        UserCacheUtil.allLivings = allLivings;
    }

    public static void initRoomObjectsCache(List<RoomContentEntity> roomContents, List<NpcEntity> npcs){

        Map<Integer, Long> roomContentMap = UserCacheUtil.getRoomContentMap();
        for(RoomContentEntity roomContent : roomContents){

            RoomObjects roomObjects = roomObjectsCache.get(roomContent.getRoomName());
            if(roomObjects == null){

                roomObjects = new RoomObjects();
                roomObjectsCache.put(roomContent.getRoomName(), roomObjects);
            }

            //先处理npc
            if(roomContent.getType().equals("npc")){

                Integer npcIndex = getNpcIndex(npcs, roomContent.getRefId());
                NpcEntity npcEntity = npcs.get(npcIndex);
                Living npc = initNpc(npcEntity);
                npc.setLocation(allMaps.get(roomContent.getRoomName()));

                //把npc放到对应的房间里
                Map<Long, Living> roomNpcs = roomObjects.getNpcs();
                roomNpcs.put(npc.getUuid(), npc);
                roomContentMap.put(roomContent.getId(), npc.getUuid());
                allObjects.put(npc.getUuid(), npc);
            }else{

                GoodsManager goodsManager = new GoodsManager();
                Map<Long, GoodsContainer> savedGoods = roomObjects.getGoods();
                GoodsContainer goodsContainer = goodsManager.createById(roomContent.getRefId(), roomContent.getCount(), null);
                savedGoods.put(goodsContainer.getUuid(), goodsContainer);
                roomObjects.setGoods(savedGoods);
                roomContentMap.put(roomContent.getId(), goodsContainer.getUuid());
                allObjects.put(goodsContainer.getUuid(), goodsContainer);
            }
        }
    }

    public static void initRoomGates(List<RoomGateEntity> gates){

        for(RoomGateEntity gate : gates){

            //初始化入口房间
            RoomObjects roomObjects = roomObjectsCache.get(gate.getEnterRoom());
            if(roomObjects == null){

                roomObjects = new RoomObjects();
            }

            Map<String, RoomGateEntity> gatesMap = roomObjects.getGates();
            gatesMap.put(gate.getEnterDirection(), gate);
            roomObjectsCache.put(gate.getEnterRoom(), roomObjects);

            //初始化出口房间
            roomObjects = roomObjectsCache.get(gate.getExitRoom());
            if(roomObjects == null){

                roomObjects = new RoomObjects();
            }

            gatesMap = roomObjects.getGates();
            gatesMap.put(gate.getExitDirection(), gate);
            roomObjectsCache.put(gate.getExitRoom(), roomObjects);
        }
    }

    public static void movePlayerToOtherRoom(String source, String dest, Player player){

        //从之前的房间删除玩家
        RoomObjects sourceObjects = roomObjectsCache.get(source);
        List<Player> sourcePlayers = sourceObjects.getPlayers();
        sourcePlayers.remove(player);

        //把玩家移动到新房间
        RoomObjects destObjects = roomObjectsCache.get(dest);
        if(destObjects == null){

            destObjects = new RoomObjects();
            destObjects.setPlayers(new ArrayList<Player>());
            roomObjectsCache.put(dest, destObjects);
        }
        List<Player> destPlayers = destObjects.getPlayers();
        destPlayers.add(player);
        destObjects.setPlayers(destPlayers);
    }

    public static void delPlayerFromRoom(String roomName, Player player){

        RoomObjects sourceObjects = roomObjectsCache.get(roomName);
        List<Player> sourcePlayers = sourceObjects.getPlayers();
        if(sourcePlayers == null){

            return;
        }
        sourcePlayers.remove(player);
    }

    private static Integer getNpcIndex(List<NpcEntity> list, Integer destId){

        for(Integer i = 0; i < list.size(); i++){

            if(list.get(i).getId() == destId){

                return i;
            }
        }

        return 0;
    }

    public static Living initNpc(NpcEntity npc){

        try {
            Class cls = Class.forName("com.tian.server.model.Race." + npc.getRace());
            Living living = (Living)cls.newInstance();

            Long uuid = IdUtil.getUUID();
            living.setUuid(uuid);
            living.setId(npc.getId());
            living.setStatus((byte)(LivingStatus.NORMAL.toInteger()));
            living.setResource(npc.getResource());

            //缓存npc到生物列表
            getAllObjects().put(uuid, living);

            LuaBridge bridge = new LuaBridge();
            String luaPath = UserCacheUtil.class.getResource(npc.getResource()).getPath();
            Globals globals = JsePlatform.standardGlobals();
            //加载脚本文件login.lua，并编译
            globals.loadfile(luaPath).call();
            //获取带参函数create
            LuaValue createFun = globals.get(LuaValue.valueOf("create"));
            //执行方法初始化数据
            createFun.call(CoerceJavaToLua.coerce(bridge), LuaValue.valueOf(uuid.toString()));
            return living;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
