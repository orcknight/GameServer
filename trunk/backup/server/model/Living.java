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

    protected Long uuid; //实例在系统里的唯一标识

    //生物属性
    protected String title; //称号
    protected String nickname; //昵称
    protected String name; //名字
    protected String surname; //姓
    protected String bornFamily; //出生世家
    protected String familyName; //门派名称
    protected String bunchName; //帮派名称
    protected String cmdName; //命令名、英文名
    protected Integer age; //年龄
    protected Integer mudAge; //mod参数年龄
    protected Integer ageModify; //年龄变化
    protected String gender; //性别
    protected String longDesc; //描述
    protected String classStr; //身份 官差 和尚尼姑喇嘛等
    protected String attitude; //态度 和平好战还是别的
    protected String unit; //单位: 只 个
    protected Integer weight; //重量
    protected Byte shenType; //神的正负，如果没有set默认是1,用这个乘以exp/10得到神值
    protected Integer shen; //神
    protected Long coupleId; //夫妻id

    protected Byte status; //玩家状态

    //基本属性
    protected Integer str; //膂力
    protected Integer wux; //悟性 //mud里用int由于与java关键字冲突改名
    protected Integer con; //根骨
    protected Integer dex; //身法
    protected Integer kar; //福缘
    protected Integer per; //容貌

    //气血等信息
    protected Integer maxQi;
    protected Integer effQi;
    protected Integer qi;
    protected Integer maxNeili;
    protected Integer effNeili;
    protected Integer neili;
    protected Integer maxJing;
    protected Integer effJing;
    protected Integer jing;
    protected Integer level;
    protected Integer combatExp; //实战经验
    protected Integer score; //功劳点

    protected Map<String, Integer> skills = new HashMap<String, Integer>(); //存放的是 技能名：等级
    protected Map<String, Integer> learned = new HashMap<String, Integer>(); //存放的是玩家已经学习过的技能 技能名：等级
    protected Map<String, String> skillMap = new HashMap<String, String>(); //存放的连招 技能名：技能名
    protected Map<String, String> skillPrepare = new HashMap<String, String>(); //为基本武功设置激发武功 基本技能名字：技能名 如： prepare_skill("strike", "dragon-strike");

    protected Map<String, String> buttons = new HashMap<String, String>(); //功能按钮
    protected RoomEntity location = new RoomEntity();//位置
    protected List<Living> enemy = new ArrayList<Living>(); //敌人列表
    protected Map<String, Integer> apply = new HashMap<String, Integer>(); //存储附加属性
    protected Map<String, Inquiry> inquirys = new HashMap<String, Inquiry>();
    protected List<MudObject> vendorGoods = new ArrayList<MudObject>();
    protected List<Goods> packageList = new ArrayList<Goods>();

    //属性
    public Long getUuid() {
        return this.uuid;
    }
    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
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

    public List<Goods> getPackageList() {
        return packageList;
    }

    public void setPackageList(List<Goods> packageList) {
        this.packageList = packageList;
    }

    public String getRandomLimb(){

        return "";
    }

    public void heartBeat() {}

    public SkillAction queryAction() { return null; } //创建这个函数，是为了通过Living实现多态调用

    public String getLookStr(){

        String contact = "$br#";

        String desc = name + contact + "一一一一一一一一一一一一一一一一一一一" + contact + longDesc + contact + "一一一一一一一一一一一一一一一一一一一" + contact;
        StringBuffer buttonStr = new StringBuffer();
        for (Map.Entry<String, String> entry : this.buttons.entrySet()) {

            buttonStr.append(entry.getKey() + ":" + entry.getValue() + "$zj#");
        }
        String button = StringUtil.rtrim(buttonStr.toString());

        return ZjMudUtil.getHuDongDescLine(desc);
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

    protected Integer calcAge(Integer ageModify, Integer mudAge){

        Integer age = ageModify + mudAge / 86400;
        if (age > 118) {
            age = 46 + (age - 118) / 4;
        } else if (age > 28) {
            age = 16 + (age - 28) / 3;
        } else if (age > 4) {
            age = 4  + (age - 4) / 2;
        }
        age += 14;
        return age;
    }

    protected Integer calcShen(Byte shenType, Integer combatExp){

        return shenType * combatExp / 10;
    }

}
