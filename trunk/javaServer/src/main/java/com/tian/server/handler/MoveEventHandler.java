package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.bll.MoveBll;

/**
 * Created by PPX on 2017/6/16.
 */
public class MoveEventHandler implements CmdEventHandler{

    public void handle(SocketIOClient socketIOClient, String data) {

        MoveBll moveBll = new MoveBll(socketIOClient);
        if(data.equals("east\n")){
            moveBll.moveEast();
        }else if(data.equals("south\n")){
            moveBll.moveSouth();
        }else if(data.equals("north\n")){
            moveBll.moveNorth();
        }else if(data.equals("west\n")){
            moveBll.moveWest();
        }else if(data.equals("northeast\n")){
            moveBll.moveNorthEast();
        }else if(data.equals("northwest\n")){
            moveBll.moveNorthWest();
        }else if(data.equals("southeast\n")){
            moveBll.moveSouthEest();
        }else if(data.equals("southwest\n")){
            moveBll.moveSouthWest();
        }else if(data.equals("in\n")){
            moveBll.moveIn();
        }else if(data.equals("out\n")){
            moveBll.moveOut();
        }
    }
}
