package com.tian.server.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.entity.PlayerEntity;
import com.tian.server.entity.PlayerInfoEntity;
import com.tian.server.entity.UserEntity;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.util.CmdUtil;
import com.tian.server.util.UserCacheUtil;

import java.util.Map;

/**
 * Created by PPX on 2017/6/21.
 */
public class ChatService extends BaseService{

    public ChatService(SocketIOClient socketIOClient) {
        super(socketIOClient);
    }

    public void writeChatWindow(){

        String chatChannel = getChatChannel();
        String msg = CmdUtil.getSendChatLine(chatChannel);
        sendMsg(msg);
    }

    //闲聊频道
    public void chat(String msg){

        String chatChannel = getChatChannel();
        Map<Integer, Living> cacheMap = UserCacheUtil.getPlayers();
        Player playerCache = (Player)cacheMap.get(this.userId);
        UserEntity user = playerCache.getUser();
        PlayerEntity player = playerCache.getPlayer();
        String retMsg = CmdUtil.getChatLine(chatChannel, user.getName(), player.getName(), msg);
        socketIOClient.getNamespace().getBroadcastOperations().sendEvent("stream", retMsg);
    }

    private String getChatChannel(){

        Map<Integer, Living> cacheMap = UserCacheUtil.getPlayers();
        Player player = (Player)cacheMap.get(this.userId);
        PlayerInfoEntity playerInfo = player.getPlayerInfo();
        if(playerInfo == null){

            return "闲聊";
        }else{

            return playerInfo.getChatChannel();
        }
    }


}
