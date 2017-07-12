package com.tian.server.model;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.common.Living;
import com.tian.server.common.Race;
import com.tian.server.entity.*;
import com.tian.server.model.Race.Human;

import java.util.*;

/**
 * Created by PPX on 2017/6/14.
 */
public class PlayerCache implements Living {

    //用户的socket client
    private SocketIOClient socketClient;

    //用户信息
    private UserEntity user;

    //玩家信息
    private PlayerEntity player;

    //玩家辅助信息
    private PlayerInfoEntity playerInfo;

    //玩家当前位置
    private RoomEntity room;

    //玩家当前观察的物品id
    private String lookId;

    private List<Living> enemy;

    private Race race;

    private Map<String, Integer> skills; //存放的是 技能名：等级
    private Map<String, Integer> learned; //存放的是玩家已经学习过的技能 技能名：等级
    private Map<String, String> skillMap; //存放的连招 技能名：技能名
    private Map<String, String> skillPrepare; //为基本武功设置激发武功 基本技能名字：技能名 如： prepare_skill("strike", "dragon-strike");

    Map<String, Integer> limb_damage;

    String[] danger_limbs = new String[]{"头部", "颈部", "胸口", "后心", "小腹",};

    public PlayerCache() {

        race = new Human();
    }

    public void setSocketClient(SocketIOClient socketClient) {

        this.socketClient = socketClient;
    }

    public SocketIOClient getSocketClient() {

        return this.socketClient;
    }

    public void setUser(UserEntity user) {

        this.user = user;
    }

    public UserEntity getUser() {

        return this.user;
    }

    public void setPlayer(PlayerEntity player) {

        this.player = player;
    }

    public PlayerEntity getPlayer() {

        return this.player;
    }

    public void setPlayerInfo(PlayerInfoEntity playerInfo) {

        this.playerInfo = playerInfo;
    }

    public PlayerInfoEntity getPlayerInfo() {

        return this.playerInfo;
    }

    public void setRoom(RoomEntity room) {

        this.room = room;
    }

    public RoomEntity getRoom() {

        return this.room;
    }

    public void setLookId(String lookId) {

        this.lookId = lookId;
    }

    public String getLookId() {

        return this.lookId;
    }

    public List<Living> getEnemy() {

        if (this.enemy == null) {

            this.enemy = new ArrayList<Living>();
        }
        return this.enemy;
    }

    public Map<String, Integer> getSkills(){

        if(this.skills == null){

            this.skills = new HashMap<String, Integer>();
        }
        return this.skills;
    }

    public Map<String, Integer> getLearned(){

        if(this.learned == null){

            this.learned = new HashMap<String, Integer>();
        }
        return this.learned;
    }

    public Map<String, String> getSkillMap(){

        if(this.skillMap == null){

            this.skillMap = new HashMap<String, String>();
        }
        return this.skillMap;
    }

    public Map<String, String> getSkillPrepare(){

        if(this.skillPrepare == null){

            this.skillPrepare = new HashMap<String, String>();
        }
        return this.skillPrepare;
    }

    public void initSkills(List<PlayerSkillEntity> playerSkillEntityList){

        Map<String, Integer> refSkill = getSkills();
        Map<String, Integer> refLearned = getLearned();
        for(PlayerSkillEntity playerSkillEntity : playerSkillEntityList){

            refSkill.put(playerSkillEntity.getSkillName(), playerSkillEntity.getLevel());
            refLearned.put(playerSkillEntity.getSkillName(), playerSkillEntity.getLevel());
        }

    }

    public void initLimbDamage() {

        this.limb_damage = new HashMap<String, Integer>();
        // 人类身体部位
        this.limb_damage.put("头部", 100);
        this.limb_damage.put("颈部", 90);
        this.limb_damage.put("胸口", 90);
        this.limb_damage.put("后心", 80);
        this.limb_damage.put("左肩", 50);
        this.limb_damage.put("右肩", 55);
        this.limb_damage.put("左臂", 40);
        this.limb_damage.put("右臂", 45);
        this.limb_damage.put("左手", 20);
        this.limb_damage.put("右手", 30);
        this.limb_damage.put("腰间", 60);
        this.limb_damage.put("小腹", 70);
        this.limb_damage.put("左腿", 40);
        this.limb_damage.put("右腿", 50);
        this.limb_damage.put("左脚", 35);
        this.limb_damage.put("右脚", 40);
    }

    public void addEnemy(Living enemy){

        if(this.enemy == null){

            this.enemy = new ArrayList<Living>();
        }

        this.enemy.add(enemy);
    }

