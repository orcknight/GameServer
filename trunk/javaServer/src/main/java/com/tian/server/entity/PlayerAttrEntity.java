package com.tian.server.entity;

import javax.persistence.*;

/**
 * Created by PPX on 2017/8/14.
 */
@Entity
@Table(name = "player_attr", schema = "bdm25683027_db", catalog = "")
public class PlayerAttrEntity {
    private int playerId;
    private String type;
    private String value;

    @Id
    @Column(name = "playerId", nullable = false)
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
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
    @Column(name = "value", nullable = true, length = 255)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerAttrEntity that = (PlayerAttrEntity) o;

        if (playerId != that.playerId) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playerId;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
