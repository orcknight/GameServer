package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.service.MoveService;
import com.tian.server.service.UserService;

/**
 * Created by PPX on 2017/6/16.
 */
public class MoveEventHandler implements CmdEventHandler{

    public void handle(SocketIOClient socketIOClient, String data) {

        MoveService userService = new MoveService(socketIOClient);
        if(data.equals("east\n")){
            userService.moveEast();
        }else if(data.equals("south\n")){
            userService.moveSouth();
        }else if(data.equals("north\n")){
            userService.moveNorth();
        }else if(data.equals("west\n")){
            userService.moveWest();
        }else if(data.equals("northeast\n")){
            userService.moveNorthEast();
        }else if(data.equals("northwest\n")){
            userService.moveNorthWest();
        }else if(data.equals("southeast\n")){
            userService.moveSouthEest();
        }else if(data.equals("southwest\n")){
            userService.moveSouthWest();
        }else if(data.equals("in\n")){
            userService.moveIn();
        }else if(data.equals("out\n")){
            userService.moveOut();
        }
    }
}
