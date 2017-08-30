package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.bll.DefaultBll;

/**
 * Created by PPX on 2017/6/8.
 */
public class DefaultEventHandler implements CmdEventHandler {


    public void handle(SocketIOClient socketIOClient, String data) {

        DefaultBll defaultBll = new DefaultBll(socketIOClient);
        if(data.equals( "\n")){ //验证版本号

            defaultBll.checkVersion();
        }else{ //未定义消息不做回应

            defaultBll.sendEmpty();
        }
    }
}
