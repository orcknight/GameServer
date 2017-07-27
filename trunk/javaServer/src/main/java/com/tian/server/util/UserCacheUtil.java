package com.tian.server.util;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.entity.*;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.model.RoomObjects;
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
    private static Map<Long, Living> allLivings = new HashMap<Long, Living>();
    private static Map<Integer, Living> players = new HashMap<Integer, Living>();
    private static Map<SocketIOClient, Integer> playerSockets = new HashMap<SocketIOClient, Integer>();
    private static Map<String, RoomEntity> mapCache = new HashMap<String, RoomEntity>();
    private static Map<String, RoomObjects> roomObjectsCache = new HashMap<String, RoomObjects>();

    public static Map<Long, Living> getAllLivings() {
        return allLivings;
    }

    public static void setAllLivings(Map<Long, Living> allLivings) {
        UserCacheUtil.allLivings = allLivings;
    }

    public static Map<Integer, Living> getPlayers(){

        return players;
    }

    public static Map<SocketIOClient, Integer> getPlayerSockets(){

        return playerSockets;
    }

    public static Map<String, RoomEntity> getMapCache(){

        return mapCache;
    }

    public static void initMapCache(List<RoomEntity> list){

        for(RoomEntity entity : list){

            mapCache.put(entity.getName(), entity);
        }
    }

    public static Map<String, RoomObjects> getRoomObjectsCache(){

        return roomObjectsCache;
    }

    public static void initRoomObjectsCache(List<RoomContentEntity> roomContents, List<ItemEntity> items, List<NpcEntity> npcs){

        for(NpcEntity npc : npcs){

            try {
                Class cls = Class.forName("com.tian.server.model.Race." + npc.getRace());
                Living living = (Living)cls.newInstance();

                Long uuid = IdUtil.getUUID();
                living.setUuid(uuid);

                //RoomObjects roomObjects = roomObjectsCache.get(roomContent.getRoomName());
                //if(roomObjects == null){

                   //roomObjects = new RoomObjects();
                //}
                //}

                getAllLivings().put(uuid, living);
                LuaBridge bridge = new LuaBridge();
                String luaPath = UserCacheUtil.class.getResource(npc.getResource()).getPath();
                //= "resources/lua/login.lua";   //lua脚本文件所在路径
                Globals globals = JsePlatform.standardGlobals();
                //加载脚本文件login.lua，并编译
                globals.loadfile(luaPath).call();

                //获取带参函数create
                LuaValue createFun = globals.get(LuaValue.valueOf("create"));
                //执行方法初始化数据
                createFun.call(CoerceJavaToLua.coerce(bridge), LuaValue.valueOf(uuid.toString()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(RoomContentEntity roomContent : roomContents){

            RoomObjects roomObjects = roomObjectsCache.get(roomContent.getRoomName());
            if(roomObjects == null){

                roomObjects = new RoomObjects();
            }

            List<ItemEntity> savedItems = roomObjects.getItems();

            ItemEntity item = (ItemEntity)items.get(roomContent.getRefId()).clone();
            item.setUuid(IdUtil.getUUID());
            savedItems.add(item);
            roomObjects.setItems(savedItems);
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

}
