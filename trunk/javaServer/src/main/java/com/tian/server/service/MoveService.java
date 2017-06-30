package com.tian.server.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.tian.server.entity.PlayerEntity;
import com.tian.server.entity.RoomEntity;
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

            //发送断开连接信息,并且断开连接，并重新缓存用户数据
            PlayerCache playerCache = playerCacheMap.get(this.userId);
            RoomEntity room = playerCache.getRoom();
            String destRoomName = "";
            if(direction == "east"){
                destRoomName =  room.getEname();
            }else if(direction == "west") {
                destRoomName =  room.getWname();
            }else if(direction == "south") {
                destRoomName =  room.getSname();
            }else if(direction == "north") {
                destRoomName =  room.getNname();
            }else if(direction == "northeast") {
                destRoomName =  room.getNename();
            }else if(direction == "northwest") {
                destRoomName =  room.getNwname();
            }else if(direction == "southeast") {
                destRoomName =  room.getSename();
            }else if(direction == "southwest") {
                destRoomName =  room.getSwname();
            }else if(direction == "in") {
                destRoomName =  room.getInname();
            }else if(direction == "out"){
                destRoomName =  room.getOutname();
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

            RoomEntity out = roomMap.get(location.getOutname());
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

}
