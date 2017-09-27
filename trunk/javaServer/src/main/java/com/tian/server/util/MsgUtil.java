package com.tian.server.util;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by PPX on 2017/9/27.
 */
public class MsgUtil {

    public static void sendMsg(SocketIOClient client, JSONArray jsonArray){

        client.sendEvent("stream", jsonArray);
    }

    public static void sendMsg(JSONArray jsonArray, List<SocketIOClient> excludeClients, Collection<SocketIOClient> clients){

        for (SocketIOClient client : clients) {

            if(excludeClients.contains(client)){
                continue;
            }

            Packet packet = createPacket(jsonArray);
            client.send(packet);
        }
    }

    public static Packet createPacket(Object data){

        List<Object> msgList = new ArrayList<Object>();
        msgList.add(data);
        Packet packet = new Packet(PacketType.MESSAGE);
        packet.setSubType(PacketType.EVENT);
        packet.setName("stream");
        packet.setData(msgList);

        return packet;
    }
}
