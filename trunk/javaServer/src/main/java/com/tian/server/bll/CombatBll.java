package com.tian.server.bll;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.model.Player;
import com.tian.server.model.RoomObjects;
import com.tian.server.util.UserCacheUtil;

/**
 * Created by PPX on 2017/7/11.
 */
public class CombatBll extends BaseBll {

    public CombatBll(SocketIOClient socketIOClient) {
        super(socketIOClient);
    }

    public void handleFight(String euid){

        int userId = UserCacheUtil.getPlayerSockets().get(this.socketIOClient);
        if(userId  < 1){

            return;
        }

        Player me = (Player)UserCacheUtil.getPlayers().get(userId);

        RoomObjects roomObjects = UserCacheUtil.getRoomObjectsCache().get(me.getPlayerInfo().getRoomName());

        for(Player player : roomObjects.getPlayers()){

            if(player.getCmdName().equals(euid)){

                me.addEnemy(UserCacheUtil.getPlayers().get(player.getUser().getId()));
                UserCacheUtil.getPlayers().get(player.getUser().getId()).addEnemy(me);
                break;
            }
        }



    }
}
