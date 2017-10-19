package com.tian.server.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PPX on 2017/10/18.
 */
public class BodyPart {

    private Integer level;
    private String unit;
    private String name;
    private String leftName;
    private String leftId;
    private Map<String, String> componentsMap = new HashMap<String, String>();
    private String verbOfPart;
    private String cloneObject;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeftName() {
        return leftName;
    }

    public void setLeftName(String leftName) {
        this.leftName = leftName;
    }

    public String getLeftId() {
        return leftId;
    }

    public void setLeftId(String leftId) {
        this.leftId = leftId;
    }

    public Map<String, String> getComponentsMap() {
        return componentsMap;
    }

    public void setComponentsMap(Map<String, String> componentsMap) {
        this.componentsMap = componentsMap;
    }

    public String getVerbOfPart() {
        return verbOfPart;
    }

    public void setVerbOfPart(String verbOfPart) {
        this.verbOfPart = verbOfPart;
    }

    public String getCloneObject() {
        return cloneObject;
    }

    public void setCloneObject(String cloneObject) {
        this.cloneObject = cloneObject;
    }

}
