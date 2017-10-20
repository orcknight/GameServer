package com.tian.server.service;

import com.tian.server.model.GoodsContainer;
import com.tian.server.model.Living;
import com.tian.server.model.MudObject;
import com.tian.server.util.GoodsManager;
import com.tian.server.util.LuaBridge;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

/**
 * Created by PPX on 2017/10/19.
 */
public class CharService {

    public GoodsContainer makeCorpse(Living victim, Living killer) {
        int i;
        GoodsContainer corpse = null;

        if(victim.getGhost()){
            return null;
        }

        GoodsManager goodsManager = new GoodsManager();
        corpse = goodsManager.createByPathName("/lua/clone/misc/corpse", 1, null);
        if(corpse.getCmdActions().containsKey("make_corpse")){

            String funName = corpse.getCmdActions().get("make_corpse").toString();
            LuaBridge bridge = new LuaBridge();
            String luaPath = this.getClass().getResource(corpse.getResource()).getPath();
            Globals globals = JsePlatform.standardGlobals();
            //加载脚本文件login.lua，并编译
            globals.loadfile(luaPath).call();
            //获取带参函数create
            LuaValue createFun = globals.get(LuaValue.valueOf(funName));
            LuaValue[] params = new LuaValue[2];
            params[0] = LuaValue.valueOf(victim.getUuid().toString());
            params[1] = LuaValue.valueOf(killer.getUuid().toString());
            //执行方法初始化数据
            createFun.call(CoerceJavaToLua.coerce(bridge), LuaValue.valueOf(corpse.getUuid().toString()),
                    LuaValue.listOf(params));

        }

        //把物品放到当前环境
        return corpse;
    }
}
