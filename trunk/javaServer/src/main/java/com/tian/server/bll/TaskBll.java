package com.tian.server.bll;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.dao.PlayerDao;
import com.tian.server.dao.PlayerTrackActionDao;
import com.tian.server.dao.PlayerTrackDao;
import com.tian.server.entity.PlayerEntity;
import com.tian.server.entity.PlayerTrackActionEntity;
import com.tian.server.entity.PlayerTrackEntity;
import com.tian.server.model.*;
import com.tian.server.util.UserCacheUtil;
import com.tian.server.util.XmlUtil;
import org.hibernate.Transaction;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/9/11.
 */
public class TaskBll extends BaseBll {

    private PlayerTrackDao playerTrackDao = new PlayerTrackDao();
    private PlayerTrackActionDao playerTrackActionDao = new PlayerTrackActionDao();

    public TaskBll(SocketIOClient socketIOClient) {
        super(socketIOClient);
    }

    public void getTaskList(){

        Player me = (Player)UserCacheUtil.getPlayers().get(this.userId);
        List<PlayerTask> taskList = me.getTaskList();
    }

    /* 发放奖励 */
    public void doReward(Integer trackId, Integer trackActionId, Integer rewardId){

        //1.获取当前玩家
        Map<Integer, Living> cacheMap = UserCacheUtil.getPlayers();
        Player player = (Player) cacheMap.get(this.userId);
        if (player == null) {
            return;
        }

        Transaction transaction = getSession().getTransaction();

        try {
            transaction.begin();

            //2.发放奖励更新任务状态
            grantRewards(rewardId, player);

            //3.发放完奖励以后,更新任务状态，并设置新可接任务
            updateTask(trackId, trackActionId, player);

            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
        }
    }

    //发放奖励
    private void grantRewards(Integer rewardId, Player player){

        TaskReward taskReward = XmlUtil.loadRewardFromXml(rewardId.toString());
        for(TaskRewardItem taskRewardItem : taskReward.getRewardItems()){

            switch(taskRewardItem.getRewardType()){
                case ATTRIBUTE:
                    addAttributeToPlayer(player, taskRewardItem);
                    break;
                case GOODS:
                    break;
            }

        }

    }

    //更新任务状态
    private void updateTask(Integer trackId, Integer trackActionId, Player player){

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

        //2.检查action, 更改状态
        PlayerTrackActionEntity trackActionEntity = playerTask.getAction();
        if(trackActionEntity.getActionId() == trackActionId){

            trackActionEntity.setStatus(2);
            playerTrackActionDao.update(trackActionEntity);
        }

        //3.从task缓存中找到任务的taskTrack
        TaskTrack taskTrack = UserCacheUtil.getTaskTrackMap().get(trackId);
        if(taskTrack == null){
            return;
        }

        //4.检查当前action是否是任务的最后一个action
        if(taskTrack.getTrackActions().size() == trackActionId){

            //把位置状态设置为已经完成
            PlayerTrackEntity playerTrackEntity = playerTask.getTrack();
            playerTrackEntity.setStatus(1);
            playerTrackDao.update(playerTrackEntity);

            //任务完成移除当前任务
            player.getTaskList().remove(playerTask);

            //添加新任务添加新任务条目
            if(taskTrack.getNextTrackId() >1){

                TaskTrack nextTaskTrack = UserCacheUtil.getTaskTrackMap().get(taskTrack.getNextTrackId());
                PlayerTrackEntity nextPlayerTrackEntity = new PlayerTrackEntity();
                nextPlayerTrackEntity.setStatus(0);
                nextPlayerTrackEntity.setLineId(nextTaskTrack.getLineId());
                nextPlayerTrackEntity.setPlayerId(player.getPlayerId());
                nextPlayerTrackEntity.setStep(1);
                nextPlayerTrackEntity.setTrackId(nextTaskTrack.getId());
                playerTrackDao.add(nextPlayerTrackEntity);

                TaskTrackAction nextTaskTrackAction = nextTaskTrack.getTrackActions().get(0);
                PlayerTrackActionEntity nextPlayerTrackAction = new PlayerTrackActionEntity();
                nextPlayerTrackAction.setPid(nextPlayerTrackEntity.getId());
                nextPlayerTrackAction.setActionId(nextTaskTrackAction.getId());
                nextPlayerTrackAction.setStatus(0);
                nextPlayerTrackAction.setProgress(0);
                nextPlayerTrackAction.setCreateTime(new Timestamp(System.currentTimeMillis()));
                nextPlayerTrackAction.setFinishTime(new Timestamp(System.currentTimeMillis()));
                playerTrackActionDao.add(nextPlayerTrackAction);

                PlayerTask nextPlayerTask = new PlayerTask();
                nextPlayerTask.setTrack(nextPlayerTrackEntity);
                nextPlayerTask.setAction(nextPlayerTrackAction);

                player.getTaskList().add(nextPlayerTask);
            }

        }else{

            //添加新任务条目
            TaskTrackAction taskTrackAction = taskTrack.getTrackActions().get(trackActionId);
            PlayerTrackActionEntity playerTrackActionEntity = new PlayerTrackActionEntity();
            playerTrackActionEntity.setStatus(0);
            playerTrackActionEntity.setActionId(taskTrackAction.getId());
            playerTrackActionEntity.setPid(playerTask.getTrack().getId());
            playerTrackActionEntity.setProgress(0);
            playerTrackActionEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            playerTrackActionEntity.setFinishTime(new Timestamp(System.currentTimeMillis()));

            playerTrackActionDao.add(playerTrackActionEntity);
            playerTask.setAction(playerTrackActionEntity);
        }

        //3.更改当前TrackAction的状态，并持久化到数据库
        TaskTrackAction taskTrackAction = taskTrack.getTrackActions().get(trackActionId-1);
    }

    private void addAttributeToPlayer(Player player, TaskRewardItem taskRewardItem){

        switch(taskRewardItem.getAttrType()){
            case EXP:

                //计算经验
                Integer newExp = player.getCombatExp() + taskRewardItem.getCount();
                if(player.getCombatExp()> 0 && newExp < 0){

                    newExp = 2147483647;
                }
                //设置经验
                player.setCombatExp(newExp);

                //持久化经验
                PlayerDao playerDao = new PlayerDao();
                PlayerEntity playerEntity = playerDao.getById(player.getPlayerId());
                playerEntity.setCombatExp(player.getCombatExp());
                playerDao.update(playerEntity);
        }
    }

}
