package com.tian.server.entity;

import javax.persistence.*;

/**
 * Created by PPX on 2017/7/21.
 */
@Entity
@Table(name = "player", schema = "bdm25683027_db", catalog = "")
public class PlayerEntity {
    private int id;
    private Integer userId;
    private String name;
    private String sex;
    private String character;
    private Integer level;
    private Integer str;
    private Integer con;
    private Integer dex;
    private Integer kar;
    private Integer per;
    private Integer wux;
    private Integer maxQi;
    private Integer effQi;
    private Integer qi;
    private Integer maxNeili;
    private Integer effNeili;
    private Integer neili;
    private Integer maxJing;
    private Integer effJing;
    private Integer jing;
    private Integer food;
    private Integer water;
    private String title;
    private String nickname;
    private Integer age;
    private Integer ageModify;
    private Integer maxJingLi;
    private Integer effJingLi;
    private Integer jingLi;
    private Integer combatExp;
    private Byte isAlive;
    private String bornFamily;
    private String surname;
    private String familyName;
    private String bunchName;
    private String cmdName;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "userId", nullable = true)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "sex", nullable = true, length = 32)
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "character", nullable = true, length = 255)
    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    @Basic
    @Column(name = "level", nullable = true)
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Basic
    @Column(name = "str", nullable = true)
    public Integer getStr() {
        return str;
    }

    public void setStr(Integer str) {
        this.str = str;
    }

    @Basic
    @Column(name = "con", nullable = true)
    public Integer getCon() {
        return con;
    }

    public void setCon(Integer con) {
        this.con = con;
    }

    @Basic
    @Column(name = "dex", nullable = true)
    public Integer getDex() {
        return dex;
    }

    public void setDex(Integer dex) {
        this.dex = dex;
    }

    @Basic
    @Column(name = "kar", nullable = true)
    public Integer getKar() {
        return kar;
    }

    public void setKar(Integer kar) {
        this.kar = kar;
    }

    @Basic
    @Column(name = "per", nullable = true)
    public Integer getPer() {
        return per;
    }

    public void setPer(Integer per) {
        this.per = per;
    }

    @Basic
    @Column(name = "wux", nullable = true)
    public Integer getWux() {
        return wux;
    }

    public void setWux(Integer wux) {
        this.wux = wux;
    }

    @Basic
    @Column(name = "maxQi", nullable = true)
    public Integer getMaxQi() {
        return maxQi;
    }

    public void setMaxQi(Integer maxQi) {
        this.maxQi = maxQi;
    }

    @Basic
    @Column(name = "effQi", nullable = true)
    public Integer getEffQi() {
        return effQi;
    }

    public void setEffQi(Integer effQi) {
        this.effQi = effQi;
    }

    @Basic
    @Column(name = "qi", nullable = true)
    public Integer getQi() {
        return qi;
    }

    public void setQi(Integer qi) {
        this.qi = qi;
    }

    @Basic
    @Column(name = "maxNeili", nullable = true)
    public Integer getMaxNeili() {
        return maxNeili;
    }

    public void setMaxNeili(Integer maxNeili) {
        this.maxNeili = maxNeili;
    }

    @Basic
    @Column(name = "effNeili", nullable = true)
    public Integer getEffNeili() {
        return effNeili;
    }

    public void setEffNeili(Integer effNeili) {
        this.effNeili = effNeili;
    }

    @Basic
    @Column(name = "neili", nullable = true)
    public Integer getNeili() {
        return neili;
    }

    public void setNeili(Integer neili) {
        this.neili = neili;
    }

    @Basic
    @Column(name = "maxJing", nullable = true)
    public Integer getMaxJing() {
        return maxJing;
    }

    public void setMaxJing(Integer maxJing) {
        this.maxJing = maxJing;
    }

    @Basic
    @Column(name = "effJing", nullable = true)
    public Integer getEffJing() {
        return effJing;
    }

    public void setEffJing(Integer effJing) {
        this.effJing = effJing;
    }

    @Basic
    @Column(name = "jing", nullable = true)
    public Integer getJing() {
        return jing;
    }

    public void setJing(Integer jing) {
        this.jing = jing;
    }

    @Basic
    @Column(name = "food", nullable = true)
    public Integer getFood() {
        return food;
    }

    public void setFood(Integer food) {
        this.food = food;
    }

    @Basic
    @Column(name = "water", nullable = true)
    public Integer getWater() {
        return water;
    }

    public void setWater(Integer water) {
        this.water = water;
    }

    @Basic
    @Column(name = "title", nullable = true, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "nickname", nullable = true, length = 255)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Basic
    @Column(name = "age", nullable = true)
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Basic
    @Column(name = "ageModify", nullable = true)
    public Integer getAgeModify() {
        return ageModify;
    }

    public void setAgeModify(Integer ageModify) {
        this.ageModify = ageModify;
    }

    @Basic
    @Column(name = "maxJingLi", nullable = true)
    public Integer getMaxJingLi() {
        return maxJingLi;
    }

    public void setMaxJingLi(Integer maxJingLi) {
        this.maxJingLi = maxJingLi;
    }

    @Basic
    @Column(name = "effJingLi", nullable = true)
    public Integer getEffJingLi() {
        return effJingLi;
    }

    public void setEffJingLi(Integer effJingLi) {
        this.effJingLi = effJingLi;
    }

    @Basic
    @Column(name = "JingLi", nullable = true)
    public Integer getJingLi() {
        return jingLi;
    }

    public void setJingLi(Integer jingLi) {
        this.jingLi = jingLi;
    }

    @Basic
    @Column(name = "combatExp", nullable = true)
    public Integer getCombatExp() {
        return combatExp;
    }

    public void setCombatExp(Integer combatExp) {
        this.combatExp = combatExp;
    }

    @Basic
    @Column(name = "isAlive", nullable = true)
    public Byte getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Byte isAlive) {
        this.isAlive = isAlive;
    }

    @Basic
    @Column(name = "bornFamily", nullable = true, length = 255)
    public String getBornFamily() {
        return bornFamily;
    }

    public void setBornFamily(String bornFamily) {
        this.bornFamily = bornFamily;
    }

    @Basic
    @Column(name = "surname", nullable = true, length = 255)
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Basic
    @Column(name = "familyName", nullable = true, length = 255)
    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    @Basic
    @Column(name = "bunchName", nullable = true, length = 255)
    public String getBunchName() {
        return bunchName;
    }

    public void setBunchName(String bunchName) {
        this.bunchName = bunchName;
    }

    @Basic
    @Column(name = "cmdName", nullable = true, length = 255)
    public String getCmdName() {
        return cmdName;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerEntity that = (PlayerEntity) o;

        if (id != that.id) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (sex != null ? !sex.equals(that.sex) : that.sex != null) return false;
        if (character != null ? !character.equals(that.character) : that.character != null) return false;
        if (level != null ? !level.equals(that.level) : that.level != null) return false;
        if (str != null ? !str.equals(that.str) : that.str != null) return false;
        if (con != null ? !con.equals(that.con) : that.con != null) return false;
        if (dex != null ? !dex.equals(that.dex) : that.dex != null) return false;
        if (kar != null ? !kar.equals(that.kar) : that.kar != null) return false;
        if (per != null ? !per.equals(that.per) : that.per != null) return false;
        if (wux != null ? !wux.equals(that.wux) : that.wux != null) return false;
        if (maxQi != null ? !maxQi.equals(that.maxQi) : that.maxQi != null) return false;
        if (effQi != null ? !effQi.equals(that.effQi) : that.effQi != null) return false;
        if (qi != null ? !qi.equals(that.qi) : that.qi != null) return false;
        if (maxNeili != null ? !maxNeili.equals(that.maxNeili) : that.maxNeili != null) return false;
        if (effNeili != null ? !effNeili.equals(that.effNeili) : that.effNeili != null) return false;
        if (neili != null ? !neili.equals(that.neili) : that.neili != null) return false;
        if (maxJing != null ? !maxJing.equals(that.maxJing) : that.maxJing != null) return false;
        if (effJing != null ? !effJing.equals(that.effJing) : that.effJing != null) return false;
        if (jing != null ? !jing.equals(that.jing) : that.jing != null) return false;
        if (food != null ? !food.equals(that.food) : that.food != null) return false;
        if (water != null ? !water.equals(that.water) : that.water != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (nickname != null ? !nickname.equals(that.nickname) : that.nickname != null) return false;
        if (age != null ? !age.equals(that.age) : that.age != null) return false;
        if (ageModify != null ? !ageModify.equals(that.ageModify) : that.ageModify != null) return false;
        if (maxJingLi != null ? !maxJingLi.equals(that.maxJingLi) : that.maxJingLi != null) return false;
        if (effJingLi != null ? !effJingLi.equals(that.effJingLi) : that.effJingLi != null) return false;
        if (jingLi != null ? !jingLi.equals(that.jingLi) : that.jingLi != null) return false;
        if (combatExp != null ? !combatExp.equals(that.combatExp) : that.combatExp != null) return false;
        if (isAlive != null ? !isAlive.equals(that.isAlive) : that.isAlive != null) return false;
        if (bornFamily != null ? !bornFamily.equals(that.bornFamily) : that.bornFamily != null) return false;
        if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
        if (familyName != null ? !familyName.equals(that.familyName) : that.familyName != null) return false;
        if (bunchName != null ? !bunchName.equals(that.bunchName) : that.bunchName != null) return false;
        if (cmdName != null ? !cmdName.equals(that.cmdName) : that.cmdName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (character != null ? character.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (str != null ? str.hashCode() : 0);
        result = 31 * result + (con != null ? con.hashCode() : 0);
        result = 31 * result + (dex != null ? dex.hashCode() : 0);
        result = 31 * result + (kar != null ? kar.hashCode() : 0);
        result = 31 * result + (per != null ? per.hashCode() : 0);
        result = 31 * result + (wux != null ? wux.hashCode() : 0);
        result = 31 * result + (maxQi != null ? maxQi.hashCode() : 0);
        result = 31 * result + (effQi != null ? effQi.hashCode() : 0);
        result = 31 * result + (qi != null ? qi.hashCode() : 0);
        result = 31 * result + (maxNeili != null ? maxNeili.hashCode() : 0);
        result = 31 * result + (effNeili != null ? effNeili.hashCode() : 0);
        result = 31 * result + (neili != null ? neili.hashCode() : 0);
        result = 31 * result + (maxJing != null ? maxJing.hashCode() : 0);
        result = 31 * result + (effJing != null ? effJing.hashCode() : 0);
        result = 31 * result + (jing != null ? jing.hashCode() : 0);
        result = 31 * result + (food != null ? food.hashCode() : 0);
        result = 31 * result + (water != null ? water.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (ageModify != null ? ageModify.hashCode() : 0);
        result = 31 * result + (maxJingLi != null ? maxJingLi.hashCode() : 0);
        result = 31 * result + (effJingLi != null ? effJingLi.hashCode() : 0);
        result = 31 * result + (jingLi != null ? jingLi.hashCode() : 0);
        result = 31 * result + (combatExp != null ? combatExp.hashCode() : 0);
        result = 31 * result + (isAlive != null ? isAlive.hashCode() : 0);
        result = 31 * result + (bornFamily != null ? bornFamily.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (familyName != null ? familyName.hashCode() : 0);
        result = 31 * result + (bunchName != null ? bunchName.hashCode() : 0);
        result = 31 * result + (cmdName != null ? cmdName.hashCode() : 0);
        return result;
    }
}
