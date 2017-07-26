package com.tian.server.entity;

import javax.persistence.*;

/**
 * Created by PPX on 2017/7/26.
 */
@Entity
@Table(name = "npc", schema = "bdm25683027_db", catalog = "")
public class NpcEntity {
    private int id;
    private String name;
    private String cmdName;
    private String resource;
    private String race;

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
    @Column(name = "resource", nullable = true, length = 255)
    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    @Basic
    @Column(name = "race", nullable = true, length = 255)
    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NpcEntity npcEntity = (NpcEntity) o;

        if (id != npcEntity.id) return false;
        if (name != null ? !name.equals(npcEntity.name) : npcEntity.name != null) return false;
        if (cmdName != null ? !cmdName.equals(npcEntity.cmdName) : npcEntity.cmdName != null) return false;
        if (resource != null ? !resource.equals(npcEntity.resource) : npcEntity.resource != null) return false;
        if (race != null ? !race.equals(npcEntity.race) : npcEntity.race != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (cmdName != null ? cmdName.hashCode() : 0);
        result = 31 * result + (resource != null ? resource.hashCode() : 0);
        result = 31 * result + (race != null ? race.hashCode() : 0);
        return result;
    }
}
