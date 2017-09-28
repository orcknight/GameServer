package com.tian.server.model;

import com.tian.server.entity.RoomEntity;
import com.tian.server.util.StringUtil;
import com.tian.server.util.ZjMudUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/7/19.
 */
public class Living extends MudObject{

    //生物属性
    protected String title = ""; //称号
    protected String nickname = ""; //昵称
    protected String name = ""; //名字
    protected String surname = ""; //姓
    protected String bornFamily = ""; //出生世家
    protected String familyName = ""; //门派名称
    protected String bunchName = ""; //帮派名称
    protected String cmdName = ""; //命令名、英文名
    protected Integer age = 0; //计算后的age
    protected Integer mudAge = 0; //mod参数年龄
    protected Integer ageModify = 0; //年龄变化
    protected String gender = ""; //性别
    protected String longDesc = ""; //描述
    protected String classStr = ""; //身份 官差 和尚尼姑喇嘛等
    protected String attitude = ""; //态度 和平好战还是别的
    protected String unit = ""; //单位: 只 个
    protected Byte shenType = 0; //神的正负，如果没有set默认是1,用这个乘以exp/10得到神值
    protected Integer shen = 0; //神
    protected Long coupleId = 0L; //夫妻id

    protected Byte status = 0; //玩家状态

    //基本属性
    protected Integer str = 0; //膂力
    protected Integer wux = 0; //悟性 //mud里用int由于与java关键字冲突改名
    protected Integer con = 0; //根骨
    protected Integer dex = 0; //身法
    protected Integer kar = 0; //福缘
    protected Integer per = 0; //容貌

    //气血等信息
    protected Integer maxQi = 0;
    protected Integer effQi = 0;
    protected Integer qi = 0;
    protected Integer maxNeili = 0;
    protected Integer effNeili = 0;
    protected Integer neili = 0;
    protected Integer maxJing = 0;
    protected Integer effJing = 0;
    protected Integer jing = 0;
    protected Integer maxJingLi = 0;
    protected Integer effJingLi = 0;
    protected Integer jingLi = 0;
    protected Integer level = 0;
    protected Integer combatExp = 0; //实战经验
    protected Integer score = 0; //功劳点
    protected Boolean heartBeatFlag = false; //心跳表示
    protected Boolean isGhost = false;

    protected Map<String, Integer> skills = new HashMap<String, Integer>(); //存放的是 技能名：等级
    protected Map<String, Integer> learned = new HashMap<String, Integer>(); //存放的是玩家已经学习过的技能 技能名：等级
    protected Map<String, String> skillMap = new HashMap<String, String>(); //存放的连招 技能名：技能名
    protected Map<String, String> skillPrepare = new HashMap<String, String>(); //为基本武功设置激发武功 基本技能名字：技能名 如： prepare_skill("strike", "dragon-strike");

    protected Map<String, String> buttons = new HashMap<String, String>(); //功能按钮
    protected RoomEntity location = new RoomEntity();//位置
    protected List<Living> enemy = new ArrayList<Living>(); //敌人列表
    protected List<Living> killer = new ArrayList<Living>(); //敌人列表
    protected List<Living> wantKills = new ArrayList<Living>(); //敌人列表
    protected Map<String, Integer> apply = new HashMap<String, Integer>(); //存储附加属性
    protected Map<String, Inquiry> inquirys = new HashMap<String, Inquiry>();
    protected List<MudObject> vendorGoods = new ArrayList<MudObject>();
    protected List<GoodsContainer> packageList = new ArrayList<GoodsContainer>();

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Living> getEnemy() {
        return enemy;
    }

    public void setEnemy(List<Living> enemy) {
        this.enemy = enemy;
    }

    public List<Living> getKiller() {
        return killer;
    }

    public void setKiller(List<Living> killer) {
        this.killer = killer;
    }

    public List<Living> getWantKills() {
        return wantKills;
    }

