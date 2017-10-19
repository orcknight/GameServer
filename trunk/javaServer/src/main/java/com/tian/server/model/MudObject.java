package com.tian.server.model;

import com.tian.server.entity.RoomEntity;
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
    protected RoomEntity location = null;//物品所处的位置，在玩家身上时为null

    protected Map<String, Object> temp = new HashMap<String, Object>();
    protected Map<String, Object> apply = new HashMap<String, Object>();
    protected Map<String, Object> condApplyer = new HashMap<String, Object>();
    protected Map<String, Object> conditions = new HashMap<String, Object>();
    private Living lastApplyerName = null;
    private Living lastApplyerId = null;
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

    public RoomEntity getLocation() {
        return location;
    }

    public void setLocation(RoomEntity location) {
        this.location = location;
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
        }else{
            this.apply.put(prop, data);
        }
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
        Object old = null;

        if( prop.equals("combat_exp") || prop.equals("potential") ) {
            if(this.queryTemp("last_eat/exp") != null && Integer.parseInt(this.queryTemp("last_eat/exp").toString()) > 1) {
                data = (Integer)data * 4;
            }else{
                data = (Integer)data * 3;
            }
        }

        old = this.query(prop);
        if( old == null ) {
            set(prop, data);
            return;
        }

        set(prop, (Integer)old + (Integer)data);
    }

    public Map<String, Object> queryEntire(){
        return this.apply;
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
        }else{
            this.temp.put(prop, data);
        }
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

    public void addTemp(String prop, Object data) {

        Object old = this.queryTemp(prop);
        if( old == null ) {
            setTemp(prop, data);
            return;
        }

        setTemp(prop, (Integer)old + (Integer)data);
    }

    public Map<String ,Object> queryEntireTemp(){
        return this.temp;
    }

    public Object queryCondition(String cnd) {

        if (cnd == null || cnd.length() < 1) {
            return null;
        }

        return conditions.get(cnd);
    }

    public void clearCondition(String cnd) {

        if(cnd == null || cnd.length() < 1){

            this.conditions.clear();
            this.condApplyer = null;
            this.lastApplyerName = null;
            this.lastApplyerId = null;
            return;
        }

        if(conditions.containsKey(cnd)) {
            this.conditions.remove(cnd);
            if (this.condApplyer != null) {
                this.condApplyer.remove(cnd);
            }
        }
    }

    /*public Integer updateCondition() {
        String[] cnd;
        String[] last_applyer;
        int i, flag, update_flag;
        Object cnd_d;

        cnd = (String[])conditions.keySet().toArray();
        update_flag = 0;
        while (i--)
        {

            // In order to not casue player lost heart beat occasionally while
            // calling external condition daemons, we take careful calling
            // convention here.

            cnd_d = get_cnd_object(cnd[i]);
            if (! cnd_d)
            {
                if (cnd[i]) clear_condition(cnd[i]);
                continue;
            }

            // We assume since the condition daemon is loaded successfully, the
            // calling on its update_condition() should success as well. Because
            // catch() is somewhat costly, so we don't attempt to catch possible
            // error from the call_other. It is condition daemon's reponsibility
            // that don't cause error in users's heart beat.
            // If condition daemon returns 0 (or update_condition() not defined),
            // we can just assume the condition expired and remove it.

            if (cond_applyer && (last_applyer = cond_applyer[cnd[i]]))
            {
                last_applyer_id = last_applyer[0];
                last_applyer_name = last_applyer[1];
            } else
            {
                last_applyer_id = 0;
                last_applyer_name = 0;
            }

            flag = call_other(cnd_d, "update_condition", this_object(), conditions[cnd[i]]);
            if (! conditions)
            {
                update_flag |= flag;
                break;
            }
            if (! (flag & CND_CONTINUE))
            {
                clear_condition(cnd[i]);
            }
            update_flag |= flag;
        }

        return update_flag;
    }*/



    public Living getLastApplyerName() {
        return lastApplyerName;
    }

    public void setLastApplyerName(Living lastApplyerName) {
        this.lastApplyerName = lastApplyerName;
    }

    public Living getLastApplyerId() {
        return lastApplyerId;
    }

    public void setLastApplyerId(Living lastApplyerId) {
        this.lastApplyerId = lastApplyerId;
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
