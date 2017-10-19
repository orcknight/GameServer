package com.tian.server.service;

import com.tian.server.model.*;
import com.tian.server.util.UnityCmdUtil;
import com.tian.server.util.UserCacheUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
            return 0;
        }

        String typeStr = "goods";
        String nameStr = "未知";
        if(me instanceof Player){
            destRoomObjects.getPlayers().add((Player)me);
            typeStr = "user";
            nameStr = ((Player) me).getName();
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
        return 1;
    }
}
