package com.tian.server.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.common.Ansi;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.model.Race.Human;
import com.tian.server.model.RoomObjects;
import com.tian.server.util.LuaBridge;
import com.tian.server.util.MsgUtil;
import com.tian.server.util.UnityCmdUtil;
import com.tian.server.util.UserCacheUtil;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.*;

/**
 * Created by PPX on 2017/7/11.
 */
public class CombatService {

    // 关于玩家数据(/combat/)的说明
    // PKS：杀害的玩家数目
    // MKS；杀死的NPC数目(动物不计算)
    // WPK：主动杀死玩家的次数
    // WPK_NOTGOOD：主动杀死非正派(shen < 500)玩家的次数
    // WPK_BAD：    主动杀死的邪派(shen <-500)玩家的次数
    // WPK_NOTBAD： 主动杀死非邪派(shen >-500)玩家的次数
    // WPK_GOOD：   主动杀死的正派(shen > 500)玩家的次数
    // DPS：主动打晕玩家的次数
    // DPS_NOTGOOD：主动打晕非正派(shen < 500)玩家的次数
    // DPS_BAD：    主动打晕的邪派(shen <-500)玩家的次数
    // DPS_NOTBAD： 主动打晕非邪派(shen >-500)玩家的次数
    // DPS_GOOD：   主动打晕的正派(shen > 500)玩家的次数
    // dietimes：   死亡的次数

    // combat/today纪录(mapping)
    // which_day: 日期(实际时间中日期)
    // id       : n, 主动打晕某个ID的次数

    private static final Integer DEFAULT_MAX_PK_PERDAY = 3;
    private static final Integer DEFAULT_MAX_PK_PERMAN = 1;

    // 经验底线(random(my_exp) > EXP_LIMIT，则不加经验)
    private static final Integer EXP_LIMIT = 20000000;

    private static final String[] GUARD_MSG = {
            append_color(Ansi.CYN + "$N注视著$n的行动，企图寻找机会出手。" + Ansi.NOR + "\n", Ansi.CYN),
            append_color(Ansi.CYN + "$N正盯著$n的一举一动，随时准备发动攻势。" + Ansi.NOR + "\n", Ansi.CYN),
            append_color(Ansi.CYN + "$N缓缓地移动脚步，想要找出$n的破绽。" + Ansi.NOR + "\n", Ansi.CYN),
            append_color(Ansi.CYN + "$N目不转睛地盯著$n的动作，寻找进攻的最佳时机。" + Ansi.NOR + "\n", Ansi.CYN),
            append_color(Ansi.CYN + "$N慢慢地移动著脚步，伺机出手。" + Ansi.NOR + "\n", Ansi.CYN),
    };

    private static final String[] CATCH_HUNT_MSG = {
            append_color(Ansi.HIW + "$N和$n仇人相见分外眼红，立刻打了起来！" + Ansi.NOR + "\n", Ansi.HIW),
            append_color(Ansi.HIW + "$N对著$n大喝：「可恶，又是你！」" + Ansi.NOR + "\n", Ansi.HIW),
            append_color(Ansi.HIW + "$N和$n一碰面，二话不说就打了起来！" + Ansi.NOR + "\n", Ansi.HIW),
            append_color(Ansi.HIW + "$N一眼瞥见$n，「哼」的一声冲了过来！" + Ansi.NOR + "\n", Ansi.HIW),
            append_color(Ansi.HIW + "$N一见到$n，愣了一愣，大叫：「我宰了你！」" + Ansi.NOR + "\n", Ansi.HIW),
            append_color(Ansi.HIW + "$N喝道：「$n，我们的帐还没算完，看招！」" + Ansi.NOR + "\n", Ansi.HIW),
            append_color(Ansi.HIW + "$N喝道：「$n，看招！」" + Ansi.NOR + "\n", Ansi.HIW)
    };

