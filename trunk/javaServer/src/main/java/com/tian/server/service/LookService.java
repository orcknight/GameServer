package com.tian.server.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.model.PlayerCache;
import com.tian.server.util.UserCacheUtil;

import java.util.Map;

/**
 * Created by PPX on 2017/6/29.
 */
public class LookService extends BaseService{

    public LookService(SocketIOClient socketIOClient) {
        super(socketIOClient);
    }


    public void look(String msg){

        String type = msg.split("/")[0];
        String id = msg.split("#")[1];

        //存储观察id
        Map<Integer, PlayerCache> cacheMap = UserCacheUtil.getPlayerCache();
        PlayerCache playerCache = cacheMap.get(this.userId);
        playerCache.setLookId(new StringBuffer(msg).toString());

        if(type.equals("user")){


        }


    }

    private String getPlayerButtonStr(String userName, String playerName){



        return null;
    }


}
