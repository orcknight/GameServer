package com.tian.server.model;

import com.tian.server.entity.RoomEntity;
import com.tian.server.util.CmdUtil;
import com.tian.server.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/7/19.
 */
public class Living {

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
    protected Integer ageModify; //年龄变化
    protected String gender; //性别
    protected String longDesc; //描述
    protected String classStr; //身份 官差 和尚尼姑喇嘛等
    protected String attitude; //态度 和平好战还是别的
    protected String unit; //单位: 只 个
    protected Integer weight; //重量

    protected boolean isAlive; //是否存活

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

    protected Map<String, Integer> skills; //存放的是 技能名：等级
    protected Map<String, Integer> learned; //存放的是玩家已经学习过的技能 技能名：等级
    protected Map<String, String> skillMap; //存放的连招 技能名：技能名
    protected Map<String, String> skillPrepare; //为基本武功设置激发武功 基本技能名字：技能名 如： prepare_skill("strike", "dragon-strike");

    protected Map<String, String> buttons; //功能按钮
    protected RoomEntity location; //位置
    protected List<Living> enemy; //敌人列表

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

    public boolean getIsAlive() {
        return this.isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
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


    public String getRandomLimb(){

        return "";
    }

    public void heartBeat() {}

    public SkillAction queryAction() { return null; } //创建这个函数，是为了通过Living实现多态调用

    public String getLookStr(){

        String contact = "$br#";

        String desc = name + contact + longDesc;
        StringBuffer buttonStr = new StringBuffer();
        for (Map.Entry<String, String> entry : this.buttons.entrySet()) {

            buttonStr.append(entry.getKey() + ":" + entry.getValue() + "$zj#");
        }
        String button = StringUtil.rtrim(buttonStr.toString());

        return CmdUtil.getHuDongDescLine(desc) + CmdUtil.getHuDongButtonLine(button);
    }

    public void setButtons(List<String> names, List<String> cmds) {

    }

    public void addEnemy(Living enemy){

        if(this.enemy == null){

            this.enemy = new ArrayList<Living>();
        }

        this.enemy.add(enemy);
    }

}
