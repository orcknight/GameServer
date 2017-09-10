package com.tian.server.common;

/**
 * Created by PPX on 2017/9/8.
 */
public enum RewardAttributeType {

    MONEY("money"), //金钱
    EXP("exp"), //经验
    QI("qi"),   //气血
    NEILI("neili"), //内力
    JING("jing"), //精神
    JINGLI("jingli"), //精力
    POTENTIAL("potential"); //潜能

    //私有变量，用来存储分配的值
    private String nCode;

    //构造函数传参
    RewardAttributeType(String nCode){
        this.nCode = nCode;
    }

    @Override
    public String toString(){
        return this.nCode;
    }

}
