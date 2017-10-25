package com.tian.server.service;

import com.tian.server.model.PlayerTask;
import com.tian.server.model.TaskTrack;
import com.tian.server.model.TaskTrackAction;
import com.tian.server.util.UserCacheUtil;
import net.sf.json.JSONObject;

/**
 * Created by PPX on 2017/9/8.
 */
public class TaskService {

    public JSONObject transPlayerTask(PlayerTask playerTask){

        JSONObject taskObject = new JSONObject();

        taskObject.put("id", playerTask.getTrack().getTrackId());
        TaskTrack taskTrack = UserCacheUtil.getTaskTrackMap().get(playerTask.getTrack().getTrackId());
        taskObject.put("desc", taskTrack.getDesc());
        TaskTrackAction trackAction = taskTrack.getTrackActions().get(playerTask.getAction().getActionId());
        JSONObject actionObject = new JSONObject();
        actionObject.put("id", trackAction.getId());
        actionObject.put("Desc", trackAction.getDesc());
        taskObject.put("action", actionObject);

        return taskObject;
    }



}
