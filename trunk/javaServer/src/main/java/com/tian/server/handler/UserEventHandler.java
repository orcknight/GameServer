package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.bll.UserBll;
import net.sf.json.JSONObject;

/**
 * Created by PPX on 2017/6/9.
 */
public class UserEventHandler implements CmdEventHandler {

    public void handle(SocketIOClient socketIOClient, String cmd, JSONObject data) {

        UserBll userBll = new UserBll(socketIOClient);
        //登陆
        if(cmd.equals("login")){

            String name = data.getString("userName");
            String password = data.getString("password");
            userBll.login(name, password);
        }else if(cmd.equals("createrole")){ //创建角色

            String mySex        = data.getString("sex");
            String myName       = data.getString("name");
            userBll.createRole(myName, mySex);
        }else if(cmd.equals("quit")){ //退出游戏

            userBll.logout();
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
