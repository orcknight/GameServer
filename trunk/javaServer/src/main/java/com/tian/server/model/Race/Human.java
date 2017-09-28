package com.tian.server.model.Race;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import com.tian.server.entity.PlayerFamilyEntity;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.model.SkillAction;
import com.tian.server.util.LuaBridge;
import com.tian.server.util.UnityCmdUtil;
import com.tian.server.util.UserCacheUtil;
import net.sf.json.JSONArray;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Created by PPX on 2017/7/11.
 */
public class Human extends Living {

    private static List<SkillAction> actions = new ArrayList<SkillAction>();
    private static List<String> limbs = new ArrayList<String>();
    protected PlayerFamilyEntity family = new PlayerFamilyEntity();

    public Human(){

        this.weight = 40000;

        actions.add(new SkillAction("$N挥拳攻击$n的$l", "瘀伤"));
        actions.add(new SkillAction("$N往$n的$l一抓", "抓伤"));
        actions.add(new SkillAction("$N往$n的$l狠狠地踢了一脚", "瘀伤"));
        actions.add(new SkillAction("$N提起拳头往$n的$l捶去", "瘀伤"));
        actions.add(new SkillAction("$N对准$n的$l用力挥出一拳", "瘀伤"));
        limbs.add(0, "头部");
        limbs.add(1, "颈部");
        limbs.add(2, "胸口");
        limbs.add(3, "后心");
        limbs.add(4, "左肩");
        limbs.add(5, "右肩");
        limbs.add(6, "左臂");
        limbs.add(7, "右臂");
        limbs.add(8, "左手");
        limbs.add(9, "右手");
        limbs.add(10, "两肋");
        limbs.add(11, "左脸");
        limbs.add(12, "腰间");
        limbs.add(13, "小腹");
        limbs.add(14, "左腿");
        limbs.add(15, "右腿");
        limbs.add(16, "右脸");
        limbs.add(17, "左脚");
        limbs.add(18, "右脚");
        limbs.add(19, "左脸");
        limbs.add(20, "左耳");
        limbs.add(21, "右耳");

        addButton("给予", "give $ID");
        addButton("查看技能", "skills $ID");
        addButton("跟随", "follow $ID");
        addButton("偷窃", "steal $ID");
        addButton("切磋", "fight $ID");
        addButton("偷袭", "touxi $ID");
        addButton("杀死", "kill $ID");
    }

    public PlayerFamilyEntity getFamily() {
        return family;
    }

    public void setFamily(PlayerFamilyEntity family) {
        this.family = family;
    }

    @Override
    public SkillAction queryAction() {

        if(actions.size() < 1){

            return null;
        }

        Random r = new Random();
        int randomIndex = r.nextInt(actions.size()-1);
        return actions.get(randomIndex);
    }

    @Override
    public String getRandomLimb(){

        if(actions.size() < 1){

            return "";
        }

        Random r = new Random();
        int randomIndex = r.nextInt(limbs.size()-1);
        return limbs.get(randomIndex);
    }

    public SkillAction createAction(String action, String damageType, Integer damage){

        SkillAction skillAction = new SkillAction(action, damageType);
        skillAction.setDamage(damage);

        return skillAction;
    }

