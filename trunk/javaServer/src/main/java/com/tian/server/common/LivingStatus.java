package com.tian.server.common;

/**
 * Created by PPX on 2017/9/24.
 */
public enum LivingStatus {

    NORMAL(1); //正常存活状态

    //私有变量，用来存储分配的值
    private int nCode;

    //构造函数传参
    private LivingStatus(int nCode){
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
