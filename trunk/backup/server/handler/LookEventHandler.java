package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.bll.LookBll;

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

        LookBll lookBll = new LookBll(socketIOClient);

        if(dataArray[0].equals("look")){

            lookBll.look(dataArray[1]);
        }else if(dataArray[0].equals("open")){

            lookBll.openGate(dataArray[1]);
        }else if(dataArray[0].equals("close")){

            lookBll.closeGate(dataArray[1]);
        }


    }
}
