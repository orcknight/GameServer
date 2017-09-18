package com.tian.server.dao;

import com.tian.server.entity.ServerInfoEntity;
import com.tian.server.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/8/31.
 */
public class ServerInfoDao extends BaseDao {

    public ServerInfoEntity getServerInfo() {

        String queryStr = "SELECT * FROM server_info";
        Query q = getSession().createNativeQuery(queryStr).addEntity(ServerInfoEntity.class);
        List<ServerInfoEntity> list = q.getResultList();

        ServerInfoEntity serverInfo;
        if(list.isEmpty()){

            serverInfo = new ServerInfoEntity();
        }else{

            serverInfo = list.get(0);
        }

        return serverInfo;
    }
}
