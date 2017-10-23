package com.tian.server.bll;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.common.Ansi;
import com.tian.server.common.LivingStatus;
import com.tian.server.model.Living;
import com.tian.server.model.MudObject;
import com.tian.server.model.Player;
import com.tian.server.model.Race.Human;
import com.tian.server.model.RoomObjects;
import com.tian.server.service.AttackService;
import com.tian.server.service.CombatService;
import com.tian.server.service.MessageService;
import com.tian.server.service.RankService;
import com.tian.server.util.MapGetUtil;
import com.tian.server.util.UnityCmdUtil;
import com.tian.server.util.UserCacheUtil;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import org.apache.commons.lang.ObjectUtils;
import sun.plugin2.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by PPX on 2017/7/11.
 */
public class CombatBll extends BaseBll {

    public CombatBll(SocketIOClient socketIOClient) {
        super(socketIOClient);
    }

    public void handleFight(String euid) {

        JSONArray jsonArray = new JSONArray();
        int userId = UserCacheUtil.getUserSockets().get(this.socketIOClient);
        if (userId < 1) {
            return;
        }

        Player me = (Player) UserCacheUtil.getPlayers().get(userId);

        //检查当前房间是否可以战斗
        if (me.getLocation().getNoFight() == 1) {
            jsonArray.add(UnityCmdUtil.getInfoWindowRet("这里禁止战斗。"));
            sendMsg(jsonArray);
            return;
        }

        Map<Long, MudObject> allObjects = UserCacheUtil.getAllObjects();
        MudObject mudOb = allObjects.get(Long.valueOf(euid));
        if (!(mudOb instanceof Living)) {
            jsonArray.add(UnityCmdUtil.getInfoWindowRet("看清楚一点，那并不是生物。"));
            sendMsg(jsonArray);
            return;
        }

        Living ob = (Living) mudOb;
        if (ob.getLocation().getId() != me.getLocation().getId()) {
            jsonArray.add(UnityCmdUtil.getInfoWindowRet("你想攻击谁？"));
            sendMsg(jsonArray);
            return;
        }

        //是不是当前的敌人
        if (ob.getEnemy().contains(me)) {
            jsonArray.add(UnityCmdUtil.getInfoWindowRet("加油！加油！加油！"));
            sendMsg(jsonArray);
            return;
        }

        if (ob.getStatus().intValue() != LivingStatus.NORMAL.toInteger()) {
            jsonArray.add(UnityCmdUtil.getInfoWindowRet(ob.getName() + "已经无法战斗了。"));
            sendMsg(jsonArray);
            return;
        }

        if (me.getQi() <= (me.getMaxQi() * 3 / 10)) {
            jsonArray.add(UnityCmdUtil.getInfoWindowRet("你现在没有力气战斗了。"));
            sendMsg(jsonArray);
            return;
        }

        if (me.getUuid() == ob.getUuid()) {
            jsonArray.add(UnityCmdUtil.getInfoWindowRet("你不能攻击自己。"));
            sendMsg(jsonArray);
            return;
        }

        if (ob instanceof Human) {
            List<SocketIOClient> excludeClients = new ArrayList<SocketIOClient>();
            excludeClients.add(((Player) me).getSocketClient());
            if (ob instanceof Player) {
                excludeClients.add(((Player) ob).getSocketClient());
            }

            MessageService messageService = new MessageService();
            RankService rankService = new RankService();
            messageService.message_vision("\n$N对著$n说道："
                    + rankService.querySelf(me)
                    + me.getName() + "，领教"
                    + rankService.queryRespect(ob) + "的高招！\n\n", me, ob);

            Object oldTarget = me.queryTemp("pending/fight");
            if (oldTarget != null && oldTarget instanceof Living) {

                if (oldTarget instanceof Player) {
                    JSONArray tellArray = new JSONArray();
                    tellArray.add(UnityCmdUtil.getInfoWindowRet(Ansi.YEL + me.getName() + "取消了和你比试的念头。" + Ansi.NOR));
                    sendMsg(((Player) oldTarget).getSocketClient(), tellArray);
                }
            }

            //设置比武对象
            me.setTemp("pending/fight", ob);

            JSONArray jsonArray1 = new JSONArray();
            jsonArray1.add(UnityCmdUtil.getInfoWindowRet("看起来" + ob.getName() + "并不想跟你较量。"));
            sendMsg(jsonArray1);

            CombatService combatService = new CombatService();
            if (combatService.accept_fight(ob, me) != 1) {
                return;
            }
            AttackService attackService = new AttackService();
            attackService.fight_ob(me, ob);
            attackService.fight_ob(ob, me);
        } else {

            MessageService messageService = new MessageService();
            RankService rankService = new RankService();
            messageService.message_vision("\\n$N大喝一声，开始对$n发动攻击！\\n\\n", me, ob);
            AttackService attackService = new AttackService();
            attackService.fight_ob(me, ob);
            attackService.kill_ob(ob, me);
            //me->fight_ob(obj);
            // obj->kill_ob(me);
        }


        RoomObjects roomObjects = UserCacheUtil.getRoomObjectsCache().get(me.getPlayerInfo().getRoomName());
        for (Player player : roomObjects.getPlayers()) {

            if (player.getCmdName().equals(euid)) {

                me.addEnemy(UserCacheUtil.getPlayers().get(player.getUser().getId()));
                UserCacheUtil.getPlayers().get(player.getUser().getId()).addEnemy(me);
                break;
            }
        }
    }

