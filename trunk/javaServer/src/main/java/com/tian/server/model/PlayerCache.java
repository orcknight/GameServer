package com.tian.server.model;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.entity.PlayerEntity;
import com.tian.server.entity.PlayerInfoEntity;
import com.tian.server.entity.RoomEntity;
import com.tian.server.entity.UserEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PPX on 2017/6/14.
 */
public class PlayerCache {

    //用户的socket client
    private SocketIOClient socketClient;

    //用户信息
    private UserEntity user;

    //玩家信息
    private PlayerEntity player;

    //玩家辅助信息
    private PlayerInfoEntity playerInfo;

    //玩家当前位置
    private RoomEntity room;

    //玩家当前观察的物品id
    private String lookId;

    public void setSocketClient(SocketIOClient socketClient){

        this.socketClient = socketClient;
    }

    public SocketIOClient getSocketClient(){

        return this.socketClient;
    }

    public void setUser(UserEntity user){

        this.user = user;
    }

    public UserEntity getUser(){

        return this.user;
    }

    public void setPlayer(PlayerEntity player){

        this.player = player;
    }

    public PlayerEntity getPlayer(){

        return this.player;
    }

    public void setPlayerInfo(PlayerInfoEntity playerInfo){

        this.playerInfo = playerInfo;
    }

    public PlayerInfoEntity getPlayerInfo(){

        return this.playerInfo;
    }

    public void setRoom(RoomEntity room){

        this.room = room;
    }

    public RoomEntity getRoom(){

        return this.room;
    }

    public void setLookId(String lookId){

        this.lookId = lookId;
    }

    public String getLookId(){

        return this.lookId;
    }

}
