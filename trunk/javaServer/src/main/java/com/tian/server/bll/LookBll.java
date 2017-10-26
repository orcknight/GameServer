package com.tian.server.bll;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.common.Ansi;
import com.tian.server.common.GoodsType;
import com.tian.server.common.TaskActionType;
import com.tian.server.entity.PlayerFamilyEntity;
import com.tian.server.entity.PlayerTrackEntity;
import com.tian.server.entity.RoomGateEntity;
import com.tian.server.model.*;
import com.tian.server.model.Race.Human;
import com.tian.server.service.CombatService;
import com.tian.server.util.*;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/6/29.
 */
public class LookBll extends BaseBll {

    public LookBll(SocketIOClient socketIOClient) {
        super(socketIOClient);
    }


    public void look(String target) {

        String type = target.split("/")[1];
        String id = target.split("#")[1];

        //存储观察id
        Map<Integer, Living> cacheMap = UserCacheUtil.getPlayers();
        Player player = (Player) cacheMap.get(this.userId);
        if (player == null) {
            return;
        }
        player.setLookId(new StringBuffer(target).toString());

        if (type.equals("user")) {

            Map<Long, MudObject> allLivings = UserCacheUtil.getAllObjects();
            Living npc = (Living) allLivings.get(Long.valueOf(id));
            String desc = lookLiving(getMe(), npc);

            if (npc != null) {

                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("desc", desc);
                jsonObject.put("buttons", getAct(getMe(), npc));
                jsonArray.add(UnityCmdUtil.getObjectInfoPopRet(jsonObject));
                sendMsg(jsonArray);
            }
            return;
            /*if (npc != null) {
                sendMsg(npc.getLookStr() + getAct(getMe(), npc));
            }*/
        } else if (type.equals("npc")) {

            Map<Long, MudObject> allLivings = UserCacheUtil.getAllObjects();
            Living npc = (Living) allLivings.get(Long.valueOf(id));
            String desc = lookLiving(getMe(), npc);

            if(player.getTalkTaskCount() > 0){

                List<PlayerTask> playerTasks = player.getTaskList();

                for(PlayerTask playerTask : playerTasks){

                    TaskTrack taskTrack = UserCacheUtil.getTaskTrackMap().get(playerTask.getTrack().getTrackId());

                    TaskTrackAction taskTrackAction = taskTrack.getTrackActions().get(playerTask.getAction().getActionId()-1);
                    if(!taskTrackAction.getActionType().equals(TaskActionType.TALK)){
                        continue;
                    }

                    if(taskTrackAction.getTargetId().equals(npc.getId())){

                        //Todo:
                        JSONArray retArray = new JSONArray();
                        JSONArray storiesArray = new JSONArray();
                        JSONObject dataObject = new JSONObject();

                        dataObject.put("trackId", taskTrack.getId());
                        dataObject.put("trackActionId", taskTrackAction.getId());
                        dataObject.put("rewardId", taskTrackAction.getRewardId());

                        List<TaskStory> taskStories = XmlUtil.loadStoriesFromXml(taskTrackAction.getStoryId().toString());
                        for(TaskStory story : taskStories){

                            storiesArray.add(JSONObject.fromObject(story));
                        }

                        dataObject.put("stories", storiesArray);
                        retArray.add(UnityCmdUtil.getGameStoryRet(dataObject));
                        sendMsg(retArray);
                        return;
                    }


                }
            }

            if (npc != null) {

                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("desc", desc);
                jsonObject.put("buttons", getAct(getMe(), npc));
                jsonArray.add(UnityCmdUtil.getObjectInfoPopRet(jsonObject));
                sendMsg(jsonArray);
            }
            return;

            /*Map<Long, MudObject> allLivings = UserCacheUtil.getAllObjects();
            Living npc = (Living) allLivings.get(Long.valueOf(id));
            if (npc != null) {

                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("desc", npc.getLookStr());
                jsonObject.put("buttons", getAct(getMe(), npc));
                jsonArray.addOrUpdate(UnityCmdUtil.getObjectInfoPopRet(jsonObject));
                sendMsg(jsonArray);
            }*/
        } else if (type.equals("gates")) {

            JSONObject retMsg = getLookGateStr(id);
            JSONArray retArray = new JSONArray();
            retArray.add(retMsg);
            sendMsg(retArray);
        } else if(type.equals("goods")) {

            JSONObject retMsg = getLookGoodsStr(id);
            JSONArray retArray = new JSONArray();
            retArray.add(retMsg);
            sendMsg(retArray);
        }
    }

    public void openGate(String direction) {

        openOrCloseGate(direction, "打开");
    }

    public void closeGate(String direction) {

        openOrCloseGate(direction, "关闭");
    }

    private String getPlayerButtonStr(String userName, String playerName) {

        return null;
    }

