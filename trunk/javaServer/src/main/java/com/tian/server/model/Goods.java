package com.tian.server.model;

import com.tian.server.common.GoodsType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PPX on 2017/8/16.
 */
public class Goods extends MudObject {

    protected GoodsType type;
    protected String name;
    protected String cmdName;
    protected String unit;
    protected GoodsPrice price; //价格
    protected String material; //材料
    protected String longDesc; //描述
    protected String resource; //脚本文件路径
    protected String pathName; //路径标识的全局唯一名称
    protected Boolean stackable; //是否可以叠加，药品类等物品可以，装备类无法叠加
    protected Integer deadline; //限时时间
    protected Integer amount; //数量

    protected Map<String ,String> actions = new HashMap<String, String>();

    public GoodsType getType() {
        return type;
    }

    public void setType(GoodsType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCmdName() {
        return cmdName;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public GoodsPrice getPrice() {
        return price;
    }

    public void setPrice(GoodsPrice price) {
        this.price = price;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public Boolean getStackable() {
        return stackable;
    }

    public void setStackable(Boolean stackable) {
        this.stackable = stackable;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }
}
