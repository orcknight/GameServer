package com.tian.server.model;

/**
 * Created by PPX on 2017/9/11.
 */
public class TaskStory {

    private Integer id;
    private String type;
    private String name;
    private String said;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSaid() {
        return said;
    }

    public void setSaid(String said) {
        this.said = said;
    }

}
