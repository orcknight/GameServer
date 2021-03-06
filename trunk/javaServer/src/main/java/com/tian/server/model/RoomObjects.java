package com.tian.server.model;

import com.tian.server.entity.GoodsEntity;
import com.tian.server.entity.ItemEntity;
import com.tian.server.entity.PlayerEntity;
import com.tian.server.entity.RoomGateEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/6/26.
 */
public class RoomObjects {

    private List<Player> players; //玩家列表

    private Map<Long, Living> npcs;
    //private List<Living> npcs; //npc列表
    private Map<Long, GoodsContainer> goods; //物品列表;
    private Map<String, RoomGateEntity> gates; //门

    public List<Player> getPlayers(){

        if(this.players == null){

            this.players = new ArrayList<Player>();
        }

        return this.players;
    }

    public void setPlayers(List<Player> players){

        this.players = players;
    }

    public Map<Long, Living> getNpcs() {

        if(this.npcs == null){

            this.npcs = new HashMap<Long, Living>();
        }

        return npcs;
    }

    public void setNpcs(Map<Long, Living> npcs) {
        this.npcs = npcs;
    }

    public Map<Long, GoodsContainer> getGoods(){

        if(this.goods == null){
            this.goods = new HashMap<Long, GoodsContainer>();
        }

        return this.goods;
    }

    public void setGoods(Map<Long, GoodsContainer> goods){

        this.goods = goods;
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
