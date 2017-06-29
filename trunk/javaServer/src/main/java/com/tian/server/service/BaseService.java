package com.tian.server.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.util.UserCacheUtil;

/**
 * Created by PPX on 2017/6/14.
 */
public class BaseService {

    protected SocketIOClient socketIOClient;
    protected Integer userId;

    public BaseService(SocketIOClient socketIOClient){

        this.socketIOClient = socketIOClient;
        if(UserCacheUtil.getSocketCache().containsKey(socketIOClient)){

            this.userId = UserCacheUtil.getSocketCache().get(socketIOClient);
        }else{

            this.userId = 0;
        }
    }

    protected void sendMsg(String msg){

        socketIOClient.sendEvent("stream", msg);
    }
}
