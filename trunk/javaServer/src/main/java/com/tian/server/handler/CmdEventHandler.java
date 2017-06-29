package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;

/**
 * Created by PPX on 2017/6/8.
 */
public interface CmdEventHandler {

    public void handle(SocketIOClient socketIOClient, String data);
}
