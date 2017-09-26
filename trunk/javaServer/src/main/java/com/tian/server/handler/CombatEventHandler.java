package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.bll.CombatBll;
import net.sf.json.JSONObject;

/**
 * Created by PPX on 2017/7/11.
 */
public class CombatEventHandler implements CmdEventHandler {

    public void handle(SocketIOClient socketIOClient, String cmd, JSONObject data) {

        CombatBll combatBll = new CombatBll(socketIOClient);

        if(cmd.equals("fight")){

            String target = data. getString("target");
            combatBll.handleFight(target);
        }

    }
}