    public void heartBeat() {

        if(enemy == null){

            return;
        }

        if (enemy.size() > 0) {


            StringBuffer sb = new StringBuffer();
            String result;

            //获取准备技能 prepare技能

            //没有技能把 attack_skill 设置为 unarmed
            String attack_skill = "unarmed";
            //随机选择攻击身体的部位

            SkillAction skillAction = race.queryAction();

            sb.append(skillAction.getAction());


            //计算ap

            int level = 3; //unarmed等级

            int power = level * level * level / 100;

            power = power / 30 * 20;

            power += 5000 / 1000;


            int ap = power;
            if (ap < 1) {
                ap = 1;
            }

            int dp = 2 * 2 * 2 / 100; //dodge
            dp = dp / 30 * 15;
            dp += 5000 / 1000;
            if (dp < 1) {
                dp = 1;
            }


            Random r = new Random();
            int randomAp = r.nextInt(ap + dp);

            if (randomAp >= dp) {

                int pp = 1 * 1 * 1 / 100; //parry等级
                pp = pp / 30 * 15;
                pp += 5000 / 1000;


            }

            String limb = "右肩";

            int damage = 3 / 2;
            int damageBonus = 20; //臂力

            damage += (damageBonus + r.nextInt(damageBonus)) / 2;

            initLimbDamage();
            damage += (damage * limb_damage.get(limb)) / 100;

            if (damage > 0) {

                int wounded = damage;

                damage = damage * 20 / 100;
                sb.append(getDamageMsg(damage, skillAction.getDamageType()));

                result = sb.toString();

                result = result.replace("$l", limb);
                result = result.replace("$w", "拳头");
                result = result.replace("$N", player.getName());
                result = result.replace("$n", ((PlayerCache) enemy.get(0)).getPlayer().getName());

                System.out.println(result);
                System.out.println(player.getName() + "对" + ((PlayerCache) enemy.get(0)).getPlayer().getName() + "造成" + damage + "点伤害");
            }
        }

    }


