package com.tian.server.util;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.entity.ItemEntity;
import com.tian.server.entity.PlayerEntity;
import com.tian.server.entity.RoomContentEntity;
import com.tian.server.entity.RoomEntity;
import com.tian.server.model.PlayerCache;
import com.tian.server.model.RoomObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/6/14.
 */
public class UserCacheUtil {

    private static Map<Integer, PlayerCache> playerCache = new HashMap<Integer, PlayerCache>();
    private static Map<SocketIOClient, Integer> socketCache = new HashMap<SocketIOClient, Integer>();
    private static Map<String, RoomEntity> mapCache = new HashMap<String, RoomEntity>();
    private static Map<String, RoomObjects> roomObjectsCache = new HashMap<String, RoomObjects>();

    public static Map<Integer, PlayerCache> getPlayerCache(){

        return playerCache;
    }

    public static Map<SocketIOClient, Integer> getSocketCache(){

        return socketCache;
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

    public static void initRoomObjectsCache(List<RoomContentEntity> roomContents, List<ItemEntity> items){

        for(RoomContentEntity roomContent : roomContents){

            roomContent.hashCode();

            RoomObjects roomObjects = roomObjectsCache.get(roomContent.getRoomName());
            if(roomObjects == null){

                roomObjects = new RoomObjects();
            }

            List<ItemEntity> savedItems = roomObjects.getItems();

            ItemEntity item = (ItemEntity)items.get(roomContent.getItemId()).clone();
            item.setId(IdUtil.getUnUsedId());
            savedItems.add(item);
            roomObjects.setItems(savedItems);
        }
    }

    public static void movePlayerToOtherRoom(String source, String dest, PlayerEntity player){

        //从之前的房间删除玩家
        RoomObjects sourceObjects = roomObjectsCache.get(source);
        List<PlayerEntity> sourcePlayers = sourceObjects.getPlayers();
        sourcePlayers.remove(player);

        //把玩家移动到新房间
        RoomObjects destObjects = roomObjectsCache.get(dest);
        if(destObjects == null){

            destObjects = new RoomObjects();
            destObjects.setPlayers(new ArrayList<PlayerEntity>());
            roomObjectsCache.put(dest, destObjects);
        }
        List<PlayerEntity> destPlayers = destObjects.getPlayers();
        destPlayers.add(player);
        destObjects.setPlayers(destPlayers);
    }

    public static void delPlayerFromRoom(String roomName, PlayerEntity player){

        RoomObjects sourceObjects = roomObjectsCache.get(roomName);
        List<PlayerEntity> sourcePlayers = sourceObjects.getPlayers();
        if(sourcePlayers == null){

            return;
        }
        sourcePlayers.remove(player);
    }

}
