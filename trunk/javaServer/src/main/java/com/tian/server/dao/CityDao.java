package com.tian.server.dao;

import com.tian.server.entity.CityEntity;
import com.tian.server.entity.ItemEntity;
import com.tian.server.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/9/1.
 */
public class CityDao {

    public List<CityEntity> getList(){

        String queryStr = "SELECT * FROM city";
        Session session = SessionUtil.getDataSession();
        Query q = session.createNativeQuery(queryStr).addEntity(CityEntity.class);
        List<CityEntity> list = q.getResultList();
        session.close();

        return list;
    }
}
