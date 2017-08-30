package com.tian.server.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.tian.server.resolver.CmdResolver;

/**
 * Created by PPX on 2017/6/8.
 */
public class StreamListener implements DataListener<String> {

    SocketIOServer server;

    public void setServer(SocketIOServer server) {

        this.server = server;

    }

    public void onData(SocketIOClient socketIOClient, String data, AckRequest ackRequest) throws Exception {

        System.out.println(data);
        System.out.println("get a message!");

        CmdResolver resolver = new CmdResolver(socketIOClient);
        resolver.Resolver(data);

    }
}
