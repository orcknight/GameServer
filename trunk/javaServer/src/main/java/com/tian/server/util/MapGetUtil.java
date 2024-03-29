package com.tian.server.util;

import com.tian.server.model.MudObject;

import java.util.Map;

/**
 * Created by PPX on 2017/10/7.
 */
public class MapGetUtil {

    public static Integer getInteger(Map<String ,Object> map, String key){

        if(map.get(key) != null){
            if(map.get(key) instanceof Integer){
                return (Integer)map.get(key);
            }else{
                return 0;
            }
        }else{
            return 0;
        }
    }

    public static String getString(Map<String ,Object> map, String key){

        if(map.get(key) != null){
            if(map.get(key) instanceof String){
                return map.get(key).toString();
            }else{
                return "";
            }
        }else{
            return "";
        }
    }

    public static String queryString(MudObject ob, String key){

        if(ob.query(key) != null){
            if(ob.query(key) instanceof String){
                return ob.query(key).toString();
            }else{
                return "";
            }
        }else{
            return "";
        }
    }

    public static Integer queryInteger(MudObject ob, String key){

        if(ob.query(key) != null){
            if(ob.query(key) instanceof Integer){
                return (Integer)ob.query(key);
            }else{
                return 0;
            }
        }else{
            return 0;
        }
    }

    public static Integer queryTempInteger(MudObject ob, String key){

        if(ob.queryTemp(key) != null){
            if(ob.queryTemp(key) instanceof Integer){
                return (Integer)ob.queryTemp(key);
            }else{
                return 0;
            }
        }else{
            return 0;
        }
    }
}
