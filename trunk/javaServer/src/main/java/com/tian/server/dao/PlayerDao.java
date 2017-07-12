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

        Session session = SessionUtil.getSession();
        Transaction transaction = session.getTransaction();
        //开启事务
        transaction.begin();
        //插入数据
        session.save(player);
        //提交事务
        transaction.commit();
    }

    public PlayerEntity getById(int userId){

        String queryStr = "SELECT * FROM player WHERE userId = " + userId;
        Session session = SessionUtil.getSession();
        Query q = session.createNativeQuery(queryStr).addEntity(PlayerEntity.class);
        List<PlayerEntity> retList = q.getResultList();

        PlayerEntity player;
        if(retList.isEmpty()){

            player = new PlayerEntity();
        }else{

            player = retList.get(0);
        }

        return player;
    }
}
