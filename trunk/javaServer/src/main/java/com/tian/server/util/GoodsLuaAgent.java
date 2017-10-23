package com.tian.server.util;

import com.tian.server.common.GoodsType;
import com.tian.server.entity.PlayerPackageEntity;
import com.tian.server.model.BodyPart;
import com.tian.server.model.GoodsContainer;
import com.tian.server.service.ObjectService;
import net.sf.json.JSONObject;
import org.hibernate.Transaction;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/9/19.
 */
public class GoodsLuaAgent {

    public static void addAction(String uuid, String action, String callback){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer != null){
            Map<String ,String> actions = goodsContainer.getCmdActions();
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

    public static void addTypes(String uuid, String type){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer != null){
            List<Integer> types = goodsContainer.getTypes();
            Integer typeValue = GoodsType.valueOf(type.toUpperCase()).toInteger();
            if(!types.contains(typeValue)){
                types.add(typeValue);
            }
        }
    }

    public static void setDefaultClone(String uuid, String value){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer != null){
            goodsContainer.setDefaultClone(value);
        }
    }

    public static void setCuttable(String uuid, Integer value){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer != null){
            goodsContainer.setCuttable(Boolean.valueOf(value.toString()));
        }
    }

    public static void setName(String uuid, String name){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer != null){
            goodsContainer.getGoodsEntity().setName(name);
        }
    }

    public static void setCmdName(String uuid, String cmdName){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer != null){
            goodsContainer.getGoodsEntity().setCmdName(cmdName);
        }
    }

    public static void set(String uuid, String key, Object data){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer != null){
            goodsContainer.set(key, data);
        }
    }

    public static String queryString(String uuid, String key){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer == null){
            return "";
        }
        return MapGetUtil.queryString(goodsContainer, key);
    }

    public static void delete(String uuid, String key){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer != null){
            goodsContainer.delete(key);
        }
    }

    public static void setUnit(String uuid, String unit){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer != null){
            goodsContainer.getGoodsEntity().setUnit(unit);
        }
    }

    public static void setLongDesc(String uuid, String longDesc){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer != null){
            goodsContainer.getGoodsEntity().setLongDesc(longDesc);
        }
    }

    public static void setParts(String uuid, String key, String data){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer != null){
            JSONObject jsonObject = JSONObject.fromObject(data);
            Integer level = jsonObject.getInt("level");
            String unit = jsonObject.getString("unit");
            String name = jsonObject.getString("name");
            String leftName = jsonObject.getString("leftName");
            String leftId = jsonObject.getString("leftId");
            String verbOfPart = jsonObject.getString("verbOfPart");
            String cloneObject = jsonObject.getString("cloneObject");

            BodyPart part = new BodyPart();
            part.setLevel(level);
            part.setUnit(unit);
            part.setName(name);
            part.setLeftName(leftName);
            part.setLeftId(leftId);
            part.setVerbOfPart(verbOfPart);
            part.setCloneObject(verbOfPart);
            JSONObject componentsObject = jsonObject.getJSONObject("components");
            Iterator iterator = componentsObject.keys();
            while(iterator.hasNext()){
                String tempKey = (String) iterator.next();
                String tempValue = componentsObject.getString(tempKey);
                part.getComponentsMap().put(tempKey, tempValue);
            }

            goodsContainer.getParts().put(key, part);
        }
    }

    public static void setWeight(String uuid, Integer weight) {
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer != null) {
            goodsContainer.setWeight(weight);
        }
    }

    public static void createDecayTask(String uuid, Integer seconds, Integer phase){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer != null) {
            goodsContainer.createDecayTask(seconds, phase);
        }
    }

    public static String getLocationName(String uuid){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer == null) {
            return "";
        }
        return goodsContainer.getLocation().getName();
    }

    public static String getName(String uuid){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer == null) {
            return "";
        }
        return goodsContainer.getGoodsEntity().getName();
    }

    public static void destructGoods(String uuid){
        GoodsContainer goodsContainer  = (GoodsContainer)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(goodsContainer == null) {
            return ;
        }

        ObjectService objectService = new ObjectService();
        Transaction transaction = SessionUtil.getDataSession().getTransaction();
        try{
            transaction.begin();
            objectService.destruct(goodsContainer);
            transaction.commit();
        }catch(Exception e){
            transaction.rollback();
        }
    }

}
