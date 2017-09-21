package com.tian.server.model;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.entity.*;
import com.tian.server.model.Race.Human;
import com.tian.server.util.CombatUtil;
import javassist.bytecode.stackmap.TypeData;

import java.util.*;

/**
 * Created by PPX on 2017/6/14.
 */
public class Player extends Human {

    //用户的socket client
    private SocketIOClient socketClient;
    private Integer playerId = 0;
    private String character = "";
    private Integer maxFood = 0;
    private Integer food = 0;
    private Integer maxWater = 0;
    private Integer water = 0;
    private Long money = 0L;
    private Integer ticket = 0;

    //最大负重和最小负重
    private Integer encumbrance = 0;

    //用户信息
    private UserEntity user;

    //玩家辅助信息
    private PlayerInfoEntity playerInfo;

    //玩家当前任务信息列表
    private List<PlayerTask> taskList = new ArrayList<PlayerTask>();
    private Integer talkTaskCount = 0;
    private Integer killTaskCount = 0;


    //玩家当前观察的物品id
    private String lookId;

    public Player() {

    }

    @Override
    public String getLookStr(){

        StringBuffer sb = new StringBuffer();
        return "";
    }

    public void initInfo(PlayerEntity player){

        int playerId = player.getId();
        String name = player.getName();
        String cmdName = player.getCmdName();
        Long uuid  = player.getUuid();
        String title = player.getTitle() == null ? "" : player.getTitle();
        String nickname = player.getNickname() == null ? "" : player.getNickname();
        Integer mudAge = player.getMudAge() == null ? 0 : player.getMudAge();
        Integer ageModify = player.getAgeModify() == null ? 0 : player.getAgeModify();
        String gender = player.getSex() == null ? "" : player.getSex();
        String character = player.getCharacter() == null ? "" : player.getCharacter();
        Integer level = player.getLevel() == null ? 0 : player.getLevel();
        Byte shenType = player.getShenType() == null ? (byte)0 : player.getShenType();
        Integer combatExp = player.getCombatExp() == null ? 0 : player.getCombatExp();
        Long coupleId = player.getCoupleId() == null ? 0L : player.getCoupleId();
        Integer str = player.getStr() == null ? 0 : player.getStr();
        Integer wux = player.getWux() == null ? 0 : player.getWux();
        Integer con = player.getCon() == null ? 0 : player.getCon();
        Integer dex = player.getDex() == null ? 0 : player.getDex();
        Integer kar = player.getKar() == null ? 0 : player.getKar();
        Integer per = player.getPer() == null ? 0 : player.getPer();
        Integer maxQi = player.getMaxQi() == null ? 0 : player.getMaxQi();
        Integer effQi = player.getEffQi() == null ? 0 : player.getEffQi();
        Integer qi = player.getQi() == null ? 0 : player.getQi();
        Integer maxNeili = player.getMaxNeili() == null ? 0 : player.getMaxNeili();
        Integer effNeili = player.getEffNeili() == null ? 0 : player.getEffNeili();
        Integer neili = player.getNeili() == null ? 0 : player.getNeili();
        Integer maxJing = player.getMaxJing() == null ? 0 : player.getMaxJing();
        Integer effJing = player.getEffJing() == null ? 0 : player.getEffJing();
        Integer jing = player.getJing() == null ? 0 : player.getJing();
        Integer maxJingLi = player.getMaxJingLi() == null ? 0 : player.getMaxJingLi();
        Integer effJingLi = player.getEffJingLi() == null ? 0 : player.getEffJingLi();
        Integer jingLi = player.getJingLi() == null ? 0 : player.getJingLi();
        Integer food = player.getFood() == null ? 0 : player.getFood();
        Integer water = player.getWater() == null ? 0 : player.getWater();
        Byte status = player.getStatus() == null ? (byte)0 : player.getStatus();
        String bornFamily = player.getBornFamily() == null ? "" : player.getBornFamily();
        String surname = player.getSurname() == null ? "" : player.getSurname();
        String familyName = player.getFamilyName() == null ? "" : player.getFamilyName();
        String bunchName = player.getBunchName() == null ? "" : player.getBunchName();
        Long money = player.getMoney() == null ? 0L : player.getMoney();
        Integer ticket = player.getTicket() == null ? 0 : player.getTicket();

        setPlayerId(playerId);
        setCmdName(cmdName);
        setUuid(uuid);
        setTitle(title);
        setNickname(nickname);
        setName(name);
        setMudAge(mudAge);
        setAgeModify(ageModify);
        setAge(calcAge(ageModify, mudAge));
        setGender(gender);
        setCharacter(character);
        setLevel(level);
        setShenType(shenType);
        setShen(calcShen(shenType, combatExp));
        setCoupleId(coupleId);
        setStr(str);
        setWux(wux);
        setCon(con);
        setDex(dex);
        setKar(kar);
        setPer(per);
        setMaxQi(maxQi);
        setEffQi(effQi);
        setQi(qi);
        setMaxNeili(maxNeili);
        setEffNeili(effNeili);
        setNeili(neili);
        setMaxJing(maxJing);
        setEffJing(effJing);
        setJing(jing);
        setMaxJingLi(maxJingLi);
        setEffJingLi(effJingLi);
        setJingLi(jingLi);
        setCombatExp(combatExp);
        setFood(food);
        setWater(water);
        setMaxFood(getMaxFoodCapacity());
        setMaxWater(getMaxWaterCapacity());
        setStatus(status);
        setBornFamily(bornFamily);
        setSurname(surname);
        setFamilyName(familyName);
        setBunchName(bunchName);
        setMoney(money);
        setTicket(ticket);
    }