    private JSONObject getLookGateStr(String name) {

        Map<Integer, Living> cacheMap = UserCacheUtil.getPlayers();
        Player player = (Player) cacheMap.get(this.userId);
        String roomName = player.getLocation().getName();

        JSONObject gateObject = new JSONObject();
        JSONArray buttonArray = new JSONArray();

        Map<String, RoomObjects> roomObjectsCache = UserCacheUtil.getRoomObjectsCache();
        RoomObjects roomObjects = roomObjectsCache.get(roomName);
        if (roomObjects == null) {

            return null;
        }

        for (Map.Entry<String, RoomGateEntity> entry : roomObjects.getGates().entrySet()) {

            RoomGateEntity gate = entry.getValue();

            if (name.equals(gate.getName())) {

                //去掉名称的特殊字符
                name = name.replaceAll("【", "");
                name = name.replaceAll("】", "");
                StringBuffer desc = new StringBuffer();
                StringBuffer button = new StringBuffer();

                desc.append("这个" + name + "是");

                JSONObject buttonObject = new JSONObject();

                if (gate.getStatus() == 1) {

                    desc.append("开着的。");
                    buttonObject.put("cmd", "close");
                    buttonObject.put("displayName", "关门");
                    buttonObject.put("objId", entry.getKey());
                } else {

                    desc.append("关着的。");
                    buttonObject.put("cmd", "open");
                    buttonObject.put("displayName", "开门");
                    buttonObject.put("objId", entry.getKey());
                }

                buttonArray.add(buttonObject);

                gateObject.put("desc", desc.toString());
                gateObject.put("buttons", buttonArray);

                return UnityCmdUtil.getObjectInfoPopRet(gateObject);

                //String msg = ZjMudUtil.getHuDongDescLine(desc.toString()) + ZjMudUtil.getHuDongButtonLine(button.toString());
                //return msg;
            }
        }

        return null;
    }

    private JSONObject getLookGoodsStr(String id) {

        Map<Integer, Living> cacheMap = UserCacheUtil.getPlayers();
        Player player = (Player) cacheMap.get(this.userId);
        Map<Long, MudObject> allObjects = UserCacheUtil.getAllObjects();
        GoodsContainer goodsContainer = (GoodsContainer) allObjects.get(Long.valueOf(id));
        if (goodsContainer == null) {

            return null;
        }

        JSONObject buttonObject = new JSONObject();
        JSONArray buttonArray = new JSONArray();
        StringBuffer desc = new StringBuffer();
        StringBuffer button = new StringBuffer();

        desc.append("这是" + goodsContainer.getCount().toString() +
                goodsContainer.getGoodsEntity().getUnit() + goodsContainer.getGoodsEntity().getName());

        if(goodsContainer.getBelongsInfo() == null){

            //如果是环境物品，显示拾取按钮
            if(goodsContainer.getGoodsEntity().getPickable()) {

                buttonArray.add(createButtonItem("get", "拾取", "/goods/goods#" + goodsContainer.getUuid().toString()));
            }
        }else if(goodsContainer.getBelongsInfo().getPlayerId() != player.getPlayerId()){

            //如果是别人的物品只能查看不做任何操作

        }else if(goodsContainer.getBelongsInfo().getPlayerId() == player.getPlayerId()){

            //如果是食物可以吃
            if(goodsContainer.getGoodsEntity().getType() == GoodsType.FOOD.toInteger()){

                buttonArray.add(createButtonItem("eat", "吃" + goodsContainer.getGoodsEntity().getName(),
                        "/goods/goods#" + goodsContainer.getUuid().toString()));
            }


        }

        buttonObject.put("desc", desc.toString());
        buttonObject.put("buttons", buttonArray);

        return UnityCmdUtil.getObjectInfoPopRet(buttonObject);
    }

