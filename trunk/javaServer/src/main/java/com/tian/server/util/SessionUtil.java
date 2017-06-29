package com.tian.server.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by PPX on 2017/6/13.
 */
public class SessionUtil {

    private static Session session = null;

    public static Session getSession(){

        if(session == null){

            //创建会话工厂对象，类似于JDBC的Connection
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            //会话对象
            session = sessionFactory.openSession();
        }

        return session;
    }
}
