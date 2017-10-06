package com.tian.server.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.model.RoomObjects;
import com.tian.server.util.GenderUtil;
import com.tian.server.util.MsgUtil;
import com.tian.server.util.UnityCmdUtil;
import com.tian.server.util.UserCacheUtil;
import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/10/6.
 */
public class MessageService {

    public void message_vision(String msg, Living me, Living you) {

        Map<String, RoomObjects> roomObjects = UserCacheUtil.getRoomObjectsCache();
        List<Player> roomPlayers = roomObjects.get(me.getLocation().getName()).getPlayers();
        List<SocketIOClient> clients = new ArrayList<SocketIOClient>();
        List<SocketIOClient> excludeClients = new ArrayList<SocketIOClient>();
        for(Player player : roomPlayers){
            clients.add(player.getSocketClient());
        }
        if(me instanceof  Player){
            excludeClients.add(((Player) me).getSocketClient());
        }
        if(you instanceof  Player){
            excludeClients.add(((Player) me).getSocketClient());
        }

        String yourGender,  yourName;
        String str1, str2, str3;

        String myName = me.getName();
        String myGender = me.getGender();
        JSONArray jsonArray1 = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();
        JSONArray jsonArray3 = new JSONArray();

        str1 = msg.replace("$P", GenderUtil.genderSelf(myGender));
        str1 = str1.replace("$N", GenderUtil.genderSelf(myGender));
        str3 = msg.replace("$P", myName);
        str3 = str3.replace("$N", myName);

        if (you != null) {

            yourName = you.getName();
            yourGender = you.getGender();
            str1 = str1.replace("$p", GenderUtil.genderPronoun(yourGender));
            str1 = str1.replace("$n", yourName);

            str3 = str3.replace("$p", yourName);
            str3 = str3.replace("$n", yourName);

            str2 = msg.replace("$P", GenderUtil.genderPronoun(myGender));
            str2 = str2.replace("$p", GenderUtil.genderSelf(yourGender));
            str2 = str2.replace("$N", myName);
            str2 = str2.replace("$n", GenderUtil.genderSelf(yourGender));

            jsonArray2.add(UnityCmdUtil.getInfoWindowRet(str2));
            if(you instanceof  Player){
                MsgUtil.sendMsg(((Player) you).getSocketClient(), jsonArray2);
            }
        }

        if(me instanceof  Player){
            jsonArray1.add(UnityCmdUtil.getInfoWindowRet(str1));
            MsgUtil.sendMsg(((Player) me).getSocketClient(), jsonArray1);
        }

        jsonArray3.add(UnityCmdUtil.getInfoWindowRet(str3));
        MsgUtil.sendMsg(jsonArray3, excludeClients, clients);
    }
}
