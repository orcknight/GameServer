package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.bll.GetBll;
import net.sf.json.JSONObject;

/**
 * Created by PPX on 2017/9/20.
 */
public class GetEventHandler implements CmdEventHandler{

    public void handle(SocketIOClient socketIOClient, String cmd, JSONObject data) {

        GetBll getBll = new GetBll(socketIOClient);
        //登陆
        if(cmd.equals("get")) {

            String target = data.getString("target");
            getBll.handleGet(target);
        }

    }
}
