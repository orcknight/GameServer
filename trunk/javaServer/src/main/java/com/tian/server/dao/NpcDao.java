package com.tian.server.dao;

import com.tian.server.entity.NpcEntity;
import com.tian.server.entity.RoomContentEntity;
import com.tian.server.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PPX on 2017/7/26.
 */
public class NpcDao extends BaseDao {

    public List<NpcEntity> getList(){

        String queryStr = "SELECT * FROM npc";
        Query q = getSession().createNativeQuery(queryStr).addEntity(NpcEntity.class);
        List<NpcEntity> retList = q.getResultList();

        return retList;
    }

    public NpcEntity getById(int id){

        String queryStr = "SELECT * FROM npc WHERE id = " + id;
        Query q = getSession().createNativeQuery(queryStr).addEntity(NpcEntity.class);
        List<NpcEntity> retList = q.getResultList();

        NpcEntity npcEntity;
        if(retList.isEmpty()){

            return null;
        }else{

            npcEntity = retList.get(0);
        }

        return npcEntity;
    }

}
