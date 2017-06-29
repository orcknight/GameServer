package com.tian.server.entity;

import javax.persistence.*;

/**
 * Created by PPX on 2017/6/12.
 */
@Entity
@Table(name = "npc", schema = "bdm25683027_db", catalog = "")
public class NpcEntity {
    private int id;
    private String cityName;
    private String roomName;
    private String title;
    private String name;
    private String cname;
    private String gender;
    private String attitude;
    private Byte shenType;
    private Integer per;
    private Integer age;
    private String desc;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "cityName", nullable = true, length = 255)
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Basic
    @Column(name = "roomName", nullable = true, length = 255)
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
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
    @Column(name = "name", nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "cname", nullable = true, length = 255)
    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    @Basic
    @Column(name = "gender", nullable = true, length = 255)
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "attitude", nullable = true, length = 255)
    public String getAttitude() {
        return attitude;
    }

    public void setAttitude(String attitude) {
        this.attitude = attitude;
    }

    @Basic
    @Column(name = "shenType", nullable = true)
    public Byte getShenType() {
        return shenType;
    }

    public void setShenType(Byte shenType) {
        this.shenType = shenType;
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
    @Column(name = "age", nullable = true)
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Basic
    @Column(name = "desc", nullable = true, length = 2048)
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NpcEntity npcEntity = (NpcEntity) o;

        if (id != npcEntity.id) return false;
        if (cityName != null ? !cityName.equals(npcEntity.cityName) : npcEntity.cityName != null) return false;
        if (roomName != null ? !roomName.equals(npcEntity.roomName) : npcEntity.roomName != null) return false;
        if (title != null ? !title.equals(npcEntity.title) : npcEntity.title != null) return false;
        if (name != null ? !name.equals(npcEntity.name) : npcEntity.name != null) return false;
        if (cname != null ? !cname.equals(npcEntity.cname) : npcEntity.cname != null) return false;
        if (gender != null ? !gender.equals(npcEntity.gender) : npcEntity.gender != null) return false;
        if (attitude != null ? !attitude.equals(npcEntity.attitude) : npcEntity.attitude != null) return false;
        if (shenType != null ? !shenType.equals(npcEntity.shenType) : npcEntity.shenType != null) return false;
        if (per != null ? !per.equals(npcEntity.per) : npcEntity.per != null) return false;
        if (age != null ? !age.equals(npcEntity.age) : npcEntity.age != null) return false;
        if (desc != null ? !desc.equals(npcEntity.desc) : npcEntity.desc != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (cityName != null ? cityName.hashCode() : 0);
        result = 31 * result + (roomName != null ? roomName.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (cname != null ? cname.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (attitude != null ? attitude.hashCode() : 0);
        result = 31 * result + (shenType != null ? shenType.hashCode() : 0);
        result = 31 * result + (per != null ? per.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        return result;
    }
}