    private static final String[] WINNER_MSG = {
            append_color(Ansi.CYN + "\n$N哈哈大笑，说道：承让了！" + Ansi.NOR + "\n", Ansi.CYN),
            append_color(Ansi.CYN + "\n$N双手一拱，笑著说道：承让！" + Ansi.NOR + "\n", Ansi.CYN),
            append_color(Ansi.CYN + "\n$N胜了这招，向后跃开三尺，笑道：承让！" + Ansi.NOR + "\n", Ansi.CYN),
            append_color(Ansi.CYN + "\n$N双手一拱，笑著说道：知道我的利害了吧！" + Ansi.NOR + "\n", Ansi.CYN),
            append_color(Ansi.CYN + "\n$n向后退了几步，说道：这场比试算我输了，下回看我怎么收拾你！" + Ansi.NOR + "\n", Ansi.CYN),
            append_color(Ansi.CYN + "\n$n向后一纵，恨恨地说道：君子报仇，十年不晚！" + Ansi.NOR + "\n", Ansi.CYN),
            append_color(Ansi.CYN + "\n$n脸色一寒，说道：算了算了，就当是我让你吧！" + Ansi.NOR + "\n", Ansi.CYN),
            append_color(Ansi.CYN + "\n$n纵声而笑，叫道：“你运气好！你运气好！”一面身子向后跳开。" + Ansi.NOR + "\n", Ansi.CYN),
            append_color(Ansi.CYN + "\n$n脸色微变，说道：佩服，佩服！" + Ansi.NOR + "\n", Ansi.CYN),
            append_color(Ansi.CYN + "\n$n向后退了几步，说道：这场比试算我输了，佩服，佩服！" + Ansi.NOR + "\n", Ansi.CYN),
            append_color(Ansi.CYN + "\n$n向后一纵，躬身做揖说道：阁下武艺不凡，果然高明！" + Ansi.NOR + "\n", Ansi.CYN),
    };

    public String getEffStatusMsg(Integer ratio) {
        if (ratio==100) return Ansi.HIG + "看起来气血充盈，并没有受伤。" + Ansi.NOR;
        if (ratio > 95) return Ansi.HIG + "似乎受了点轻伤，不过光从外表看不大出来。" + Ansi.NOR;
        if (ratio > 90) return Ansi.HIY + "看起来可能受了点轻伤。" + Ansi.NOR;
        if (ratio > 80) return Ansi.HIY + "受了几处伤，不过似乎并不碍事。" + Ansi.NOR;
        if (ratio > 60) return Ansi.HIY + "受伤不轻，看起来状况并不太好。" + Ansi.NOR;
        if (ratio > 50) return Ansi.HIR + "气息粗重，动作开始散乱，看来所受的伤著实不轻。" + Ansi.NOR;
        if (ratio > 40) return Ansi.HIR + "已经伤痕累累，正在勉力支撑著不倒下去。" + Ansi.NOR;
        if (ratio > 30) return Ansi.HIR + "受了相当重的伤，只怕会有生命危险。" + Ansi.NOR;
        if (ratio > 20) return Ansi.RED + "伤重之下已经难以支撑，眼看就要倒在地上。" + Ansi.NOR;
        if (ratio > 10 ) return Ansi.RED + "摇头晃脑、歪歪斜斜地站都站不稳，眼看就要倒在地上。" + Ansi.NOR;
        if (ratio > 5 )return Ansi.RED  + "已经陷入半昏迷状态，随时都可能摔倒晕去。" + Ansi.NOR;
        return Ansi.RED + "受伤过重，已经奄奄一息，命在旦夕了" + Ansi.NOR;
    }

    /*void create()
    {
        int pd, pm;

        seteuid(getuid());
        set("channel_id", "战斗精灵");

        if (undefinedp(pd = CONFIG_D->query_int("max pk perday")))
            pd = DEFAULT_MAX_PK_PERDAY;

        if (undefinedp(pm = CONFIG_D->query_int("max pk perman")))
            pm = DEFAULT_MAX_PK_PERMAN;

        set("pk_perday", pd);
        set("pk_perman", pm);
    }*/

