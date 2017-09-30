package com.tian.server.service;

import com.tian.server.model.Living;
import com.tian.server.model.MudObject;

/**
 * Created by PPX on 2017/9/29.
 */
public class DamageService {

    void unconcious(Living me)
    {
        int n;
        int i;
        int avoid;

        //if (! living(me)) return;
        //if (wizardp(me) && query("env/immortal")) return;

        //Todo:暂时先不处理天赋
        /*if( me->query("special_skill/zhuque") && random(10) < 3)
        {
            if( me->query("qi") < me->query("max_qi") * 3 / 5)
            {
                me->set("qi", me->query("max_qi") * 3 / 5);
                me->set("eff_qi", me->query("max_qi") * 3 / 5);
            }
            if( me->query("jingli") < me->query("max_jingli") * 3 / 5)
                me->set("jingli", me->query("max_jingli") * 3 / 5);

            message_vision(HIR "\n突然间，$N身后红光爆现，犹如传说中的凤凰般美妙！\n" NOR, me);
            return;
        }*/

        //Todo:比武的处理
        Living ob = me.getCompetitor();
        if( ob != null && !ob.isKiller(me)) {
            win(ob);
            lost(me);
        }

        if(me.isBusy()){
            me.interruptMe();
        }

        if( run_override("unconcious") ) return;
        if( is_ghost() ) return;
        if( playerp(me) && env && function_exists("user_cant_die", env) ) {
            if( environment()->user_cant_die(me) )
            return;
        }
        avoid = (int)query_temp("apply/avoid_die");
        if( avoid > 90 ) avoid = 90;
        if( query_temp("special_skill/immortal") ||
                random(100) < avoid ) {
            set("eff_qi",query("max_qi"));
            set("qi",query("max_qi"));
            set("eff_jing",query("max_jing"));
            set("jing",query("max_jing"));
            message_vision(HIY "\n突然间，$N全身散发出一阵金光，如同浴血重生一般。\n" NOR, me);
            COMBAT_D->report_status(this_object());
            return;
        }


        // I am lost if in competition with others
        if (objectp(ob = me->query_competitor()) &&
                ! ob->is_killing(me->query("id")))
        {
            ob->win();
            me->lost();
        }

        if (me->is_busy()) me->interrupt_me();

        if (run_override("unconcious")) return;

        if (! last_damage_from && (applyer = query_last_applyer_id()))
        {
            last_damage_from = UPDATE_D->global_find_player(applyer);
            last_damage_name = query_last_applyer_name();
        }

        defeated_by_who = last_damage_name;
        if (defeated_by = last_damage_from)
        {
            object *dp;

            // 如果此人有主，则算主人打晕的
            if (objectp(defeated_by->query_temp("owner")))
            {
                defeated_by = defeated_by->query_temp("owner");
                defeated_by_who = defeated_by->name(1);
            } else
            if (stringp(owner_id = defeated_by->query_temp("owner_id")))
            {
                defeated_by = UPDATE_D->global_find_player(owner_id);
                if (objectp(defeated_by))
                    defeated_by_who = defeated_by->name(1);
            }

            if (query("qi") < 0 || query("jing") < 0)
                COMBAT_D->winner_reward(defeated_by, me);

            // 如果对方有意杀害我，则我愤怒，并且对方累积杀气。
            if (playerp(me) && defeated_by->is_want_kill(me->query("id")))
            {
                defeated_by->record_dp(me);
                me->craze_of_defeated(defeated_by->query("id"));
                n = 100 + random(100) + me->query_skill("force");
                if (n > defeated_by->query_skill("force"))
                    n = defeated_by->query_skill("force") / 2 +
                            random(defeated_by->query_skill("force") / 2);
                defeated_by->add("total_hatred", n);
            }
        }

        me->remove_all_enemy(0);

        // notice the write_prompt function: do not show prompt
        me->clear_written();

        message("vision", HIR "\n你的眼前一黑，接著什么也不知道了...."NOR"\n",
                me);

        me->disable_player(" <昏迷不醒>");
        me->delete_temp("sleeped");

        if (objectp(riding = me->query_temp("is_riding")))
        {
            message_vision("$N一头从$n上面栽了下来！\n",
                    me, riding);
            me->delete_temp("is_riding");
            riding->delete_temp("is_rided_by");
            riding->move(environment(me));
        }

        set("jing", 0);
        set("qi", 0);
        set_temp("block_msg/all", 10);

//	call_out("revive", 1);
        call_out("revive", random(100 - query("con")) + 30);
        COMBAT_D->announce(me, "unconcious");

        // remove the user if loaded by updated
        UPDATE_D->global_destruct_player(defeated_by, 1);
    }

