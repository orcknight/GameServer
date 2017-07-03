package com.tian.server.model;

import com.tian.server.entity.ItemEntity;
import com.tian.server.entity.PlayerEntity;
import com.tian.server.entity.RoomGateEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/6/26.
 */
public class RoomObjects {

    private List<PlayerEntity> players; //玩家列表
    private List<ItemEntity> items; //物品列表;
    private Map<String, RoomGateEntity> gates; //门

    public List<PlayerEntity> getPlayers(){

        return this.players;
    }

    public void setPlayers(List<PlayerEntity> players){

        this.players = players;
    }

    public List<ItemEntity> getItems(){

        return this.items;
    }

    public void setItems(List<ItemEntity> items){

        this.items = items;
    }

    public Map<String, RoomGateEntity> getGates(){

        if(this.gates == null){

            this.gates = new HashMap<String, RoomGateEntity>();
        }

        return this.gates;
    }

    public void setGates(Map<String, RoomGateEntity> gates){

        this.gates = gates;
    }

}
