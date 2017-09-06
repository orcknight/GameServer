package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.bll.LookBll;
import net.sf.json.JSONObject;

/**
 * Created by PPX on 2017/6/29.
 */
public class LookEventHandler implements CmdEventHandler{

    public void handle(SocketIOClient socketIOClient, String cmd, JSONObject data) {

        String msg = data.getString("data");
        if(msg == null){

            return;
        }

        LookBll lookBll = new LookBll(socketIOClient);

        if(cmd.equals("look")){

            String target = data.getString("target");
            lookBll.look(target);
        }else if(cmd.equals("open")){

            lookBll.openGate(msg);
        }else if(cmd.equals("close")){

            lookBll.closeGate(msg);
        }


    }
}
