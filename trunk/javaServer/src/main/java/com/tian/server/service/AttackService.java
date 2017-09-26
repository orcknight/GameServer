package com.tian.server.service;

import com.tian.server.common.Ansi;
import com.tian.server.common.LivingStatus;
import com.tian.server.model.Living;
import com.tian.server.model.MudObject;
import com.tian.server.model.Player;
import com.tian.server.util.UnityCmdUtil;
import net.sf.json.JSONArray;

import java.util.List;

/**
 * Created by PPX on 2017/9/24.
 */
public class AttackService {



    //This function starts fight between this_object() and ob
    public void fight_ob(Living me, Living ob) {

        //不能为空
        if(me == null || ob == null){
            return;
        }

        //不能为同一个对象
        if(me.getUuid() == ob.getUuid()){
            return;
        }

        //如果已经开始切磋，直接返回
        if(me.getEnemy().contains(ob)){
            return;
        }

        //如果当前环境禁止战斗返回
        if(me.getLocation().getNoFight() == 1){
            return;
        }

        //查看对手是不是处于正常存活状态
        if(me.getStatus().intValue() != LivingStatus.NORMAL.toInteger()) {
            return;
        }

        me.setHeartBeatFlag(true);
        me.getEnemy().add(ob);

        //Todo:如果是要击杀的对手，击杀对手
        /*if (this_object()->is_guarder() &&
            is_killing(ob->query("id")))
        {
            // guarder will look for help
            this_object()->kill_enemy(ob);
        }*/
    }

    // This function starts killing between this_object() and ob
    void kill_ob(Living me, Living ob) {
        //不能为空
        if(me == null || ob == null){
            return;
        }

        //不能战斗返回
        if(me.getLocation().getNoFight() == 1){
            return;
        }

        List<Living> guarded = (List<Living>)ob.queryTemp("guarded");
        if(guarded != null) {

            if (guarded.contains(me)) {

                if(!(ob instanceof  Player)){
                    return;
                }
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(UnityCmdUtil.getInfoWindowRet(Ansi.HIR + "不能杀你要保护的人！" + Ansi.NOR));
                ((Player)ob).getSocketClient().sendEvent("stream", jsonArray);
                return;
            }
        }

        Living owner = (Living)me.queryTemp("owner");
        if(owner != null && owner.getUuid() == ob.getUuid()){

            if(!(me instanceof  Player)){
                return;
            }
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(UnityCmdUtil.getInfoWindowRet(Ansi.HIR + "不能对你的主人下毒手。" + Ansi.NOR));
            ((Player)me).getSocketClient().sendEvent("stream", jsonArray);
            return;
        }

        if(!me.getKiller().contains(ob)){

            me.getKiller().add(ob);
            if(!(ob instanceof  Player)){
                return;
            }
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(UnityCmdUtil.getInfoWindowRet(Ansi.HIR + "看起来" + me.getName() +
                    "想杀死你！" + Ansi.NOR));
            ((Player)ob).getSocketClient().sendEvent("stream", jsonArray);
        }

        //Todo:守护者暂时不处理
        /*for(Living gob:guarded)
        {
            if ( gob.getUuid() == me.getUuid() ||
                    (!gob.getLocation().getName().equals(me.getLocation().getName()))) {
                continue;
            }

            if (! living(gob))
                continue;

            if (gob->is_killing(me->query("id")))
                continue;

            tell_object(gob, HIR + ob->name(1) +
                    "受到攻击，你挺身而出，加入战团！"NOR"\n");
            switch (random(8))
            {
                case 0:
                    message_vision(HIW "$N" HIW "一言不发，对$n"
                            HIW "发动了攻击。"NOR"\n", gob, me);
                    break;
                case 1:
                    message_vision(HIW "$N" HIW "一声怒吼，冲上前"
                            "去，看来是要和$n" HIW "拼命。"NOR"\n",
                            gob, me);
                    break;
                case 2:
                    message_vision(HIW "$N" HIW "冷笑了一声，道："
                            "“接招吧！”说罢就对$n" HIW
                            "发动了攻击。"NOR"\n", gob, me);
                    break;
                case 3:
                    message_vision(HIW "$N" HIW "迈上一步，挡在前"
                            "面，开始和$n" HIW "进行殊死搏"
                            "斗！"NOR"\n", gob, me);
                    break;
                case 4:
                    message_vision(HIW "$N" HIW "双臂一振，脸如寒水，"
                            "已经向$n" HIW "接连发出数招！\n"
                            NOR, gob, me);
                    break;
                case 5:
                    message_vision(HIW "$N" HIW "深吸一口气，一招"
                            "直指$n" HIW "的要害，竟然是要"
                            "取人的性命！"NOR"\n", gob, me);
                    break;
                case 6:
                    message_vision(HIW "紧接着$N" HIW "已经攻上！"
                            "招招进逼$n" HIW "，毫无容情余"
                            "地！"NOR"\n", gob, me);
                    break;
                default:
                    message_vision(HIW "$N" HIW "飞也似的扑上前来"
                            "，开始进攻$n" HIW "。"NOR"\n", gob, me);
                    break;
            }
            if(ob.getWantKills() != null && ob.getWantKills().contains())

            if (ob->is_want_kill(me->query("id")))
                gob->want_kill(me);
            else
            if (ob->can_learn_from())
                // For master, kill action cause "want_kill" to guarder
                me->want_kill(gob);

            gob->kill_ob(me);
        }
         */


        fight_ob(me, ob);
    }


    void want_kill(Living me, Living ob) {

        if(!(ob instanceof  Player)){
            return;
        }

        if(me.getKiller().contains(ob) || me.getWantKills().contains(ob)){
            return;
        }

        if(ob.getWantKills().contains(me)){
            return;
        }

        me.getKiller().add(ob);
    }

}
