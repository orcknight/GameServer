package com.tian.server.model;

import com.tian.server.entity.GoodsEntity;
import com.tian.server.entity.PlayerPackageEntity;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PPX on 2017/9/19.
 */
public class GoodsContainer extends MudObject {

    private GoodsEntity goodsEntity; //物品对应的
    private Integer count;
    private Integer belongsId; //所有人ID
    private PlayerPackageEntity belongsInfo; //归属信息
    private JSONObject attr = new JSONObject();


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

    public Integer getBelongsId() {
        return belongsId;
    }

    public void setBelongsId(Integer belongsId) {
        this.belongsId = belongsId;
    }

    public PlayerPackageEntity getBelongsInfo() {
        return belongsInfo;
    }

    public void setBelongsInfo(PlayerPackageEntity belongsInfo) {
        this.belongsInfo = belongsInfo;
    }

    public JSONObject getAttr() {
        return attr;
    }

    public void setAttr(JSONObject attr) {
        this.attr = attr;
    }
}
