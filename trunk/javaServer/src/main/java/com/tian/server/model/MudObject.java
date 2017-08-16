package com.tian.server.model;

import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PPX on 2017/8/8.
 */
public class MudObject {

    public Map<String, Object> apply = new HashMap<String, Object>();
    public Map<String, Object> temp = new HashMap<String, Object>();
    protected Map<String, Object> apply = new HashMap<String, Object>();

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
}
