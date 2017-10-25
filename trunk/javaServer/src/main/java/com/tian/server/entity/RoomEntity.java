package com.tian.server.entity;

import javax.persistence.*;

/**
 * Created by PPX on 2017/7/18.
 */
@Entity
@Table(name = "room", schema = "bdm25683027_db", catalog = "")
public class RoomEntity {
    private int id;
    private String name;
    private String pname;
    private String shortDesc;
    private String longDesc;
    private Integer noFight;
    private Integer noMagic;
    private String outdoors;
    private Byte isStartRoom;
    private Byte isChatRoom;
    private String east;
    private String west;
    private String south;
    private String north;
    private String northwest;
    private String northeast;
    private String southwest;
    private String southeast;
    private String enter;
    private String out;
    private String up;
    private String down;

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
    @Column(name = "pname", nullable = true, length = 255)
    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    @Basic
    @Column(name = "shortDesc", nullable = true, length = 255)
    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    @Basic
    @Column(name = "longDesc", nullable = true, length = 2048)
    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    @Basic
    @Column(name = "noFight", nullable = true)
    public Integer getNoFight() {
        return noFight;
    }

    public void setNoFight(Integer noFight) {
        this.noFight = noFight;
    }

    @Basic
    @Column(name = "noMagic", nullable = true)
    public Integer getNoMagic() {
        return noMagic;
    }

    public void setNoMagic(Integer noMagic) {
        this.noMagic = noMagic;
    }

    @Basic
    @Column(name = "outdoors", nullable = true, length = 64)
    public String getOutdoors() {
        return outdoors;
    }

    public void setOutdoors(String outdoors) {
        this.outdoors = outdoors;
    }

    @Basic
    @Column(name = "isStartRoom", nullable = true)
    public Byte getIsStartRoom() {
        return isStartRoom;
    }

    public void setIsStartRoom(Byte isStartRoom) {
        this.isStartRoom = isStartRoom;
    }

    @Basic
    @Column(name = "isChatRoom", nullable = true)
    public Byte getIsChatRoom() {
        return isChatRoom;
    }

    public void setIsChatRoom(Byte isChatRoom) {
        this.isChatRoom = isChatRoom;
    }

    @Basic
    @Column(name = "east", nullable = true, length = 255)
    public String getEast() {
        return east;
    }

    public void setEast(String east) {
        this.east = east;
    }

    @Basic
    @Column(name = "west", nullable = true, length = 255)
    public String getWest() {
        return west;
    }

    public void setWest(String west) {
        this.west = west;
    }

    @Basic
    @Column(name = "south", nullable = true, length = 255)
    public String getSouth() {
        return south;
    }

    public void setSouth(String south) {
        this.south = south;
    }

    @Basic
    @Column(name = "north", nullable = true, length = 255)
    public String getNorth() {
        return north;
    }

    public void setNorth(String north) {
        this.north = north;
    }

    @Basic
    @Column(name = "northwest", nullable = true, length = 255)
    public String getNorthwest() {
        return northwest;
    }

    public void setNorthwest(String northwest) {
        this.northwest = northwest;
    }

    @Basic
    @Column(name = "northeast", nullable = true, length = 255)
    public String getNortheast() {
        return northeast;
    }

    public void setNortheast(String northeast) {
        this.northeast = northeast;
    }

    @Basic
    @Column(name = "southwest", nullable = true, length = 255)
    public String getSouthwest() {
        return southwest;
    }

    public void setSouthwest(String southwest) {
        this.southwest = southwest;
    }

    @Basic
    @Column(name = "southeast", nullable = true, length = 255)
    public String getSoutheast() {
        return southeast;
    }

    public void setSoutheast(String southeast) {
        this.southeast = southeast;
    }

    @Basic
    @Column(name = "enter", nullable = true, length = 255)
    public String getEnter() {
        return enter;
    }

    public void setEnter(String enter) {
        this.enter = enter;
    }

    @Basic
    @Column(name = "out", nullable = true, length = 255)
    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    @Basic
    @Column(name = "up", nullable = true, length = 255)
    public String getUp() {
        return up;
    }

    public void setUp(String up) {
        this.up = up;
    }

    @Basic
    @Column(name = "down", nullable = true, length = 255)
    public String getDown() {
        return down;
    }

    public void setDown(String down) {
        this.down = down;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomEntity that = (RoomEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (pname != null ? !pname.equals(that.pname) : that.pname != null) return false;
        if (shortDesc != null ? !shortDesc.equals(that.shortDesc) : that.shortDesc != null) return false;
        if (longDesc != null ? !longDesc.equals(that.longDesc) : that.longDesc != null) return false;
        if (noFight != null ? !noFight.equals(that.noFight) : that.noFight != null) return false;
        if (noMagic != null ? !noMagic.equals(that.noMagic) : that.noMagic != null) return false;
        if (outdoors != null ? !outdoors.equals(that.outdoors) : that.outdoors != null) return false;
        if (isStartRoom != null ? !isStartRoom.equals(that.isStartRoom) : that.isStartRoom != null) return false;
        if (isChatRoom != null ? !isChatRoom.equals(that.isChatRoom) : that.isChatRoom != null) return false;
        if (east != null ? !east.equals(that.east) : that.east != null) return false;
        if (west != null ? !west.equals(that.west) : that.west != null) return false;
        if (south != null ? !south.equals(that.south) : that.south != null) return false;
        if (north != null ? !north.equals(that.north) : that.north != null) return false;
        if (northwest != null ? !northwest.equals(that.northwest) : that.northwest != null) return false;
        if (northeast != null ? !northeast.equals(that.northeast) : that.northeast != null) return false;
        if (southwest != null ? !southwest.equals(that.southwest) : that.southwest != null) return false;
        if (southeast != null ? !southeast.equals(that.southeast) : that.southeast != null) return false;
        if (enter != null ? !enter.equals(that.enter) : that.enter != null) return false;
        if (out != null ? !out.equals(that.out) : that.out != null) return false;
        if (up != null ? !up.equals(that.up) : that.up != null) return false;
        if (down != null ? !down.equals(that.down) : that.down != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (pname != null ? pname.hashCode() : 0);
        result = 31 * result + (shortDesc != null ? shortDesc.hashCode() : 0);
        result = 31 * result + (longDesc != null ? longDesc.hashCode() : 0);
        result = 31 * result + (noFight != null ? noFight.hashCode() : 0);
        result = 31 * result + (noMagic != null ? noMagic.hashCode() : 0);
        result = 31 * result + (outdoors != null ? outdoors.hashCode() : 0);
        result = 31 * result + (isStartRoom != null ? isStartRoom.hashCode() : 0);
        result = 31 * result + (isChatRoom != null ? isChatRoom.hashCode() : 0);
        result = 31 * result + (east != null ? east.hashCode() : 0);
        result = 31 * result + (west != null ? west.hashCode() : 0);
        result = 31 * result + (south != null ? south.hashCode() : 0);
        result = 31 * result + (north != null ? north.hashCode() : 0);
        result = 31 * result + (northwest != null ? northwest.hashCode() : 0);
        result = 31 * result + (northeast != null ? northeast.hashCode() : 0);
        result = 31 * result + (southwest != null ? southwest.hashCode() : 0);
        result = 31 * result + (southeast != null ? southeast.hashCode() : 0);
        result = 31 * result + (enter != null ? enter.hashCode() : 0);
        result = 31 * result + (out != null ? out.hashCode() : 0);
        result = 31 * result + (up != null ? up.hashCode() : 0);
        result = 31 * result + (down != null ? down.hashCode() : 0);
        return result;
    }
}
