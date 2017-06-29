package com.tian.server.entity;

import javax.persistence.*;

/**
 * Created by PPX on 2017/6/12.
 */
@Entity
@Table(name = "player_kongfu", schema = "bdm25683027_db", catalog = "")
public class PlayerKongfuEntity {
    private int id;
    private Integer playerId;
    private Integer kongfuId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "playerId", nullable = true)
    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    @Basic
    @Column(name = "kongfuId", nullable = true)
    public Integer getKongfuId() {
        return kongfuId;
    }

    public void setKongfuId(Integer kongfuId) {
        this.kongfuId = kongfuId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerKongfuEntity that = (PlayerKongfuEntity) o;

        if (id != that.id) return false;
        if (playerId != null ? !playerId.equals(that.playerId) : that.playerId != null) return false;
        if (kongfuId != null ? !kongfuId.equals(that.kongfuId) : that.kongfuId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (playerId != null ? playerId.hashCode() : 0);
        result = 31 * result + (kongfuId != null ? kongfuId.hashCode() : 0);
        return result;
    }
}
