package com.tian.server.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.util.MsgUtil;
import com.tian.server.util.UnityCmdUtil;
import com.tian.server.util.UserCacheUtil;
import net.sf.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/9/29.
 */
public class HeartBeatService {

    private AttackService attackService = new AttackService();
    private MessageService messageService = new MessageService();
    private DamageService damageService = new DamageService();

    public void heartBeat() {

        //获取living列表
        List<Living> allLivings = UserCacheUtil.getAllLivings();
        for (Living living : allLivings) {
            if (!living.getHeartBeatFlag()) {
                continue;
            }
            heartProcess(living);
        }

    }

    public void heartProcess(Living ob) {

        int t;
        int period;
        int wimpy_ratio, cnd_flag;
        int is_player;

        if (ob.getQi() <= 0 || ob.getJing() < 0) {

            if (!ob.getLiving()) {
                damageService.die(ob, ob.getLastDamageFrom());
            } else {
                damageService.unconcious(ob);
            }

            if (!ob.getLiving()) {
                return;
            }
        }

        if (ob.isBusy()) {

            if (ob.isFighting() && (ob instanceof  Player)) {
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(UnityCmdUtil.getInfoWindowRet("\n--->>你上一个动作没有完成，失去一次进攻机会。\n"));
                MsgUtil.sendMsg(((Player) ob).getSocketClient(), jsonArray);
            }

            ob.continueAction();
        } else if (ob.getLiving()) {
            String apply;
            Object apply_ob;

            // Is it time to flee?
            //Todo:自动逃跑暂时不处理
            /*if (is_fighting() &&
                    intp(wimpy_ratio = (int) query("env/wimpy")) &&
                    wimpy_ratio > 0 &&
                    (my["qi"] * 100 / my["max_qi"] <= wimpy_ratio ||
                            my["jing"] * 100 / my["max_jing"] <= wimpy_ratio)) {
                if (stringp(apply = query("env/wimpy_apply")) &&
                        objectp(apply_ob = present(apply, me)) &&
                        apply_ob -> query("can_apply_for_wimpy")) {
                    apply_ob -> apply_for_wimpy(this_object());
                } else
                    GO_CMD -> do_flee(this_object());
            }*/

            attackService.attack(ob);
        }

        //更新血量
        if(ob instanceof  Player) {

            //准备状态字符串，然后发送消息
            JSONArray jArray = UnityCmdUtil.getPlayerStatus((Player)ob);
            ((Player) ob).getSocketClient().sendEvent("status", jArray);
        }

        return;

        /*if (my["doing"] == "scheme")
            SCHEME_CMD -> execute_schedule(me);

        if (!me) return;

        if (!(is_player = playerp(me))) {
            me -> scan();
            // scan() may do anything -- include destruct(this_object())
            if (!me) return;
        }

        if ((t = time()) < next_beat) return;
        else next_beat = t + 5 + random(10);

        if (!my["not_living"])
            cnd_flag = update_condition();
        if (!me) return;

        if (!(cnd_flag & CND_NO_HEAL_UP))
            cnd_flag = heal_up();

        // If we are compeletely in peace, turn off heart beat.
        // heal_up() must be called prior to other two to make sure it is called
        // because the && operator is lazy :P
        if (!cnd_flag &&
                !is_player &&
                !keep_beat_flag &&
                !is_fighting() &&
                !is_busy() &&
                !interactive(this_object())) {
            if (environment() && query("chat_msg")) {
                ob = first_inventory(environment());
                while (ob && !interactive(ob))
                    ob = next_inventory(ob);
            } else
                ob = 0;
            if (!ob) set_heart_beat(0);
        }

        if (!me || !is_player) return;

        // Make us a bit older. Only player's update_age is defined.
        // Note: update_age() is no need to be called every heart_beat, it
        //       remember how much time has passed since last call.
        me -> update_age();

        if (living(me)) {
            period = t - ((int) my["last_save"]);
            if (period < 0 || period > 5 * 60) {
                string msg;
                msg = HIG "【档案存储】您的档案已经自动存盘。" NOR "\n";
                ;
                if (!me -> save())
                    msg = HIR "【数据保护】由于数据异常，您的档案本次存盘失败。" NOR "\n";
                set("last_save", t);
                tell_object(me, msg);
            }
        }

        if (!interactive(me))
            return;

        if (my["food"] <= 0 || my["water"] <= 0) {
            if (environment() && !environment()->is_chat_room() && !query_condition("hunger"))
            {
                apply_condition("hunger", 1);
            }
        }

        if (query_idle(me) > IDLE_TIMEOUT && !wizardp(me)) {
            if (!mapp(my["env"]) || !my["env"]["keep_idle"])
                me -> user_dump(DUMP_IDLE);
            else
                receive(NOR);
        }*/

    }

    private void unconcious(Living ob) {

    }

    private void die(Living ob) {


    }

}
