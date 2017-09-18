package com.tian.server.dao;

import com.tian.server.util.SessionUtil;
import org.hibernate.Session;

/**
 * Created by PPX on 2017/6/9.
 */
public class BaseDao {

    //从session管理器获取的session实体
    protected Session session = null;

    protected Session getSession(){

        //如果没传递session过来从SessionUtil类获取一个
        if(session == null){

            session =  SessionUtil.getDataSession();
        }

        return session;
    }



}
