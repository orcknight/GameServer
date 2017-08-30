package com.tian.server.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by PPX on 2017/6/8.
 */
public class ConnectionListener implements ConnectListener {

    public void onConnect(SocketIOClient client) {
        System.out.println(client.getRemoteAddress() + " web客户端接入");
        /*JSONArray jArray = new JSONArray();
        JSONObject outData = new JSONObject();
        outData.put("name", "name");
        outData.put("value", "杨威");
        outData.put("max", 100);
        outData.put("eff", 70);
        outData.put("cur", 50);
        jArray.add(outData);*/
        client.sendEvent("connected");
    }

}
