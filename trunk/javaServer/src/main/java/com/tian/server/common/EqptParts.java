package com.tian.server.common;

import javafx.scene.effect.Light;

/**
 * Created by PPX on 2017/7/5.
 */
public enum EqptParts {

    HEAD(1), ARMOR(2), CLOTHES(3), WAIST(4), PANTS(5), BOOTS(6), LEFTHAND(7), RIGHTHAND(8), WRIST(9), RINGS(10);

    //私有变量，用来存储分配的值
    private int nCode;

    //构造函数传参
    private EqptParts(int nCode){

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
