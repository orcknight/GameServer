package com.tian.server.bll;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.model.PlayerTask;
import com.tian.server.model.TaskTrack;
import com.tian.server.util.UserCacheUtil;

import java.util.Map;

/**
 * Created by PPX on 2017/9/11.
 */
public class TaskBll extends BaseBll {

    public TaskBll(SocketIOClient socketIOClient) {
        super(socketIOClient);
    }

    /* 发放奖励 */
    public void doReward(Integer trackId, Integer trackActionId, Integer rewardId){

        //存储观察id
        Map<Integer, Living> cacheMap = UserCacheUtil.getPlayers();
        Player player = (Player) cacheMap.get(this.userId);
        if (player == null) {
            return;
        }

        //1.首先根据trackId找到玩家的PlayerTask
        PlayerTask playerTask = null;
        for(PlayerTask item : player.getTaskList()){
            if(item.getTrack().getTrackId() == trackId){
                playerTask = item;
                break;
            }
        }

        if(playerTask == null) {
            return;
        }

        //2.从task缓存中找到任务的taskTrack
        TaskTrack taskTrack = UserCacheUtil.getTaskTrackMap().get(trackId);
        if(taskTrack == null){
            return;
        }

        //3.发放奖励




    }

}
