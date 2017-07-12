package com.tian.server.dao;

import com.tian.server.entity.RoomGateEntity;
import com.tian.server.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/7/3.
 */
public class RoomGateDao extends BaseDao {

    public List<RoomGateEntity> getList(){

        String queryStr = "SELECT * FROM room_gate";
        Session session = SessionUtil.getSession();
        Query q = session.createNativeQuery(queryStr).addEntity(RoomGateEntity.class);
        List<RoomGateEntity> retList = q.getResultList();

        return retList;

    }
}
