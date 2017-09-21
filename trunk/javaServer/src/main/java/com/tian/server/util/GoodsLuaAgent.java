package com.tian.server.util;

import com.tian.server.model.GoodsContainer;
import net.sf.json.JSONObject;

import java.util.Map;

/**
 * Created by PPX on 2017/9/19.
 */
public class GoodsLuaAgent {

    public static void addAction(String uuid, String action, String callback){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer != null){
            Map<String ,String> actions = goodsContainer.getActions();
            actions.put(action, callback);
        }
    }

    public static void addAttr(String uuid, String name, Object value){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer != null){
            JSONObject jsonObject = goodsContainer.getAttr();
            jsonObject.put(name, value);
        }
    }

}
