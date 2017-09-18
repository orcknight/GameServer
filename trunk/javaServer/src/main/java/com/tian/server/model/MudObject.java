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
    protected Integer weight = 0; //重量

    protected Map<String, Object> temp = new HashMap<String, Object>();
    protected Map<String, Object> apply = new HashMap<String, Object>();

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
}
