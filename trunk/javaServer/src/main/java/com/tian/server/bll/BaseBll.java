package com.tian.server.bll;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import com.tian.server.util.SessionUtil;
import com.tian.server.util.UserCacheUtil;
import net.sf.json.JSONArray;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by PPX on 2017/6/14.
 */
public class BaseBll {

    protected SocketIOClient socketIOClient;
    protected Integer userId;

    protected Session session = null;

    public BaseBll(SocketIOClient socketIOClient){

        this.socketIOClient = socketIOClient;
        if(UserCacheUtil.getUserSockets().containsKey(socketIOClient)){

            this.userId = UserCacheUtil.getUserSockets().get(socketIOClient);
        }else{

            this.userId = 0;
        }
    }

    protected Session getSession(){

        if(session == null){

            session = SessionUtil.getDataSession();
        }
        return session;
    }

    protected void sendMsg(JSONArray jsonArray){

        socketIOClient.sendEvent("stream", jsonArray);
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
