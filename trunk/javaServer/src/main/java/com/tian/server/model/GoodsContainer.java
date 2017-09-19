package com.tian.server.model;

import com.tian.server.entity.GoodsEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PPX on 2017/9/19.
 */
public class GoodsContainer extends MudObject {

    private GoodsEntity goodsEntity; //物品对应的
    private Integer count;

    protected Map<String ,String> actions = new HashMap<String, String>(); //动作

    public GoodsEntity getGoodsEntity() {
        return goodsEntity;
    }

    public void setGoodsEntity(GoodsEntity goodsEntity) {
        this.goodsEntity = goodsEntity;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Map<String, String> getActions() {
        return actions;
    }

    public void setActions(Map<String, String> actions) {
        this.actions = actions;
    }
}
