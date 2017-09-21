package com.tian.server.entity;

import javax.persistence.*;

/**
 * Created by PPX on 2017/8/16.
 */
@Entity
@Table(name = "player_package", schema = "bdm25683027_db", catalog = "")
public class PlayerPackageEntity {
    private int id;
    private Integer playerId;
    private Integer count;
    private Integer goodsId;
    private Long goodsUuid;
    private String goodsAttr;

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
    @Column(name = "count", nullable = true)
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Basic
    @Column(name = "goodsId", nullable = true)
    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    @Basic
    @Column(name = "goodsUUID", nullable = true)
    public Long getGoodsUuid() {
        return goodsUuid;
    }

    public void setGoodsUuid(Long goodsUuid) {
        this.goodsUuid = goodsUuid;
    }

    @Basic
    @Column(name = "goodsAttr", nullable = true, length = 1024)
    public String getGoodsAttr() {
        return goodsAttr;
    }

    public void setGoodsAttr(String goodsAttr) {
        this.goodsAttr = goodsAttr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerPackageEntity that = (PlayerPackageEntity) o;

        if (id != that.id) return false;
        if (playerId != null ? !playerId.equals(that.playerId) : that.playerId != null) return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        if (goodsId != null ? !goodsId.equals(that.goodsId) : that.goodsId != null) return false;
        if (goodsUuid != null ? !goodsUuid.equals(that.goodsUuid) : that.goodsUuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (playerId != null ? playerId.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (goodsId != null ? goodsId.hashCode() : 0);
        result = 31 * result + (goodsUuid != null ? goodsUuid.hashCode() : 0);
        return result;
    }
}