    private String lookLiving(Living me, Living target) {

        Integer targetShen = target.getShen();
        Integer meSHen = me.getShen();
        Integer age = target.getAge();
        StringBuffer sb = new StringBuffer();

        String pro = (me.getUuid() == target.getUuid()) ? genderSelf(target.getGender()) : genderPronoun(target.getGender());

        if (target instanceof Player) {

            if (((Player) target).getBornFamily().length() < 1) {

                sendMsg(ZjMudUtil.getScreenLine(pro + "还没有投胎，只有一股元神，什么都看不到耶！"));
                return "";
            }

            if (me.getUuid() != target.getUuid())
            {

                List<SocketIOClient> excludeClients = new ArrayList<SocketIOClient>();
                excludeClients.add(((Player)me).getSocketClient());
                excludeClients.add(((Player)target).getSocketClient());
                sendMsg(((Player) target).getSocketClient(), me.getName() + "正盯著你看，不知道在打什么主意。\r\n");
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(me.getName() + "盯着" + target.getName() +
                        "看了一会儿，不知道在打什么主意。\r\n");
                sendMsg( jsonArray,
                        excludeClients, socketIOClient.getNamespace().getRoomOperations(me.getLocation().getName()).getClients());
            }
        }

        sb.append(target.getName() + ZjMudUtil.ZJ_JBR);
        sb.append("一一一一一一一一一一一一一一一一一一一" + ZjMudUtil.ZJ_JBR);
        if(target.getLongDesc() != null && target.getLongDesc().length() > 0){

            sb.append(target.getLongDesc() + ZjMudUtil.ZJ_JBR);
        }

        if(target instanceof  Human){

            // 是人物角色
            if (age >= 25 && target.query("special_skill/youth") != null) { // && obj->query("special_skill/youth"))
                sb.append(pro + "看不出年纪的大小，好像只有二十多岁。" + ZjMudUtil.ZJ_JBR);
            } else {
                if (age >= 200){
                    sb.append(pro + "看起来年纪很大了，难以估计。" + ZjMudUtil.ZJ_JBR);
                } else if (age < 10) {
                    sb.append(pro + "看起来年纪尚幼。" + ZjMudUtil.ZJ_JBR);
                } else {
                    sb.append(pro + "看起来有" + ChineseUtil.chinese_number(age / 10 * 10) +
                            "多岁。" + ZjMudUtil.ZJ_JBR);
                }
            }
            sb.append(pro + "的内力达到了");
            sb.append(gettofNeili(target));
            sb.append("。" + ZjMudUtil.ZJ_JBR);
            sb.append(pro + "的武功看来");
            sb.append(gettof(target));
            sb.append("，");
            sb.append("出手似乎");
            sb.append(getdam(me, target));
            sb.append("。" + ZjMudUtil.ZJ_JBR);
        }

        String desc = description(target);
        if (desc.length() > 0){
            sb.append(pro + desc);
        }

        if(target instanceof Player){

            Player meP = (Player)me;
            Player targetP = (Player)target;

            if (target.getUuid() == me.getCoupleId())
            {
                // 夫妻关系
                if (me.getGender().equals("女性"))
                    sb.append(pro + "就是你的夫君。" + ZjMudUtil.ZJ_JBR);
                else
                    sb.append(pro + "就是你的妻子。" + ZjMudUtil.ZJ_JBR);
            } else
            if (targetP.isBorther(meP))
            {
                // 兄弟
                if (targetP.getGender().equals("女性"))
                {
                    if (targetP.getMudAge() > meP.getMudAge()) {
                        sb.append(pro + "是你的义姐。" + ZjMudUtil.ZJ_JBR);
                    } else {
                        sb.append(pro + "是你的结义妹子。" + ZjMudUtil.ZJ_JBR);
                    }
                } else
                {
                    if (targetP.getMudAge() > meP.getMudAge()){
                        sb.append(pro + "是你的结义兄长。" + ZjMudUtil.ZJ_JBR);
                    } else {
                        sb.append(pro + "是你的义弟。" + ZjMudUtil.ZJ_JBR);
                    }
                }
            } else if (targetP.getUuid() != meP.getUuid() && targetP.query("league/leagueName") != null &&
                    meP.query("league/leagueName") != null && targetP.query("league/leagueName").equals(meP.query("league/leagueName"))) {
                sb.append(pro + "和你均是" + targetP.query("league/leagueName") + "的同盟义士。"  + ZjMudUtil.ZJ_JBR);
            }
        }

        // If we both has family, check if we have any relations.
        if(target instanceof Human){

            Human targetH = (Human)target;
            Human meH = (Human)me;

            PlayerFamilyEntity myFamily = meH.getFamily();
            PlayerFamilyEntity targetFamily = targetH.getFamily();

            if(target.getUuid() != me.getUuid() &&
                    myFamily.getName() != null&& targetFamily.getName() != null &&
                    myFamily.getName().length() > 1 && targetFamily.getName().equals(myFamily.getName()))
            {

                if (targetFamily.getGeneration() == myFamily.getGeneration())
                {

                    if (target.getGender().equals("男性") ||
                            target.getGender().equals("无性")) {
                        sb.append(pro + "是你的"+ (myFamily.getMasterId().equals(targetFamily.getMasterId()) ? "" : "同门") +
                                (myFamily.getEnterTime() > targetFamily.getEnterTime() ? "师兄" : "师弟") + "。" + ZjMudUtil.ZJ_JBR);
                    } else {

                        sb.append(pro + "是你的"+ (myFamily.getMasterId().equals(targetFamily.getMasterId()) ? "" : "同门") +
                                (myFamily.getEnterTime() > targetFamily.getEnterTime() ? "师姐" : "师妹") + "。" + ZjMudUtil.ZJ_JBR);
                    }
                } else if (targetFamily.getGeneration() < myFamily.getGeneration()) {
                    if (myFamily.getMasterId().equals(target.getCmdName())) {
                        sb.append(pro + "是你的师父。" + ZjMudUtil.ZJ_JBR);
                    } else if (myFamily.getGeneration() - targetFamily.getGeneration() > 1) {
                        sb.append(pro + "是你的同门长辈。" + ZjMudUtil.ZJ_JBR);
                    }else if (targetFamily.getEnterTime() < myFamily.getEnterTime()) {
                        sb.append(pro + "是你的师伯。" + ZjMudUtil.ZJ_JBR);
                    }else {
                        sb.append(pro + "是你的师叔。" + ZjMudUtil.ZJ_JBR);
                    }
                } else {
                    if (targetFamily.getGeneration() - myFamily.getGeneration() > 1) {
                        sb.append(pro + "是你的同门晚辈。" + ZjMudUtil.ZJ_JBR);
                    } else if (targetFamily.getMasterId().equals(me.getCmdName())) {
                        sb.append(pro + "是你的弟子。" + ZjMudUtil.ZJ_JBR);
                    } else {
                        sb.append(pro + "是你的师侄。" + ZjMudUtil.ZJ_JBR);
                    }
                }
            }
        }

        //Todo:
        /*if (obj->is_chatter())
        {
            message("vision", str, me);
            return 1;
        }*/


       if (target.queryTemp("eff_status_msg") != null && target.queryTemp("eff_status_msg") .toString().length() > 1) {
            sb.append(target.queryTemp("eff_status_msg") + ZjMudUtil.ZJ_JBR);
        } else if (target.getMaxQi() != null) {

           CombatService combatService = new CombatService();
            sb.append(pro + combatService.getEffStatusMsg( target.getEffQi() * 100 / target.getMaxQi()) + ZjMudUtil.ZJ_JBR);
        }

        //Todo:
        /*if (obj->query_temp("daub/poison/remain") &&
                (me == obj || random((int)me->query_skill("poison", 1)) > 80))
        {
            str += pro + HIG "身上现在" + (me == obj ? "" : "似乎") +
                "淬了" + (me == obj ? obj->query_temp("daub/poison/name") : "毒") +
                NOR "。\n";
        }*/

       /* str += look_equiped(me, obj, pro);
        str = replace_string(str,"\n",ZJBR);
        if(str[(strlen(str)-4)..(strlen(str)-1)]=="$br#")
        str = str[0..(strlen(str)-5)];
        str += "\n";
        str += desinq(me,obj);
        str += getact(me,obj);
        message("vision", str+"\n", me);

        if (obj != me && living(obj) &&
                ! me->is_brother(obj) &&
                        me->query("couple/id") != obj->query("id") &&
                                (me_shen < -1000 && obj_shen > 1000 ||
                                        me_shen > 1000  && obj_shen < -1000))
        {
            if ( obj->query("per")>20)
            {
                if (obj->query("gender") == "女性" )
                    tell_object(me, obj->name() + "脸色一沉，转过头来瞪了你一眼，骂道：“看什么看，没见过美女啊！\n");
                if (obj->query("gender") == "男性"  )
                    tell_object(me, obj->name() + "脸色一沉，转过头来瞪了你一眼，骂道：“看什么看，没见过帅哥啊！\n");
            }
            else
            {
                if ( obj->query("gender") == "女性" )
                    tell_object(me, obj->name() + "低垂着头，害羞地说道：“虽然我长得丑,但这不是我的错呀！呜！\n");
                if ( obj->query("gender") == "男性"  )
                    tell_object(me, obj->name() + "一脸羞愧，骂道：“再看我戳瞎你眼睛！\n");
            }
            if (obj->query("total_hatred") > 4 * obj->query_skill("force") &&
                    ! wizardp(obj) && ! wizardp(me))
                COMBAT_D->auto_fight(obj, me, "berserk");
        }*/
        return sb.toString();
    }

