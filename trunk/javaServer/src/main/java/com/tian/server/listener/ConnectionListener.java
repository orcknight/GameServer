package com.tian.server.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;

/**
 * Created by PPX on 2017/6/8.
 */
public class ConnectionListener implements ConnectListener {

    public void onConnect(SocketIOClient client) {
        System.out.println(client.getRemoteAddress() + " web客户端接入");
        client.sendEvent("connected", "");
    }

}
