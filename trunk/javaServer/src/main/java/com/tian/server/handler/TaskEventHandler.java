package com.tian.server.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.bll.TaskBll;
import net.sf.json.JSONObject;

/**
 * Created by PPX on 2017/9/11.
 */
public class TaskEventHandler implements CmdEventHandler{

    public void handle(SocketIOClient socketIOClient, String cmd, JSONObject data) {

        TaskBll taskBll = new TaskBll(socketIOClient);
        //获取奖励
        if(cmd.equals("reward")){

            Integer trackId = Integer.parseInt(data.getString("trackId"));
            Integer trackActionId = Integer.parseInt(data.getString("trackActionId"));
            Integer rewardId = Integer.parseInt(data.getString("rewardId"));

            taskBll.doReward(trackId, trackActionId, rewardId);
        }

    }

}
