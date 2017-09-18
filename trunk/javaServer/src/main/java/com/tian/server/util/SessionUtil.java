package com.tian.server.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by PPX on 2017/6/13.
 */
public class SessionUtil {

    private static SessionFactory userFactory = null;
    private static SessionFactory dataFactory = null;
    private static Session userSession = null;
    private static Session dataSession = null;

    public static Session getUserSession(){

        if(userSession == null){

            if(userFactory == null){

                //创建会话工厂对象，类似于JDBC的Connection
                userFactory = new Configuration().configure("hibernate.cfg.user.xml").buildSessionFactory();
            }

            //会话对象
            userSession =  userFactory.openSession();
        }

        return userSession;
    }


    public static Session getDataSession(){

        if(dataSession == null){

            if(dataFactory == null){

                //创建会话工厂对象，类似于JDBC的Connection
                dataFactory = new Configuration().configure("hibernate.cfg.data.xml").buildSessionFactory();
            }

            //会话对象
            dataSession =  dataFactory.openSession();
        }

        return dataSession;
    }
}
