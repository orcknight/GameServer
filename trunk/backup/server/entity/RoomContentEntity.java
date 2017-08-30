package com.tian.server.entity;

import javax.persistence.*;

/**
 * Created by PPX on 2017/7/26.
 */
@Entity
@Table(name = "room_content", schema = "bdm25683027_db", catalog = "")
public class RoomContentEntity {
    private int id;
    private String roomName;
    private Integer count;
    private String type;
    private Integer refId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "count", nullable = true)
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Basic
    @Column(name = "type", nullable = true, length = 64)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "refId", nullable = true)
    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomContentEntity that = (RoomContentEntity) o;

        if (id != that.id) return false;
        if (roomName != null ? !roomName.equals(that.roomName) : that.roomName != null) return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (refId != null ? !refId.equals(that.refId) : that.refId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (roomName != null ? roomName.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (refId != null ? refId.hashCode() : 0);
        return result;
    }
}
