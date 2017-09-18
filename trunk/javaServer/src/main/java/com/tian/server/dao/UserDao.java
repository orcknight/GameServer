package com.tian.server.dao;

import com.tian.server.entity.UserEntity;
import com.tian.server.util.MD5Util;
import com.tian.server.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/6/9.
 * User类由于特殊是访问的logingate数据库，因此不继承BaseDao
 */
public class UserDao {

    public UserEntity getByNameAndPassword(String name, String password){

        String queryStr = "SELECT * FROM user WHERE name = '" + name + "' AND password = '" + MD5Util.getMd5(password) + "'";
        Session session = SessionUtil.getUserSession();
        Query<UserEntity> q = session.createNativeQuery(queryStr, UserEntity.class);
        List<UserEntity> retList = q.getResultList();

        UserEntity user;
        if(retList.isEmpty()){

            user = null;
        }else{

            user = retList.get(0);
        }

        return user;
    }

    public UserEntity getById(Integer userId){

        String queryStr = "SELECT * FROM user WHERE id = "  + userId;
        Session session = SessionUtil.getUserSession();
        Query<UserEntity> q = session.createNativeQuery(queryStr, UserEntity.class);
        List<UserEntity> retList = q.getResultList();

        UserEntity user;
        if(retList.isEmpty()){

            user = null;
        }else{

            user = retList.get(0);
        }

        return user;
    }

}
