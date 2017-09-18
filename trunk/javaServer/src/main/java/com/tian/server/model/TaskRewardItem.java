package com.tian.server.model;

import com.tian.server.common.RewardAttributeType;
import com.tian.server.common.TaskRewardType;

/**
 * Created by PPX on 2017/9/8.
 */
public class TaskRewardItem {

    private String desc; //奖励描述
    private TaskRewardType rewardType;//奖励类型
    private Integer count; //数量 为物品时是物品的数量， 属性时是增加属性的数量

    private Integer goodsId; //物品id 奖励为物品时的奖励物品id
    private RewardAttributeType attrType; //奖励类型为属性时的属性类型

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public TaskRewardType getRewardType() {
        return rewardType;
    }

    public void setRewardType(TaskRewardType rewardType) {
        this.rewardType = rewardType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public RewardAttributeType getAttrType() {
        return attrType;
    }

    public void setAttrType(RewardAttributeType attrType) {
        this.attrType = attrType;
    }

}
