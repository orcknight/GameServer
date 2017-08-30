package com.tian.server.dao;

import com.tian.server.entity.PlayerSkillEntity;
import com.tian.server.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/7/12.
 */
public class PlayerSkillDao extends BaseDao {

    public List<PlayerSkillEntity> getListByPlayerId(Integer playerId){

        String queryStr = "SELECT * FROM player_skill WHERE playerId = " + playerId;
        Session session = SessionUtil.getSession();
        Query q = session.createNativeQuery(queryStr).addEntity(PlayerSkillEntity.class);
        List<PlayerSkillEntity> retList = q.getResultList();

        return retList;
    }
}