    public void setSocketClient(SocketIOClient socketClient) {
        this.socketClient = socketClient;
    }

    public SocketIOClient getSocketClient() {
        return this.socketClient;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public Integer getMaxFood() {
        return maxFood;
    }

    public void setMaxFood(Integer maxFood) {
        this.maxFood = maxFood;
    }

    public Integer getFood() {
        return food;
    }

    public void setFood(Integer food) {
        this.food = food;
    }

    public Integer getMaxWater() {
        return maxWater;
    }

    public void setMaxWater(Integer maxWater) {
        this.maxWater = maxWater;
    }

    public Integer getWater() {
        return water;
    }

    public void setWater(Integer water) {
        this.water = water;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Integer getTicket() {
        return ticket;
    }

    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }

    public Integer getEncumbrance() {
        return encumbrance;
    }

    public void setEncumbrance(Integer encumbrance) {
        this.encumbrance = encumbrance;
    }

    public void setUser(UserEntity user) {

        this.user = user;
    }

    public UserEntity getUser() {

        return this.user;
    }

    public void setPlayerInfo(PlayerInfoEntity playerInfo) {

        this.playerInfo = playerInfo;
    }

    public PlayerInfoEntity getPlayerInfo() {

        return this.playerInfo;
    }

    public void setLocation(RoomEntity location) {

        this.location = location;
    }

    public RoomEntity getLocation() {

        return this.location;
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

    public List<PlayerTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<PlayerTask> taskList) {
        this.taskList = taskList;
    }

    public Integer getTalkTaskCount() {
        return talkTaskCount;
    }

    public void setTalkTaskCount(Integer talkTaskCount) {
        this.talkTaskCount = talkTaskCount;
    }

    public Integer getKillTaskCount() {
        return killTaskCount;
    }

    public void setKillTaskCount(Integer killTaskCount) {
        this.killTaskCount = killTaskCount;
    }

    public Boolean isBorther(Player target){

        if(query("brothers/" + target.getPlayerId()) == null){

            return false;
        }

        return true;
    }

    public void initSkills(List<PlayerSkillEntity> playerSkillEntityList){

        Map<String, Integer> refSkill = getSkills();
        Map<String, Integer> refLearned = getLearned();
        for(PlayerSkillEntity playerSkillEntity : playerSkillEntityList){

            refSkill.put(playerSkillEntity.getSkillName(), playerSkillEntity.getLevel());
            refLearned.put(playerSkillEntity.getSkillName(), playerSkillEntity.getLevel());
            System.out.println(playerSkillEntity.getSkillName() + "-" + playerSkillEntity.getLevel());
        }
    }

    public void initLimbDamage() {

    }

    public void addEnemy(Living enemy){

        if(this.enemy == null){

            this.enemy = new ArrayList<Living>();
        }

        this.enemy.add(enemy);
    }

    public PlayerEntity  packagePlayerEntity(){

        PlayerEntity playerEntity = new PlayerEntity();

        playerEntity.setId(this.getPlayerId());
        playerEntity.setUserId(this.getUser().getId());
        playerEntity.setUuid(this.uuid);
        playerEntity.setName(this.name);
        playerEntity.setSex(this.gender);
        playerEntity.setCharacter(this.character);
        playerEntity.setLevel(this.level);
        playerEntity.setStr(this.str);
        playerEntity.setCon(this.con);
        playerEntity.setDex(this.dex);
        playerEntity.setKar(this.kar);
        playerEntity.setPer(this.per);
        playerEntity.setWux(this.wux);
        playerEntity.setMaxQi(this.maxQi);
        playerEntity.setEffQi(this.effQi);
        playerEntity.setQi(this.qi);
        playerEntity.setMaxNeili(this.maxNeili);
        playerEntity.setEffNeili(this.effNeili);
        playerEntity.setNeili(this.neili);
        playerEntity.setMaxJing(this.maxJing);
        playerEntity.setEffJing(this.effJing);
        playerEntity.setJing(this.jing);
        playerEntity.setFood(this.food);
        playerEntity.setWater(this.water);
        playerEntity.setTitle(this.title);
        playerEntity.setNickname(this.nickname);
        playerEntity.setMudAge(this.mudAge);
        playerEntity.setAgeModify(this.ageModify);
        playerEntity.setMaxJingLi(this.maxJingLi);
        playerEntity.setEffJingLi(this.effJingLi);
        playerEntity.setJingLi(this.jingLi);
        playerEntity.setCombatExp(this.combatExp);
        playerEntity.setStatus(this.status);
        playerEntity.setBornFamily(this.bornFamily);
        playerEntity.setSurname(this.surname);
        playerEntity.setFamilyName(this.familyName);
        playerEntity.setBunchName(this.bunchName);
        playerEntity.setCmdName(this.cmdName);
        playerEntity.setShenType(this.shenType);
        playerEntity.setCoupleId(this.coupleId);

        return playerEntity;
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

            SkillAction skillAction = this.queryAction();
            if(skillAction != null){

                sb.append(skillAction.getAction());
            }




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

            String limb = getRandomLimb();

            int damage = 3 / 2;
            int damageBonus = 20; //臂力

            damage += (damageBonus + r.nextInt(damageBonus)) / 2;

            damage += (damage * CombatUtil.getDamageWithLimb(limb)) / 100;

            if (damage > 0) {

                int wounded = damage;

                damage = damage * 20 / 100;
                sb.append(getDamageMsg(damage, skillAction.getDamageType()));

                result = sb.toString();

                result = result.replace("$l", limb);
                result = result.replace("$w", "拳头");
                //result = result.replace("$N", player.getName());
                //result = result.replace("$n", ((Player) enemy.get(0)).getPlayer().getName());

                System.out.println(result);
                //System.out.println(player.getName() + "对" + ((Player) enemy.get(0)).getPlayer().getName() + "造成" + damage + "点伤害");
            }
        }

    }

    //获取食物的最大值
    private Integer getMaxFoodCapacity() {
        return getStr() * 10 + 100;
    }

    //获取水的最大值
    private Integer getMaxWaterCapacity() {
        return getStr() * 10 + 100;
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