    private void openOrCloseGate(String direction, String action) {

        Map<Integer, Living> cacheMap = UserCacheUtil.getPlayers();
        Player player = (Player) cacheMap.get(this.userId);
        String roomName = player.getLocation().getName();

        Map<String, RoomObjects> roomObjectsCache = UserCacheUtil.getRoomObjectsCache();
        RoomObjects roomObjects = roomObjectsCache.get(roomName);
        if (roomObjects == null) {

            return;
        }

        RoomGateEntity gate = roomObjects.getGates().get(direction);
        //去掉名称的特殊字符
        String name = gate.getName().replaceAll("【", "");
        name = name.replaceAll("】", "");
        String msg = "将" + name + action + "。";

        //向自己发送打开门的讯息
        JSONArray meJsonArray = new JSONArray();
        meJsonArray.add(UnityCmdUtil.getInfoWindowRet("你" + msg));
        sendMsg(meJsonArray);

        //向门连接的两个房间，广播发送的信息
        JSONArray boardCastJsonArray = new JSONArray();
        boardCastJsonArray.add(UnityCmdUtil.getInfoWindowRet(player.getName() + msg));
        Collection<SocketIOClient> cl = socketIOClient.getNamespace().getRoomOperations(gate.getEnterRoom()).getClients();
        socketIOClient.getNamespace().getRoomOperations(player.getLocation().getName()).sendEvent("stream", socketIOClient,
                boardCastJsonArray);
        socketIOClient.getNamespace().getRoomOperations(gate.getExitRoom()).sendEvent("stream", socketIOClient,
                boardCastJsonArray);

        if (action.equals("打开")) {

            gate.setStatus(Byte.valueOf("1"));
        } else {

            gate.setStatus(Byte.valueOf("0"));
        }
    }

    private JSONArray getAct(Living me, Living target) {

        JSONArray jsonArray = new JSONArray();

        StringBuffer sb = new StringBuffer();
        if (!target.getInquirys().isEmpty()) {
            sb.append(ZjMudUtil.getObActs2() + ZjMudUtil.getZjMenuF(2, 3, 9, 28));
        } else {
            sb.append(ZjMudUtil.getObActs2() + ZjMudUtil.getZjMenuF(3, 3, 9, 28));
        }

        if (!target.getVendorGoods().isEmpty()) {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cmd", "list");
            jsonObject.put("objId", target.getUuid().toString());
            jsonObject.put("displayName", "购物");
            jsonArray.add(jsonObject);
            //sb.append("购物:list" + +target.getUuid() + ZjMudUtil.ZJ_SEP);
        }

        if (target.getLocation().getNoFight() != 1) {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cmd", "fight");
            jsonObject.put("objId", target.getUuid().toString());
            jsonObject.put("displayName", "切磋");
            jsonArray.add(jsonObject);
            jsonObject = new JSONObject();
            jsonObject.put("cmd", "kill");
            jsonObject.put("objId", target.getUuid().toString());
            jsonObject.put("displayName", "杀死");
            jsonArray.add(jsonObject);
            //sb.append("切磋:fight " + target.getUuid() + ZjMudUtil.ZJ_SEP);
            //sb.append("杀死:kill " + target.getUuid() + ZjMudUtil.ZJ_SEP);
        }

        JSONObject stealObject = new JSONObject();
        stealObject.put("cmd", "steal");
        stealObject.put("objId", target.getUuid().toString());
        stealObject.put("displayName", "偷窃");
        jsonArray.add(stealObject);
        //sb.append("偷窃:steal " + target.getUuid() + ZjMudUtil.ZJ_SEP);

        if (target instanceof Human) {

            JSONObject giveObject = new JSONObject();
            giveObject.put("cmd", "give");
            giveObject.put("objId", target.getUuid().toString());
            giveObject.put("displayName", "给予");
            jsonArray.add(giveObject);
            //sb.append("给予:give " + target.getUuid() + ZjMudUtil.ZJ_SEP);
        }

        JSONObject followObject = new JSONObject();
        followObject.put("cmd", "follow");
        followObject.put("objId", target.getUuid().toString());
        followObject.put("displayName", "跟随");
        jsonArray.add(followObject);
        //sb.append("跟随:follow " + target.getUuid());

        if (!target.getSkills().isEmpty()) {

            JSONObject skillObject = new JSONObject();
            skillObject.put("cmd", "skills");
            skillObject.put("objId", target.getUuid().toString());
            skillObject.put("displayName", "查看技能");
            jsonArray.add(skillObject);
            //sb.append(ZjMudUtil.ZJ_SEP + "查看技能:skills " + target.getUuid());
        }
        //sb.append("\n");

        return jsonArray;
    }

