package com.tian.server.model;

import java.util.List;

/**
 * Created by PPX on 2017/9/7.
 */
public class TaskTrack {

    private Integer id; //任务id
    private Integer needLv; //需要的id
    private String  desc; //任务标题
    private Integer acceptCount; //可接次数
    private List<TaskTrackAction> trackActions; //任务节点列表

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNeedLv() {
        return needLv;
    }

    public void setNeedLv(Integer needLv) {
        this.needLv = needLv;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getAcceptCount() {
        return acceptCount;
    }

    public void setAcceptCount(Integer acceptCount) {
        this.acceptCount = acceptCount;
    }

    public List<TaskTrackAction> getTrackActions() {
        return trackActions;
    }

    public void setTrackActions(List<TaskTrackAction> trackActions) {
        this.trackActions = trackActions;
    }
}
