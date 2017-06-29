package com.tian.server.resolver;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.handler.CmdEventHandler;

import java.util.Arrays;

/**
 * Created by PPX on 2017/6/8.
 */
public class CmdResolver {

    private SocketIOClient server;
    private String[] userCmdArray = new String[] {"pianshu"};
    private String[] moveCmdArray = new String[] {"east", "west", "south", "north", "northeast", "northwest",
    "southeast", "southwest", "in", "out"};
    private String[] chatCmdArray = new String[] {"liaotian", "chat"};

    public CmdResolver(SocketIOClient server){

        this.server = server;
    }

    public String Resolver(String data) {

        String handlerStr = "Default";
        StringBuffer msg = new StringBuffer(data);
        int index = msg.toString().lastIndexOf("\n");
        msg.deleteCharAt(index);
        String str = msg.toString();
        Arrays.sort(userCmdArray);
        Arrays.sort(moveCmdArray);
        Arrays.sort(chatCmdArray);

        if(str.length() == 0){

            handlerStr = "Default";
        }else if(3 == getStringNumber("║", str) || 1 == getStringNumber("║001║", str)
                || Arrays.binarySearch(userCmdArray, str.split(" ")[0]) > -1){

            handlerStr = "User";
        }else if(Arrays.binarySearch(moveCmdArray, str.split(" ")[0]) > -1){

            handlerStr = "Move";
        }else if(Arrays.binarySearch(chatCmdArray, str.split(" ")[0]) > -1){

            handlerStr = "Chat";
        }

        try {

            Class cls = Class.forName("com.tian.server.handler." + handlerStr + "EventHandler");
            CmdEventHandler handler = (CmdEventHandler)cls.newInstance();
            handler.handle(server, data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public int getStringNumber(String patterns, String source){

        String str = source;
        int count = 0;
        if (str.indexOf(patterns)==-1) {

            return 0;
        }

        while(str.indexOf(patterns) != -1) {

            count++;
            str = str.substring(str.indexOf(patterns) + patterns.length());
        }

        return count;
    }

}
