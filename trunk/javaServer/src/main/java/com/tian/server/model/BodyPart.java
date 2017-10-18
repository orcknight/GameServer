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
    private String partId;
    private Map<String, String> leftIdMap = new HashMap<String, String>();
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

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public Map<String, String> getLeftIdMap() {
        return leftIdMap;
    }

    public void setLeftIdMap(Map<String, String> leftIdMap) {
        this.leftIdMap = leftIdMap;
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
