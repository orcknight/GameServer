package com.tian.server.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.entity.PlayerEntity;
import com.tian.server.model.PlayerCache;
import com.tian.server.model.RoomObjects;
import com.tian.server.util.UserCacheUtil;

/**
 * Created by PPX on 2017/7/11.
 */
public class CombatService extends  BaseService {

    public CombatService(SocketIOClient socketIOClient) {
        super(socketIOClient);
    }

    public void handleFight(String euid){

        int userId = UserCacheUtil.getSocketCache().get(this.socketIOClient);
        if(userId  < 1){

            return;
        }

        PlayerCache me = UserCacheUtil.getPlayerCache().get(userId);

        RoomObjects roomObjects = UserCacheUtil.getRoomObjectsCache().get(me.getPlayerInfo().getRoomName());

        for(PlayerEntity player : roomObjects.getPlayers()){

            if(player.getEuid().equals(euid)){

                me.addEnemy(UserCacheUtil.getPlayerCache().get(player.getId()));
                UserCacheUtil.getPlayerCache().get(player.getId()).addEnemy(me);
                break;
            }
        }



    }
}
