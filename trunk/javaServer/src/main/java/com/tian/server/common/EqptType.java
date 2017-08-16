package com.tian.server.common;

/**
 * Created by PPX on 2017/7/5.
 */
public enum EqptType {

    WEAPON(1), //武器
    ARMOR(2),  //护甲
    RINGS(3),  //戒指
    GEM(4);    //珠宝

    //私有变量，用来存储分配的值
    private int nCode;

    //构造函数传参
    private EqptType(int nCode){

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
