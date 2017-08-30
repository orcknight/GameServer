package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.bll.ChatBll;
import net.sf.json.JSONObject;

/**
 * Created by PPX on 2017/6/21.
 */
public class ChatEventHandler implements CmdEventHandler{

    public void handle(SocketIOClient socketIOClient, String cmd, JSONObject data) {

        ChatBll chatBll = new ChatBll(socketIOClient);

        String msg = data.getString("data");

        if(cmd.equals("liaotian")){

            chatBll.writeChatWindow();
        }else if(cmd.equals("chat")){

            chatBll.chat(msg);
        }
    }

}
