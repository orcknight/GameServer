package com.tian.server.entity;

import javax.persistence.*;

/**
 * Created by PPX on 2017/7/3.
 */
@Entity
@Table(name = "room_gate", schema = "bdm25683027_db", catalog = "")
public class RoomGateEntity {
    private int id;
    private String name;
    private String enterRoom;
    private String enterDirection;
    private String exitRoom;
    private String exitDirection;
    private Byte status;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "enterRoom", nullable = true, length = 255)
    public String getEnterRoom() {
        return enterRoom;
    }

    public void setEnterRoom(String enterRoom) {
        this.enterRoom = enterRoom;
    }

    @Basic
    @Column(name = "enterDirection", nullable = true, length = 255)
    public String getEnterDirection() {
        return enterDirection;
    }

    public void setEnterDirection(String enterDirection) {
        this.enterDirection = enterDirection;
    }

    @Basic
    @Column(name = "exitRoom", nullable = true, length = 255)
    public String getExitRoom() {
        return exitRoom;
    }

    public void setExitRoom(String exitRoom) {
        this.exitRoom = exitRoom;
    }

    @Basic
    @Column(name = "exitDirection", nullable = true, length = 255)
    public String getExitDirection() {
        return exitDirection;
    }

    public void setExitDirection(String exitDirection) {
        this.exitDirection = exitDirection;
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomGateEntity that = (RoomGateEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (enterRoom != null ? !enterRoom.equals(that.enterRoom) : that.enterRoom != null) return false;
        if (enterDirection != null ? !enterDirection.equals(that.enterDirection) : that.enterDirection != null)
            return false;
        if (exitRoom != null ? !exitRoom.equals(that.exitRoom) : that.exitRoom != null) return false;
        if (exitDirection != null ? !exitDirection.equals(that.exitDirection) : that.exitDirection != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (enterRoom != null ? enterRoom.hashCode() : 0);
        result = 31 * result + (enterDirection != null ? enterDirection.hashCode() : 0);
        result = 31 * result + (exitRoom != null ? exitRoom.hashCode() : 0);
        result = 31 * result + (exitDirection != null ? exitDirection.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
