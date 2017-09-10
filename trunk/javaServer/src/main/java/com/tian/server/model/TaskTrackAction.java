package com.tian.server.model;

import com.tian.server.common.TaskActionType;

/**
 * Created by PPX on 2017/9/7.
 */
public class TaskTrackAction {

    private Integer id; //任务节点的id
    private TaskActionType actionType; //任务节点类型
    private String desc; //任务节点的详细描述

    private Integer targetId; //任务目标id
    private Integer targetCount; //任务目标数量
    private String targetMap; //任务目标所在的房间名称
    private Integer storyId; //剧情对话的id
    private Integer rewardId; //奖励id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TaskActionType getActionType() {
        return actionType;
    }

    public void setActionType(TaskActionType actionType) {
        this.actionType = actionType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Integer getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(Integer targetCount) {
        this.targetCount = targetCount;
    }

    public String getTargetMap() {
        return targetMap;
    }

    public void setTargetMap(String targetMap) {
        this.targetMap = targetMap;
    }

    public Integer getStoryId() {
        return storyId;
    }

    public void setStoryId(Integer storyId) {
        this.storyId = storyId;
    }

    public Integer getRewardId() {
        return rewardId;
    }

    public void setRewardId(Integer rewardId) {
        this.rewardId = rewardId;
    }
}
