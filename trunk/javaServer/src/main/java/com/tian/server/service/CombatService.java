package com.tian.server.service;

import com.tian.server.common.Ansi;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.model.Race.Human;

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

    void set_bhinfo(String msg)
    {
        if (foo_before_hit.length() < 1)
        {
            foo_before_hit = msg;
            return;
        }

        foo_before_hit += msg;
    }

    void set_ahinfo(String msg)
    {
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

    public String getDamageMsg(int damage, String type)
    {
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

    public boolean accept_fight(Living me, Player who){

        AttackService attackService = new AttackService();
        if(!(me instanceof Human)){
            attackService.kill_ob(me, who);
            return true;
        }


        //Todo:守卫模式暂时不处理
        /*if( this_object()->is_guarder() )
        return this_object()->check_enemy(who, "fight");*/

        String att = me.getAttitude();
        Integer perqi = (int)me.getQi() * 100 / me.getMaxQi();
        Integer perjing = (int)me.getJing() * 100 / me.getMaxJing();

        if(  me.getEnemy().size() > 0) {
            switch(att)
            {
                case "heroism":
                    if( perqi >= 50 ) {
                        command("say 哼！出招吧！");
                        return 1;
                    } else {
                        command("say 哼！我小歇片刻再收拾你不迟。");
                        return 0;
                    }
                    break;
                default:
                    command("say 想倚多为胜，这不是欺人太甚吗！");
                    return 0;
            }
        }

        if( perqi >= 75 && perjing >= 75 ) {
            switch (att)
            {
                case "friendly":
                    command("say " + RANK_D->query_self(this_object())
                            + "怎么可能是" + RANK_D->query_respect(who)
                            + "的对手？");
                    return 0;
                case "aggressive":
                case "killer":
                    command("say 哼！出招吧！");
                    break;
                default:
                    command("say 既然" + RANK_D->query_respect(who)
                            + "赐教，" + RANK_D->query_self(this_object())
                            + "只好奉陪。");
                    break;
            }
            return 1;
        }

        command("say 今天有些疲惫，改日再战也不迟啊。");
        return 0;
    }

}
