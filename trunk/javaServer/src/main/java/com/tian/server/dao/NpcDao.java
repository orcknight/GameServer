package com.tian.server.dao;

import com.tian.server.entity.NpcEntity;
import com.tian.server.entity.RoomContentEntity;
import com.tian.server.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/7/26.
 */
public class NpcDao extends BaseDao {

    public List<NpcEntity> getList(){

        String queryStr = "SELECT * FROM npc";
        Session session = SessionUtil.getDataSession();
        Query q = session.createNativeQuery(queryStr).addEntity(NpcEntity.class);
        List<NpcEntity> retList = q.getResultList();
        session.close();

        return retList;
    }
}