    public void setWantKills(List<Living> wantKills) {
        this.wantKills = wantKills;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBornFamily() {
        return bornFamily;
    }

    public void setBornFamily(String bornFamily) {
        this.bornFamily = bornFamily;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getBunchName() {
        return bunchName;
    }

    public void setBunchName(String bunchName) {
        this.bunchName = bunchName;
    }

    public String getCmdName() {
        return cmdName;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getMudAge() {
        return mudAge;
    }

    public void setMudAge(Integer mudAge) {
        this.mudAge = mudAge;
    }

    public Integer getAgeModify() {
        return ageModify;
    }

    public void setAgeModify(Integer ageModify) {
        this.ageModify = ageModify;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public String getClassStr() {
        return classStr;
    }

    public void setClassStr(String classStr) {
        this.classStr = classStr;
    }

    public String getAttitude() {
        return attitude;
    }

    public void setAttitude(String attitude) {
        this.attitude = attitude;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Byte getShenType() {
        return shenType;
    }

    public void setShenType(Byte shenType) {
        this.shenType = shenType;
    }

    public Integer getShen() {
        return shen;
    }

    public void setShen(Integer shen) {
        this.shen = shen;
    }

    public Long getCoupleId() {
        return coupleId;
    }

    public void setCoupleId(Long coupleId) {
        this.coupleId = coupleId;
    }

    public Byte getStatus() {
        return this.status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getStr() {
        return str;
    }

    public void setStr(Integer str) {
        this.str = str;
    }

    public Integer getWux() {
        return wux;
    }

    public void setWux(Integer wux) {
        this.wux = wux;
    }

    public Integer getCon() {
        return con;
    }

    public void setCon(Integer con) {
        this.con = con;
    }

    public Integer getDex() {
        return dex;
    }

    public void setDex(Integer dex) {
        this.dex = dex;
    }

    public Integer getKar() {
        return kar;
    }

    public void setKar(Integer kar) {
        this.kar = kar;
    }

    public Integer getPer() {
        return per;
    }

    public void setPer(Integer per) {
        this.per = per;
    }

    public Integer getMaxQi() {
        return maxQi;
    }

    public void setMaxQi(Integer maxQi) {
        this.maxQi = maxQi;
    }

    public Integer getEffQi() {
        return effQi;
    }

    public void setEffQi(Integer effQi) {
        this.effQi = effQi;
    }

    public Integer getQi() {
        return qi;
    }

    public void setQi(Integer qi) {
        this.qi = qi;
    }

    public Integer getMaxNeili() {
        return maxNeili;
    }

    public void setMaxNeili(Integer maxNeili) {
        this.maxNeili = maxNeili;
    }

    public Integer getEffNeili() {
        return effNeili;
    }

    public void setEffNeili(Integer effNeili) {
        this.effNeili = effNeili;
    }

    public Integer getNeili() {
        return neili;
    }

    public void setNeili(Integer neili) {
        this.neili = neili;
    }

    public Integer getMaxJing() {
        return maxJing;
    }

    public void setMaxJing(Integer maxJing) {
        this.maxJing = maxJing;
    }

    public Integer getEffJing() {
        return effJing;
    }

    public void setEffJing(Integer effJing) {
        this.effJing = effJing;
    }

    public Integer getJing() {
        return jing;
    }

    public void setJing(Integer jing) {
        this.jing = jing;
    }

    public Integer getMaxJingLi() {
        return maxJingLi;
    }

    public void setMaxJingLi(Integer maxJingLi) {
        this.maxJingLi = maxJingLi;
    }

    public Integer getEffJingLi() {
        return effJingLi;
    }

    public void setEffJingLi(Integer effJingLi) {
        this.effJingLi = effJingLi;
    }

    public Integer getJingLi() {
        return jingLi;
    }

    public void setJingLi(Integer jingLi) {
        this.jingLi = jingLi;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCombatExp() {
        return combatExp;
    }

    public void setCombatExp(Integer combatExp) {
        this.combatExp = combatExp;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getHeartBeatFlag() {
        return heartBeatFlag;
    }

    public void setHeartBeatFlag(Boolean heartBeatFlag) {
        this.heartBeatFlag = heartBeatFlag;
    }

    public Boolean getGhost() {
        return isGhost;
    }

    public void setGhost(Boolean ghost) {
        isGhost = ghost;
    }

    public Map<String, Integer> getSkills() {
        return skills;
    }

    public void setSkills(Map<String, Integer> skills) {
        this.skills = skills;
    }

    public Map<String, Integer> getLearned() {
        return learned;
    }

    public void setLearned(Map<String, Integer> learned) {
        this.learned = learned;
    }

    public Map<String, String> getSkillMap() {
        return skillMap;
    }

    public void setSkillMap(Map<String, String> skillMap) {
        this.skillMap = skillMap;
    }

    public Map<String, String> getSkillPrepare() {
        return skillPrepare;
    }

    public void setSkillPrepare(Map<String, String> skillPrepare) {
        this.skillPrepare = skillPrepare;
    }

    public Map<String, String> getButtons() {
        return buttons;
    }

    public void setButtons(Map<String, String> buttons) {
        this.buttons = buttons;
    }

    public RoomEntity getLocation() {
        return location;
    }

    public void setLocation(RoomEntity location) {
        this.location = location;
    }

    public Map<String, Integer> getApply() {
        return apply;
    }

    public void setApply(Map<String, Integer> apply) {
        this.apply = apply;
    }

    public Map<String, Inquiry> getInquirys() {
        return inquirys;
    }

    public void setInquirys(Map<String, Inquiry> inquirys) {
        this.inquirys = inquirys;
    }

    public List<MudObject> getVendorGoods() {
        return vendorGoods;
    }

    public void setVendorGoods(List<MudObject> vendorGoods) {
        this.vendorGoods = vendorGoods;
    }

    public void setSkill(String skillName, Integer level) {
        this.getSkills().put(skillName, level);
    }

    public void mapSkill(String skillName1, String skillName2){
        this.getSkillMap().put(skillName1, skillName2);
    }

    public void prepareSkill(String baseSkill, String skillName){
        this.getSkillPrepare().put(baseSkill, skillName);
    }

    public List<GoodsContainer> getPackageList() {
        return packageList;
    }

    public void setPackageList(List<GoodsContainer> packageList) {
        this.packageList = packageList;
    }

    public String getRandomLimb(){

        return "";
    }

    public void heartBeat() {}

    public SkillAction queryAction() { return null; } //创建这个函数，是为了通过Living实现多态调用

    public String getLookStr(){

        String contact = "\\n";
        String desc = name + contact + "一一一一一一一一一一一一一一一一一一一" + contact +
                longDesc + contact + "一一一一一一一一一一一一一一一一一一一";
        return desc;
        /*StringBuffer buttonStr = new StringBuffer();
        for (Map.Entry<String, String> entry : this.buttons.entrySet()) {

            buttonStr.append(entry.getKey() + ":" + entry.getValue() + "$zj#");
        }
        String button = StringUtil.rtrim(buttonStr.toString());

        return ZjMudUtil.getHuDongDescLine(desc);*/
    }

    public void addEnemy(Living enemy){

        if(this.enemy == null){

            this.enemy = new ArrayList<Living>();
        }

        this.enemy.add(enemy);
    }

    public void addButton(String title, String cmd){

        this.buttons.put(title, cmd);
    }

    public void addInquiry(String title, String type, String content){


    }

    public void setApplyValue(String key, Integer value){

        getApply().put(key, value);
    }

    public Integer querySkill(String skillName, Integer raw){

        Integer num = 0;
        if(queryTemp("suit_skill/" + skillName) != null){
            num = Integer.parseInt(queryTemp("suit_skill/" + skillName).toString());   //套装技能
        }

        if (raw == 0)
        {
            Integer s = 0;
            if(queryTemp("apply/" + skillName) != null){
                s = Integer.parseInt(queryTemp("apply/" + skillName).toString());
            }

            if(getSkills().get(skillName) == null){
                return s;
            }

            s += getSkills().get(skillName);
            if(getSkillMap().get(skillName) == null){
                return s;
            }

            String mapSkillName = getSkillMap().get(skillName);
            if(getSkills().get(mapSkillName) == null){
                return s;
            }

            s += getSkills().get(mapSkillName);
            return s;
        }

        if(getSkills().get(skillName) == null){
            return 0;
        }

        return getSkills().get(skillName) + num;
    }

    protected Integer calcShen(Byte shenType, Integer combatExp){

        return shenType * combatExp / 10;
    }

}
