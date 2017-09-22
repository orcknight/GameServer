package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.bll.InfoBll;
import net.sf.json.JSONObject;

/**
 * Created by PPX on 2017/9/22.
 */
public class InfoEventHandler implements CmdEventHandler {

    public void handle(SocketIOClient socketIOClient, String cmd, JSONObject data) {

        InfoBll infoBll = new InfoBll(socketIOClient);
        //登陆
        if(cmd.equals("bag")) {

            infoBll.handleBag();
        }
    }
}
