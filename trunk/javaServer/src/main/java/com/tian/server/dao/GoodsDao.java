package com.tian.server.dao;

import com.tian.server.entity.GoodsEntity;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/9/18.
 */
public class GoodsDao extends BaseDao {

    public List<GoodsEntity> getList(){

        String queryStr = "SELECT * FROM goods";
        Query q = getSession().createNativeQuery(queryStr).addEntity(GoodsEntity.class);
        List<GoodsEntity> list = q.getResultList();

        return list;
    }
}
