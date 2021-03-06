package com.tian.server.service;

import com.tian.server.common.Ansi;
import com.tian.server.entity.RoomEntity;
import com.tian.server.model.*;
import com.tian.server.util.LuaBridge;
import com.tian.server.util.MapGetUtil;
import com.tian.server.util.MsgUtil;
import com.tian.server.util.UnityCmdUtil;
import net.sf.json.JSONArray;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by PPX on 2017/9/29.
 */
public class DamageService {

    private CombatService combatService = new CombatService();
    private MessageService messageService = new MessageService();

    void unconcious(Living me) {
        int n;
        int avoid;

        if (!me.getLiving()) { //如果死亡直接返回
            return;
        }
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

        //调用类自己的重载函数
        Method m[] = me.getClass().getDeclaredMethods(); // 取得全部的方法
        for (int i = 0; i < m.length; i++) {
            String mod = Modifier.toString(m[i].getModifiers()); // 取得访问权限
            String metName = m[i].getName(); // 取得方法名称

            if (!metName.equals("unconcious") || !mod.equals("public")){
                continue;
            }
            try {
                m[i].invoke(null);
            } catch (Exception e) {
            }
            return;
        }

        if(me.getCmdActions().get("unconcious") != null){

            LuaBridge bridge = new LuaBridge();
            String luaPath = this.getClass().getResource(me.getResource()).getPath();
            Globals globals = JsePlatform.standardGlobals();
            //加载脚本文件login.lua，并编译
            globals.loadfile(luaPath).call();
            String funName = me.getCmdActions().get("unconcious");
            //获取带参函数create
            LuaValue createFun = globals.get(LuaValue.valueOf(me.getCmdActions().get(funName)));
            //执行方法初始化数据
            LuaValue retValue = createFun.call(CoerceJavaToLua.coerce(bridge), LuaValue.valueOf(me.getUuid().toString()));
            return;
        }

        if( me.getGhost() ) {
            return;
        }

        //Todo:暂时没用到，注释掉
        /*if( playerp(me) && env && function_exists("user_cant_die", env) ) {
            if( environment()->user_cant_die(me) )
            return;
        }*/
        Random r = new Random();

        /*avoid = Integer.parseInt(me.queryTemp("apply/avoid_die").toString());
        if( avoid > 90 ) avoid = 90;
        if( me.queryTemp("special_skill/immortal") != null || r.nextInt(100) < avoid ) {
            set("eff_qi",query("max_qi"));
            set("qi",query("max_qi"));
            set("eff_jing",query("max_jing"));
            set("jing",query("max_jing"));
            message_vision(HIY "\n突然间，$N全身散发出一阵金光，如同浴血重生一般。\n" NOR, me);
            COMBAT_D->report_status(this_object());
            return;
        }*/

        // I am lost if in competition with others
        ob = me.getCompetitor();
        if( ob != null && !ob.isKiller(me)) {
            win(ob);
            lost(me);
        }

        if(me.isBusy()){
            me.interruptMe();
        }

        //调用类自己的重载函数
        m = me.getClass().getDeclaredMethods(); // 取得全部的方法
        for (int i = 0; i < m.length; i++) {
            String mod = Modifier.toString(m[i].getModifiers()); // 取得访问权限
            String metName = m[i].getName(); // 取得方法名称

            if (!metName.equals("unconcious") || !mod.equals("public")){
                continue;
            }
            try {
                m[i].invoke(null);
            } catch (Exception e) {
            }
            return;
        }

        if(me.getCmdActions().get("unconcious") != null){

            LuaBridge bridge = new LuaBridge();
            String luaPath = this.getClass().getResource(me.getResource()).getPath();
            Globals globals = JsePlatform.standardGlobals();
            //加载脚本文件login.lua，并编译
            globals.loadfile(luaPath).call();
            String funName = me.getCmdActions().get("unconcious");
            //获取带参函数create
            LuaValue createFun = globals.get(LuaValue.valueOf(me.getCmdActions().get(funName)));
            //执行方法初始化数据
            LuaValue retValue = createFun.call(CoerceJavaToLua.coerce(bridge), LuaValue.valueOf(me.getUuid().toString()));
            return;
        }

        //
        Living applyer = me.getLastApplyerId();
        if(me.getLastDamageFrom() == null && applyer != null){

            me.setLastDamageFrom(applyer);
            me.setLastDamageName(me.getLastApplyerName());
        }

        me.setDefeatedByWho(me.getLastDamageFrom());

        if (me.getLastDamageFrom() != null) {
            me.setDefeatedBy(me.getLastDamageFrom());

            Living owner = (Living)me.getDefeatedBy().queryTemp("owner");

            // 如果此人有主，则算主人打晕的
            if (owner != null) {
                me.setDefeatedBy(owner);
                me.setDefeatedByWho(owner);
            } /*else if (stringp(owner_id = defeated_by->query_temp("owner_id"))) {
                defeated_by = UPDATE_D->global_find_player(owner_id);
                if (objectp(defeated_by))
                    defeated_by_who = defeated_by->name(1);
            }*/

            CombatService combatService = new CombatService();
            if (me.getQi() < 0 || me.getJing() < 0) {
                combatService.winnerReward(me.getDefeatedBy(), me);
            }

            // 如果对方有意杀害我，则我愤怒，并且对方累积杀气。
            if((me instanceof Player) && me.getDefeatedBy().getWantKills().contains(me)){

                recordDp(me.getDefeatedBy(), me);

                //因被打倒愤怒
                String defeateId = "";
                if(me.getDefeatedBy() instanceof  Player){
                    defeateId = "player/" + me.getDefeatedBy().getId().toString();
                }else{
                    defeateId = "npc/" + me.getDefeatedBy().getId().toString();
                }
                me.setMyDefeaterId(defeateId);

                Living defeatedBy = me.getDefeatedBy();
                n = 100 + r.nextInt(100) + me.querySkill("force", 0);
                if (n > defeatedBy.querySkill("force", 0))
                    n = defeatedBy.querySkill("force", 0) / 2 +
                            r.nextInt(defeatedBy.querySkill("force", 0) / 2);
                defeatedBy.add("total_hatred", n);
            }
        }

        me.getEnemy().clear();

        //Todo:被打晕以后不接受任何信息
        // notice the write_prompt function: do not show prompt
        //me->clear_written();


        if(me instanceof  Player) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(UnityCmdUtil.getInfoWindowRet(Ansi.HIR + "\n你的眼前一黑，接著什么也不知道了...." + Ansi.NOR + "\n"));
            MsgUtil.sendMsg(((Player) me).getSocketClient(), jsonArray);
        }

