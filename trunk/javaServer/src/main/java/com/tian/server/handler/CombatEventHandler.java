package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.service.CombatService;

/**
 * Created by PPX on 2017/7/11.
 */
public class CombatEventHandler implements CmdEventHandler {

    public void handle(SocketIOClient socketIOClient, String data) {

        data = data.trim();
        String[] dataArray = data.split(" ");
        if(dataArray.length < 1){

            return;
        }

        CombatService combatService = new CombatService(socketIOClient);

        if(dataArray[0].equals("fight")){

            combatService.handleFight(dataArray[1]);
        }

    }
}