    private String getDamageMsg(int damage, String type) {
        String str;

        if (damage == 0)
            return "结果没有造成任何伤害。\n";

        if(type.equals("擦伤") || type.equals("拉伤") || type.equals("割伤")){

            if (damage < 100) return "结果只是轻轻地划破$p的皮肉。\n";
            else if (damage < 200) return "结果在$p$l划出一道细长的血痕。\n";
            else if (damage < 400) return "结果「嗤」地一声，$w已在$p$l划出一道伤口！\n";
            else if (damage < 600) return "结果「嗤」地一声，$w已在$p$l划出一道血淋淋的伤口！\n";
            else if (damage < 1000) return "结果「嗤」地一声，$w已在$p$l划出一道又长又深的伤口，溅得$N满脸鲜血！\n";
            else
                return "结果只听见$n一声惨嚎，$w已在$p$l划出一道深及见骨的可怕伤口！\n";
        }else if(type.equals("刺伤")){

            if (damage < 100) return "结果只是轻轻地刺破$p的皮肉。\n";
            else if (damage < 200) return "结果在$p$l刺出一个创口。\n";
            else if (damage < 400) return "结果「噗」地一声，$w已刺入了$n$l寸许！\n";
            else if (damage < 800) return "结果「噗」地一声，$w已刺进$n的$l，使$p不由自主地退了几步！\n";
            else if (damage < 1000) return "结果「噗嗤」地一声，$w已在$p$l刺出一个血肉□糊的血窟窿！\n";
            else
                return "结果只听见$n一声惨嚎，$w已在$p的$l对穿而出，鲜血溅得满地！\n";
        }else if(type.equals("踢伤")){

            if (damage < 50) return "结果只是轻轻地踢到，比拍苍蝇稍微重了点。\n";
            else if (damage < 100) return "结果在$n的$l造成一处瘀青。\n";
            else if (damage < 200) return "结果一踢命中，$n的$l登时肿了一块老高！\n";
            else if (damage < 400) return "结果一踢命中，$n闷哼了一声显然吃了不小的亏！\n";
            else if (damage < 600) return "结果「砰」地一声，$n退了两步！\n";
            else if (damage < 800) return "结果这一下「砰」地一声踢得$n连退了好几步，差一点摔倒！\n";
            else if (damage < 1000) return "结果重重地踢中，$n「哇」地一声吐出一口鲜血！\n";
            else
                return "结果只听见「砰」地一声巨响，$n像一捆稻草般飞了出去！！\n";
        }else if(type.equals("瘀伤") || type.equals("震伤")){

            if (damage < 50) return "结果只是轻轻地碰到，比拍苍蝇稍微重了点。\n";
            else if (damage < 100) return "结果在$p的$l造成一处瘀青。\n";
            else if (damage < 200) return "结果一击命中，$n的$l登时肿了一块老高！\n";
            else if (damage < 400) return "结果一击命中，$n闷哼了一声显然吃了不小的亏！\n";
            else if (damage < 600) return "结果「砰」地一声，$n退了两步！\n";
            else if (damage < 800) return "结果这一下「砰」地一声打得$n连退了好几步，差一点摔倒！\n";
            else if (damage < 1000) return "结果重重地击中，$n「哇」地一声吐出一口鲜血！\n";
            else
                return "结果只听见「砰」地一声巨响，$n像一捆稻草般飞了出去！\n";
        }else if(type.equals("抓伤")) {

            if (damage < 50) return "结果只是轻轻地抓到，比抓痒稍微重了点。\n";
            else if (damage < 100) return "结果在$p的$l抓出几条血痕。\n";
            else if (damage < 200) return "结果一抓命中，$n的$l被抓得鲜血飞溅！\n";
            else if (damage < 400) return "结果「嗤」地一声，$l被抓得深可见骨！！\n";
            else if (damage < 600) return "结果一抓命中，$n的$l被抓得血肉横飞！！！\n";
            else if (damage < 800) return "结果这一下「嗤」地一声抓得$n连晃好几下，差一点摔倒！\n";
            else if (damage < 1000) return "结果$n哀号一声，$l被抓得筋断骨折！！\n";
            else
                return "结果只听见$n一声惨嚎，$l被抓出五个血窟窿！鲜血溅得满地！！\n";
        }else if(type.equals("内伤")) {

            if (damage < 50) return "结果只是把$n打得退了半步，毫发无损。\n";
            else if (damage < 100) return "结果$n痛哼一声，在$p的$l造成一处瘀伤。\n";
            else if (damage < 200) return "结果一击命中，把$n打得痛得弯下腰去！\n";
            else if (damage < 400) return "结果$n闷哼了一声，脸上一阵青一阵白，显然受了点内伤！\n";
            else if (damage < 600) return "结果$n脸色一下变得惨白，昏昏沉沉接连退了好几步！\n";
            else if (damage < 800) return "结果重重地击中，$n「哇」地一声吐出一口鲜血！\n";
            else if (damage < 1000) return "结果「轰」地一声，$n全身气血倒流，口中鲜血狂喷而出！\n";
            else
                return "结果只听见几声喀喀轻响，$n一声惨叫，像滩软泥般塌了下去！\n";
        }else if(type.equals("点穴")) {

            if (damage < 60) return "结果只是轻轻的碰到$n的$l，根本没有点到穴道。\n";
            else if (damage < 100) return "结果$n痛哼一声，在$p的$l造成一处淤青。\n";
            else if (damage < 200) return "结果一击命中，$N点中了$n$l上的穴道，$n只觉一阵麻木！\n";
            else if (damage < 400) return "结果$n闷哼了一声，脸上一阵青一阵白，登时觉得$l麻木！\n";
            else if (damage < 800) return "结果$n脸色一下变得惨白，被$N点中$l的穴道,一阵疼痛遍布整个$l！\n";
            else if (damage < 1000) return "结果$n一声大叫，$l的穴道被点中,疼痛直入心肺！\n";
            else
                return "结果只听见$n一声惨叫，一阵剧痛夹杂着麻痒游遍全身，跟着直挺挺的倒了下去！\n";
        }else if(type.equals("抽伤") || type.equals("鞭伤")) {

            if (damage < 50) return "结果只是在$n的皮肉上碰了碰，好象只蹭破点皮。\n";
            else if (damage < 100) return "结果在$n$l抽出一道轻微的紫痕。\n";
            else if (damage < 200) return "结果「啪」地一声在$n$l抽出一道长长的血痕！\n";
            else if (damage < 400) return "结果只听「啪」地一声，$n连晃好几下，差一点摔倒！\n";
            else if (damage < 600) return "结果$p的$l上被抽了一道血淋淋的创口！\n";
            else if (damage < 800) return "结果只听「啪」地一声，$n的$l被抽得皮开肉绽，痛得$p咬牙切齿！\n";
            else if (damage < 1000) return "结果「啪」地一声爆响！这一下好厉害，只抽得$n皮开肉绽，血花飞溅！\n";
            else
                return "结果只听见$n一声惨嚎，$w重重地抽上了$p的$l，$n顿时血肉横飞，十命断了九条！\n";
        }else if(type.equals("反震伤") || type.equals("反弹伤")) {

            if (damage < 50) return "结果$N受到$n的内力反震，闷哼一声。\n";
            else if (damage < 100) return "结果$N被$n的反震得气血翻腾，大惊失色。\n";
            else if (damage < 200) return "结果$N被$n的反震得站立不稳，摇摇晃晃。\n";
            else if (damage < 400) return "结果$N被$n以内力反震，「嘿」地一声退了两步。\n";
            else if (damage < 600) return "结果$N被$n的震得反弹回来的力量震得半身发麻。\n";
            else if (damage < 800) return "结果$N被$n的内力反震，胸口有如受到一记重击，连退了五六步！\n";
            else if (damage < 1000) return "结果$N被$n内力反震，眼前一黑，身子向後飞出丈许！\n";
            else
                return "结果$N被$n内力反震，眼前一黑，狂吐鲜血，身子象断了线的风筝向後飞去！\n";
        }else if(type.equals("砸伤") || type.equals("挫伤")) {

            if (damage < 50) return "结果只是轻轻地碰到，像是给$n搔了一下痒。\n";
            else if (damage < 100) return "结果在$n的$l砸出一个小臌包。\n";
            else if (damage < 200) return "结果$N这一下砸个正着，$n的$l登时肿了一块老高！\n";
            else if (damage < 400) return "结果$N这一下砸个正着，$n闷哼一声显然吃了不小的亏！\n";
            else if (damage < 600) return "结果只听「砰」地一声，$n疼得连腰都弯了下来！\n";
            else if (damage < 800) return "结果这一下「轰」地一声砸得$n眼冒金星，差一点摔倒！\n";
            else if (damage < 1000) return "结果重重地砸中，$n眼前一黑，「哇」地一声吐出一口鲜血！\n";
            else
                return "结果只听见「轰」地一声巨响，$n被砸得血肉模糊，惨不忍睹！\n";
        }else if(type.equals("咬伤")) {

            if (damage < 100) return "结果只是轻轻地蹭了一下$p的皮肉。\n";
            else if (damage < 200) return "结果在$p$l咬出一排牙印。\n";
            else if (damage < 400) return "结果$p被咬下一片肉来！\n";
            else if (damage < 800) return "结果$p连皮带肉被咬下一大块！\n";
            else if (damage < 1000) return "结果在$p的身上咬下来血肉模糊的一大块$l肉！\n";
            else
                return "结果只听见$n一声惨嚎，$p的$l上的肉被一口咬掉，露出骨头！！\n";
        }else if(type.equals("灼伤") || type.equals("烧伤")) {

            if (damage < 100) return "结果只是把$p的$l烫了一下。\n";
            else if (damage < 200) return "结果$p的$l被$w灼炙得起了个小疱。\n";
            else if (damage < 400) return "结果$p的$l被$w烧得红肿疼痛，痛得不住冒汗咬牙！\n";
            else if (damage < 800) return "结果「嗤」一声轻响，$p祗觉$l上一片热痛，已被$w烧成了暗红色，犹如焦炭！\n";
            else if (damage < 1000) return "结果「嗤嗤」一阵声响过去，$n神色痛楚难当，$l被$w烧得一片焦黑，青烟直冒！\n";
            else
                return "结果只听见$n长声惨呼，在地上不住打滚，$l已被$w烧得皮肉尽烂，焦臭四溢！！\n";
        }else if(type.equals("冻伤")) {

            if (damage < 100) return "结果$p祗是觉得$l有些冷飕飕地，还挺凉快的。\n";
            else if (damage < 200) return "结果$p的$l被冻得有些麻木。\n";
            else if (damage < 400) return "结果$p机伶伶地打了个寒颤！\n";
            else if (damage < 800) return "结果$p脸色一变，神情有些僵硬，身子也冷得瑟瑟发抖！\n";
            else if (damage < 1000) return "结果$p身子打颤，脸色苍白，嘴唇冻得发紫，牙关格格直响！\n";
            else
                return "结果祗听见$n声音一哑，脸上惨白得没半分血色，颤抖不休，浑身血液似乎都结成了冰！！\n";

        }else {

            if (type.length() <= 0) type = "伤害";
            if (damage < 50) str = "结果只是勉强造成一处轻微";
            else if (damage < 100) str = "结果造成轻微的";
            else if (damage < 200) str = "结果造成一处";
            else if (damage < 400) str = "造成一处严重";
            else if (damage < 500) str = "结果造成颇为严重的";
            else if (damage < 600) str = "结果造成相当严重的";
            else if (damage < 800) str = "结果造成十分严重的";
            else if (damage < 1000) str = "结果造成极其严重的";
            else
                str = "结果造成非常可怕的严重";
            return str + type + "！\n";
        }

    }
}
