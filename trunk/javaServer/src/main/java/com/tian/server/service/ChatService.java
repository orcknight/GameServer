package com.tian.server.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.entity.PlayerInfoEntity;
import com.tian.server.entity.UserEntity;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.util.UserCacheUtil;
import com.tian.server.util.ZjMudUtil;

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
        String msg = ZjMudUtil.getSendChatLine(chatChannel);
        sendMsg(msg);
    }

    //闲聊频道
    public void chat(String msg){

        String chatChannel = getChatChannel();
        Map<Integer, Living> cacheMap = UserCacheUtil.getPlayers();
        Player playerCache = (Player)cacheMap.get(this.userId);
        UserEntity user = playerCache.getUser();
        String retMsg = ZjMudUtil.getChatLine(chatChannel, user.getName(), playerCache.getName(), msg);
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
