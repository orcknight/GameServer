package com.tian.server.entity;

import javax.persistence.*;

/**
 * Created by PPX on 2017/6/12.
 */
@Entity
@Table(name = "room_content", schema = "bdm25683027_db", catalog = "")
public class RoomContentEntity {
    private int id;
    private Integer itemId;
    private String roomName;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomContentEntity that = (RoomContentEntity) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Basic
    @Column(name = "itemId", nullable = true)
    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    @Basic
    @Column(name = "roomName", nullable = true, length = 255)
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