    private Living getMe() {

        Map<Integer, Living> playerCacheMap = UserCacheUtil.getPlayers();
        Player player = (Player) playerCacheMap.get(this.userId);

        return player;
    }

    private String genderSelf(String sex) {
        if (sex.equals("女性")) {

            return "你";
        } else {
            return "你";
        }
    }

    private String genderPronoun(String sex) {
        if (sex.equals("中性神") || sex.equals("男性") || sex.equals("无性")) {
            return "他";
        } else if (sex.equals("女性")) {
            return "她";
        } else if (sex.equals("雄性") || sex.equals("雌性")) {
            return "它";
        } else {
            return "它";
        }
    }

    private String gettof(Living ob)
    {
        int exp_temp ,i;
        Integer expTemp = ob.getCombatExp();
        for(i=0;i<TOUGH_LEVEL_DESC.length;i++)
        {
            if(expTemp < 500)
            {
                return Ansi.HIR + "不堪一击" + Ansi.NOR;
            }
            if((i+1) == TOUGH_LEVEL_DESC.length)
            {
                return TOUGH_LEVEL_DESC[i];
            }
            if(expTemp >= LOOK_EXP[i] && expTemp < LOOK_EXP[i+1])
            {
                return TOUGH_LEVEL_DESC[i+1];
            }
        }
        return Ansi.HIR + "深不可测" + Ansi.NOR;
    }

    private String getdam(Living me, Living obj)
    {

        Integer damage = obj.queryTemp("apply/damage") == null ? 0 : (Integer)obj.queryTemp("apply/damage");
        Integer jiali = obj.queryTemp("jiali") == null ? 0 : (Integer)obj.queryTemp("jiali");
        Integer level = damage  + jiali;
        level = level / 30;
        if( level >= HEAVY_LEVEL_DESC.length )
            level = HEAVY_LEVEL_DESC.length - 1;
        return HEAVY_LEVEL_DESC[level];
    }

    private String gettofNeili(Living ob)
    {
        Integer neiliTemp= ob.getMaxNeili();
        for(Integer i = 0; i < NEILI_LVL.length; i++)
        {
            if(neiliTemp<400)
            {
                return Ansi.HIR + "出入境" + Ansi.NOR;
            }
            if(( i + 1 ) == NEILI_LVL.length)
            {
                return NEILI_LVL[i];
            }
            if(neiliTemp >= LOOK_NEILI[i] && neiliTemp < LOOK_NEILI[i+1])
            {
                return NEILI_LVL[i+1];
            }

        }
        return Ansi.HIR + "浩渺星云.大圆满" + Ansi.NOR;
    }

