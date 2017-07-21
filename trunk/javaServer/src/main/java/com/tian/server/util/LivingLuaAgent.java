package com.tian.server.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Iterator;

/**
 * Created by PPX on 2017/7/4.
 */
public class LivingLuaAgent {

    public static void info(String msg){

        System.out.println(msg);
    }

    public static void setButtons(String jsonStr){

        JSONArray jsonArray = JSONArray.fromObject(jsonStr);
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = (String)jsonObject.get(key);

                System.out.println(key);
                System.out.println(value);
            }
            //String tile = jsonObject.getString("tile");
            //System.out.println(jsonObject.get("tile"));
            //System.out.println(jsonObject.get("name"));
        }
    }


}
