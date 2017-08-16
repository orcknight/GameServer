package com.tian.server.common;

/**
 * Created by PPX on 2017/8/16.
 */
public enum GoodsType {

    EQPT(1), //装备
    FOOD(2), //食物
    WATER(3); //饮水

    //私有变量，用来存储分配的值
    private int nCode;

    //构造函数传参
    private GoodsType(int nCode){

        this.nCode = nCode;
    }

    @Override
    public String toString(){

        return String.valueOf(this.nCode);
    }

    public int toInteger(){

        return this.nCode;
    }
}
