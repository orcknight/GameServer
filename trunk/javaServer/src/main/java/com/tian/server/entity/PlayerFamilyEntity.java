package com.tian.server.entity;

import com.tian.server.util.ChineseUtil;

import javax.persistence.*;

/**
 * Created by PPX on 2017/8/14.
 */
@Entity
@Table(name = "player_family", schema = "bdm25683027_db", catalog = "")
public class PlayerFamilyEntity {
    private int playerId;
    private String name;
    private String masterId;
    private String masterName;
    private Integer generation;
    private String title;
    private Integer privs;
    private String desc;
    private Integer enterTime;

    @Id
    @Column(name = "playerId", nullable = false)
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
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
    @Column(name = "masterId", nullable = true, length = 255)
    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    @Basic
    @Column(name = "masterName", nullable = true, length = 255)
    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    @Basic
    @Column(name = "generation", nullable = true)
    public Integer getGeneration() {
        return generation;
    }

    public void setGeneration(Integer generation) {
        this.generation = generation;
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
    @Column(name = "privs", nullable = true)
    public Integer getPrivs() {
        return privs;
    }

    public void setPrivs(Integer privs) {
        this.privs = privs;
    }

    @Basic
    @Column(name = "desc", nullable = true, length = 255)
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Basic
    @Column(name = "enterTime", nullable = true)
    public Integer getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Integer enterTime) {
        this.enterTime = enterTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerFamilyEntity that = (PlayerFamilyEntity) o;

        if (playerId != that.playerId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (masterId != null ? !masterId.equals(that.masterId) : that.masterId != null) return false;
        if (masterName != null ? !masterName.equals(that.masterName) : that.masterName != null) return false;
        if (generation != null ? !generation.equals(that.generation) : that.generation != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (privs != null ? !privs.equals(that.privs) : that.privs != null) return false;
        if (desc != null ? !desc.equals(that.desc) : that.desc != null) return false;
        if (enterTime != null ? !enterTime.equals(that.enterTime) : that.enterTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playerId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (masterId != null ? masterId.hashCode() : 0);
        result = 31 * result + (masterName != null ? masterName.hashCode() : 0);
        result = 31 * result + (generation != null ? generation.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (privs != null ? privs.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (enterTime != null ? enterTime.hashCode() : 0);
        return result;
    }

    public void createFamily(String name, Integer generation, String title){

        this.name = name;
        this.generation = generation;
        this.title = title;
        this.privs = -1;
        this.desc = generateDesc();
        this.enterTime = 0;
    }

    private String generateDesc() {

        if(this.title.length() < 1){
            return "";
        }

        switch(this.generation) {
            case 0:
                return this.name + this.title;
            case 1:
                return this.name + "开山祖师";
            default:
                return this.name + "第" + ChineseUtil.chinese_number(this.generation) + "代" + this.title;

        }
    }
}
