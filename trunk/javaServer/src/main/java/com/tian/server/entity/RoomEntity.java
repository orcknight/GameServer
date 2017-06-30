package com.tian.server.entity;

import javax.persistence.*;

/**
 * Created by PPX on 2017/6/12.
 */
@Entity
@Table(name = "room", schema = "bdm25683027_db", catalog = "")
public class RoomEntity {
    private int id;
    private String name;
    private String pname;
    private String shortDesc;
    private String longDesc;
    private String ename;
    private String wname;
    private String sname;
    private String nname;
    private String nwname;
    private String nename;
    private String swname;
    private String sename;
    private String outname;
    private String inname;
    private Integer noFight;
    private Integer noMagic;

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
    @Column(name = "ename", nullable = true, length = 255)
    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    @Basic
    @Column(name = "wname", nullable = true, length = 255)
    public String getWname() {
        return wname;
    }

    public void setWname(String wname) {
        this.wname = wname;
    }

    @Basic
    @Column(name = "sname", nullable = true, length = 255)
    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    @Basic
    @Column(name = "nname", nullable = true, length = 255)
    public String getNname() {
        return nname;
    }

    public void setNname(String nname) {
        this.nname = nname;
    }

    @Basic
    @Column(name = "nwname", nullable = true, length = 255)
    public String getNwname() {
        return nwname;
    }

    public void setNwname(String nwname) {
        this.nwname = nwname;
    }

    @Basic
    @Column(name = "nename", nullable = true, length = 255)
    public String getNename() {
        return nename;
    }

    public void setNename(String nename) {
        this.nename = nename;
    }

    @Basic
    @Column(name = "swname", nullable = true, length = 255)
    public String getSwname() {
        return swname;
    }

    public void setSwname(String swname) {
        this.swname = swname;
    }

    @Basic
    @Column(name = "sename", nullable = true, length = 255)
    public String getSename() {
        return sename;
    }

    public void setSename(String sename) {
        this.sename = sename;
    }

    @Basic
    @Column(name = "outname", nullable = true, length = 255)
    public String getOutname() {
        return outname;
    }

    public void setOutname(String outname) {
        this.outname = outname;
    }

    @Basic
    @Column(name = "inname", nullable = true, length = 255)
    public String getInname() {
        return inname;
    }

    public void setInname(String inname) {
        this.inname = inname;
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
        if (ename != null ? !ename.equals(that.ename) : that.ename != null) return false;
        if (wname != null ? !wname.equals(that.wname) : that.wname != null) return false;
        if (sname != null ? !sname.equals(that.sname) : that.sname != null) return false;
        if (nname != null ? !nname.equals(that.nname) : that.nname != null) return false;
        if (nwname != null ? !nwname.equals(that.nwname) : that.nwname != null) return false;
        if (nename != null ? !nename.equals(that.nename) : that.nename != null) return false;
        if (swname != null ? !swname.equals(that.swname) : that.swname != null) return false;
        if (sename != null ? !sename.equals(that.sename) : that.sename != null) return false;
        if (outname != null ? !outname.equals(that.outname) : that.outname != null) return false;
        if (inname != null ? !inname.equals(that.inname) : that.inname != null) return false;
        if (noFight != null ? !noFight.equals(that.noFight) : that.noFight != null) return false;
        if (noMagic != null ? !noMagic.equals(that.noMagic) : that.noMagic != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (pname != null ? pname.hashCode() : 0);
        result = 31 * result + (shortDesc != null ? shortDesc.hashCode() : 0);
        result = 31 * result + (longDesc != null ? longDesc.hashCode() : 0);
        result = 31 * result + (ename != null ? ename.hashCode() : 0);
        result = 31 * result + (wname != null ? wname.hashCode() : 0);
        result = 31 * result + (sname != null ? sname.hashCode() : 0);
        result = 31 * result + (nname != null ? nname.hashCode() : 0);
        result = 31 * result + (nwname != null ? nwname.hashCode() : 0);
        result = 31 * result + (nename != null ? nename.hashCode() : 0);
        result = 31 * result + (swname != null ? swname.hashCode() : 0);
        result = 31 * result + (sename != null ? sename.hashCode() : 0);
        result = 31 * result + (outname != null ? outname.hashCode() : 0);
        result = 31 * result + (inname != null ? inname.hashCode() : 0);
        result = 31 * result + (noFight != null ? noFight.hashCode() : 0);
        result = 31 * result + (noMagic != null ? noMagic.hashCode() : 0);
        return result;
    }
}
