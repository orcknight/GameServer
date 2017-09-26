package com.tian.server.model;

import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PPX on 2017/8/8.
 */
public class MudObject {

    protected Long uuid = 0L; //实例在系统里的唯一标识
    protected Integer id = 0; //标识号，同种物品共享一个id
    protected Integer weight = 0; //
    private String resource;

    protected Map<String, Object> temp = new HashMap<String, Object>();
    protected Map<String, Object> apply = new HashMap<String, Object>();
    protected Map<String ,String> cmdActions = new HashMap<String, String>(); //lua文件里定义的：命令-函数映射

    //属性
    public Long getUuid() {
        return this.uuid;
    }
    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Object queryTemp(String key){
        return temp.get(key);
    }

    public Object query(String key){
        return apply.get(key);
    }

    public void set(String key, Object value){
        apply.put(key, value);
    }

    public void setTemp(String key, Object value){
        temp.put(key, value);
    }

    public void deleteTemp(String key){
        temp.remove(key);
    }

    public Map<String, String> getCmdActions() {
        return cmdActions;
    }

    public void setCmdActions(Map<String, String> cmdActions) {
        this.cmdActions = cmdActions;
    }
}
