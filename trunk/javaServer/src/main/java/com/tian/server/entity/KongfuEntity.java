package com.tian.server.entity;

import javax.persistence.*;

/**
 * Created by PPX on 2017/6/12.
 */
@Entity
@Table(name = "kongfu", schema = "bdm25683027_db", catalog = "")
public class KongfuEntity {
    private int id;
    private String name;
    private Byte isForce;
    private Byte isUnarmed;
    private Byte isStrike;
    private Byte isDodge;
    private Byte isParry;
    private Byte isSword;
    private Byte isLiterate;
    private Byte isQimen;

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
    @Column(name = "isForce", nullable = true)
    public Byte getIsForce() {
        return isForce;
    }

    public void setIsForce(Byte isForce) {
        this.isForce = isForce;
    }

    @Basic
    @Column(name = "isUnarmed", nullable = true)
    public Byte getIsUnarmed() {
        return isUnarmed;
    }

    public void setIsUnarmed(Byte isUnarmed) {
        this.isUnarmed = isUnarmed;
    }

    @Basic
    @Column(name = "isStrike", nullable = true)
    public Byte getIsStrike() {
        return isStrike;
    }

    public void setIsStrike(Byte isStrike) {
        this.isStrike = isStrike;
    }

    @Basic
    @Column(name = "isDodge", nullable = true)
    public Byte getIsDodge() {
        return isDodge;
    }

    public void setIsDodge(Byte isDodge) {
        this.isDodge = isDodge;
    }

    @Basic
    @Column(name = "isParry", nullable = true)
    public Byte getIsParry() {
        return isParry;
    }

    public void setIsParry(Byte isParry) {
        this.isParry = isParry;
    }

    @Basic
    @Column(name = "isSword", nullable = true)
    public Byte getIsSword() {
        return isSword;
    }

    public void setIsSword(Byte isSword) {
        this.isSword = isSword;
    }

    @Basic
    @Column(name = "isLiterate", nullable = true)
    public Byte getIsLiterate() {
        return isLiterate;
    }

    public void setIsLiterate(Byte isLiterate) {
        this.isLiterate = isLiterate;
    }

    @Basic
    @Column(name = "isQimen", nullable = true)
    public Byte getIsQimen() {
        return isQimen;
    }

    public void setIsQimen(Byte isQimen) {
        this.isQimen = isQimen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KongfuEntity that = (KongfuEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (isForce != null ? !isForce.equals(that.isForce) : that.isForce != null) return false;
        if (isUnarmed != null ? !isUnarmed.equals(that.isUnarmed) : that.isUnarmed != null) return false;
        if (isStrike != null ? !isStrike.equals(that.isStrike) : that.isStrike != null) return false;
        if (isDodge != null ? !isDodge.equals(that.isDodge) : that.isDodge != null) return false;
        if (isParry != null ? !isParry.equals(that.isParry) : that.isParry != null) return false;
        if (isSword != null ? !isSword.equals(that.isSword) : that.isSword != null) return false;
        if (isLiterate != null ? !isLiterate.equals(that.isLiterate) : that.isLiterate != null) return false;
        if (isQimen != null ? !isQimen.equals(that.isQimen) : that.isQimen != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (isForce != null ? isForce.hashCode() : 0);
        result = 31 * result + (isUnarmed != null ? isUnarmed.hashCode() : 0);
        result = 31 * result + (isStrike != null ? isStrike.hashCode() : 0);
        result = 31 * result + (isDodge != null ? isDodge.hashCode() : 0);
        result = 31 * result + (isParry != null ? isParry.hashCode() : 0);
        result = 31 * result + (isSword != null ? isSword.hashCode() : 0);
        result = 31 * result + (isLiterate != null ? isLiterate.hashCode() : 0);
        result = 31 * result + (isQimen != null ? isQimen.hashCode() : 0);
        return result;
    }
}
