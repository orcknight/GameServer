package com.tian.server.model;

import com.tian.server.entity.GoodsEntity;
import com.tian.server.entity.PlayerPackageEntity;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private Map<String ,BodyPart> parts = new HashMap<String, BodyPart>();
    private Boolean cuttable = false;
    private List<Integer> types = new ArrayList<Integer>();
    private String defaultClone;

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

    public Map<String, BodyPart> getParts() {
        return parts;
    }

    public void setParts(Map<String, BodyPart> parts) {
        this.parts = parts;
    }

    public Boolean getCuttable() {
        return cuttable;
    }

    public void setCuttable(Boolean cuttable) {
        this.cuttable = cuttable;
    }

    public List<Integer> getTypes() {
        return types;
    }

    public void setTypes(List<Integer> types) {
        this.types = types;
    }

    public String getDefaultClone() {
        return defaultClone;
    }

    public void setDefaultClone(String defaultClone) {
        this.defaultClone = defaultClone;
    }
}
