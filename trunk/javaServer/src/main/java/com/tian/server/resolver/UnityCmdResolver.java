package com.tian.server.resolver;

import com.corundumstudio.socketio.SocketIOClient;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.tian.server.handler.CmdEventHandler;
import net.sf.json.JSONObject;
import org.omg.CORBA.Object;

import java.util.Arrays;

/**
 * Created by PPX on 2017/8/30.
 */
public class UnityCmdResolver {


    private SocketIOClient server;
    private String[] userCmdArray = new String[] {"login", "createrole", "pianshu", "quit"};
    private String[] moveCmdArray = new String[] {"east", "west", "south", "north", "northeast", "northwest",
            "southeast", "southwest", "in", "out"};
    private String[] chatCmdArray = new String[] {"liaotian", "chat"};
    private String[] lookCmdArray = new String[] {"look", "open", "close"};
    private String[] fightCmdArray = new String[] {"fight"};
    private String[] taskCmdArray = new String[] {"reward"};

    public UnityCmdResolver(SocketIOClient server){

        this.server = server;
    }

    public String Resolver(JSONObject jsonObject) {

        String cmd = jsonObject.getString("cmd");
        JSONObject data = null;
       if(jsonObject.get("data") instanceof  JSONObject) {

           data = jsonObject.getJSONObject("data");
       }

        String handlerStr = "Default";
        Arrays.sort(userCmdArray);
        Arrays.sort(moveCmdArray);
        Arrays.sort(chatCmdArray);
        Arrays.sort(lookCmdArray);
        Arrays.sort(fightCmdArray);

        if(cmd.equals("checkversion")){

            handlerStr = "Default";
        }else if(Arrays.binarySearch(userCmdArray, cmd) > -1){

            handlerStr = "User";
        }else if(Arrays.binarySearch(moveCmdArray, cmd) > -1){

            handlerStr = "Move";
        }else if(Arrays.binarySearch(chatCmdArray, cmd) > -1){

            handlerStr = "Chat";
        }else if(Arrays.binarySearch(lookCmdArray, cmd) > -1){

            handlerStr = "Look";
        }else if(Arrays.binarySearch(fightCmdArray, cmd) > -1){

            handlerStr = "Combat";
        }else if(Arrays.binarySearch(taskCmdArray, cmd) > -1){

            handlerStr = "Task";
        }

        try {

            Class cls = Class.forName("com.tian.server.handler." + handlerStr + "EventHandler");
            CmdEventHandler handler = (CmdEventHandler)cls.newInstance();
            handler.handle(server, cmd, data);

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