    public void handleKill(String euid) {

        JSONArray jsonArray = new JSONArray();
        int userId = UserCacheUtil.getUserSockets().get(this.socketIOClient);
        if (userId < 1) {
            return;
        }

        Player me = (Player) UserCacheUtil.getPlayers().get(userId);
        //检查当前房间是否可以战斗
        if (me.getLocation().getNoFight() == 1) {
            jsonArray.add(UnityCmdUtil.getInfoWindowRet("这里不准战斗。"));
            sendMsg(jsonArray);
            return;
        }

        Map<Long, MudObject> allObjects = UserCacheUtil.getAllObjects();
        MudObject mudOb = allObjects.get(Long.valueOf(euid));
        if(mudOb == null){
            jsonArray.add(UnityCmdUtil.getInfoWindowRet("你想杀谁？"));
            sendMsg(jsonArray);
            return;
        }

        if (!(mudOb instanceof Living)) { //obj->is_corpse()
            jsonArray.add(UnityCmdUtil.getInfoWindowRet("看清楚一点，那并不是活物。"));
            sendMsg(jsonArray);
            return;
        }

        Living ob = (Living) mudOb;
        if (ob.getLocation().getId() != me.getLocation().getId()) {
            jsonArray.add(UnityCmdUtil.getInfoWindowRet("这里没有这个人。"));
            sendMsg(jsonArray);
            return;
        }

        Long ownerUuid = Long.parseLong(ob.query("owner") == null ? "0" : ob.query("owner").toString());
        if (ownerUuid == me.getUuid()) {
            jsonArray.add(UnityCmdUtil.getInfoWindowRet("你竟然要杀自己的宠物，太不人道了吧！！！\n"));
            sendMsg(jsonArray);
            return ;
        }
        if (ob.getUuid() == me.getUuid()) {
            jsonArray.add(UnityCmdUtil.getInfoWindowRet("自杀请点击自杀按钮！\n"));
            sendMsg(jsonArray);
            return;
        }

       /* if (arrayp(me->query_team()) &&
                member_array(obj, me->query_team()) != -1)
            return notify_fail("连你自己队伍中的人也要杀吗？太不人道了吧！\n");*/

        if (me.isFighting(ob) && me.isKiller(ob)) {
            jsonArray.add(UnityCmdUtil.getInfoWindowRet("你正在和人家生死相扑呢。\n"));
            sendMsg(jsonArray);
            return;
        }

        //Todo:暂时注释，测试用到
        /*if (!me.isFighting(ob) && me.getQi() <= (me.getMaxQi() * 3 / 10)) {
            jsonArray.add(UnityCmdUtil.getInfoWindowRet("你现在没有力气战斗了。"));
            sendMsg(jsonArray);
            return;
        }*/

        RankService rankService = new RankService();
        MessageService messageService = new MessageService();
        String callname = rankService.queryRude(ob);
        Random random = new Random();
        if (ob.query("can_speak") != null) {
            if (random.nextInt(3) > 1) {
                messageService.message_vision("\n$N对著$n喝道：「"
                        + callname + "！今日不是你死就是我活！」\n\n", me, ob);
            } else {
                messageService.message_vision("\n$N对著$n喝道：「"
                        + callname + "！明年今日就是你的忌日！」\n\n", me, ob);
            }
        }

        CombatService combatService = new CombatService();
        if (combatService.acceptKill(ob, me) != 1) {
            return;
        }

        if(me instanceof  Player && ob instanceof  Player && !ob.getWantKills().contains(me)){
            me.getWantKills().add(ob);
        }

        AttackService attackService = new AttackService();
        attackService.kill_ob(me, ob);

        if(ob.getLiving() && !(ob instanceof Player)){
            attackService.kill_ob(ob, me);
        }else{
            attackService.fight_ob(ob, me);
        }

        return;
    }
}