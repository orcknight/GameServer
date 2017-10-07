package com.tian.server.util;

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
}
