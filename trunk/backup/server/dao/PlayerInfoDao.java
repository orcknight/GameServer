package com.tian.server.dao;

import com.tian.server.entity.PlayerInfoEntity;
import com.tian.server.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/6/15.
 */
public class PlayerInfoDao extends BaseDao {

    public void add(PlayerInfoEntity playerInfo){

        Session session = SessionUtil.getSession();
        Transaction transaction=session.getTransaction();
        //开启事务
        transaction.begin();
        //插入数据
        session.save(playerInfo);
        //提交事务
        transaction.commit();
    }

    public PlayerInfoEntity getByPlayerId(Integer playerId){

        String queryStr = "SELECT * FROM player_info WHERE playerId = " + playerId;
        Session session = SessionUtil.getSession();
        Query q = session.createNativeQuery(queryStr).addEntity(PlayerInfoEntity.class);
        List<PlayerInfoEntity> retList = q.getResultList();

        PlayerInfoEntity playerInfo;
        if(retList.isEmpty()){

            playerInfo = new PlayerInfoEntity();
        }else{

            playerInfo = retList.get(0);
        }

        return playerInfo;
    }


}
