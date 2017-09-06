package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.bll.MoveBll;
import net.sf.json.JSONObject;

/**
 * Created by PPX on 2017/6/16.
 */
public class MoveEventHandler implements CmdEventHandler{

    public void handle(SocketIOClient socketIOClient, String cmd, JSONObject data) {

        MoveBll moveBll = new MoveBll(socketIOClient);
        if(cmd.equals("east")){
            moveBll.moveEast();
        }else if(cmd.equals("south")){
            moveBll.moveSouth();
        }else if(cmd.equals("north")){
            moveBll.moveNorth();
        }else if(cmd.equals("west")){
            moveBll.moveWest();
        }else if(cmd.equals("northeast")){
            moveBll.moveNorthEast();
        }else if(cmd.equals("northwest")){
            moveBll.moveNorthWest();
        }else if(cmd.equals("southeast")){
            moveBll.moveSouthEest();
        }else if(cmd.equals("southwest")){
            moveBll.moveSouthWest();
        }else if(cmd.equals("in")){
            moveBll.moveIn();
        }else if(cmd.equals("out")){
            moveBll.moveOut();
        }
    }
}
