package com.tian.server.service;

import com.tian.server.dao.ServerInfoDao;

/**
 * Created by PPX on 2017/8/31.
 */
public class ServerInfoService {

    private ServerInfoDao serverInfoDao = new ServerInfoDao();


    public String getWelcomeInfo() {

        return serverInfoDao.getServerInfo().getName();
    }
}
