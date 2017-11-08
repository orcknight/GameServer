package com.tian.test;

import com.tian.server.model.Living;
import com.tian.server.service.TaskService;
import com.tian.server.service.UserService;
import com.tian.server.util.IdUtil;
import com.tian.server.util.LivingLuaAgent;
import com.tian.server.util.XmlUtil;
import junit.framework.TestCase;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

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

        /*String msg = "{\"level\":0,\"unit\":\"颗\",\"name\":\"头\",\"leftName\":\"人头\",\"leftId\":\"head\",\"components\":{\"left eye\":\"left eye\",\"right eye\":\"right eye\",\"nose\":\"nose\",\"left ear\":\"left ear\",\"right ear\":\"right ear\",\"hair\":\"hair\",\"tongue\": \"tongue\" },\"verbOfPart\":\"砍下\",\"cloneObject\":\"/clone/misc/head\" }";;
        JSONObject jsonObject = JSONObject.fromObject(msg);
        System.out.println("xx");

        Living ser = new Living();
        ser.applyCondition("kill", 1);
        Object x = ser.getCndObject("kill");
        int flag = ser.updateCondition();
        System.out.print("xxx");*/
         /*try {

           String luaPath = this.getClass().getResource("/lua/npc/xinghuacun/mengyi.lua").getPath();
            //= "resources/lua/login.lua";   //lua脚本文件所在路径
            Globals globals = JsePlatform.standardGlobals();
            //加载脚本文件login.lua，并编译
            globals.loadfile(luaPath).call();

            //获取带参函数create
            LuaValue createFun = globals.get(LuaValue.valueOf("create"));
            //执行方法初始化数据
            createFun.call(LuaValue.valueOf("123456"));
        }catch (Exception e){

            e.printStackTrace();
        }*/

        /*//获取无参函数hello
        LuaValue func = globals.get(LuaValue.valueOf("luaPrint"));
        //执行hello方法
        func.call();
        //获取带参函数test
        LuaValue func1 = globals.get(LuaValue.valueOf("test"));
        //执行test方法,传入String类型的参数参数
        String data = func1.call(LuaValue.valueOf("I'am from Java!")).toString();
        //打印lua函数回传的数据*/

        //System.out.println(EquipType.HEAD.toInteger());
        //System.out.println(EquipType.RIGHTHAND.toInteger());

        /*XmlUtil.loadRewardFromXml("1001");

        JSONArray jlist = new JSONArray();
        JSONArray jArray = new JSONArray();

        JSONObject j1 = new JSONObject();
        JSONObject j2 = new JSONObject();
        JSONObject j3 = new JSONObject();
        j1.put("code", 1);
        j1.put("msg", "1");
        j2.put("code", 2);
        j2.put("code", "2");
        j3.put("code", 3);
        j3.put("code", "3");
        jlist.add(j1);
        jlist.add(j2);

        jArray.add(j3);
        jArray.addAll(JSONArray.toCollection(jlist));

        System.out.println("");*/
        //System.out.println(IdUtil.getUUID());

        /*Beast beast = new Beast();
        SkillAction skillAction = beast.queryAction();
        if(skillAction == null){

            System.out.println("pid");
        }else{
            System.out.println("child");
        }*/
        //System.out.println(CombatUtil.getDamageWithLimb("小腹"));

        //LivingLuaAgent.setButtons("[{\"tile\":\"偏属\", \"name\":\"偏属\"}]");



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