    // message after damage info
    private static String foo_before_hit = "";
    private static String foo_after_hit = "";

    void set_bhinfo(String msg) {
        if (foo_before_hit.length() < 1)
        {
            foo_before_hit = msg;
            return;
        }

        foo_before_hit += msg;
    }

    void set_ahinfo(String msg) {
        if (foo_after_hit.length() < 1)
        {
            foo_after_hit = msg;
            return;
        }

        foo_after_hit += msg;
    }

    String query_bhinfo() { return foo_before_hit; }

    String query_ahinfo() { return foo_after_hit; }

    void clear_bhinfo() { foo_before_hit = ""; }

    void clear_ahinfo() { foo_after_hit = ""; }

    public String getDamageMsg(int damage, String type) {
        String str;
        if (damage == 0)
            return "结果没有造成任何伤害。\n";

        if (type.equals("擦伤") || type.equals("割伤")){

            if (damage < 15)  str = "结果只是轻轻地划破$p的皮肉。"; else
            if (damage < 40)  str = "结果在$p$l划出一道细长的血痕。"; else
            if (damage < 100) str = "结果「嗤」地一声划出一道伤口！"; else
            if (damage < 200) str = "结果「嗤」地一声划出一道血淋淋的伤口！"; else
            if (damage < 400) str = "结果「嗤」地一声划出一道又长又深的伤口，溅得$N满脸鲜血！"; else
                str = "结果只听见$n一声惨嚎，$w已在$p$l划出一道深及见骨的可怕伤口！！";
            return str  +"\n";
        }else if(type.equals("刺伤")){

            if (damage < 15)  str = "结果只是轻轻地刺破$p的皮肉。"; else
            if (damage < 40)  str = "结果在$p$l刺出一个创口。"; else
            if (damage < 100) str = "结果「噗」地一声刺入了$n$l寸许！"; else
            if (damage < 200) str = "结果「噗」地一声刺进$n的$l，使$p不由自主地退了几步！"; else
            if (damage < 400) str = "结果「噗嗤」地一声，$w已在$p$l刺出一个血肉模糊的血窟窿！"; else
                str = "结果只听见$n一声惨嚎，$w已在$p的$l对穿而出，鲜血溅得满地！！";
            return str  +"\n";

        }else if(type.equals("瘀伤") || type.equals("震伤")){

            if (damage < 15)  str = "结果只是轻轻地碰到，比拍苍蝇稍微重了点。"; else
            if (damage < 40)  str = "结果在$p的$l造成一处瘀青。"; else
            if (damage < 100) str = "结果一击命中，$n的$l登时肿了一块老高！"; else
            if (damage < 150) str = "结果一击命中，$n闷哼了一声显然吃了不小的亏！"; else
            if (damage < 200) str = "结果「砰」地一声，$n退了两步！"; else
            if (damage < 400) str = "结果这一下「砰」地一声打得$n连退了好几步，差一点摔倒！"; else
            if (damage < 800) str = "结果重重地击中，$n「哇」地一声吐出一口鲜血！"; else
                str = "结果只听见「砰」地一声巨响，$n像一捆稻草般飞了出去！！";
            return str +"\n";
        }else if(type.equals("内伤")){

            if (damage < 15)  str = "结果只是把$n打得退了半步，毫发无损。"; else
            if (damage < 40)  str = "结果$n痛哼一声，在$p的$l造成一处瘀伤。"; else
            if (damage < 100) str = "结果一击命中，把$n打得痛得弯下腰去！"; else
            if (damage < 150) str = "结果$n闷哼了一声，脸上一阵青一阵白，显然受了点内伤！"; else
            if (damage < 200) str = "结果$n脸色一下变得惨白，昏昏沉沉接连退了好几步！"; else
            if (damage < 400) str = "结果重重地击中，$n「哇」地一声吐出一口鲜血！"; else
            if (damage < 800) str = "结果「轰」地一声，$n全身气血倒流，口中鲜血狂喷而出！"; else
                str = "结果只听见几声喀喀轻响，$n一声惨叫，像滩软泥般塌了下去！！";
            return str +"\n";
        }else if(type.equals("点穴")){

            if (damage < 15)  str = "结果只是轻轻的碰到$n的$l，根本没有点到穴道。"; else
            if (damage < 40)  str = "结果$n痛哼一声，在$p的$l造成一处淤青。"; else
            if (damage < 100) str = "结果一击命中，$N点中了$n$l上的穴道，$n只觉一阵麻木！"; else
            if (damage < 200) str = "结果$n闷哼了一声，脸上一阵青一阵白，登时觉得$l麻木！"; else
            if (damage < 400) str = "结果$n脸色一下变得惨白，被$N点中$l的穴道,一阵疼痛遍布整个$l！"; else
            if (damage < 800) str = "结果$n一声大叫，$l的穴道被点中,疼痛直入心肺！"; else
                str = "结果只听见$n一声惨叫，一阵剧痛游遍全身，跟着直挺挺的倒了下去！";
            return str +"\n";
        }else if(type.equals("抽伤")){

            if (damage < 15)  str = "结果只是在$n的皮肉上碰了碰，好象只蹭破点皮。"; else
            if (damage < 40)  str = "结果在$n$l抽出一道轻微的紫痕。"; else
            if (damage < 100) str = "结果「啪」地一声在$n$l抽出一道长长的血痕！"; else
            if (damage < 200) str = "结果只听「啪」地一声，$n的$l被抽得皮开肉绽，痛得$p咬牙切齿！"; else
            if (damage < 400) str = "结果只听「啪」地一声，$n的$l被抽得皮开肉绽，痛得$p咬牙切齿！"; else
            if (damage < 800) str = "结果「啪」地一声爆响！这一下好厉害，只抽得$n皮开肉绽，血花飞溅！"; else
                str = "结果只听见$n一声惨嚎，$w重重地抽上了$p的$l，$n顿时血肉横飞！";
            return str +"\n";
        }else if(type.equals("反震伤")){

            if (damage < 15)  str = "结果$N受到$n的内力反震，闷哼一声。"; else
            if (damage < 40)  str = "结果$N被$n的反震得气血翻腾，大惊失色。"; else
            if (damage < 100) str = "结果$N被$n的反震得站立不稳，摇摇晃晃。"; else
            if (damage < 200) str = "结果$N被$n以内力反震，「嘿」地一声退了两步。"; else
            if (damage < 300) str = "结果$N被$n的震得反弹回来的力量震得半身发麻。"; else
            if (damage < 400) str = "结果$N被$n的内力反震，胸口有如受到一记重击，连退了五六步！"; else
            if (damage < 600) str = "结果$N被$n内力反震，眼前一黑，身子向後飞出丈许！！"; else
                str = "结果$N被$n内力反震，狂吐鲜血，身子象断了线的风筝向後飞去！！";
            return str +"\n";


        }else if(type.equals("砸伤")){

            if (damage < 10)  str = "结果只是轻轻地碰到，像是给$n搔了一下痒。"; else
            if (damage < 40)  str = "结果在$n的$l砸出一个小臌包。"; else
            if (damage < 100) str = "结果$N这一下砸个正着，$n的$l登时肿了一块老高！"; else
            if (damage < 150) str = "结果$N这一下砸个正着，$n闷哼一声显然吃了不小的亏！"; else
            if (damage < 200) str = "结果只听「砰」地一声，$n疼得连腰都弯了下来！"; else
            if (damage < 400) str = "结果这一下「轰」地一声砸得$n眼冒金星，差一点摔倒！"; else
            if (damage < 800) str = "结果重重地砸中，$n眼前一黑，「哇」地一声吐出一口鲜血！"; else
                str = "结果只听见「轰」地一声巨响，$n被砸得血肉模糊，惨不忍睹！";
            return str +"\n";
        }else{

            if (type.length() < 1) type = "伤害";
            if (damage < 15)  str = "结果只是勉强造成一处轻微"; else
            if (damage < 40)  str = "结果造成轻微的"; else
            if (damage < 100) str = "结果造成一处"; else
            if (damage < 150) str = "造成一处严重"; else
            if (damage < 200) str = "结果造成颇为严重的"; else
            if (damage < 300) str = "结果造成相当严重的"; else
            if (damage < 400) str = "结果造成十分严重的"; else
            if (damage < 600) str = "结果造成极其严重的"; else
                str = "结果造成非常可怕的严重";
            return  str+ type  +"！\n";

        }
    }


