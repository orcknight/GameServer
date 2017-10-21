package com.tian.server.util;

import com.tian.server.entity.GoodsEntity;
import com.tian.server.entity.PlayerPackageEntity;
import com.tian.server.model.GoodsContainer;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/9/19.
 */
public class GoodsManager {

    private static Map<Integer, GoodsEntity> goodsEntityMap = new HashMap<Integer, GoodsEntity>(); //物品id关联表
    private static Map<String, GoodsEntity> goodsPathMap = new HashMap<String, GoodsEntity>(); //物品路径名称关联表

    public void initData(List<GoodsEntity> goodsEntityList){

        for(GoodsEntity goodsEntity : goodsEntityList){

            goodsEntityMap.put(goodsEntity.getId(), goodsEntity);
            goodsPathMap.put(goodsEntity.getPathName(), goodsEntity);
        }
    }

    public GoodsContainer createById(Integer id, Integer count, PlayerPackageEntity belongsInfo){

        GoodsEntity goodsEntity = goodsEntityMap.get(id);
        if(goodsEntity == null){

            return null;
        }

        return createGoodsContainer(goodsEntity, count, belongsInfo);

    }

    public GoodsContainer createById(Integer id, Long uuid, Integer count, PlayerPackageEntity belongsInfo){

        GoodsEntity goodsEntity = goodsEntityMap.get(id);
        if(goodsEntity == null){

            return null;
        }

        return createGoodsContainer(goodsEntity, uuid, count, belongsInfo);

    }

    public GoodsContainer createByPathName(String pathName, Integer count, PlayerPackageEntity belongsInfo){

        GoodsEntity goodsEntity = goodsPathMap.get(pathName);
        if(goodsEntity == null){

            return null;
        }

        return createGoodsContainer(goodsEntity, count, belongsInfo);
    }

    public GoodsContainer createByPathName(String pathName, Long uuid, Integer count, PlayerPackageEntity belongsInfo){

        GoodsEntity goodsEntity = goodsPathMap.get(pathName);
        if(goodsEntity == null){

            return null;
        }

        return createGoodsContainer(goodsEntity, uuid, count, belongsInfo);
    }

    private GoodsContainer createGoodsContainer(GoodsEntity goodsEntity, Integer count, PlayerPackageEntity belongsInfo){

        return createGoodsContainer(goodsEntity, IdUtil.getUUID(), count, belongsInfo) ;
    }

    private GoodsContainer createGoodsContainer(GoodsEntity goodsEntity, Long uuid, Integer count, PlayerPackageEntity belongsInfo){

        GoodsContainer goodsContainer = new GoodsContainer();
        goodsContainer.setUuid(uuid);
        goodsContainer.setCount(count);
        goodsContainer.setBelongsInfo(belongsInfo);
        goodsContainer.setGoodsEntity(goodsEntity);
        goodsContainer.setResource(goodsEntity.getResource());
        if(belongsInfo != null){
            goodsContainer.setBelongsId(belongsInfo.getPlayerId());
        }
        //放到物品缓存中
        UserCacheUtil.getAllObjects().put(goodsContainer.getUuid(), goodsContainer);

        LuaBridge bridge = new LuaBridge();
        String luaPath = this.getClass().getResource(goodsEntity.getResource()).getPath();
        Globals globals = JsePlatform.standardGlobals();
        //加载脚本文件login.lua，并编译
        globals.loadfile(luaPath).call();
        //获取带参函数create
        LuaValue createFun = globals.get(LuaValue.valueOf("create"));
        //执行方法初始化数据
        createFun.call(CoerceJavaToLua.coerce(bridge), LuaValue.valueOf(goodsContainer.getUuid().toString()));

        return goodsContainer;
    }

}