    varargs void die(object killer)
    {
        object me, env;
        object corpse, room, obj, *inv;
        mixed  riding;
        object dob;
        object ob;
        string dob_name;
        string killer_name;
        string applyer;
        object tmp_load;
        int direct_die;
        int avoid;
        int i;
        int duration;
        string follow_msg = 0;

        me = this_object();
        env = environment(me);
        delete_temp("sleeped");
        delete("last_sleep");

        // 朱雀重生效果 50%几率 瞬间爆发恢复气血精力到60%
        // 如超过60%则不恢复
        if( me->query("special_skill/zhuque") && random(10) < 5)
        {
            if( me->query("qi") < me->query("max_qi") * 3 / 5)
            {
                me->set("qi", me->query("max_qi") * 3 / 5);
                me->set("eff_qi", me->query("max_qi") * 3 / 5);
            }
            if( me->query("jingli") < me->query("max_jingli") * 3 / 5)
                me->set("jingli", me->query("max_jingli") * 3 / 5);

            message_vision(HIR "\n突然间，$N身后红光爆现，犹如传说中的凤凰般美妙！\n" NOR, me);
            return;
        }

        // I am lost if in competition with others
        if( ob = me->query_competitor() ) {
            ob->win();
            me->lost();
        }
        if( wizardp(me) && query("env/immortal") ) {
            delete_temp("die_reason");
            return;
        }
        if( me->is_busy() ) me->interrupt_me();
        if( run_override("die") ) return;
        if( is_ghost() ) return;
        if( playerp(me) && env && function_exists("user_cant_die", env) ) {
            if( environment()->user_cant_die(me) )
            return;
        }
        avoid = (int)query_temp("apply/avoid_die");
        if( avoid > 90 ) avoid = 90;
        if( query_temp("special_skill/immortal") ||
                random(100) < avoid ) {
            set("eff_qi",query("max_qi"));
            set("qi",query("max_qi"));
            set("eff_jing",query("max_jing"));
            set("jing",query("max_jing"));
            message_vision(HIY "\n突然间，$N全身散发出一阵金光，如同浴血重生一般。\n" NOR, me);
            COMBAT_D->report_status(this_object());
            return;
        }

        if( playerp(me) && env && env->query("no_death") ) {
            remove_call_out("revive");
            unconcious();
            return;
        }

        if (ob = me->query_competitor())
        {
            ob->win();
            me->lost();
        }

        if (wizardp(me) && query("env/immortal"))
        {
            delete_temp("die_reason");
            return;
        }

        if (me->is_busy()) me->interrupt_me();

        if (run_override("die")) return;

        if (! last_damage_from && (applyer = query_last_applyer_id()))
        {
            tmp_load = UPDATE_D->global_find_player(applyer);
            last_damage_from = tmp_load;
            last_damage_name = query_last_applyer_name();
        }

        if (! killer)
        {
            killer = last_damage_from;
            killer_name = last_damage_name;
        } else
            killer_name = killer->name(1);

        // record defeater first, because revive will clear it
        if (! living(me))
        {
            direct_die = 0;
            if (userp(me) || playerp(me))
                revive(1);
            else
                me->delete("disable_type");
        } else
            direct_die = 1;

        if (direct_die && killer)
            // direct to die ? call winner_reward
            COMBAT_D->winner_reward(killer, me);

        if (objectp(riding = me->query_temp("is_riding")))
        {
            message_vision("$N一头从$n上面栽了下来！\n",
                    me, riding);
            me->delete_temp("is_riding");
            riding->delete_temp("is_rided_by");
            riding->move(environment(me));
        }

        // Check how am I to die
        dob = defeated_by;
        dob_name = defeated_by_who;
        if( !query_temp("die_reason") ) {
            if( playerp(me) && dob_name && killer_name &&
                    (dob_name != killer_name || dob != killer) ) {
                if( dob && playerp(dob) && dob->is_want_kill(query("id")) ) {
                    if( !dob->query_condition("killer") ) {
                        follow_msg = "听说官府发下海捕文书，缉拿杀人肇事凶手" +
                                dob->name(1) + "。";
                        dob->apply_condition("killer", 500);
                    } else {
                        follow_msg = "听说官府加紧捉拿累犯重案的肇事暴徒" +
                                dob->name(1) + "。";
                        dob->apply_condition("killer", 800 +
                                (int)dob->query_condition("killer"));
                    }
                    dob->set("combat/pktime", time());
                }
                // set the die reason
                set_temp("die_reason", "被" +
                        dob_name + "打晕以后，被" +
                        (dob_name == killer_name ? "另一个" : "") +
                        killer_name + "趁机杀掉了");
            } else if( playerp(me) && killer_name && !killer ) {
                set_temp("die_reason", "被" + killer_name +
                        HIM "杀害了");
            }
        }

        if (COMBAT_D->player_escape(killer, this_object()))
        {
            UPDATE_D->global_destruct_player(tmp_load, 1);
            return;
        }

        COMBAT_D->announce(me, "dead");

        if (objectp(killer))
            set_temp("my_killer", killer->query("id"));

        COMBAT_D->killer_reward(killer, me);

        // remove the user if loaded by updated
        UPDATE_D->global_destruct_player(tmp_load, 1);

        me->add("combat/dietimes", 1);

        if (objectp(corpse = CHAR_D->make_corpse(me, killer)))
            corpse->move(environment());

        defeated_by = 0;
        defeated_by_who = 0;
        me->remove_all_killer();
        all_inventory(environment())->remove_killer(me);

        me->dismiss_team();
        if (userp(me) || playerp(me))
        {
            if (me->is_busy())
                me->interrupt_me();
            set("jing", 1); set("eff_jing", 1);
            set("qi", 1);   set("eff_qi", 1);
            ghost = 1;
            me->move(DEATH_ROOM);
            DEATH_ROOM->start_death(me);
            me->delete_temp("die_reason");
            me->craze_of_die(killer ? killer->query("id") : 0);

            // add by chenzzz，死亡保护
            me->set("die_protect/last_dead", time());
            duration = (int)me->query("die_protect/duration", 1);

            if (me->query("combat_exp") > 1000000 && duration > 0)
            {
                duration = duration * 2;
            }
            else
            {
                duration = 2 * 60 * 60;
            }

            if (duration > 3 * 86400)
                duration = 3 * 86400;

            me->set("die_protect/duration", duration);
        } else
            destruct(me);
    }

    public void win(Living ob){

    }

    public void lost(Living ob){

    }

}