    void setup_human(Living ob) {
        mapping my;
        int s;
        int x, y;
        int limit;
        int xism_age;
        int skill;
        int jing_age;
        int add_point;
        int die_point;
        int r;

        my = ob->query_entire_dbase();
        ob->set_default_action(__FILE__, "query_action");
        ob->set("default_actions", (: call_other, __FILE__, "query_action" :));

        if(ob.getUnit() == null || ob.getUnit().length() < 1){
            ob.setUnit("位");
        }
        if(ob.getGender() == null || ob.getGender().length() < 1){
            ob.setGender("男性");
        }
        if(ob.query("can_speak") == null){
            ob.set("can_speak", 1);
        }
        if(ob.getAttitude() == null || ob.getAttitude().length() < 1){
            ob.setAttitude("peaceful");
        }

        Random random = new Random();
        if(ob.getAge() == null || ob.getAge() < 1){
            ob.setAge(14);
        }
        if(ob.getStr() == null || ob.getStr() < 1){
            ob.setStr(10 + random.nextInt(21));
        }
        if(ob.getCon() == null || ob.getCon() < 1){
            ob.setCon(10 + random.nextInt(21));
        }
        if(ob.getDex() == null || ob.getDex() < 1){
            ob.setDex(10 + random.nextInt(21));
        }
        if(ob.getWux() == null || ob.getWux() < 1){
            ob.setWux(10 + random.nextInt(21));
        }
        if(ob.getPer() == null || ob.getPer() < 1){
            ob.setPer(10 + random.nextInt(21));
        }
        if(ob.getKar() == null || ob.getKar() < 1){
            ob.setKar(10 + random.nextInt(21));
        }

        if( !(ob instanceof Player) && (ob.getMaxJingLi() == null || ob.getMaxJingLi() < 1)){

            limit = ob.getCombatExp()/1000;
            ob.setMaxJingLi(limit);
            ob.setEffJingLi(limit);
            ob.setJingLi(limit);
        }

        if( (ob instanceof Player) || ob.getMaxJing() == null || ob.getMaxJing() < 1){

            s = ob.getWux() * 5 + ob.getCon() + ob.getDex() + ob.getStr();
            ob.setMaxJing(100);
            if(ob.getBornFamily() == null || ob.getBornFamily().length() < 1){

            }else if(ob.getAge() <11){
                ob.setMaxJing(50 + ob.getAge() * s * 2 / 3);
            }else if(ob.getAge() < 24){
                ob.setMaxJing(ob.getMaxJing() + (ob.getAge() - 11) * s * 2 / 3);
            }else{
                ob.setMaxJing(ob.getMaxJing() + (24 - 11) * s * 2 / 3);
            }

            if ( ob.getMaxJingLi() > 0) {
                ob.setMaxJing(ob.getMaxJing() + ob.getMaxJingLi() / 3);
            }

            //Todo:暂时不处理，只按年龄赋值气血
            /*// 佛家养精：３０岁前补精，３０岁后长精
            if (userp(ob) && mapp(my["family"])) {
                if ( my["family"]["family_name"] == "峨嵋派" )
                    xism_age = (int)ob->query_skill("mahayana", 1);
                else if ( my["family"]["family_name"] == "少林派" )
                    xism_age = (int)ob->query_skill("buddhism", 1);
                else if ( my["family"]["family_name"] == "段氏皇族" )
                    xism_age = (int)ob->query_skill("buddhism", 1);
                else if ( my["family"]["family_name"] == "雪山寺" || my["family"]["family_name"] == "血刀门" )
                    xism_age = (int)ob->query_skill("lamaism", 1);
            }
            else
                xism_age = 0;

            if (xism_age > 250) xism_age = 250;

            if (xism_age > 39) {
                xism_age = xism_age/2;
                if (my["age"] <= 30) xism_age -= my["age"];
                else xism_age -= 30;

                skill = ob->query_skill("force");
                if (xism_age > 0 && skill > 0) {
                    skill = xism_age * (skill/20);
                    if ( my["family"]["family_name"] == "少林派" )
                    {
                        my["max_jing"] += skill;
                        //my["max_qi"] += skill/2;
                    }
                    else if ( my["family"]["family_name"] == "峨嵋派" )
                    {
                        my["max_jing"] += skill;
                        //my["max_qi"] += skill/2;
                    }
                    else if ( my["family"]["family_name"] == "段氏皇族" )
                    {
                        my["max_jing"] += skill*2/3;
                        //my["max_qi"] += skill/2;
                    }
                    else
                    {
                        my["max_jing"] += skill*3/4;
                        //my["max_qi"] += skill/2;
                    }
                }
            }

            // 地刹炼魂：每死一次，丐帮精长根骨值
            if (userp(ob) && mapp(my["family"]) && my["family"]["family_name"] == "丐帮")
            {
                add_point = my["combat_exp"]/50000;
                if (mapp(my["combat"]))
                {
                    die_point = my["combat"]["dietimes"];
                    if (die_point > add_point) die_point = add_point;

                    if (die_point < 100)
                        my["max_jing"] += ob->query_con() * die_point * 10;
                    else
                        my["max_jing"] += ob->query_con() * 100 * 10;
                }
            }

            // 华山紫霞神功；３０岁前补精，３０岁后长精
            if (userp(ob) && mapp(my["family"]) && my["family"]["family_name"] == "华山派"
                    && (jing_age = (int)ob->query_skill("zixia-shengong", 1)/3) > 39)
            {
                if (jing_age > 250) jing_age = 250;
                jing_age = jing_age/2;

                if (my["age"] <= 30) jing_age -= my["age"];
                else jing_age -= 30;

                skill = (int)ob->query_skill("force");

                if ( jing_age > 0 )
                    my["max_jing"] += jing_age * (skill/30);
            }

            // 古墓素女心法；３０岁前补精，３０岁后长精
            if (userp(ob) && mapp(my["family"]) && my["family"]["family_name"] == "古墓派"
                    && (jing_age =(int)ob->query_skill("yunv-xinjing", 1)) > 39)
            {
                if (jing_age > 250) jing_age = 250;
                jing_age = jing_age/2;

                if (my["age"] <= 30) jing_age -= my["age"];
                else jing_age -= 30;

                skill = (int)ob->query_skill("force");

                if ( jing_age > 0 )
                    my["max_jing"] += jing_age * (skill/30);
            }

            // 桃花岛奇门五行：３０岁前补精，３０岁后长精，但效用小
            if (userp(ob) && mapp(my["family"]) && my["family"]["family_name"] == "桃花岛"
                    && (xism_age=(int)ob->query_skill("qimen-wuxing", 1)/3) > 39)
            {
                if (xism_age > 250) xism_age = 250;
                xism_age = xism_age/2;

                if (my["age"] <= 30) xism_age -= my["age"];
                else xism_age -= 30;

                skill = (int)ob->query_skill("force");

                if (xism_age > 0 ) my["max_jing"] += xism_age * (skill/35);
            }

            // 明教圣火玄冥：３０岁前补精，３０岁后长精，但效用小
            if (userp(ob) && mapp(my["family"]) && my["family"]["family_name"] == "明教"
                    && (xism_age=(int)ob->query_skill("shenghuo-shengong", 1)/3) > 39)
            {
                if (xism_age > 250) xism_age = 250;
                xism_age = xism_age/2;

                if (my["age"] <= 30) xism_age -= my["age"];
                else xism_age -= 30;

                skill = (int)ob->query_skill("force");

                if (xism_age > 0 ) my["max_jing"] += xism_age * (skill/35);
            }

            // 日月神教日月光华：３０岁前补精，３０岁后长精，但效用小
            if (userp(ob) && mapp(my["family"]) && my["family"]["family_name"] == "日月神教"
                    && (xism_age=(int)ob->query_skill("riyue-guanghua", 1)/3) > 39)
            {
                if (xism_age > 250) xism_age = 250;
                xism_age = xism_age/2;

                if (my["age"] <= 30) xism_age -= my["age"];
                else xism_age -= 30;

                skill = (int)ob->query_skill("force");

                if (xism_age > 0 ) my["max_jing"] += xism_age * (skill/35);
            }

            // 关外胡家妙手驱毒：３０岁前补精，３０岁后长精，但效用小
            if (userp(ob) && mapp(my["family"]) && my["family"]["family_name"] == "关外胡家"
                    && (xism_age=(int)ob->query_skill("dispel-poison", 1)) > 39)
            {
                if (xism_age > 250) xism_age = 250;
                xism_age = xism_age/2;

                if (my["age"] <= 30) xism_age -= my["age"];
                else xism_age -= 30;

                skill = (int)ob->query_skill("force");

                if (xism_age > 0 ) my["max_jing"] += xism_age * (skill/35);
            }

            if (userp(ob) && my["breakup"])
                my["max_jing"] += my["max_jing"];

            if (userp(ob) && my["animaout"])
                my["max_jing"] += my["max_jing"];

            if (userp(ob) && MEMBER_D->is_valid_member(ob))
                my["max_jing"] += my["max_jing"] / 2;

            if( userp(ob) && (r=ob->query("reborn/times")) )
                my["max_jing"] += my["max_jing"] * r;

            if( userp(ob) && ob->query("jingmai/jing") )
                my["max_jing"] += ob->query("jingmai/jing");

            if( userp(ob) && ob->query("jingmai/finish") )
                my["max_jing"] += ZHOUTIAN_D->query_jingmai_effect("jing");*/

            if( ob.queryTemp("apply/max_jing") != null ) {
                ob.setMaxJing(ob.getMaxJing() + Integer.parseInt(ob.queryTemp("apply/max_jing").toString()));
            }
        }



        if (ob.getMaxQi() == null || ob.getMaxQi() < 1) {
            s = ob.getCon() * 5 + ob.getStr() + ob.getCon() + ob.getDex();
            ob.setMaxQi(100);

            if(ob.getBornFamily() == null || ob.getBornFamily().length() < 1){

            }else
            if (/*undefinedp*/!(my["born"]))
                ; else
            if (my["age"] < 11)
                my["max_qi"] = 60 + my["age"] * my["con"] / 2;
            else
            if (my["age"] < 27)
                my["max_qi"] += (my["age"] - 11) * s * 2 / 3;
            else
                my["max_qi"] += (27 - 11) * s * 2 / 3;

            if ((int)my["max_neili"] > 0)
                my["max_qi"] += (int)my["max_neili"] / 4;

            // 武当太极加气
            if (userp(ob) && mapp(my["family"]) && my["family"]["family_name"] == "武当派" &&
                    (x = (int)ob->query_skill("taoism", 1)) > 39 &&
                    (y = (int)ob->query_skill("taiji-shengong", 1)) > 39)
            {
                if (x > 350) x = (x - 350) / 2 + 350;
                if (y > 350) y = (y - 350) / 2 + 350;
                if (x > 200) x = (x - 200) / 2 + 200;
                if (y > 200) y = (y - 200) / 2 + 200;

                my["max_qi"] += (x + 100 ) * (y + 100) / 100;
            }

            //全真先天功加气
            if (userp(ob) && mapp(my["family"]) && my["family"]["family_name"] == "全真教" &&
                    (x = (int)ob->query_skill("taoism", 1)) > 39 &&
                    (y = (int)ob->query_skill("xiantian-gong", 1)) > 39)
            {
                if (x > 350) x = (x - 350) / 2 + 350;
                if (y > 350) y = (y - 350) / 2 + 350;
                if (x > 200) x = (x - 200) / 2 + 200;
                if (y > 200) y = (y - 200) / 2 + 200;

                my["max_qi"] += (x + 100 ) * (y + 100) / 100;
            }

            // 星宿聚毒练气：３０岁前补气，３０岁后长气
            if (userp(ob) &&  mapp(my["family"]) && my["family"]["family_name"] == "星宿派"
                    && (xism_age=(int)ob->query_skill("poison", 1)/3) > 39)
            {
                if (xism_age > 250) xism_age = 250;
                xism_age = xism_age/2;

                if (my["age"] <= 30) xism_age -= my["age"];
                else xism_age -= 30;

                skill = (int)ob->query_skill("huagong-dafa",1);

                if (xism_age > 0 )
                    my["max_qi"] += xism_age * (skill/30);
            }

            // 白驼山庄聚毒练气：３０岁前补气，３０岁后长气
            if (userp(ob) && mapp(my["family"]) && my["family"]["family_name"] == "欧阳世家"
                    && (xism_age=(int)ob->query_skill("poison", 1)/3) > 39)
            {
                if (xism_age > 250) xism_age = 250;
                xism_age = xism_age/2;
                if (my["age"] <= 30) xism_age -= my["age"];
                else xism_age -= 30;

                skill = (int)ob->query_skill("hamagong",1);

                if (xism_age > 0 )
                    my["max_qi"] += xism_age * (skill/30);
            }

            // 逍遥派逍遥奇学练气：３０岁前补气，３０岁后长气
            if (userp(ob) && mapp(my["family"]) && my["family"]["family_name"] == "逍遥派"
                    && (xism_age=(int)ob->query_skill("xiaoyao-qixue", 1)) > 39)
            {
                if (xism_age > 250) xism_age = 250;
                xism_age = xism_age/2;

                if (my["age"] <= 30) xism_age -= my["age"];
                else xism_age -= 30;

                skill = (int)ob->query_skill("force");

                if (xism_age > 0 )
                    my["max_qi"] += xism_age * (skill/40);
            }

            // 灵鹫宫八荒神功：３０岁前补气，３０岁后长气
            if (userp(ob) && mapp(my["family"]) && my["family"]["family_name"] == "灵鹫宫"
                    && (xism_age=(int)ob->query_skill("bahuang-gong", 1)/3) > 39)
            {
                if (xism_age > 250) xism_age = 250;
                xism_age = xism_age/2;

                if (my["age"] <= 30) xism_age -= my["age"];
                else xism_age -= 30;

                skill = (int)ob->query_skill("force",1);

                if (xism_age > 0 )
                    my["max_qi"] += xism_age * (skill/40);
            }

            // 慕容世家紫徽心法练气：３０岁前补气，３０岁后长气
            if (userp(ob) && mapp(my["family"]) && my["family"]["family_name"] == "慕容世家"
                    && (xism_age=(int)ob->query_skill("zihui-xinfa", 1)/3) > 39)
            {
                if (xism_age > 250) xism_age = 250;
                xism_age = xism_age/2;

                if (my["age"] <= 30) xism_age -= my["age"];
                else xism_age -= 30;

                skill = (int)ob->query_skill("force");

                if (xism_age > 0 )
                    my["max_qi"] += xism_age * (skill/40);
            }

            // 神龙岛神龙迷辛练气：３０岁前补气，３０岁后长气
            if (userp(ob) && mapp(my["family"]) && my["family"]["family_name"] == "神龙教"
                    && (xism_age=(int)ob->query_skill("shenlong-mixin", 1)) > 39)
            {
                if (xism_age > 250) xism_age = 250;
                xism_age = xism_age/2;

                if (my["age"] <= 30) xism_age -= my["age"];
                else xism_age -= 30;

                skill = (int)ob->query_skill("force");

                if (xism_age > 0 )
                    my["max_qi"] += xism_age * (skill/50);
            }

            if (userp(ob) && my["breakup"])
                my["max_qi"] += my["max_qi"] * 2;

#ifdef DB_SAVE
            if (userp(ob) && MEMBER_D->is_valid_member(ob))
                my["max_qi"] += my["max_qi"] / 2;
#endif

            if( userp(ob) && (r=ob->query("reborn/times")) )
                my["max_qi"] += my["max_qi"] * r;

            if( userp(ob) && ob->query("jingmai/qi") )
                my["max_qi"]+=ob->query("jingmai/qi");

            if( userp(ob) && ob->query("jingmai/finish") )
                my["max_qi"] += ZHOUTIAN_D->query_jingmai_effect("qi");

            // 服用super药品
            if( userp(ob) && ob->query("drug_addqi") )
                my["max_qi"] += ob->query("drug_addqi");

            if( ob->query_temp("apply/max_qi") )
                my["max_qi"]+=ob->query_temp("apply/max_qi");
        }

        if (! ob->query_weight())
            ob->set_weight((BASE_WEIGHT + (my["str"] - 10) * 2000) * 7 / 10);


    }
}
