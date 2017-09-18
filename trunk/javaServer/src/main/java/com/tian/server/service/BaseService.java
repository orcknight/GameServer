package com.tian.server.service;

import org.hibernate.Session;

/**
 * Created by PPX on 2017/9/14.
 */
public class BaseService {

    protected Session session = null;

    public BaseService(){}

    public BaseService(Session session){

        this.session = session;
    }
}
