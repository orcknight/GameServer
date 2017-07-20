package com.tian.test;

import com.tian.server.common.EqptParts;
import com.tian.server.dao.ItemDao;
import com.tian.server.entity.ItemEntity;
import com.tian.server.model.Race.Beast;
import com.tian.server.model.SkillAction;
import com.tian.server.util.IdUtil;
import junit.framework.TestCase;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

/**
 * Created by PPX on 2017/6/22.
 */
public class RoomDaoTest extends TestCase {
    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    @Test
    public void testGetList() throws Exception {

        /*String luaPath = this.getClass().getResource("/lua/login.lua").getPath();
         //= "resources/lua/login.lua";   //lua脚本文件所在路径
        Globals globals = JsePlatform.standardGlobals();
        //加载脚本文件login.lua，并编译
        globals.loadfile(luaPath).call();
        //获取无参函数hello
        LuaValue func = globals.get(LuaValue.valueOf("luaPrint"));
        //执行hello方法
        func.call();
        //获取带参函数test
        LuaValue func1 = globals.get(LuaValue.valueOf("test"));
        //执行test方法,传入String类型的参数参数
        String data = func1.call(LuaValue.valueOf("I'am from Java!")).toString();
        //打印lua函数回传的数据*/

        //System.out.println(EqptParts.HEAD.toInteger());
        //System.out.println(EqptParts.RIGHTHAND.toInteger());

        //System.out.println(IdUtil.getUUID());

        Beast beast = new Beast();
        SkillAction skillAction = beast.queryAction();
        if(skillAction == null){

            System.out.println("pid");
        }else{
            System.out.println("child");
        }




        /*UUID uuid = UUID.randomUUID();
        System.out.println(uuid);

        ItemDao itemDao = new ItemDao();
        List<ItemEntity> list = itemDao.getList();
        for(ItemEntity item : list){

            System.out.println(item.getDesc());
            JSONArray jsonArray = JSONArray.fromObject(item.getJsonAttr());
            for(int i = 0; i < jsonArray.size(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //String tile = jsonObject.getString("tile");
                //System.out.println(jsonObject.get("tile"));
                //System.out.println(jsonObject.get("name"));
            }
        }*/

    }

}