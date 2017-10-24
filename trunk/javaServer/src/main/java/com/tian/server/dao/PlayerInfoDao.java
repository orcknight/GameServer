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
        //插入数据
        getSession().save(playerInfo);
    }

    public PlayerInfoEntity getByPlayerId(Integer playerId){

        String queryStr = "SELECT * FROM player_info WHERE playerId = " + playerId;
        Query q = getSession().createNativeQuery(queryStr).addEntity(PlayerInfoEntity.class);
        List<PlayerInfoEntity> retList = q.getResultList();
        PlayerInfoEntity playerInfo;
        if(retList.isEmpty()){

            playerInfo = new PlayerInfoEntity();
        }else{

            playerInfo = retList.get(0);
        }

        return playerInfo;
    }

    public void update(PlayerInfoEntity playerInfoEntity){
        getSession().update(playerInfoEntity);
    }

}
