package com.tian.server.model;

import com.tian.server.entity.RoomEntity;
import com.tian.server.entity.RoomGateEntity;

/**
 * Created by PPX on 2017/6/16.
 */
public class PlayerLocation {

    private RoomEntity location;
    private RoomEntity north; //北
    private RoomEntity south; //南
    private RoomEntity east; //东
    private RoomEntity west; //西
    private RoomEntity southEast; //东南
    private RoomEntity southWest; //西南
    private RoomEntity northEast; //东北
    private RoomEntity northWest; //西北
    private RoomEntity enter; //里面
    private RoomEntity out; //外面
    private RoomEntity up; //上面
    private RoomEntity down; //下面

    public RoomEntity getLocation(){

        return this.location;
    }

    public void setLocation(RoomEntity location){

        this.location = location;
    }

    public RoomEntity getNorth(){

        return this.north;
    }

    public void setNorth(RoomEntity north){

        this.north = north;
    }

    public RoomEntity getSouth(){

        return this.south;
    }

    public void setSouth(RoomEntity south){

        this.south = south;
    }

    public RoomEntity getEast(){

        return this.east;
    }

    public void setEast(RoomEntity east){

        this.east = east;
    }

    public RoomEntity getWest(){

        return this.west;
    }

    public void setWest(RoomEntity west){

        this.west = west;
    }

    public RoomEntity getSouthEast(){

        return this.southEast;
    }

    public void setSouthEast(RoomEntity southEast){

        this.southEast = southEast;
    }

    public RoomEntity getSouthWest(){

        return this.southWest;
    }

    public void setSouthWest(RoomEntity southWest){

        this.southWest = southWest;
    }

    public RoomEntity getNorthEast(){

        return this.northEast;
    }

    public void setNorthEast(RoomEntity northEast){

        this.northEast = northEast;
    }

    public RoomEntity getNorthWest(){

        return this.northWest;
    }

    public void setNorthWest(RoomEntity northWest){

        this.northWest = northWest;
    }

    public RoomEntity getEnter(){

        return this.enter;
    }

    public void setEnter(RoomEntity enter){

        this.enter = enter;
    }

    public RoomEntity getOut(){

        return this.out;
    }

    public void setOut(RoomEntity out){

        this.out = out;
    }


}
