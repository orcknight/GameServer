package com.tian.server.bll;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.entity.RoomEntity;
import com.tian.server.entity.RoomGateEntity;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.model.PlayerLocation;
import com.tian.server.model.RoomObjects;
import com.tian.server.service.PlayerService;
import com.tian.server.service.RoomService;
import com.tian.server.util.UnityCmdUtil;
import com.tian.server.util.UserCacheUtil;
import com.tian.server.util.ZjMudUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/6/16.
 */
public class MoveBll extends BaseBll {

    public MoveBll(SocketIOClient socketIOClient) {
        super(socketIOClient);
    }

    public void moveEast(){

        move("east");
    }

    public void moveWest(){

        move("west");
    }

    public void moveNorth(){

        move("north");
    }

    public void moveSouth(){

        move("south");
    }

    public void moveNorthEast(){

        move("northeast");
    }

    public void moveNorthWest(){

        move("northeast");
    }

    public void moveSouthEest(){

        move("southeast");
    }

    public void moveSouthWest(){

        move("southeast");
    }

    public void moveIn(){

        move("in");
    }

    public void moveOut(){

        move("out");
    }

    public void move(String direction){

        JSONArray jsonArray = new JSONArray();
        //检查用户是否已经登陆
        Map<Integer, Living> playerCacheMap = UserCacheUtil.getPlayers();
        Map<String, RoomEntity> roomMap = UserCacheUtil.getAllMaps();
        //登录了就进行移动操作
        if(playerCacheMap.containsKey(this.userId)){

            //检查是否可以移动
            if(!checkMove(direction)){
                return;
            }

            //通过用户的移动方向，获取目标房间
            Player player = (Player)playerCacheMap.get(this.userId);
            RoomEntity room = player.getLocation();
            String destRoomName = "";
            if(direction == "east"){
                destRoomName =  room.getEast();
            }else if(direction == "west") {
                destRoomName =  room.getWest();
            }else if(direction == "south") {
                destRoomName =  room.getSouth();
            }else if(direction == "north") {
                destRoomName =  room.getNorth();
            }else if(direction == "northeast") {
                destRoomName =  room.getNortheast();
            }else if(direction == "northwest") {
                destRoomName =  room.getNorthwest();
            }else if(direction == "southeast") {
                destRoomName =  room.getSoutheast();
            }else if(direction == "southwest") {
                destRoomName =  room.getSouthwest();
            }else if(direction == "enter") {
                destRoomName =  room.getEnter();
            }else if(direction == "out"){
                destRoomName =  room.getOut();
            }else if(direction == "up") {
                destRoomName =  room.getUp();
            }else if(direction == "down"){
                destRoomName =  room.getDown();
            }

            if(destRoomName.length() < 1) {
                return;
            }

            //发送信息，清除当前NPCBar列表
            JSONObject jsonObject = new JSONObject();
            jsonArray.add(UnityCmdUtil.getObjectClearRet(jsonObject));

            RoomService roomService = new RoomService();
            //获取地图字符串
            JSONArray roomJsonArray = roomService.getRoomDesc(getLocation(destRoomName));
            jsonArray.addAll(JSONArray.toCollection(roomJsonArray));

            sendMsg(jsonArray);

            //切换room并广播信息
            socketIOClient.leaveRoom(room.getName());
            String destName = getDirectionCnName(direction);

            PlayerService playerService = new PlayerService();
            JSONArray leaveObject = playerService.getLeaveRoomLine(roomMap.get(destRoomName).getShortDesc() + "("  + destName + ")", player);

            //广播玩家离开房间的信息
            socketIOClient.getNamespace().getRoomOperations(room.getName())
                    .sendEvent("stream", leaveObject);

            //广播玩家进入房间的信息
            JSONArray enterObject = playerService.getEnterRoomLine("金丝甲", player);
            socketIOClient.getNamespace().getRoomOperations(destRoomName)
                    .sendEvent("stream", enterObject);

            socketIOClient.joinRoom(destRoomName);

            //更新房间内玩家信息
            UserCacheUtil.movePlayerToOtherRoom(room.getName(), destRoomName, player);
            loadItemsToRoom(destRoomName, player);

            //缓存玩家信息
            player.setLocation(roomMap.get(destRoomName));
        }

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

            RoomEntity enter = roomMap.get(location.getEnter());
            if(enter != null){

                playerLocation.setEnter(enter);
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

    private void loadItemsToRoom(String roomNames, Player me){

        //载入房间内的玩家和物品
        Map<String, RoomObjects> roomObjectsMap = UserCacheUtil.getRoomObjectsCache();
        //获取当前房间的物品
        RoomObjects roomObjects = roomObjectsMap.get(roomNames);

        JSONArray jsonArray = new JSONArray();
        PlayerService playerService = new PlayerService();
        Map<Integer, Living> npcs = roomObjects.getNpcs();
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
        sendMsg(jsonArray);
    }

    private String getDirectionCnName(String direction){

        String cnName = "";
        if(direction == "east"){
            cnName = "东方";
        }else if(direction == "west") {
            cnName = "西方";
        }else if(direction == "south") {
            cnName = "南方";
        }else if(direction == "north") {
            cnName = "北方";
        }else if(direction == "northeast") {
            cnName = "东北";
        }else if(direction == "northwest") {
            cnName = "西北";
        }else if(direction == "southeast") {
            cnName = "东南";
        }else if(direction == "southwest") {
            cnName = "西南";
        }else if(direction == "in") {
            cnName = "上面";
        }else if(direction == "out"){
            cnName = "下面";
        }

        return cnName;
    }

    /**
     * 检查玩家是否可以向某个方向移动
     * @param direction 移动的方向
     * @return true ： 可以移动 false ： 不能移动
     */
    private Boolean checkMove(String direction){

        //检查下有没有门阻挡

        //检查用户是否已经登陆
        Map<Integer, Living> playerCacheMap = UserCacheUtil.getPlayers();
        Player player = (Player)playerCacheMap.get(this.userId);
        RoomEntity room = player.getLocation();
        Map<String, RoomObjects> roomObjectsMap = UserCacheUtil.getRoomObjectsCache();
        RoomObjects roomObjects = roomObjectsMap.get(room.getName());
        if(roomObjects != null){

            RoomGateEntity gate = roomObjects.getGates().get(direction);
            if(gate != null){

                if(gate.getStatus() == 1){

                    return true;
                }else{

                    String name = gate.getName();
                    name = name.replaceAll("【", "");
                    name = name.replaceAll("】", "");

                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = UnityCmdUtil.getInfoWindowRet("你必须先把" + name + "打开！");
                    jsonArray.add(jsonObject);
                    sendMsg(jsonArray);
                    return false;
                }
            }
        }

        return true;
    }

}
