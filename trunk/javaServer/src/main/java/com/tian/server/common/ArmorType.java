package com.tian.server.common;

/**
 * Created by PPX on 2017/8/15.
 */
public enum ArmorType {

    HEAD(1),   //头
    NECK(2),  //脖子
    CLOTH(3), //衣服
    ARMOR(4), //装甲
    SURCOAT(5), //外套
    WAIST(6), //腰部
    WRISTS(7), //手腕
    SHIELD(8), //盾牌
    FINGER(9), //手掌
    HANDS(10), //手指
    BOOTS(11), //鞋
    PANTS(12), //裤子
    LEG(13);   //腿

    //私有变量，用来存储分配的值
    private int nCode;

    //构造函数传参
    private ArmorType(int nCode){

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
