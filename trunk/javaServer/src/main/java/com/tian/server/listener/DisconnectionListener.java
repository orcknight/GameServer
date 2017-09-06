package com.tian.server.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.tian.server.model.Living;
import com.tian.server.model.MudObject;
import com.tian.server.model.Player;
import com.tian.server.service.PlayerService;
import com.tian.server.util.UserCacheUtil;

import java.util.Map;

/**
 * Created by PPX on 2017/6/8.
 */
public class DisconnectionListener implements DisconnectListener {

    public void onDisconnect(SocketIOClient socketIOClient) {

        removeUser(socketIOClient);
        socketIOClient.disconnect();
    }

    private void removeUser(SocketIOClient socketIOClient){

        Map<Long, MudObject> allObjects = UserCacheUtil.getAllObjects();
        Map<Integer, Living> players = UserCacheUtil.getPlayers();
        Map<SocketIOClient, Integer> userSockets = UserCacheUtil.getUserSockets();

        //清理userSockets
        Integer userId  = 0;
        if(userSockets.containsKey(socketIOClient)) {
            userId = userSockets.get(socketIOClient);
            userSockets.remove(socketIOClient);
        }

        if(userId == 0){
            return;
        }

        Long uuid = 0L;
       if(players.containsKey(userId)) {

           Player player = (Player)players.get(userId);
           if(player != null) {

               //广播玩家离开的信息
               PlayerService playerService = new PlayerService();
               socketIOClient.getNamespace().getRoomOperations(player.getPlayerInfo().getRoomName()).sendEvent("stream",
                       playerService.getLogoutBoradcastLine(player));

               //清理环境缓存中的玩家信息
               UserCacheUtil.delPlayerFromRoom(player.getPlayerInfo().getRoomName(), player);
           }

           //清理players
           uuid = players.get(userId).getUuid();
           players.remove(userId);
       }

       if(uuid == 0L) {
           return;
       }

       //清理allObjects
       if(allObjects.containsKey(uuid)){
           allObjects.remove(uuid);
       }

        //关闭连接
        socketIOClient.disconnect();
    }

}
