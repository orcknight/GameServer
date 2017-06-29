package com.tian.server.dao;

import com.tian.server.entity.ItemEntity;
import com.tian.server.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/6/22.
 */
public class ItemDao extends BaseDao{

    public List<ItemEntity> getList(){

        String queryStr = "SELECT * FROM item";
        Session session = SessionUtil.getSession();
        Query q = session.createNativeQuery(queryStr).addEntity(ItemEntity.class);
        List<ItemEntity> list = q.getResultList();

        return list;
    }


}
