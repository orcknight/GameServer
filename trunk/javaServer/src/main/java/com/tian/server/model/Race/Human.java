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

    @Override
    public void setup() {

        //先调用基类的函数
        super.setup();

        int s;
        int x, y;
        int limit;
        int xism_age;
        int skill;
        int jing_age;
        int add_point;
        int die_point;
        int r;

        if(getUnit() == null || getUnit().length() < 1){
            setUnit("位");
        }
        if(getGender() == null || getGender().length() < 1){
            setGender("男性");
        }
        if(query("can_speak") == null){
            set("can_speak", 1);
        }
        if(getAttitude() == null || getAttitude().length() < 1){
            setAttitude("peaceful");
        }

        Random random = new Random();
        if(getAge() == null || getAge() < 1){
            setAge(14);
        }
        if(getStr() == null || getStr() < 1){
            setStr(10 + random.nextInt(21));
        }
        if(getCon() == null || getCon() < 1){
            setCon(10 + random.nextInt(21));
        }
        if(getDex() == null || getDex() < 1){
            setDex(10 + random.nextInt(21));
        }
        if(getWux() == null || getWux() < 1){
            setWux(10 + random.nextInt(21));
        }
        if(getPer() == null || getPer() < 1){
            setPer(10 + random.nextInt(21));
        }
        if(getKar() == null || getKar() < 1){
            setKar(10 + random.nextInt(21));
        }

        if( !(this instanceof Player) && (getMaxJingLi() == null || getMaxJingLi() < 1)){

            limit = getCombatExp()/1000;
            setMaxJingLi(limit);
            setEffJingLi(limit);
            setJingLi(limit);
        }

        if( (this instanceof Player) || getMaxJing() == null || getMaxJing() < 1){

            s = getWux() * 5 + getCon() + getDex() + getStr();
            setMaxJing(100);
            if(getBornFamily() == null || getBornFamily().length() < 1){

            }else if(getAge() <11){
                setMaxJing(50 + getAge() * s * 2 / 3);
            }else if(getAge() < 24){
                setMaxJing(getMaxJing() + (getAge() - 11) * s * 2 / 3);
            }else{
                setMaxJing(getMaxJing() + (24 - 11) * s * 2 / 3);
            }

            if ( getMaxJingLi() > 0) {
                setMaxJing(getMaxJing() + getMaxJingLi() / 3);
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

            if( queryTemp("apply/max_jing") != null ) {
                setMaxJing(getMaxJing() + Integer.parseInt(queryTemp("apply/max_jing").toString()));
            }
        }



        if ((this instanceof Player) || getMaxQi() == null || getMaxQi() < 1) {
            s = getCon() * 5 + getStr() + getCon() + getDex();
            setMaxQi(100);

            if(getBornFamily() == null || getBornFamily().length() < 1){

            }else if(getAge() < 11){
                setMaxQi(60 + getAge() * getCon() / 2);
            }else if(getAge() < 27){
                setMaxQi(getMaxQi() + (getAge() - 11) * s * 2 / 3);
            }else{
                setMaxQi(getMaxQi() + (27 - 11) * s * 2 / 3);
            }

            if(getMaxNeili() > 0){
                setMaxQi(getMaxQi() + getMaxNeili() / 4);
            }

            /*// 武当太极加气
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
                my["max_qi"] += ob->query("drug_addqi");*/

            if(queryTemp("apply/max_qi") != null) {
                setMaxQi(getMaxQi() + Integer.parseInt(queryTemp("apply/max_qi").toString()));
            }
        }

        if(getWeight() == null || getWeight() < 1){
            setWeight((40000 + (getStr() - 10) * 2000) * 7 / 10);
        }

        if(getJing() == null || getJing() < 1){
            setJing(getMaxJing());
        }
        if(getQi() == null || getQi() < 1){
            setQi(getMaxQi());
        }
        if(getEffJing() == null || getEffJing() < 1 || getEffJing() > getMaxJing()){
            setEffJing(getMaxJing());
        }
        if(getEffQi() == null || getEffQi() < 1 || getEffQi() > getMaxQi()){
            setEffQi(getMaxQi());
        }
        if(getShenType() == null){
            setShenType((byte) 0);
        }

        if(getShen() == null ||  getShen() < 1){

            if (this instanceof Player) {
                setShen(0);
            }else {
                setShen(getShenType() * getCombatExp() / 10);
            }
        }

        if (getMaxEncumbrance() == null || getMaxEncumbrance() < 1) {

            //Todo:判断是否是会员，这里暂时不不判断　
        }

        //Todo:
        //ob->reset_action();
        //ob->update_killer();

    }
}
