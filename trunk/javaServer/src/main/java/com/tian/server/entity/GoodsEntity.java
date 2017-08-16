package com.tian.server.entity;

import javax.persistence.*;

/**
 * Created by PPX on 2017/8/16.
 */
@Entity
@Table(name = "goods", schema = "bdm25683027_db", catalog = "")
public class GoodsEntity {
    private int id;
    private String type;
    private Integer refId;
    private String name;
    private String cmdName;
    private Integer weight;
    private String unit;
    private Integer value;
    private String material;
    private String longDesc;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "type", nullable = true)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "refId", nullable = true)
    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
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
    @Column(name = "cmdName", nullable = true, length = 255)
    public String getCmdName() {
        return cmdName;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }

    @Basic
    @Column(name = "weight", nullable = true)
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Basic
    @Column(name = "unit", nullable = true, length = 64)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Basic
    @Column(name = "value", nullable = true)
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Basic
    @Column(name = "material", nullable = true, length = 64)
    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Basic
    @Column(name = "longDesc", nullable = true, length = 2048)
    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoodsEntity that = (GoodsEntity) o;

        if (id != that.id) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (refId != null ? !refId.equals(that.refId) : that.refId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (cmdName != null ? !cmdName.equals(that.cmdName) : that.cmdName != null) return false;
        if (weight != null ? !weight.equals(that.weight) : that.weight != null) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        if (material != null ? !material.equals(that.material) : that.material != null) return false;
        if (longDesc != null ? !longDesc.equals(that.longDesc) : that.longDesc != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (refId != null ? refId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (cmdName != null ? cmdName.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (material != null ? material.hashCode() : 0);
        result = 31 * result + (longDesc != null ? longDesc.hashCode() : 0);
        return result;
    }
}