    private static final String[] TOUGH_LEVEL_DESC = {
        Ansi.BLU  + "不堪一击" + Ansi.NOR,     // 0    exp: 0--400
        Ansi.BLU + "毫不足虑" + Ansi.NOR,     // 1    exp: 400--800
        Ansi.BLU + "不足挂齿" + Ansi.NOR,     // 2    exp: 800--2000
        Ansi.BLU + "初学乍练" + Ansi.NOR,     // 3    exp: 2000--5000
        Ansi.HIB + "初窥门径" + Ansi.NOR,     // 4    exp: 5000--8000
        Ansi.HIB + "略知一二" + Ansi.NOR,     // 5    exp: 8000--18000
        Ansi.HIB + "普普通通" + Ansi.NOR,     // 6    exp: 18000--35000
        Ansi.HIB + "平平淡淡" + Ansi.NOR,     // 7    exp: 35000--65000
        Ansi.HIB + "平淡无奇" + Ansi.NOR,     // 8    exp: 65000--120000
        Ansi.HIB + "粗通皮毛" + Ansi.NOR,     // 9    exp: 120000--160000
        Ansi.HIB + "半生不熟" + Ansi.NOR,     // 10   exp: 160000--250000
        Ansi.HIB + "马马虎虎" + Ansi.NOR,     // 11   exp: 250000--350000
        Ansi.HIB + "略有小成" + Ansi.NOR,     // 12   exp: 350000--550000
        Ansi.HIB + "已有小成" + Ansi.NOR,     // 13   exp: 550000--750000
        Ansi.HIB + "驾轻就熟" + Ansi.NOR,     // 14   exp: 750000--900000
        Ansi.CYN + "心领神会" + Ansi.NOR,     // 15   exp:  900000--1150000
        Ansi.CYN + "了然於胸" + Ansi.NOR,     // 16   exp: 1150000--1350000
        Ansi.CYN + "略有大成" + Ansi.NOR,     // 17   exp: 1350000--1500000
        Ansi.CYN + "已有大成" + Ansi.NOR,     // 18   exp: 1500000--1700000
        Ansi.CYN + "豁然贯通" + Ansi.NOR,     // 19   exp: 1700000--1900000
        Ansi.CYN + "出类拔萃" + Ansi.NOR,     // 20   exp: 1900000--2150000
        Ansi.CYN + "无可匹敌" + Ansi.NOR,     // 21   exp: 2150000--2400000
        Ansi.CYN + "技冠群雄" + Ansi.NOR,     // 22   exp: 2400000--2550000
        Ansi.CYN + "神乎其技" + Ansi.NOR,     // 23   exp: 2550000--2700000
        Ansi.CYN + "出神入化" + Ansi.NOR,     // 24   exp: 2700000--2850000
        Ansi.CYN + "傲视群雄" + Ansi.NOR,     // 25   exp: 2850000--3000000
        Ansi.HIC + "登峰造极" + Ansi.NOR,     // 26   exp: 3000000--3500000
        Ansi.HIC + "所向披靡" + Ansi.NOR,     // 27   exp: 3500000--4000000
        Ansi.HIC + "一代宗师" + Ansi.NOR,     // 28   exp: 4000000--5250000
        Ansi.HIC + "神功盖世" + Ansi.NOR,     // 29   exp: 5250000--7000000
        Ansi.HIC + "举世无双" + Ansi.NOR,     // 30   exp: 7000000--7500000
        Ansi.HIC + "惊世骇俗" + Ansi.NOR,     // 31   exp: 7500000--8250000
        Ansi.HIC + "震古铄今" + Ansi.NOR,     // 32   exp: 8250000--9000000
        Ansi.HIC + "深藏不露" + Ansi.NOR,     // 33   exp: 9000000--12000000
        Ansi.HIR + "深不可测" + Ansi.NOR      // 34   exp: 12000000--15000000
    };
    private static final Integer[] LOOK_EXP = {
        400,800,2000,5000,8000,18000,35000,65000,100000,150000,220000,330000,500000,6500000,850000,
        1100000,1300000,1500000,1700000,1900000,2150000,2400000,2550000,270000,2850000,
        3000000,4000000,5250000,6500000,7500000,8250000,9000000,12000000,15000000,50000000,
    };
    private static final String[] HEAVY_LEVEL_DESC = {
        "极轻",
        "很轻",
        "不重",
        "不轻",
        "很重",
        "极重"
    };

    private static final String[] NEILI_LVL = {
        Ansi.HIW + "出入境" + Ansi.NOR,
        Ansi.HIW + "造化境" + Ansi.NOR,
        Ansi.HIW + "玄妙境" + Ansi.NOR,
        Ansi.HIW + "生死境" + Ansi.NOR,
        Ansi.HIW + "解脱境" + Ansi.NOR,
        Ansi.HIW + "无为境" + Ansi.NOR,
        Ansi.HIW + "神话境" + Ansi.NOR,
        Ansi.HIW + "无上武念境" + Ansi.NOR,
        Ansi.HIW + "天人合一境" + Ansi.NOR,
        Ansi.HIW + "至尊无上境" + Ansi.NOR,
        Ansi.HIW + "一念通天境.初悟" + Ansi.NOR,
        Ansi.HIW + "一念通天境.进阶" + Ansi.NOR,
        Ansi.HIW + "一念通天境.圆满" + Ansi.NOR,
        Ansi.HIW + "空前绝后境.初悟" + Ansi.NOR,
        Ansi.HIW + "空前绝后境.进阶" + Ansi.NOR,
        Ansi.HIW + "空前绝后境.圆满" + Ansi.NOR,
        Ansi.HIW + "泰山北斗境.初悟" + Ansi.NOR,
        Ansi.HIW + "泰山北斗境.进阶" + Ansi.NOR,
        Ansi.HIW + "泰山北斗境.圆满" + Ansi.NOR,
        Ansi.HIW + "斗转乾坤境.初悟" + Ansi.NOR,
        Ansi.HIW + "斗转乾坤境.进阶" + Ansi.NOR,
        Ansi.HIW + "斗转乾坤境.圆满" + Ansi.NOR,
        Ansi.HIW + "万法归一境.初悟" + Ansi.NOR,
        Ansi.HIW + "万法归一境.进阶" + Ansi.NOR,
        Ansi.HIW + "万法归一境.圆满" + Ansi.NOR,
        Ansi.HIW + "神念广天境.初悟" + Ansi.NOR,
        Ansi.HIW + "神念广天境.进阶" + Ansi.NOR,
        Ansi.HIW + "神念广天境.圆满" + Ansi.NOR,
        Ansi.HIC + "浩渺星云境.初悟" + Ansi.NOR,
        Ansi.HIC + "浩渺星云境.进阶" + Ansi.NOR,
        Ansi.HIC + "浩渺星云境.圆满" + Ansi.NOR
    };

