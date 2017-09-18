package com.tian.server.dao;

import com.tian.server.entity.PlayerTrackEntity;
import com.tian.server.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/9/8.
 */
public class PlayerTrackDao extends BaseDao {

    public List<PlayerTrackEntity> getDoingTaskByPlayerId(Integer playerId){

        String queryStr = "SELECT * FROM player_track WHERE status = 0 AND playerId = " + playerId;
        Query q = getSession().createNativeQuery(queryStr).addEntity(PlayerTrackEntity.class);
        List<PlayerTrackEntity> retList = q.getResultList();

        return retList;
    }

    public void add(PlayerTrackEntity playerTrackEntity){

        getSession().save(playerTrackEntity);
    }

    public void update(PlayerTrackEntity playerTrackEntity) {

        getSession().update(playerTrackEntity);

    }

}
