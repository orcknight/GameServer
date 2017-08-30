package com.tian.server.dao;

import com.tian.server.entity.PlayerEntity;
import com.tian.server.entity.RoomEntity;
import com.tian.server.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/6/15.
 */
public class RoomDao extends BaseDao {

    public List<RoomEntity> getList(){

        String queryStr = "SELECT * FROM room";
        Session session = SessionUtil.getSession();
        Query q = session.createNativeQuery(queryStr).addEntity(RoomEntity.class);
        List<RoomEntity> retList = q.getResultList();

        return retList;
    }

}

