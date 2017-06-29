package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.service.UserService;

/**
 * Created by PPX on 2017/6/9.
 */
public class UserEventHandler implements CmdEventHandler {

    public void handle(SocketIOClient socketIOClient, String data) {

        UserService userService = new UserService(socketIOClient);
        //登陆
        if(3 == getStringNumber("║", data)){

            String name = data.split("║")[0];
            String password = data.split("║")[1];
            userService.login(name, password);
        }else if(1 == getStringNumber("║001║", data)){ //创建角色

            String[] strArray   = data.split("║001║");
            String mySex        = strArray[0];
            String myName       = strArray[1];
            userService.createRole(myName, mySex);
        }else if(data.equals("quit\n")){ //创建角色

            userService.logout();
        }else{

        }
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
