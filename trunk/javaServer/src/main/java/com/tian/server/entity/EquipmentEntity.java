package com.tian.server.entity;

import javax.persistence.*;

/**
 * Created by PPX on 2017/8/16.
 */
@Entity
@Table(name = "equipment", schema = "bdm25683027_db", catalog = "")
public class EquipmentEntity {
    private int id;
    private String name;
    private String cmdName;
    private Byte clazz;
    private Byte subClass;
    private String props;

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
    @Column(name = "cmdName", nullable = true, length = 255)
    public String getCmdName() {
        return cmdName;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }

    @Basic
    @Column(name = "class", nullable = true)
    public Byte getClazz() {
        return clazz;
    }

    public void setClazz(Byte clazz) {
        this.clazz = clazz;
    }

    @Basic
    @Column(name = "subClass", nullable = true)
    public Byte getSubClass() {
        return subClass;
    }

    public void setSubClass(Byte subClass) {
        this.subClass = subClass;
    }

    @Basic
    @Column(name = "props", nullable = true, length = 2048)
    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EquipmentEntity that = (EquipmentEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (cmdName != null ? !cmdName.equals(that.cmdName) : that.cmdName != null) return false;
        if (clazz != null ? !clazz.equals(that.clazz) : that.clazz != null) return false;
        if (subClass != null ? !subClass.equals(that.subClass) : that.subClass != null) return false;
        if (props != null ? !props.equals(that.props) : that.props != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (cmdName != null ? cmdName.hashCode() : 0);
        result = 31 * result + (clazz != null ? clazz.hashCode() : 0);
        result = 31 * result + (subClass != null ? subClass.hashCode() : 0);
        result = 31 * result + (props != null ? props.hashCode() : 0);
        return result;
    }
}
