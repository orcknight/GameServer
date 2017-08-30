package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.bll.ChatBll;

/**
 * Created by PPX on 2017/6/21.
 */
public class ChatEventHandler implements CmdEventHandler{

    public void handle(SocketIOClient socketIOClient, String data) {

        ChatBll chatBll = new ChatBll(socketIOClient);

        String[] dataArray = data.split(" ");

        if(data.equals("liaotian\n")){

            chatBll.writeChatWindow();
        }else if(dataArray[0].equals("chat")){

            chatBll.chat(dataArray[1]);
        }
    }

}
