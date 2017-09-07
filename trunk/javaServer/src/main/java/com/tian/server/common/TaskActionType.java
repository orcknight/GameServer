package com.tian.server.common;

/**
 * Created by PPX on 2017/9/7.
 */
public enum TaskActionType {

    TALK("talk"), //对话任务
    KILL("kill"); //杀怪物

    //私有变量，用来存储分配的值
    private String nCode;

    //构造函数传参
    TaskActionType(String nCode){
        this.nCode = nCode;
    }

    @Override
    public String toString(){
        return this.nCode;
    }
}
