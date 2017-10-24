package com.tian.server.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.util.SessionUtil;
import com.tian.server.util.UserCacheUtil;
import org.hibernate.Session;

/**
 * Created by PPX on 2017/9/14.
 */
public class BaseService {

    protected Session session = null;

    public BaseService(){}

    protected Session getSession(){

        if(session == null){

            session = SessionUtil.getDataSession();
        }
        return session;
    }
}
