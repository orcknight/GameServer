package com.tian.server.dao;

import com.tian.server.entity.PlayerFamilyEntity;
import com.tian.server.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/8/14.
 */
public class PlayerFamilyDao extends BaseDao {

    public PlayerFamilyEntity getByPlayerId(Integer playerId){

        String queryStr = "SELECT * FROM player_family WHERE playerId = " + playerId;
        Session session = SessionUtil.getDataSession();
        Query q = session.createNativeQuery(queryStr).addEntity(PlayerFamilyEntity.class);
        List<PlayerFamilyEntity> retList = q.getResultList();
        session.close();

        PlayerFamilyEntity family;
        if(retList.isEmpty()){

            family = new PlayerFamilyEntity();
        }else{

            family = retList.get(0);
        }

        return family;
    }
}
