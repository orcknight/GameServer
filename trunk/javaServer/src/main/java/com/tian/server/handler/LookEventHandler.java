package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.service.LookService;

/**
 * Created by PPX on 2017/6/29.
 */
public class LookEventHandler implements CmdEventHandler{

    public void handle(SocketIOClient socketIOClient, String data) {

        data = data.trim();
        String[] dataArray = data.split(" ");
        if(dataArray.length < 1){

            return;
        }

        LookService lookService = new LookService(socketIOClient);

        if(dataArray[0].equals("look")){

            lookService.look(dataArray[1]);
        }else if(dataArray[0].equals("open")){

            lookService.openGate(dataArray[1]);
        }else if(dataArray[0].equals("close")){

            lookService.closeGate(dataArray[1]);
        }


    }
}
