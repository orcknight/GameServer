package com.tian.server.model;

import com.tian.server.common.GoodsType;
import com.tian.server.entity.GoodsEntity;

/**
 * Created by PPX on 2017/8/16.
 */
public class Goods extends MudObject {

    private GoodsEntity baseInfo; //物品基础类
    private Object refEntity;     //物品对应物品的实体类

    public GoodsEntity getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(GoodsEntity baseInfo) {
        this.baseInfo = baseInfo;
    }

    public Object getRefEntity() {
        return refEntity;
    }

    public void setRefEntity(Object refEntity) {
        this.refEntity = refEntity;
    }
}
