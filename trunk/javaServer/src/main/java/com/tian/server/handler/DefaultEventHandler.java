package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.service.DefaultService;

/**
 * Created by PPX on 2017/6/8.
 */
public class DefaultEventHandler implements CmdEventHandler {


    public void handle(SocketIOClient socketIOClient, String data) {

        DefaultService defaultService = new DefaultService(socketIOClient);
        if(data.equals( "\n")){ //验证版本号

            defaultService.checkVersion();
        }else{ //未定义消息不做回应

            defaultService.sendEmpty();
        }
    }
}
