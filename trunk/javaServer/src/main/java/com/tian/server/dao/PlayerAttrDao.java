package com.tian.server.dao;

import com.tian.server.entity.PlayerAttrEntity;
import com.tian.server.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/8/14.
 */
public class PlayerAttrDao extends BaseDao {

    public List<PlayerAttrEntity> getListByPlayerId(Integer playerId){

        String queryStr = "SELECT * FROM player_attr WHERE playerId = " + playerId;
        Session session = SessionUtil.getSession();
        Query q = session.createNativeQuery(queryStr).addEntity(PlayerAttrEntity.class);
        List<PlayerAttrEntity> retList = q.getResultList();

        return retList;
    }
}
