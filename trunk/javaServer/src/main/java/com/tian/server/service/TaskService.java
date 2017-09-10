package com.tian.server.service;

import com.tian.server.common.TaskActionType;
import com.tian.server.dao.PlayerTrackActionDao;
import com.tian.server.dao.PlayerTrackDao;
import com.tian.server.entity.PlayerTrackEntity;
import com.tian.server.model.Player;
import com.tian.server.model.PlayerTask;
import com.tian.server.model.TaskTrack;
import com.tian.server.util.UserCacheUtil;

import java.util.List;

/**
 * Created by PPX on 2017/9/8.
 */
public class TaskService {

    private PlayerTrackDao playerTrackDao = new PlayerTrackDao();
    private PlayerTrackActionDao playerTrackActionDao = new PlayerTrackActionDao();

    public void loadTaskInfo(Player player){

        List<PlayerTask> playerTasks = player.getTaskList();
        List<PlayerTrackEntity> playerTrackEntityList = playerTrackDao.getDoingTaskByPlayerId(player.getPlayerId());
        for(PlayerTrackEntity playerTrackEntity : playerTrackEntityList){

            TaskTrack taskTrack = UserCacheUtil.getTaskTrackMap().get(playerTrackEntity.getTrackId());

            if(taskTrack.getTrackActions().get(playerTrackEntity.getStep()-1).getActionType().equals(TaskActionType.TALK)){

                player.setTalkTaskCount(player.getTalkTaskCount() + 1);
            }else if(taskTrack.getTrackActions().get(playerTrackEntity.getStep()-1).getActionType().equals(TaskActionType.KILL)){

                player.setKillTaskCount(player.getKillTaskCount() + 1);
            }

            PlayerTask playerTask = new PlayerTask();
            playerTask.setTrack(playerTrackEntity);
            playerTask.setAction(playerTrackActionDao.getDoingTrackActionByTrackId(playerTrackEntity.getId()));
            playerTasks.add(playerTask);
        }
    }

}
