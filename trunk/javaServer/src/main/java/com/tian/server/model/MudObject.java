package com.tian.server.model;

import org.apache.commons.collections.map.HashedMap;

import java.util.Arrays;
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

    public Object query(String prop){

        Object data;
        if(this.apply.get(prop) == null && (prop.indexOf('/') != -1)){
            data = _query(this.apply, prop.split("/"));
        }else{
            data = this.apply.get(prop);
        }

        return data;
    }

    public void set(String prop, Object data){

        if( prop.indexOf('/') != -1 ) {

            String[] props = prop.split("/");
            _set( this.apply, props, data );
        }

        this.apply.put(prop, data);
    }

    public void delete(String prop){

        String[] props;
        if( prop.indexOf('/') != -1 ) {
            props = prop.split("/");
            _delete(this.apply, props);
        } else {
            this.apply.remove(prop);
        }
    }

    public void add(String prop, Object data) {
        mixed old;

        if( prop.equals("combat_exp") || prop.equals("potential") ) {
            if(this.queryTemp("last_eat/exp") != null && query_temp("last_eat/exp") > 1)
            {
                data = data * 4;
            }else
            {
                data = data * 3;
            }
        }
        if( !mapp(dbase) || !(old = query(prop, 1)) )
            return set(prop, data);

        if( functionp(old) )
            error("dbase: add() - called on a function type property.\n");

        return set(prop, old + data);
    }

    public Object queryTemp(String prop){

        Object data;
        if(this.temp.get(prop) == null && (prop.indexOf('/') != -1)){
            data = _query(this.temp, prop.split("/"));
        }else{
            data = this.temp.get(prop);
        }

        return data;
    }

    public void setTemp(String prop, Object data){

        if( prop.indexOf('/') != -1 ) {
            String[] props = prop.split("/");
            _set( this.temp, props, data );
        }

        this.temp.put(prop, data);
    }

    public void deleteTemp(String prop){

        String[] props;
        if( prop.indexOf('/') != -1 ) {
            props = prop.split("/");
            _delete(this.temp, props);
        } else {
            this.temp.remove(prop);
        }
    }

    public Map<String, String> getCmdActions() {
        return cmdActions;
    }

    public void setCmdActions(Map<String, String> cmdActions) {
        this.cmdActions = cmdActions;
    }

    private void _set( Map<String, Object> map, String[] parts, Object value ) {

        if( parts.length == 1 ) {
            map.put(parts[0],value);
            return;
        }

        if( map.get(parts[0]) == null || ! (map.get(parts[0]) instanceof  Map)) {
            map.put(parts[0], new HashMap<String, Object>());
        }

        String[] loopParts = new String[parts.length - 1];
        System.arraycopy(parts, 1, loopParts, 0, parts.length - 1);

        _set( (Map<String, Object>)map.get(parts[0]), loopParts, value );
    }

    private Boolean _delete( Map<String, Object> map, String[] parts )
    {
        if( parts.length  == 1 ) {
            map.remove(parts[0]);
            return true;
        }
        if( map.get(parts[0]) == null || ! (map.get(parts[0]) instanceof  Map) ) {
            return false;
        }

        String[] loopParts = new String[parts.length - 1];
        return _delete( (Map<String, Object>)map.get(parts[0]), loopParts );
    }

    private Object _query( Map<String, Object> map, String[] parts ) {
        int i;

        Object value = map;
        for( i = 0 ; i < parts.length ; i++ ) {

            value = ((Map<String, Object>)value).get(parts[i]);
            if(value == null){
                break;
            }

            if(!(value instanceof  Map)){
                break;
            }
        }
        return value;
    }

}