    private static final Integer[] LOOK_NEILI={
        400,700,1000,1400,1800,2200,2600,3400,4200,4800,5400,6000,6600,7200,8000,
        8500,9000,9500,10000,10500,11000,11500,12000,12500,13000,
        14000,15000,16000,17000,18000,50000,
    };

    private String description(MudObject obj)
    {
        if (obj instanceof  Player)
        {
            Player player = (Player)obj;
            Integer per = player.getPer();
            Integer age = player.getAge();
            if (player.query("special_skill/youth") != null) age = 14;
            if (player.getGender().equals("男性") || player.getGender().equals("无性"))
            {
                if (per >=80)
                    return Ansi.HIG + "现在一派神人气度，仙风道骨，举止出尘。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 50 && (per > 40))
                    return Ansi.HIG + "现在神清气爽，骨格清奇，宛若仙人。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 40 && (per > 37))
                    return Ansi.HIG + "现在丰神俊朗，长身玉立，宛如玉树临风。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 37 && (per > 36))
                    return Ansi.HIG + "现在飘逸出尘，潇洒绝伦。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 36 && (per > 35))
                    return Ansi.HIG + "现在面如美玉，粉妆玉琢，俊美不凡。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 35 && (per > 34))
                    return Ansi.HIG + "现在丰神如玉，目似朗星，令人过目难忘。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 34 && (per > 33))
                    return Ansi.HIY + "现在粉面朱唇，身姿俊俏，举止风流无限。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 33 && (per > 32))
                    return Ansi.HIY + "现在双目如星，眉梢传情，所见者无不为之心动。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 32 && (per > 31))
                    return Ansi.HIY + "现在举动如行云游水，独蕴风情，吸引所有异性目光。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 31 && (per > 30))
                    return Ansi.HIY + "现在双眼光华莹润，透出摄人心魄的光芒。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 30 && (per > 29))
                    return Ansi.HIY + "生得英俊潇洒，风流倜傥。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 29 && (per > 28))
                    return Ansi.MAG + "生得目似点漆，高大挺俊，令人心动。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 28 && (per > 27))
                    return Ansi.MAG + "生得面若秋月，儒雅斯文，举止适度。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 27 && (per > 26))
                    return Ansi.MAG + "生得剑眉星目，英姿勃勃，仪表不凡。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 26 && (per > 25))
                    return Ansi.MAG + "生得满面浓髯，环眼豹鼻，威风凛凛，让人倾倒。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 25 && (per > 24))
                    return Ansi.MAG + "生得眉如双剑，眼如明星，英挺出众。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 24 && (per > 23))
                    return Ansi.CYN + "生得虎背熊腰，壮健有力，英姿勃发。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 23 && (per > 22))
                    return Ansi.CYN + "生得肤色白皙，红唇墨发，斯文有礼。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 22 && (per > 21))
                    return Ansi.CYN + "生得浓眉大眼，高大挺拔，气宇轩昂。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 21 && (per > 20))
                    return Ansi.CYN + "生得鼻直口方，线条分明，显出刚毅性格。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 20 && (per > 19))
                    return Ansi.CYN + "生得眉目清秀，端正大方，一表人才。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 19 && (per > 18))
                    return Ansi.YEL + "生得腰圆背厚，面阔口方，骨格不凡。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 18 && (per > 17))
                    return Ansi.YEL + "生得相貌平平，不会给人留下什么印象。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 17 && (per > 16))
                    return Ansi.YEL + "生得膀大腰圆，满脸横肉，恶形恶相。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 16 && (per > 15))
                    return Ansi.YEL + "生得獐头鼠须，让人一看就不生好感。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 15 && (per > 14))
                    return Ansi.YEL + "生得面颊深陷，瘦如枯骨，让人要发恶梦。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 14 && (per > 13))
                    return Ansi.RED + "生得肥头大耳，腹圆如鼓，手脚短粗，令人发笑。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 13 && (per > 12))
                    return Ansi.RED + "生得贼眉鼠眼，身高三尺，宛若猴状。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 12 && (per > 11))
                    return Ansi.RED + "生得面如桔皮，头肿如猪，让人不想再看第二眼。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 11 && (per > 10))
                    return Ansi.RED + "生得呲牙咧嘴，黑如锅底，奇丑无比。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 10)
                    return Ansi.RED + "生得眉歪眼斜，瘌头癣脚，不象人样。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                return "长得有点对不住别人。";
            }
            else
            {
                if (per >=40)
                    return Ansi.HIW + "现在宛如玉雕冰塑，似梦似幻，已不再是凡间人物。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 39 && (per > 38))
                    return Ansi.HIG + "现在美若天仙，不沾一丝烟尘。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 38 && (per > 37))
                    return  Ansi.HIG + "现在灿若明霞，宝润如玉，恍如神妃仙子。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 37 && (per > 36))
                    return Ansi.HIG + "现在气质美如兰，才华馥比山，令人见之忘俗。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 36 && (per > 35))
                    return Ansi.HIG + "现在丰润嫩白，婴桃小口，眉目含情，仿佛太真重临。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 35 && (per > 34))
                    return Ansi.HIG + "现在鲜艳妩媚，袅娜风流，柔媚姣俏，粉光脂艳。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 34 && (per > 33))
                    return Ansi.HIY + "现在鬓若刀裁，眉如墨画，面如桃瓣，目若秋波。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 33 && (per > 32))
                    return Ansi.HIY + "现在凤眼柳眉，粉面含春，丹唇贝齿，转盼多情。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 32 && (per > 31))
                    return Ansi.HIY + "现在眉目如画，肌肤胜雪，真可谓闭月羞花。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 31 && (per > 30))
                    return Ansi.HIY + "现在娇若春花，媚如秋月，真的能沉鱼落雁。。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 30 && (per > 29))
                    return Ansi.HIY + "生得闲静如姣花照水，行动似弱柳扶风，体态万千。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 29 && (per > 28))
                    return Ansi.MAG + "生得娇小玲珑，宛如飞燕再世，楚楚动人。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 28 && (per > 27))
                    return Ansi.MAG + "生得鸭蛋秀脸，俊眼修眉，黑发如瀑，风情万种。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 27 && (per > 26))
                    return Ansi.MAG + "生得削肩细腰，身材苗条，娇媚动人，顾盼神飞。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 26 && (per > 25))
                    return Ansi.MAG + "生得丰胸细腰，妖娆多姿，让人一看就心跳不已。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 25 && (per > 24))
                    return Ansi.MAG + "生得粉嫩白至，如芍药笼烟，雾里看花。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 24 && (per > 23))
                    return Ansi.CYN + "生得腮凝新荔，目若秋水，千娇百媚。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 23 && (per > 22))
                    return Ansi.CYN + "生得鲜艳妍媚，肌肤莹透，引人遐思。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 22 && (per > 21))
                    return Ansi.CYN + "生得巧笑嫣然，宛约可人。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 21 && (per > 20))
                    return Ansi.CYN + "生得如梨花带露，清新秀丽。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 20 && (per > 19))
                    return Ansi.CYN + "生得风姿楚楚，明丽动人。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 19 && (per > 18))
                    return Ansi.YEL + "生得肌肤微丰，雅淡温宛，清新可人。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 18 && (per > 17))
                    return Ansi.YEL + "生得虽不标致，倒也白净，有些动人之处。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 17 && (per > 16))
                    return Ansi.YEL + "生得身材瘦小，肌肤无光，两眼无神。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 16 && (per > 15))
                    return Ansi.YEL + "生得干黄枯瘦，脸色腊黄，毫无女人味。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 15 && (per > 14))
                    return Ansi.YEL + "生得满脸疙瘩，皮色粗黑，丑陋不堪。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 14 && (per > 13))
                    return Ansi.RED + "生得一嘴大暴牙，让人一看就没好感。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 13 && (per > 12))
                    return Ansi.RED + "生得眼小如豆，眉毛稀疏，手如猴爪，不成人样。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 12 && (per > 11))
                    return Ansi.RED + "生得八字眉，三角眼，鸡皮黄发，让人一见就想吐。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                if (per <= 11 && (per > 10))
                    return Ansi.RED + "生得歪鼻斜眼，脸色灰败，直如鬼怪一般。" + ZjMudUtil.ZJ_JBR + Ansi.NOR;
                return "长得和无盐有点相似耶。" + ZjMudUtil.ZJ_JBR;

            }
        } else
        if (!(obj instanceof Human))
        {
            if (obj.queryTemp("owner") == null){
                return "是一只未被驯服的畜生，眼光里满是戒心和敌意。" + ZjMudUtil.ZJ_JBR;
            } else {
                return "是一只被" + obj.queryTemp("owner_name") +
                        "驯服的畜生，一副很温驯的样子。" + ZjMudUtil.ZJ_JBR;
            }
        }
        return "";
    }

