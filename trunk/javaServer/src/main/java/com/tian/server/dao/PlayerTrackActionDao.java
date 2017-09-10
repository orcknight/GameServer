package com.tian.server.dao;

import com.tian.server.entity.PlayerTrackActionEntity;
import com.tian.server.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/9/8.
 */
public class PlayerTrackActionDao extends BaseDao {

        public PlayerTrackActionEntity getDoingTrackActionByTrackId(Integer pid){

        String queryStr = "SELECT * FROM player_track_action WHERE status <> 2 AND pid = " + pid;
        Session session = SessionUtil.getDataSession();
        Query q = session.createNativeQuery(queryStr).addEntity(PlayerTrackActionEntity.class);
        List<PlayerTrackActionEntity> retList = q.getResultList();
        session.close();

        PlayerTrackActionEntity playerTrackAction = null;
        if(!retList.isEmpty()){
            playerTrackAction = retList.get(0);
        }

        return playerTrackAction;
    }
}
