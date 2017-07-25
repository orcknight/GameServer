package com.tian.server.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.entity.RoomGateEntity;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.model.RoomObjects;
import com.tian.server.util.CmdUtil;
import com.tian.server.util.UserCacheUtil;

import java.util.Collection;
import java.util.Map;

/**
 * Created by PPX on 2017/6/29.
 */
public class LookService extends BaseService{

    public LookService(SocketIOClient socketIOClient) {
        super(socketIOClient);
    }


    public void look(String msg){

        String type = msg.split("/")[1];
        String id = msg.split("#")[1];

        //存储观察id
        Map<Integer, Living> cacheMap = UserCacheUtil.getPlayers();
        Player player = (Player)cacheMap.get(this.userId);
        player.setLookId(new StringBuffer(msg).toString());

        if(type.equals("user")){


        }else if(type.equals("gate")){

            String retMsg = getLookGateStr(id);
            sendMsg(retMsg);
        }
    }

    public void openGate(String direction){

        openOrCloseGate(direction, "打开");
    }

    public void closeGate(String direction){

        openOrCloseGate(direction, "关闭");
    }

    private String getPlayerButtonStr(String userName, String playerName){

        return null;
    }

    private String getLookGateStr(String name){

        Map<Integer, Living> cacheMap = UserCacheUtil.getPlayers();
        Player player = (Player)cacheMap.get(this.userId);
        String roomName = player.getLocation().getName();

        Map<String, RoomObjects> roomObjectsCache = UserCacheUtil.getRoomObjectsCache();
        RoomObjects roomObjects = roomObjectsCache.get(roomName);
        if(roomObjects == null){

            return "";
        }

        for (Map.Entry<String, RoomGateEntity> entry : roomObjects.getGates().entrySet()) {

            RoomGateEntity gate = entry.getValue();

            if(name.equals(gate.getName())){

                //去掉名称的特殊字符
                name = name.replaceAll("【", "");
                name = name.replaceAll("】", "");
                StringBuffer desc = new StringBuffer();
                StringBuffer button = new StringBuffer();

                desc.append("这个" + name + "是");

                if(gate.getStatus() == 1){

                    desc.append("开着的。");
                    button.append("关门:close " + entry.getKey());
                }else {

                    desc.append("关着的。");
                    button.append("开门:open " + entry.getKey());
                }

                String msg = CmdUtil.getHuDongDescLine(desc.toString()) + CmdUtil.getHuDongButtonLine(button.toString());
                return msg;
            }
        }

        return "";
    }

    private void openOrCloseGate(String direction, String action){

        Map<Integer, Living> cacheMap = UserCacheUtil.getPlayers();
        Player player = (Player)cacheMap.get(this.userId);
        String roomName = player.getLocation().getName();

        Map<String, RoomObjects> roomObjectsCache = UserCacheUtil.getRoomObjectsCache();
        RoomObjects roomObjects = roomObjectsCache.get(roomName);
        if(roomObjects == null){

            return ;
        }

        RoomGateEntity gate = roomObjects.getGates().get(direction);
        //去掉名称的特殊字符
        String name = gate.getName().replaceAll("【", "");
        name = name.replaceAll("】", "");
        String msg = "将" + name + action  + "。";

        sendMsg(CmdUtil.getScreenLine("你" + msg));
        Collection<SocketIOClient> cl = socketIOClient.getNamespace().getRoomOperations(gate.getEnterRoom()).getClients();
        socketIOClient.getNamespace().getRoomOperations(player.getLocation().getName()).sendEvent("stream", socketIOClient,
                CmdUtil.getScreenLine(player.getName() + msg));
        socketIOClient.getNamespace().getRoomOperations(gate.getExitRoom()).sendEvent("stream", socketIOClient,
                CmdUtil.getScreenLine(player.getName() + msg));

        if(action.equals("打开")){

            gate.setStatus(Byte.valueOf("1"));
        }else{

            gate.setStatus(Byte.valueOf("0"));
        }
    }


}