    // append color after the $N、$n、$w for the string color won't be
    // break by the NOR after the name
    private static String append_color(String source, String defaultColor) {
        source = source.replaceAll("$N", "\\$N" + defaultColor);
        source = source.replaceAll("$n", "\\$n" + defaultColor);
        source = source.replaceAll("$w", "\\$w" + defaultColor);

        return source;
    }

    public Integer accept_fight(Living me, Player who){

        //如果自定义了处理函数直接调用
        if(me.getCmdActions() != null && me.getCmdActions().get("accept_fight") != null ){

            LuaBridge bridge = new LuaBridge();
            String luaPath = this.getClass().getResource(me.getResource()).getPath();
            Globals globals = JsePlatform.standardGlobals();
            //加载脚本文件login.lua，并编译
            globals.loadfile(luaPath).call();
            //获取带参函数create
            LuaValue createFun = globals.get(LuaValue.valueOf(me.getCmdActions().get("accept_fight")));
            //执行方法初始化数据
            LuaValue retValue = createFun.call(CoerceJavaToLua.coerce(bridge), LuaValue.valueOf(me.getUuid().toString()));

            return retValue.toint();
        }

        if(me instanceof  Player) {
            //玩家有自己的处理方法
            return 1;
        }else{

            //npc的默认方法
            AttackService attackService = new AttackService();
            RankService rankService = new RankService();
            if(!(me instanceof Human)){
                attackService.kill_ob(me, who);
                return 1;
            }

            List<SocketIOClient> excludeClients = new ArrayList<SocketIOClient>();
            Collection<SocketIOClient> clients = who.getSocketClient().getNamespace().getRoomOperations(me.getLocation().getName()).getClients();
            //Todo:守卫模式暂时不处理
        /*if( this_object()->is_guarder() )
        return this_object()->check_enemy(who, "fight");*/

            String att = me.getAttitude();
            Integer perqi = (int)me.getQi() * 100 / me.getMaxQi();
            Integer perjing = (int)me.getJing() * 100 / me.getMaxJing();
            JSONArray jsonArray = new JSONArray();

            if(me instanceof  Player){
                excludeClients.add(((Player)me).getSocketClient());
            }

            if(me.getEnemy().size() > 0) {

                if(att.equals("heroism")){
                    if(perqi >= 50) {
                        jsonArray.add(UnityCmdUtil.getInfoWindowRet(me.getName() + "说道：哼！出招吧！"));
                        MsgUtil.sendMsg(jsonArray, excludeClients, clients);
                        return 1;
                    }else{
                        jsonArray.add(UnityCmdUtil.getInfoWindowRet(me.getName() + "说道：哼！我小歇片刻再收拾你不迟。"));
                        MsgUtil.sendMsg(jsonArray, excludeClients, clients);
                        return 0;
                    }
                }else{
                    jsonArray.add(UnityCmdUtil.getInfoWindowRet(me.getName() + "说道：想倚多为胜，这不是欺人太甚吗！"));
                    MsgUtil.sendMsg(jsonArray, excludeClients, clients);
                    return 0;
                }
            }

            if( perqi >= 75 && perjing >= 75 ) {

                if(att.equals("friendly")){
                    jsonArray.add(UnityCmdUtil.getInfoWindowRet(me.getName() + "说道：" + rankService.querySelf(me)
                            + "怎么可能是" + rankService.queryRespect(who) + "的对手？"));
                    MsgUtil.sendMsg(jsonArray, excludeClients, clients);
                    return 0;
                }else if(att.equals("aggressive") || att.equals("killer")){
                    jsonArray.add(UnityCmdUtil.getInfoWindowRet(me.getName() + "说道：哼！出招吧！"));
                    MsgUtil.sendMsg(jsonArray, excludeClients, clients);
                    return 1;
                }else{
                    jsonArray.add(UnityCmdUtil.getInfoWindowRet(me.getName() + "说道：既然" + rankService.queryRespect(who)
                            + "赐教，" + rankService.querySelf(me) + "只好奉陪。"));
                    MsgUtil.sendMsg(jsonArray, excludeClients, clients);
                    return 1;
                }
            }

            jsonArray.add(UnityCmdUtil.getInfoWindowRet(me.getName() + "说道：今天有些疲惫，改日再战也不迟啊。"));
            MsgUtil.sendMsg(jsonArray, excludeClients, clients);
            return 0;
        }

    }

