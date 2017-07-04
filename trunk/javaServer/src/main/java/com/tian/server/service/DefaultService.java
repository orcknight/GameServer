package com.tian.server.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.dao.ItemDao;
import com.tian.server.dao.RoomContentDao;
import com.tian.server.dao.RoomDao;
import com.tian.server.dao.RoomGateDao;
import com.tian.server.entity.*;
import com.tian.server.model.PlayerCache;
import com.tian.server.model.RoomObjects;
import com.tian.server.util.CmdUtil;
import com.tian.server.util.UserCacheUtil;

import java.util.*;

/**
 * Created by PPX on 2017/6/15.
 */
public class DefaultService extends BaseService{

    public DefaultService(SocketIOClient socketIOClient) {
        super(socketIOClient);
    }

    public void checkVersion(){

        String msg = CmdUtil.getScreenLine("版本验证成功");
        socketIOClient.sendEvent("stream", msg);

        //初始化数据
        initData();
    }

    public void sendEmpty(){

        socketIOClient.sendEvent("stream", "");
    }

    private void initData(){

        if(UserCacheUtil.getMapCache().isEmpty()){

            RoomDao roomDao = new RoomDao();
            List<RoomEntity> list = roomDao.getList();
            UserCacheUtil.initMapCache(list);
        }

        //初始化房间内物品
        if(UserCacheUtil.getRoomObjectsCache().isEmpty()){

            RoomContentDao roomContentDao = new RoomContentDao();
            ItemDao itemDao = new ItemDao();
            List<RoomContentEntity> roomContents = roomContentDao.getList();
            List<ItemEntity> items = itemDao.getList();

            UserCacheUtil.initRoomObjectsCache(roomContents, items);

            //初始化门
            RoomGateDao roomGateDao = new RoomGateDao();
            List<RoomGateEntity> roomGates = roomGateDao.getList();
            UserCacheUtil.initRoomGates(roomGates);
        }

        //初始完数据以后生成定时器
       /* Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {

                //获取player列表
                Map<Integer, PlayerCache> playerCacheMap = UserCacheUtil.getPlayerCache();
                Map<SocketIOClient, Integer> socketCache = UserCacheUtil.getSocketCache();
                if(socketCache.isEmpty()){

                    return;
                }
                for (Map.Entry<SocketIOClient, Integer> entry : socketCache.entrySet()) {

                    Integer userId = entry.getValue();
                    SocketIOClient client = entry.getKey();

                    //获取玩家信息并提取信息
                    PlayerCache player = playerCacheMap.get(userId);
                    if(player.getPlayer() == null){

                        continue;
                    }
                    if(player.getPlayer().getMaxQi() < 1){

                        continue;
                    }

                    //准备状态字符串，然后发送消息
                    String msg = CmdUtil.getPlayerStatLine(player.getPlayer());
                    client.sendEvent("stream", msg);
                }

            }
        }, 2 * 1000, 2 * 1000);*/
    }

}
