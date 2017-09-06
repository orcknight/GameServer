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

        Session session = SessionUtil.getDataSession();
        Transaction transaction = session.getTransaction();
        //开启事务
        transaction.begin();
        //插入数据
        session.save(player);
        //提交事务
        transaction.commit();
        session.close();
    }

    public PlayerEntity getById(int id){

        String queryStr = "SELECT * FROM player WHERE id = " + id;
        Session session = SessionUtil.getDataSession();
        Query q = session.createNativeQuery(queryStr).addEntity(PlayerEntity.class);
        List<PlayerEntity> retList = q.getResultList();
        session.close();

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
        Session session = SessionUtil.getDataSession();
        Query q = session.createNativeQuery(queryStr).addEntity(PlayerEntity.class);
        List<PlayerEntity> retList = q.getResultList();
        session.close();

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
        Session session = SessionUtil.getDataSession();
        Query q = session.createNativeQuery(queryStr).addEntity(PlayerEntity.class);
        List<PlayerEntity> retList = q.getResultList();
        session.close();

        PlayerEntity player;
        if(retList.isEmpty()){

            player = null;
        }else{

            player = retList.get(0);
        }

        return player;
    }

}