    public void broadcastToRoom(){


    }

    // called when winner hit the victim to unconcious
    void winnerReward(Living winner, Living victim) {
        Living owner;
        int temp;
        int td;
        Map<String, Object> today = null;
        //mapping today;

        owner = (Living)winner.queryTemp("owner");
        if (owner != null) {
            winner = owner;
        }

        //Todo:不知道干什么的，后续处理
        //winner->defeated_enemy(victim);

        if( !(winner instanceof  Player)){
            return;
        }

        if(!winner.getWantKills().contains(victim)){
            return;
        }

        winner.add("combat/DPS", 1);
        if (victim.isNotBad())  winner.add("combat/DPS_NOTBAD", 1);
        if (victim.isNotGood()) winner.add("combat/DPS_NOTGOOD", 1);
        if (victim.isBad())     winner.add("combat/DPS_BAD", 1);
        if (victim.isGood())    winner.add("combat/DPS_GOOD", 1);

        if (victim.queryTemp("killer") != null) {
            return;
        }

        if(victim.getCombatExp() < 150){
            return;
        }

        //Todo:击杀记录，暂时不处理，后续存储到数据库中
        /*log_file("static/killrecord",
                sprintf("%s %s defeat %s\n",
                        log_time(), log_id(winner), log_id(victim)));*/

        td = (int)(System.currentTimeMillis() / 1000) / 86400;
        today = (Map<String ,Object>)winner.query("combat/today");

        if(today == null || Integer.parseInt(today.get("which_day").toString()) != td){

            today = new HashMap<String ,Object>();
            today.put("which_day", td);
            today.put("total_count", 1);
            today.put(victim.getUuid().toString(), 1);

        }else {

            // count how many times that winner hit the victim to unconcious


            temp = Integer.parseInt(today.get(victim.getUuid().toString()).toString()) + 1;
            today.put(victim.getUuid().toString(), temp);
            //Todo:pk限制暂时不处理，后续补充
            /*if (temp == query("pk_perman")) {
                // reach limit
                tell_object(winner, BLINK HIR "\n今天你已经打晕" +
                                victim->name() + chinese_number(temp) +
                                        "次了，手下留"
                        "情吧，否则麻烦可要找上门了。"NOR"\n");
            } else
            if (temp > query("pk_perman"))
                // too many times
                winner->set("combat/need_punish", "这厮逼人太甚，真是岂有此理！");

            // count how many users that winner hit to unconcious
            temp = ++today["total_count"];
            if (temp == query("pk_perday"))
            {
                // reach limit
                tell_object(winner, BLINK HIR "\n今天你已经打晕" +
                                chinese_number(temp) + "次玩家了，手下留"
                        "情吧，否则麻烦可要找上门了。"NOR"\n");
            } else
            if (temp > query("pk_perday"))
                // too many users
                winner->set("combat/need_punish", "丧尽天良，大肆屠戮，罪无可恕！");
        }*/
        }
        winner.set("combat/today", today);
    }

