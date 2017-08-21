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
    ArmorType(int nCode){

        this.nCode = nCode;
    }

    @Override
    public String toString(){

        return String.valueOf(this.nCode);
    }

    public int toInteger(){

        return this.nCode;
    }

    public static ArmorType valueOf(int value) {    //手写的从int到enum的转换函数
        switch (value) {
            case 1:
                return HEAD;
            case 2:
                return NECK;
            case 3:
                return CLOTH;
            case 4:
                return ARMOR;
            case 5:
                return SURCOAT;
            case 6:
                return WAIST;
            case 7:
                return WRISTS;
            case 8:
                return SHIELD;
            case 9:
                return FINGER;
            case 10:
                return HANDS;
            case 11:
                return BOOTS;
            case 12:
                return PANTS;
            case 13:
                return LEG;
            default:
                return null;
        }
    }
}
