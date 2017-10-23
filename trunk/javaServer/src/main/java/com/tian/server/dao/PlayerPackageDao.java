package com.tian.server.dao;

import com.tian.server.entity.PlayerPackageEntity;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/9/20.
 */
public class PlayerPackageDao extends BaseDao {

    public List<PlayerPackageEntity> getListByPlayerId(Integer playerId){

        String queryStr = "SELECT * FROM player_package WHERE playerId = " + playerId;
        Query q = getSession().createNativeQuery(queryStr).addEntity(PlayerPackageEntity.class);
        List<PlayerPackageEntity> retList = q.getResultList();

        return retList;
    }

    public void add(PlayerPackageEntity playerPackageEntity){

        getSession().save(playerPackageEntity);
    }

    public void update(PlayerPackageEntity playerPackageEntity){

        getSession().update(playerPackageEntity);
    }

    public void delete(PlayerPackageEntity playerPackageEntity){

        getSession().delete(playerPackageEntity);
    }

}
