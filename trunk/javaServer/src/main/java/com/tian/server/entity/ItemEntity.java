package com.tian.server.entity;

import javax.persistence.*;

/**
 * Created by PPX on 2017/6/12.
 */
@Entity
@Table(name = "item", schema = "bdm25683027_db", catalog = "")
public class ItemEntity implements  Cloneable{
    private int id;
    private String cityName;
    private String roomName;
    private String tile;
    private String name;
    private String cname;
    private String desc;
    private String type;
    private String jsonAttr;

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
    @Column(name = "tile", nullable = true, length = 255)
    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile;
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
    @Column(name = "desc", nullable = true, length = 2048)
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Basic
    @Column(name = "type", nullable = true, length = 255)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "jsonAttr", nullable = true, length = 2048)
    public String getJsonAttr() {
        return jsonAttr;
    }

    public void setJsonAttr(String jsonAttr) {
        this.jsonAttr = jsonAttr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemEntity that = (ItemEntity) o;

        if (id != that.id) return false;
        if (cityName != null ? !cityName.equals(that.cityName) : that.cityName != null) return false;
        if (roomName != null ? !roomName.equals(that.roomName) : that.roomName != null) return false;
        if (tile != null ? !tile.equals(that.tile) : that.tile != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (cname != null ? !cname.equals(that.cname) : that.cname != null) return false;
        if (desc != null ? !desc.equals(that.desc) : that.desc != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (jsonAttr != null ? !jsonAttr.equals(that.jsonAttr) : that.jsonAttr != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (cityName != null ? cityName.hashCode() : 0);
        result = 31 * result + (roomName != null ? roomName.hashCode() : 0);
        result = 31 * result + (tile != null ? tile.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (cname != null ? cname.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (jsonAttr != null ? jsonAttr.hashCode() : 0);
        return result;
    }

    public Object clone() {

        ItemEntity item = null;

        try {

            item = (ItemEntity) super.clone();
            item.id = this.id;
            item.cityName = new StringBuffer(this.cityName).toString();
            item.roomName = new StringBuffer(this.roomName).toString();
            item.tile = new StringBuffer(this.tile).toString();
            item.name = new StringBuffer(this.name).toString();
            item.cname = new StringBuffer(this.cname).toString();
            item.desc = new StringBuffer(this.desc).toString();
            item.type = new StringBuffer(this.type).toString();
            item.jsonAttr = new StringBuffer(this.jsonAttr).toString();
        } catch (CloneNotSupportedException e) {

            e.printStackTrace();
        }

        return item;
    }


}
