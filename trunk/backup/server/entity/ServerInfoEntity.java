package com.tian.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by PPX on 2017/6/12.
 */
@Entity
@Table(name = "server_info", schema = "bdm25683027_db", catalog = "")
public class ServerInfoEntity {
    private String name;

    @Id
    @Column(name = "name", nullable = false, length = 256)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerInfoEntity that = (ServerInfoEntity) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
