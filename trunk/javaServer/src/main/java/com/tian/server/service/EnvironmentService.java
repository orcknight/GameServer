package com.tian.server.service;

import com.tian.server.entity.RoomEntity;
import com.tian.server.model.*;
import com.tian.server.util.MsgUtil;
import com.tian.server.util.UnityCmdUtil;
import com.tian.server.util.UserCacheUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Map;

/**
 * Created by PPX on 2017/10/19.
 */
public class EnvironmentService {

    public Integer move(MudObject me, String dest){

        if(me.getLocation() != null){ //物品原先已经存在房间中了

            //1.先把他从RoomObjects里删除
            RoomObjects preRoomObejcts = UserCacheUtil.getRoomObjectsCache().get(me.getLocation().getName());
            if(preRoomObejcts != null){

                String typeStr = "goods";
                String nameStr = "未知";
                if(me instanceof Player){
                    preRoomObejcts.getPlayers().remove(me);
                    typeStr = "user";
                    nameStr = ((Player) me).getName();
                    //离开旧房间
                    ((Player) me).getSocketClient().leaveRoom(me.getLocation().getName());
                }else if(me instanceof Living){
                    preRoomObejcts.getNpcs().remove(me.getUuid());
                    typeStr = "npc";
                    nameStr = ((Living) me).getName();
                }else if(me instanceof GoodsContainer){
                    preRoomObejcts.getGoods().remove(me.getUuid());
                    typeStr = "goods";
                    nameStr = ((GoodsContainer) me).getGoodsEntity().getName();
                }

                //2.广播离开消息给房间里所有的人
                MessageService messageService = new MessageService();
                JSONArray jsonArray = new JSONArray();
                JSONObject leaveObject = new JSONObject();
                leaveObject.put("cmd", "look");
                leaveObject.put("displayName", nameStr);
                leaveObject.put("objId", "/" + typeStr + "/" + typeStr + "#" + me.getUuid().toString());
                jsonArray.add(UnityCmdUtil.getObjectOutRet(leaveObject));
                messageService.tellRoom(dest, jsonArray);
            }
        }

        //1.把物品加入到dest指定的新房间中
        RoomObjects destRoomObjects = UserCacheUtil.getRoomObjectsCache().get(dest);
        if(destRoomObjects == null){
            destRoomObjects = new RoomObjects();
            UserCacheUtil.getRoomObjectsCache().put(dest, destRoomObjects);
        }

        //设置物品的location
        me.setLocation(UserCacheUtil.getAllMaps().get(dest));

        String typeStr = "goods";
        String nameStr = "未知";
        if(me instanceof Player){
            typeStr = "user";
            nameStr = ((Player) me).getName();

            JSONArray jsonArray = new JSONArray();
            //发送信息，清除当前NPCBar列表
            JSONObject jsonObject = new JSONObject();
            jsonArray.add(UnityCmdUtil.getObjectClearRet(jsonObject));
            RoomService roomService = new RoomService();

            //获取地图字符串
            JSONArray roomJsonArray = roomService.getRoomDesc(getLocation(dest));
            jsonArray.addAll(JSONArray.toCollection(roomJsonArray));
            MsgUtil.sendMsg(((Player) me).getSocketClient(), jsonArray);
        }else if(me instanceof Living){
            destRoomObjects.getNpcs().put(me.getUuid(), (Living)me);
            typeStr = "npc";
            nameStr = ((Living) me).getName();
        }else if(me instanceof GoodsContainer){
            destRoomObjects.getGoods().put(me.getUuid(), (GoodsContainer)me);
            typeStr = "goods";
            nameStr = ((GoodsContainer) me).getGoodsEntity().getName();
        }

        //2.广播进入消息给房间里的所有人
        MessageService messageService = new MessageService();
        JSONArray jsonArray = new JSONArray();
        JSONArray enterArray = new JSONArray();
        JSONObject enterObject = new JSONObject();
        enterObject.put("cmd", "look");
        enterObject.put("displayName", nameStr);
        enterObject.put("objId", "/" + typeStr + "/" + typeStr + "#" + me.getUuid().toString());
        enterArray.add(enterObject);
        jsonArray.add(UnityCmdUtil.getObjectEnterRet(enterArray));
        messageService.tellRoom(dest, jsonArray);
        if(me instanceof Player){
            //加入新房间
            destRoomObjects.getPlayers().add((Player)me);
            ((Player) me).getSocketClient().joinRoom(dest);
        }
        return 1;
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
}
