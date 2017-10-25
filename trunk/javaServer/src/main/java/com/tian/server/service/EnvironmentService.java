package com.tian.server.service;

import com.tian.server.dao.PlayerInfoDao;
import com.tian.server.entity.PlayerInfoEntity;
import com.tian.server.entity.RoomEntity;
import com.tian.server.entity.RoomGateEntity;
import com.tian.server.model.*;
import com.tian.server.model.Race.Human;
import com.tian.server.util.LuaBridge;
import com.tian.server.util.MsgUtil;
import com.tian.server.util.UnityCmdUtil;
import com.tian.server.util.UserCacheUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.Transaction;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/10/19.
 */
public class EnvironmentService extends  BaseService{

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
        RoomEntity roomEntity = UserCacheUtil.getAllMaps().get(dest);
        me.setLocation(roomEntity);
        if(me instanceof  Player){

            //更新用户的位置
            Transaction transaction = getSession().getTransaction();
            try{
                transaction.begin();
                PlayerInfoDao playerInfoDao = new PlayerInfoDao();
                PlayerInfoEntity playerInfoEntity = ((Player) me).getPlayerInfo();
                playerInfoEntity.setRoomName(dest);
                playerInfoEntity.setCityName(roomEntity.getPname());
                playerInfoDao.update(playerInfoEntity);
                transaction.commit();
            }catch (Exception e){
                transaction.rollback();
            }
        }

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
            loadItemsToRoom(dest, ((Player) me));
        }

        //执行所有npc的init函数
        Collection<Living> npcs = destRoomObjects.getNpcs().values();
        for(Living npc : npcs){

            if(npc.getResource() == null || npc.getResource().length() < 1){
                continue;
            }

            if(npc.getCmdActions().containsKey("init") && me instanceof Living){

                LuaBridge bridge = new LuaBridge();
                String luaPath = this.getClass().getResource(npc.getResource()).getPath();
                Globals globals = JsePlatform.standardGlobals();
                //加载脚本文件login.lua，并编译
                globals.loadfile(luaPath).call();
                String funName = npc.getCmdActions().get("init");

                LuaValue[] luaParams = new LuaValue[1];
                luaParams[0] = LuaValue.valueOf(me.getUuid().toString());
                //获取带参函数create
                LuaValue initFun = globals.get(LuaValue.valueOf(funName));
                //执行方法初始化数据
                initFun.call(CoerceJavaToLua.coerce(bridge), LuaValue.valueOf(npc.getUuid().toString()), LuaValue.listOf(luaParams));
            }
        }

        return 1;
    }

    public Boolean isChatRoom(Living me){

        if(me.getLocation() == null){
            return false;
        }

        if(me.getLocation().getIsChatRoom() == 0){
            return false;
        }else{
            return true;
        }
    }

    private void loadItemsToRoom(String roomNames, Player me){

        //载入房间内的玩家和物品
        Map<String, RoomObjects> roomObjectsMap = UserCacheUtil.getRoomObjectsCache();
        //获取当前房间的物品
        RoomObjects roomObjects = roomObjectsMap.get(roomNames);

        JSONArray jsonArray = new JSONArray();
        PlayerService playerService = new PlayerService();
        Map<Long, Living> npcs = roomObjects.getNpcs();
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

        Map<Long, GoodsContainer> goodsContainers = roomObjects.getGoods();
        if(goodsContainers.size() > 0){

            JSONObject goodsObject = playerService.getLookGoodsProto(goodsContainers);
            jsonArray.add(goodsObject);
        }

        Map<String, RoomGateEntity> roomGates = roomObjects.getGates();
        if(roomGates.size() > 0){

            JSONObject userObject = playerService.getLookGateProto(roomGates, "gates");
            jsonArray.add(userObject);
        }

        //获取房间物品等信息
        //Map<String, RoomObjects> roomObjectsMap = UserCacheUtil.getRoomObjectsCache();
        //获取当前房间的物品
        //RoomObjects roomObjects = roomObjectsMap.get(roomNames);
        //if(roomObjects == null){
        //return;
        //}

        //String msg = ZjMudUtil.getObjectsLine(roomObjects, player);
        MsgUtil.sendMsg(me.getSocketClient(), jsonArray);
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
