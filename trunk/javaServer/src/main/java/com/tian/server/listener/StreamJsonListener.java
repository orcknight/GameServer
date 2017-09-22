package com.tian.server.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.tian.server.resolver.UnityCmdResolver;
import net.sf.json.JSONObject;

/**
 * Created by PPX on 2017/8/30.
 */
public class StreamJsonListener implements DataListener<JSONObject> {

    SocketIOServer server;
    UnityCmdResolver resolver = null;

    public void setServer(SocketIOServer server) {

        this.server = server;

    }

    public void onData(SocketIOClient socketIOClient, JSONObject data, AckRequest ackRequest) throws Exception {

        System.out.println(data);
        System.out.println("get a message!");

        if(resolver == null) {
            resolver = new UnityCmdResolver(socketIOClient);
        }

        resolver.setServer(socketIOClient);
        resolver.Resolver(data);

    }
}
