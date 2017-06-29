package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.service.ChatService;

/**
 * Created by PPX on 2017/6/21.
 */
public class ChatEventHandler implements CmdEventHandler{

    public void handle(SocketIOClient socketIOClient, String data) {

        ChatService chatService = new ChatService(socketIOClient);

        String[] dataArray = data.split(" ");

        if(data.equals("liaotian\n")){

            chatService.writeChatWindow();
        }else if(dataArray[0].equals("chat")){

            chatService.chat(dataArray[1]);
        }
    }

}
