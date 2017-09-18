package com.tian.server.dao;

import com.tian.server.entity.PlayerEntity;
import com.tian.server.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/6/9.
 */
public class PlayerDao extends BaseDao {

    public void add(PlayerEntity player){
        //插入数据
        getSession().save(player);
    }

    public void update(PlayerEntity player){
        getSession().update(player);
    }

    public PlayerEntity getById(int id){

        String queryStr = "SELECT * FROM player WHERE id = " + id;
        Query q = getSession().createNativeQuery(queryStr).addEntity(PlayerEntity.class);
        List<PlayerEntity> retList = q.getResultList();

        PlayerEntity player;
        if(retList.isEmpty()){

            player = new PlayerEntity();
        }else{

            player = retList.get(0);
        }

        return player;
    }

    public PlayerEntity getByUserId(int userId){

        String queryStr = "SELECT * FROM player WHERE userId = " + userId;
        Query q = getSession().createNativeQuery(queryStr).addEntity(PlayerEntity.class);
        List<PlayerEntity> retList = q.getResultList();

        PlayerEntity player;
        if(retList.isEmpty()){

            player = new PlayerEntity();
        }else{

            player = retList.get(0);
        }

        return player;
    }

    public PlayerEntity getByName(String name){

        String queryStr = "SELECT * FROM player WHERE name = '" + name + "'";
        Query q = getSession().createNativeQuery(queryStr).addEntity(PlayerEntity.class);
        List<PlayerEntity> retList = q.getResultList();

        PlayerEntity player;
        if(retList.isEmpty()){

            player = null;
        }else{

            player = retList.get(0);
        }

        return player;
    }

}