    private JSONObject createButtonItem(String cmd, String name, String objId){

        JSONObject buttonItemObject = new JSONObject();
        buttonItemObject.put("cmd", cmd);
        buttonItemObject.put("displayName", name);
        buttonItemObject.put("objId", objId);
        return buttonItemObject;
    }

    /*String lookEquiped(object me, object obj, String pro)
    {
        mixed *inv;
        String str;
        String subs;
        object hob;
        int i;
        int n;

        inv = all_inventory(obj);
        n = 0;

        str = "";
        subs = "";
        for (i = 0; i < sizeof(inv); i++)
        {
            switch (inv[i]->query("equipped"))
            {
                case "wielded":
                    n++;
                    subs = HIC "㊣" NOR + inv[i]->short() + "\n" + subs;
                    break;

                case "worn":
                    n++;
                    subs += HIC "㊣" NOR + inv[i]->short() + "\n";
                    break;

                default:
                    break;
            }
        }

        //if (n)
        //	str += pro + "装备着：\n" + subs;

        if (objectp(hob = obj->query_temp("handing")) &&
                (me == obj || obj->query_weight() > 200))
        {
            int mad;

            // dress nothing but handing a cloth !
            mad = (! objectp(obj->query_temp("armor/cloth")) &&
                    hob->query("armor_type") == "cloth");

            str = pro + "手中" + (mad ? "却" : "" ) + "握着一" +
                    (hob->query_amount() ? hob->query("base_unit")
                            : hob->query("unit")) +
                    hob->name() +
                            (mad ? "，疯了，一定是疯了！\n" : "。\n") + str;
        }*/
/*
	if (playerp(obj) &&! objectp(obj->query_temp("armor/cloth")))
	{
		str = pro + "身上没有穿衣服啊！\n" + str;
	}
*/
    /*    return str;
    }*/


}
