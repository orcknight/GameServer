package com.tian.server.entity;

import javax.persistence.*;

/**
 * Created by PPX on 2017/7/7.
 */
@Entity
@Table(name = "skill", schema = "bdm25683027_db", catalog = "")
public class SkillEntity {
    private String name;
    private String china;
    private Integer attack;
    private Integer dodge;
    private Integer parry;
    private Integer damage;
    private Integer force2;
    private Integer difficult;
    private String level;
    private String attr;

    @Id
    @Column(name = "name", nullable = false, length = 128)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "china", nullable = true, length = 255)
    public String getChina() {
        return china;
    }

    public void setChina(String china) {
        this.china = china;
    }

    @Basic
    @Column(name = "attack", nullable = true)
    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    @Basic
    @Column(name = "dodge", nullable = true)
    public Integer getDodge() {
        return dodge;
    }

    public void setDodge(Integer dodge) {
        this.dodge = dodge;
    }

    @Basic
    @Column(name = "parry", nullable = true)
    public Integer getParry() {
        return parry;
    }

    public void setParry(Integer parry) {
        this.parry = parry;
    }

    @Basic
    @Column(name = "damage", nullable = true)
    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    @Basic
    @Column(name = "force2", nullable = true)
    public Integer getForce2() {
        return force2;
    }

    public void setForce2(Integer force2) {
        this.force2 = force2;
    }

    @Basic
    @Column(name = "difficult", nullable = true)
    public Integer getDifficult() {
        return difficult;
    }

    public void setDifficult(Integer difficult) {
        this.difficult = difficult;
    }

    @Basic
    @Column(name = "level", nullable = true, length = 64)
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Basic
    @Column(name = "attr", nullable = true, length = 64)
    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SkillEntity that = (SkillEntity) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (china != null ? !china.equals(that.china) : that.china != null) return false;
        if (attack != null ? !attack.equals(that.attack) : that.attack != null) return false;
        if (dodge != null ? !dodge.equals(that.dodge) : that.dodge != null) return false;
        if (parry != null ? !parry.equals(that.parry) : that.parry != null) return false;
        if (damage != null ? !damage.equals(that.damage) : that.damage != null) return false;
        if (force2 != null ? !force2.equals(that.force2) : that.force2 != null) return false;
        if (difficult != null ? !difficult.equals(that.difficult) : that.difficult != null) return false;
        if (level != null ? !level.equals(that.level) : that.level != null) return false;
        if (attr != null ? !attr.equals(that.attr) : that.attr != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (china != null ? china.hashCode() : 0);
        result = 31 * result + (attack != null ? attack.hashCode() : 0);
        result = 31 * result + (dodge != null ? dodge.hashCode() : 0);
        result = 31 * result + (parry != null ? parry.hashCode() : 0);
        result = 31 * result + (damage != null ? damage.hashCode() : 0);
        result = 31 * result + (force2 != null ? force2.hashCode() : 0);
        result = 31 * result + (difficult != null ? difficult.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (attr != null ? attr.hashCode() : 0);
        return result;
    }
}
