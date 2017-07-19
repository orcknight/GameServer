package com.tian.server.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.tian.server.entity.PlayerEntity;
import com.tian.server.entity.RoomEntity;
import com.tian.server.entity.RoomGateEntity;
import com.tian.server.model.PlayerCache;
import com.tian.server.model.PlayerLocation;
import com.tian.server.model.RoomObjects;
import com.tian.server.util.CmdUtil;
import com.tian.server.util.UserCacheUtil;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by PPX on 2017/6/16.
 */
public class MoveService extends BaseService{

    public MoveService(SocketIOClient socketIOClient) {
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

        //检查用户是否已经登陆
        Map<Integer, PlayerCache> playerCacheMap = UserCacheUtil.getPlayerCache();
        Map<String, RoomEntity> roomMap = UserCacheUtil.getMapCache();
        //登录了就进行移动操作
        if(playerCacheMap.containsKey(this.userId)){

            //检查是否可以移动
            if(!checkMove(direction)){
                return;
            }

            //通过用户的移动方向，获取目标房间
            PlayerCache playerCache = playerCacheMap.get(this.userId);
            RoomEntity room = playerCache.getRoom();
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

            //获取地图字符串
            String msg = CmdUtil.getLocationLine(getLocation(destRoomName));
            sendMsg(msg);

            //切换room并广播信息
            socketIOClient.leaveRoom(room.getName());

            String destName = getDirectionCnName(direction);
            //广播玩家离开房间的信息
            socketIOClient.getNamespace().getRoomOperations(room.getName())
                    .sendEvent("stream", CmdUtil.getLeaveRoomLine(roomMap.get(destRoomName).getShortDesc() + "("  + destName + ")", playerCache.getPlayer()));

            //广播玩家进入房间的信息
            socketIOClient.getNamespace().getRoomOperations(destRoomName)
                    .sendEvent("stream", CmdUtil.getEnterRoomLine(playerCache.getPlayer().getName(), "金丝甲", playerCache.getPlayer()));

            socketIOClient.joinRoom(destRoomName);

            //更新房间内玩家信息
            UserCacheUtil.movePlayerToOtherRoom(room.getName(), destRoomName, playerCache.getPlayer());
            loadItemsToRoom(destRoomName, playerCache.getPlayer());

            //缓存玩家信息
            playerCache.setRoom(roomMap.get(destRoomName));
        }

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

    private void loadItemsToRoom(String roomNames, PlayerEntity player){

        //获取房间物品等信息
        Map<String, RoomObjects> roomObjectsMap = UserCacheUtil.getRoomObjectsCache();
        //获取当前房间的物品
        RoomObjects roomObjects = roomObjectsMap.get(roomNames);
        if(roomObjects == null){
            return;
        }

        String msg = CmdUtil.getObjectsLine(roomObjects, player);
        sendMsg(msg);
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
        Map<Integer, PlayerCache> playerCacheMap = UserCacheUtil.getPlayerCache();
        PlayerCache playerCache = playerCacheMap.get(this.userId);
        RoomEntity room = playerCache.getRoom();
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

                    sendMsg(CmdUtil.getScreenLine("你必须先把" + name + "打开！"));
                    return false;
                }
            }
        }

        return true;
    }

}
