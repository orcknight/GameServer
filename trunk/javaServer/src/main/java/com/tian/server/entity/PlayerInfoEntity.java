package com.tian.server.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by PPX on 2017/6/12.
 */
@Entity
@Table(name = "player_info", schema = "bdm25683027_db", catalog = "")
public class PlayerInfoEntity {
    private Integer playerId;
    private Byte isLine;
    private String ip;
    private Timestamp loginTime;
    private String cityName;
    private String roomName;
    private String chatChannel;

    @Id
    @Column(name = "playerId", nullable = false)
    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    @Basic
    @Column(name = "isLine", nullable = true)
    public Byte getIsLine() {
        return isLine;
    }

    public void setIsLine(Byte isLine) {
        this.isLine = isLine;
    }

    @Basic
    @Column(name = "ip", nullable = true, length = 255)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Basic
    @Column(name = "loginTime", nullable = true)
    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
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
    @Column(name = "chatChannel", nullable = true, length = 255)
    public String getChatChannel() {
        return chatChannel;
    }

    public void setChatChannel(String chatChannel) {
        this.chatChannel = chatChannel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerInfoEntity that = (PlayerInfoEntity) o;

        if (playerId != null ? !playerId.equals(that.playerId) : that.playerId != null) return false;
        if (isLine != null ? !isLine.equals(that.isLine) : that.isLine != null) return false;
        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
        if (loginTime != null ? !loginTime.equals(that.loginTime) : that.loginTime != null) return false;
        if (cityName != null ? !cityName.equals(that.cityName) : that.cityName != null) return false;
        if (roomName != null ? !roomName.equals(that.roomName) : that.roomName != null) return false;
        if (chatChannel != null ? !chatChannel.equals(that.chatChannel) : that.chatChannel != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playerId != null ? playerId.hashCode() : 0;
        result = 31 * result + (isLine != null ? isLine.hashCode() : 0);
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (loginTime != null ? loginTime.hashCode() : 0);
        result = 31 * result + (cityName != null ? cityName.hashCode() : 0);
        result = 31 * result + (roomName != null ? roomName.hashCode() : 0);
        result = 31 * result + (chatChannel != null ? chatChannel.hashCode() : 0);
        return result;
    }
}
