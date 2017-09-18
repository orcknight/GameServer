package com.tian.server.common;

/**
 * Created by PPX on 2017/9/8.
 */
public enum TaskRewardType {

    NONE("none"), //空值
    ATTRIBUTE("attribute"), //增加属性
    GOODS("goods"); //获取物品

    //私有变量，用来存储分配的值
    private String nCode;

    //构造函数传参
    TaskRewardType(String nCode){
        this.nCode = nCode;
    }

    @Override
    public String toString(){
        return this.nCode;
    }

}
