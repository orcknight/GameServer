package com.tian.server.dao;

import com.tian.server.entity.RoomContentEntity;
import com.tian.server.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/6/26.
 */
public class RoomContentDao extends BaseDao{

    public List<RoomContentEntity> getList(){

        String queryStr = "SELECT * FROM room_content";
        Session session = SessionUtil.getSession();
        Query q = session.createNativeQuery(queryStr).addEntity(RoomContentEntity.class);
        List<RoomContentEntity> retList = q.getResultList();

        return retList;
    }
}
