package com.tian.server.model;

import com.tian.server.entity.ItemEntity;
import com.tian.server.entity.PlayerEntity;

import java.util.List;

/**
 * Created by PPX on 2017/6/26.
 */
public class RoomObjects {

    private List<PlayerEntity> players; //玩家列表
    private List<ItemEntity> items; //物品列表;

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

}
