package com.tian.server.model;

import com.tian.server.entity.PlayerEntity;

import java.util.List;

/**
 * Created by PPX on 2017/9/8.
 */
public class TaskReward {

    private Integer id; //奖励id
    private String desc; //所有奖励描述
    private List<TaskRewardItem> rewardItems; //所有的奖励

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<TaskRewardItem> getRewardItems() {
        return rewardItems;
    }

    public void setRewardItems(List<TaskRewardItem> rewardItems) {
        this.rewardItems = rewardItems;
    }

}