    public void announce(Living ob, String event) {

        Map<String, RoomObjects> roomObjects = UserCacheUtil.getRoomObjectsCache();
        List<Player> roomPlayers = roomObjects.get(ob.getLocation().getName()).getPlayers();
        List<SocketIOClient> clients = new ArrayList<SocketIOClient>();
        List<SocketIOClient> excludeClients = new ArrayList<SocketIOClient>();
        for(Player player : roomPlayers){
            clients.add(player.getSocketClient());
        }

        if(ob instanceof  Player){

            excludeClients.add(((Player) ob).getSocketClient());
        }

        JSONArray jsonArray = new JSONArray();
        if(event.equals("dead")){
            jsonArray.add(UnityCmdUtil.getInfoWindowRet("\n$N扑在地上挣扎了几下，腿一伸，口中喷出几口" +
                    Ansi.HIR + "鲜血" + Ansi.NOR + "，死了！" + "\n"));
            MsgUtil.sendMsg(jsonArray, excludeClients, clients);
            return;
        }else if(event.equals("unconcious")){
            jsonArray.add(UnityCmdUtil.getInfoWindowRet("\n$N脚下一个不稳，跌在地上一动也不动了。\n\n"));
            MsgUtil.sendMsg(jsonArray, excludeClients, clients);
            player_escape(null, ob);
            return;
        }else if(event.equals("revive")){

            jsonArray.add(UnityCmdUtil.getInfoWindowRet("\n$N慢慢睁开眼睛，清醒了过来。\n\n"));
            MsgUtil.sendMsg(jsonArray, excludeClients, clients);
            return;
        }
    }

