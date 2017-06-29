package com.tian.server.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;

/**
 * Created by PPX on 2017/6/8.
 */
public class DisconnectionListener implements DisconnectListener {

    public void onDisconnect(SocketIOClient socketIOClient) {

        socketIOClient.disconnect();

    }

}
