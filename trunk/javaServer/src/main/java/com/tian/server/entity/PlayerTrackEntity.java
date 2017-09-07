package com.tian.server.entity;

import javax.persistence.*;

/**
 * Created by PPX on 2017/9/7.
 */
@Entity
@Table(name = "player_track", schema = "bdm25683027_db", catalog = "")
public class PlayerTrackEntity {
    private int id;
    private Integer playerId;
    private Integer lineId;
    private Integer trackId;
    private Integer step;
    private Integer status;

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
    @Column(name = "lineId", nullable = true)
    public Integer getLineId() {
        return lineId;
    }

    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    @Basic
    @Column(name = "trackId", nullable = true)
    public Integer getTrackId() {
        return trackId;
    }

    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    @Basic
    @Column(name = "step", nullable = true)
    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerTrackEntity that = (PlayerTrackEntity) o;

        if (id != that.id) return false;
        if (playerId != null ? !playerId.equals(that.playerId) : that.playerId != null) return false;
        if (lineId != null ? !lineId.equals(that.lineId) : that.lineId != null) return false;
        if (trackId != null ? !trackId.equals(that.trackId) : that.trackId != null) return false;
        if (step != null ? !step.equals(that.step) : that.step != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (playerId != null ? playerId.hashCode() : 0);
        result = 31 * result + (lineId != null ? lineId.hashCode() : 0);
        result = 31 * result + (trackId != null ? trackId.hashCode() : 0);
        result = 31 * result + (step != null ? step.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
