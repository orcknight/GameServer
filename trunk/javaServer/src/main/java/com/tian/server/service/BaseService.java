package com.tian.server.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import com.tian.server.util.UserCacheUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by PPX on 2017/6/14.
 */
public class BaseService {

    protected SocketIOClient socketIOClient;
    protected Integer userId;

    public BaseService(SocketIOClient socketIOClient){

        this.socketIOClient = socketIOClient;
        if(UserCacheUtil.getPlayerSockets().containsKey(socketIOClient)){

            this.userId = UserCacheUtil.getPlayerSockets().get(socketIOClient);
        }else{

            this.userId = 0;
        }
    }

    protected void sendMsg(String msg){

        socketIOClient.sendEvent("stream", msg);
    }

    protected void sendMsg(SocketIOClient client, String msg){

        client.sendEvent("stream", msg);
    }

    //自定义发送函数
    protected void sendMsg(String msg, List<SocketIOClient> excludeClients, Collection<SocketIOClient> clients){

        for (SocketIOClient client : clients) {

            if(excludeClients.contains(client)){
                continue;
            }

            List<Object> msgList = new ArrayList<Object>();
            msgList.add(msg);
            Packet packet = new Packet(PacketType.MESSAGE);
            packet.setSubType(PacketType.EVENT);
            packet.setName("stream");
            packet.setData(msgList);

            client.send(packet);
        }
    }
}