    int player_escape(Living killer, Living ob) {
        Living iob;
        String msg;

        if(ob == null){
            return 0;
        }

        if( !(ob instanceof  Player)){
            return 0;
        }

        Integer whichDay = 0;
        if(ob.query("combat/which_day") != null){
            whichDay = Integer.parseInt(ob.query("combat/which_day").toString());
        }
        Integer totalCount = 0;
        if(ob.query("total_count") != null){
            totalCount = Integer.parseInt(ob.query("total_count").toString());
        }

        Integer currentDay = (int)(System.currentTimeMillis() / 1000 / 86400);
        if ( whichDay == currentDay && totalCount > 0) {
            return 0;
        }

        // 真的晕倒了，察看是否是被别人有意打晕的
        if(killer == null){
            killer = ob.getDefeatedBy();
        }

        if(killer == null || !(killer instanceof Player) || !killer.getWantKills().contains(ob)){
            return 0;
        }

        if(ob.getCombatExp() >= 150000){
            return 0;
        }

        RankService rankService = new RankService();
        Random random = new Random();
        switch (random.nextInt(7)) {
            case 0:
                msg = "突然只听幽幽一声长叹，一掌轻轻按来。$N大吃一惊，不及" +
                "多加思索，只是抬手一格。匆忙之间只怕对手过于厉害，难" +
                "以招架，急忙向后跃开。却见来人并不追击，只是一伸手拎" +
                "起$n，转身飘然而去，仙踪渺然。\n";
                break;

            case 1:
                msg = "$N将$n打翻在地，“哈哈”一声笑声尚未落下，只听有人冷" +
                "哼一声，忽然间掌风袭体，$N顿感呼吸不畅，几欲窒息，慌" +
                "忙中急忙退后，待得立稳脚跟，却见$n早已无影无踪。\n";
                break;

            case 2:
                msg = "一人忽然掠至，喝道：“岂有此理？我龙岛主最恨此欺善怕" +
                "恶之辈，休走！”说罢一掌击来，$N奋力招架，一招之下几" +
                "欲吐血！只见来人轻轻提起$n，冷笑两声，转身离去，$N惊" +
                "骇之下，竟然说不出一句话来。\n";
                break;

            case 3:
                msg = "突然一人喝道：“且慢！”只见一道黑影掠到，飞起一脚将" +
                "$N踢了个跟头，左手拎起$n，冷冷对$N道：“今日所幸尚未" +
                "伤人命，你作恶不甚，饶你去吧！”$N捂胸运气，不住喘息" +
                "，眼睁睁的看着来人去了。\n";
                break;

            case 4:
                msg = "$N跨前一步，忽然看到面前已多了两人，一胖一瘦，一喜一" +
                "怒，不由暗生疑窦。一人手中亮出一面铜牌，笑道：“这位" +
                        rankService.queryRespect(killer)+ "，这面罚恶铜牌你收下可" +
                "好？”$N听了大吃一惊，手只是一软，哪里还敢搭半句话？" +
                "那瘦子冷冷看了过来，目光如电，$N讪讪笑了两声，目送两" +
                "人带了$n逍遥而去。\n";

            case 5:
                msg = "恰在此时，正逢一老者路过，只见他微一颦眉，喝道：“兀" +
                "那" + rankService.queryRude(killer)+ "，伤人做甚？”$N大" +
                "怒道：“你是何人，如此嚣张？”老者大怒，一掌拍落，$N" +
                "向上只是一格，“噗噜”一下双腿陷入土中，足有半尺。老" +
                "者森然道：“我乃侠客岛木岛主是也，如有不服，恭候大驾" +
                "！”此时$N内息如狂，连句场面话也说不出来，只能眼看$n" +
                "被那木岛主带了离去。\n";
                break;

            default:
                msg = "忽听“哈哈”一阵长笑，一人道：“龙兄，想不到我们三十" +
                "年不履中土，这些武林高手却是越来越不长进了！”另一人" +
                "道：“正是，看来赏善罚恶，漫漫无期，终无尽头。”$N听" +
                "得大汗涔涔而下，环顾四方却无一人，转回头来，更是大吃" +
                "一惊！连$n也不见了。\n";
                break;
        }

        if (killer.getLocation().getName().equals(ob.getLocation().getName()) &&
                killer.isFighting(ob)) {

            //msg = replace_string(msg, "$n", ob->name());
            //message_sort(msg, killer);
        } else
        {
            /*msg = "正逢一老者路过，见了" + ob->name() + "晕倒在地，叹口"
            "气，将他扶起带走了。\n";
            message("vision", msg, environment(ob));*/
        }

        // 将身上带的东西放下
        //Todo:
       /* foreach (iob in all_inventory(ob))
        if (! iob->query("money_id") &&
                ! iob->query("equipped"))
            iob->move(environment(ob));*/

        // 初始化玩家的状态
        ob.clearCondition("");
        ob.getKiller().remove(killer);
        killer.getKiller().remove(ob);

        // 通缉伤人凶手
        if(killer.queryCondition("killer") == null){

            msg = "听说官府发下海捕文书，缉拿伤人凶手" +
                    killer.getName() + "。";
            killer.applyCondition("killer", 500);
        } else {
            msg = "听说官府加紧捉拿累次伤人的暴徒" +
                    killer.getName() + "。";
            killer.applyCondition("killer", 800 +
                    Integer.parseInt(killer.queryCondition("killer").toString()));
        }

        //Todo:广播暂时不处理
        //CHANNEL_D->do_channel(this_object(), "rumor", msg);

       /* ob->move("/d/xiakedao/shiroom24");
        ob->set("startroom", "/d/xiakedao/shiroom24");
        ob->revive();
        ob->set("eff_qi", ob->query("max_qi"));
        ob->set("eff_jing", ob->query("max_jing"));
        ob->set("qi", 0);
        ob->set("jing", 0);

        if (intp(ob->query_busy()))
            ob->start_busy(30);

        tell_object(ob, "你睁开眼来，看到两位老者正在静坐修炼。\n"
                HIG "龙岛主告诉你：" + RANK_D->query_respect(ob) +
                        "，你要想离岛不妨和我说一声(ask long about 离岛)。"NOR"\n");*/

        return 1;
    }
}