        //Todo;
        //me->disable_player(" <昏迷不醒>");
        me.setLiving(false);
        me.deleteTemp("sleeped");

        Living riding = (Living)me.queryTemp("is_riding");
        //Todo:坐骑暂时不处理
        /*if (riding != null)
        {
            message_vision("$N一头从$n上面栽了下来！\n",
                    me, riding);
            me.deleteTemp("is_riding");
            riding.deleteTemp("is_rided_by");
            riding.move(environment(me));
        }*/

        me.set("jing", 0);
        me.set("qi", 0);
        me.setTemp("block_msg/all", 10);

//	call_out("revive", 1);

        int randomSeconds = (r.nextInt(100 - me.getCon()) + 30) * 1000;
        Timer timer=new Timer();//实例化Timer类
        timer.schedule(new ReviveTask(me), randomSeconds);//五百毫秒
        //call_out("revive", random(100 - query("con")) + 30);

        CombatService combatService = new CombatService();
        combatService.announce(me, "unconcious");

        // remove the user if loaded by updated
        //UPDATE_D->global_destruct_player(defeated_by, 1);

        /*if(me.getDefeatedBy() != null) {
            ObjectService objectService = new ObjectService();
            objectService.destruct(me.getDefeatedBy());
        }*/
        me.setLiving(false);
    }


    public void win(Living ob){

    }

    public void lost(Living ob){

    }

    private void recordDp(Living me, Living ob) {

        List<Living> dp = null;

        if (!me.getWantKills().contains(ob)) {
            // only if I want kill ob, I will record it.
            return;
        }

        dp = (List<Living>)me.queryTemp("defeat_player");
        if ( dp == null){
            dp = new ArrayList<Living>();
        }
        dp.add(ob);

        me.setTemp("defeat_player", dp);
    }

    public void revive(Living me, int quiet) {
        int i;

        RoomEntity env = me.getLocation();

        me.delete("disable_type");
        me.setTemp("block_msg/all", 0);
        //me.enable_player();

        // write the prompt
        //me->write_prompt();

        /*if (objectp(defeated_by))
            defeated_by->remove_dp(me);*/

        if (quiet == 0) {

            me.setDefeatedBy(null);
            me.setDefeatedByWho(null);
            combatService.announce(me, "revive");
            if(me instanceof  Player){

                JSONArray jsonArray = new JSONArray();
                jsonArray.add(UnityCmdUtil.getInfoWindowRet(Ansi.HIY + "\n慢慢地你终于又有了知觉...." + Ansi.NOR + "\n"));
                MsgUtil.sendMsg(((Player) me).getSocketClient(), jsonArray);
            }
        }

        me.setLastDamageFrom(null);
        me.setLastDamageName(null);
    }


    public void die(Living me, Living killer) {

        RoomEntity env;
        Living dob;
        Living ob;
        String dob_name = "";
        String killer_name = "";
        Living applyer;
        int direct_die;
        int avoid;
        int i;
        int duration;
        String follow_msg = "";

        env = me.getLocation();
        me.deleteTemp("sleeped");
        me.delete("last_sleep");

        // 朱雀重生效果 50%几率 瞬间爆发恢复气血精力到60%
        // 如超过60%则不恢复
        /*if( me->query("special_skill/zhuque") && random(10) < 5)
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

        ob = me.getCompetitor();
        // I am lost if in competition with others
        if (ob != null) {
            win(ob);
            lost(me);
        }
        /*if( wizardp(me) && query("env/immortal") ) {
            delete_temp("die_reason");
            return;
        }*/

        if (me.isBusy()) {
            me.interruptMe();
        }

        //if( run_override("die") ) return;

        if(me.getGhost()){
            return;
        }
//        if( playerp(me) && env && function_exists("user_cant_die", env) ) {
//            if( environment()->user_cant_die(me) )
//            return;
//        }
        Random random = new Random();
        avoid = MapGetUtil.queryTempInteger(me, "apply/avoid_die");
        if (avoid > 90) avoid = 90;
        /*if( me.queryTemp("special_skill/immortal") != null ||
                random.nextInt(100) < avoid ) {
            set("eff_qi",query("max_qi"));
            set("qi",query("max_qi"));
            set("eff_jing",query("max_jing"));
            set("jing",query("max_jing"));
            message_vision(HIY "\n突然间，$N全身散发出一阵金光，如同浴血重生一般。\n" NOR, me);
            COMBAT_D->report_status(this_object());
            return;
        }*/

        /*if( playerp(me) && env && env->query("no_death") ) {
            remove_call_out("revive");
            unconcious();
            return;
        }*/

        if (ob != null) {
            win(ob);
            lost(me);
        }

        /*if (wizardp(me) && query("env/immortal"))
        {
            delete_temp("die_reason");
            return;
        }*/

        if (me.isBusy()) {
            me.interruptMe();
        }

        //Todo:
        //if (run_override("die")) return;

        applyer = me.getLastApplyerId();
        //Todo:如果遭受攻击为0，就从condition里找寻最后伤害的来源
        if (me.getLastDamageFrom() == null && applyer != null) {
            me.setLastDamageFrom(applyer);
        }

        if (killer == null) {
            killer = me.getLastDamageFrom();
        }

        // record defeater first, because revive will clear it
        if (!me.getLiving()) {

            direct_die = 0;

            if (me instanceof Player) {
                revive(me, 1);
            } else {
                me.delete("disable_type");
            }
        } else {
            direct_die = 1;
        }

        if (direct_die == 1 && killer != null) {
            // direct to die ? call winner_reward
            combatService.winnerReward(killer, me);
        }

        /*if (objectp(riding = me->query_temp("is_riding")))
        {
            message_vision("$N一头从$n上面栽了下来！\n",
                    me, riding);
            me->delete_temp("is_riding");
            riding->delete_temp("is_rided_by");
            riding->move(environment(me));
        }*/

        // Check how am I to die
        dob = me.getDefeatedBy();
        if (me.queryTemp("die_reason") == null) {
            if (me instanceof Player && dob != killer) {

                if (dob != null && (dob instanceof Player) && dob.getWantKills().contains(me)) {
                    if (dob.queryCondition("killer") == null) {
                        follow_msg = "听说官府发下海捕文书，缉拿杀人肇事凶手" +
                                dob.getName() + "。";
                        dob.applyCondition("killer", 500);
                    } else {
                        follow_msg = "听说官府加紧捉拿累犯重案的肇事暴徒" +
                                dob.getName() + "。";
                        dob.applyCondition("killer", 800 +
                                Integer.parseInt(dob.queryCondition("killer").toString()));
                    }

                    Integer timeStamp = (int) (System.currentTimeMillis() / 1000);
                    dob.set("combat/pktime", timeStamp);
                }
                // set the die reason
                me.setTemp("die_reason", "被" +
                        dob_name + "打晕以后，被" +
                        (dob_name == killer_name ? "另一个" : "") +
                        killer_name + "趁机杀掉了");
            } else if ((me instanceof Player) && killer != null) {
                me.setTemp("die_reason", "被" + killer.getName() +
                        Ansi.HIM + "杀害了");
            }
        }

        if (combatService.player_escape(killer, me) != 0) {

            //UPDATE_D->global_destruct_player(tmp_load, 1);
            return;
        }

        combatService.announce(me, "dead");
        if (killer != null) {
            me.setTemp("my_killer", killer);
        }
        combatService.killerReward(killer, me);

        // remove the user if loaded by updated
        //Todo:
        //UPDATE_D->global_destruct_player(tmp_load, 1);

        me.add("combat/dietimes", 1);

        //Todo:产生尸体
        CharService charService = new CharService();
        GoodsContainer corpse = charService.makeCorpse(me, killer);
        if(corpse != null){
            EnvironmentService environmentService = new EnvironmentService();
            environmentService.move(corpse, me.getLocation().getName());
        }

        //把物品放到房间中，并广播物品进入的消息
        /*if (objectp(corpse = CHAR_D->make_corpse(me, killer)))
            corpse->move(environment());
*/
        me.setDefeatedBy(null);
        me.setDefeatedByWho(null);
        me.removeAllKiller();
        //all_inventory(environment())->remove_killer(me);

        //me->dismiss_team();

        if (me instanceof Player) {

            if (me.isBusy()) {
                me.interruptMe();
            }

            me.setJing(1);
            me.setEffJing(1);
            me.setQi(1);
            me.setEffQi(1);
            me.setGhost(true);

            EnvironmentService environmentService = new EnvironmentService();
            environmentService.move(me, "death/gate");
            me.deleteTemp("die_reason");

            // add by tian，死亡保护
            me.set("die_protect/last_dead", System.currentTimeMillis() );
            duration = MapGetUtil.queryInteger(me, "die_protect/duration");

            if(me.getCombatExp() > 1000000 && duration > 0){
                duration = duration * 2 * 1000;
            }else{
                duration = 2 * 60 * 60 * 1000;
            }

            if(duration > 3 * 86400){
                duration = 3 * 86400 * 1000;
            }
            me.set("die_protect/duration", duration);
        } else {
            ObjectService objectService = new ObjectService();
            objectService.destruct(me);
        }
    }

    public int heal_up(Living me) {
        int update_flag, i;
        int scale;

        // Update the affect of nopoison
        if (me.queryTemp("nopoison") != null) {
            me.deleteTemp("nopoison");
        }

        // Am I in prison ?
        //Todo:监狱暂时不处理
        /*if (me->is_in_prison())
        {
            me->update_in_prison();
            return 1;
        }*/

        boolean isUser = (me instanceof Player);
        scale = me.getLiving() ? 1 : (isUser ? 4 : 8);
        update_flag = 0;
        Map<String ,Object> my = me.queryEntire();
        EnvironmentService environmentService = new EnvironmentService();

        if (!isUser || ( !environmentService.isChatRoom(me)
                        && (my.get("doing") == null || my.get("doing").toString().equals("scheme")))) {

            if(me.getWater() > 0){
                me.setWater(me.getWater() - 1);
                update_flag++;
            }

            if(me.getFood() > 0){
                me.setFood(me.getFood() - 1);
                update_flag++;
            }

            if (me.getWater() < 1 && isUser) {
                return update_flag;
            }

            //Todo:守卫模式暂时不处理
            /*if ((guard = me->query_temp("guardfor")) &&
                    (! objectp(guard) || ! guard->is_character()))
            {
                if (my["jing"] * 100 / my["max_jing"] < 50)
                {
                    tell_object(me, "你觉得太累了，需要放松放松了。\n");
                    command("guard cancel");
                    return update_flag;
                }

                my["jing"] -= 30 + random(20);
                switch (random(8))
                {
                    case 0: message_vision("$N紧张的盯着四周来往的行人。\n", me);
                        break;

                    case 1: message_vision("$N打了个哈欠，随即振作精神继续观察附近情况。\n", me);
                        break;

                    case 2: message_vision("$N左瞅瞅，右看看，不放过一个可疑的人物。\n", me);
                        break;

                    case 3: message_vision("$N打起精神细细的观察周围。\n", me);
                        break;
                }
                update_flag++;
                return update_flag;
            }*/

            me.setJing( me.getJing() + (me.getCon() + me.getMaxJingLi() / 10) / scale);
            if(me.getJing() >= me.getEffJing()){
                me.setJing(me.getEffJing());
                if (me.getEffJing() < me.getMaxJing()) {
                    me.setEffJing(me.getEffJing() + 1);
                    update_flag++;
                }
            }else{
                update_flag++;
            }

            if(!me.isBusy()){
                me.setQi(me.getQi() + (me.getCon() * 2 + me.getMaxNeili() / 20) / scale);
            }
            if(me.getQi() >= me.getEffQi()){
                me.setQi(me.getEffQi());
                if (me.getEffQi() < me.getMaxQi()) {
                    me.setEffQi(me.getEffQi() + 1);
                    update_flag++;
                }
            }else{
                update_flag++;
            }

            if (me.getFood() < 1 && isUser) {
                return update_flag;
            }

            if (me.getMaxJingLi() > 0 && me.getJingLi() < me.getMaxJingLi()) {

                Integer forceAdd = 0; //(int)me->query_skill("force") / 6
                me.setJingLi(me.getJingLi() + me.getCon() + forceAdd);
                if(me.getJingLi() > me.getMaxJingLi()){
                    me.setJingLi(me.getMaxJingLi());
                }
                update_flag++;
            }

            if(me.getMaxNeili() > 0 && me.getNeili() < me.getMaxNeili()) {
                Integer forceAdd = 0; //(int)me->query_skill("force") / 3;
                me.setNeili(me.getNeili() + me.getCon() * 2 + forceAdd);
                if (me.getNeili() > me.getMaxNeili()) {
                    me.setNeili(me.getMaxNeili());
                }
                update_flag++;
            }
        }
        return update_flag;
    }

    class ReviveTask extends TimerTask {
        private Living me;

        public ReviveTask(Living me) {
            this.me = me;
        }

        @Override
        public void run() {
            revive(this.me, 0);
        }

    }

}
