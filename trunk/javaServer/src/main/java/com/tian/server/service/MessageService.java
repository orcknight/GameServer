package com.tian.server.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.common.Ansi;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.model.RoomObjects;
import com.tian.server.util.*;
import net.sf.json.JSON;
import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/10/6.
 */
public class MessageService {

    public static final Integer SHOW_ALL = 0;
    public static final Integer SHOW_DAMAGE = 1;
    public static final Integer SHOW_BRIEF_DAMAGE = 2;
    public static final Integer SHOW_BRIEF_SELF_DAMAGE = 3;
    public static final Integer SHOW_NONE = 4;

    public void message(String msg, Living me, Living you){

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
        if(you != null && you instanceof  Player){
            excludeClients.add(((Player) you).getSocketClient());
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(msg);
        MsgUtil.sendMsg(jsonArray, excludeClients, clients);
    }

    // message.c
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
        if(you != null && you instanceof  Player){
            excludeClients.add(((Player) you).getSocketClient());
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

    public void messageCombatd(String msg, Living me, Living you, String damageInfo, int damage, String type) {
        String myGender = "", yourGender = "", myName = "", yourName = "";
        String str1="", str2="", str3f="", str3d="",me_att="",you_def="",stat="",att_photo="",armor_photo="";
        int myFlag = 0;
        int yourFlag = 0;
        boolean damageFlag;
        int brief;
        int others;
        Living weapon,armor;
        Living[] obs;
        brief = 1;

        if (type == null) {
            type="";
        }
        if (damageInfo == null) {
            damageInfo = "";
            brief = 0;
            msg = "\n" + msg;
        }
        if(you != null) {
            stat = Ansi.HIG + "   <hp> "+(you.getQi()*100 / you.getMaxQi())+"%  "+ Ansi.NOR;
            me_att= Ansi.HIG + " √ ( 你对" + you.getName() + "造成 " + damage + " 点 "+ Ansi.NOR + Ansi.RED + type + Ansi.NOR + Ansi.HIG +")\n" + Ansi.NOR;
            you_def= Ansi.HIR + " × ( 你受到" + me.getName() +" "+ damage + " 点 "+ Ansi.NOR + Ansi.RED + type + Ansi.NOR + stat + Ansi.HIR + ")\n" + Ansi.NOR;
        }
        if(damage < 0) {
            me_att="";
            you_def="";
        }

        damageFlag = (damageInfo.length() > 0);
        myName = me.getName();
        myGender = me.getGender();

        if (you != null) {
            yourName = you.getName();
            yourGender = you.getGender();
        }

        if(me instanceof  Player){

            if(brief <= 0){
                myFlag = SHOW_ALL;
            }else{
                myFlag = MapGetUtil.queryInteger(me, "env/combatd");
                if (me.query("env/combatd") != null && me.query("env/combatd").toString().equals("YES")) {
                    myFlag = SHOW_BRIEF_DAMAGE;
                }
                if(myFlag <= 0){
                    myFlag = SHOW_ALL;
                }
            }

            if (myFlag >= SHOW_BRIEF_SELF_DAMAGE || myFlag > 0 && !damageFlag) {

                if(me.query("env/ignore_combat") != null) {
                    str1 = "";
                }else {
                    str1 += "\n\n";
                }
            } else if (myFlag == SHOW_BRIEF_DAMAGE) {
                str1 = damageInfo;
            }else {
                str1 = msg + me_att + damageInfo;
            }

            str1 = str1.replace("$P", GenderUtil.genderSelf(myGender));
            str1 = str1.replace("$N", GenderUtil.genderSelf(myGender));
            if (you != null) {
                str1 = str1.replace("$p", GenderUtil.genderPronoun(yourGender));
                str1 = str1.replace("$n", yourName);
            }

            if (str1.length() > 0) {

                JSONArray jsonArray = new JSONArray();
                jsonArray.add(UnityCmdUtil.getInfoWindowRet(str1));
                MsgUtil.sendMsg(((Player) me).getSocketClient(), jsonArray);
            }
        }

        if (you != null && (you instanceof  Player)) {

            if (brief < 0) {
                yourFlag = SHOW_ALL;
            } else {
                if (you.query("env/combatd") != null && you.query("env/combatd").equals("YES")) {
                    yourFlag = SHOW_BRIEF_DAMAGE;
                }
                if(yourFlag <= 0){
                    yourFlag = SHOW_ALL;
                }
            }

            if (yourFlag == SHOW_ALL || damageFlag && yourFlag < SHOW_NONE) {
                if (yourFlag <= SHOW_DAMAGE)
                {
                    str2 = msg + you_def + damageInfo;
                } else {
                    str2 = damageInfo;
                }
                str2 = str2.replace("$P", GenderUtil.genderPronoun(myGender));
                str2 = str2.replace("$p", GenderUtil.genderSelf(yourGender));
                str2 = str2.replace("$N", myName);
                str2 = str2.replace("$n", GenderUtil.genderSelf(yourGender));
                if (str2.length() > 0) {
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.add(UnityCmdUtil.getInfoWindowRet(str2));
                    MsgUtil.sendMsg(((Player) you).getSocketClient(), jsonArray);
                }
            }
        }

        Map<String, RoomObjects> roomObjects = UserCacheUtil.getRoomObjectsCache();
        List<Player> roomPlayers = new ArrayList<Player>(roomObjects.get(me.getLocation().getName()).getPlayers());
        if(me instanceof  Player){
            roomPlayers.remove(me);
        }
        if(you instanceof  Player){
            roomPlayers.remove(you);
        }

        if (roomPlayers.size() > 0) {
            str3f = msg;
            str3d = damageInfo;
            str3f = str3f.replace("$P", myName);
            str3f = str3f.replace("$N", myName);
            str3d = str3d.replace("$P", myName);
            str3d = str3d.replace("$N", myName);
            if (you != null) {
                str3f = str3f.replace("$p", yourName);
                str3f = str3f.replace("$n", yourName);
                str3d = str3d.replace("$p", yourName);
                str3d = str3d.replace("$n", yourName);
            }

            if (brief > 0) {
                str3f += str3d;
            } else {
                str3d = str3f;
            }

            if (str3f.length() < 1) {
                return;
            }

            for(Player ob : roomPlayers){

                if(ob.query("env/combatd") == null){

                    JSONArray jsonArray = new JSONArray();
                    jsonArray.add(UnityCmdUtil.getInfoWindowRet(str3f));
                    MsgUtil.sendMsg(ob.getSocketClient(), jsonArray);
                }else{

                    Integer showCondition = Integer.parseInt(ob.query("env/combatd").toString());
                    if(showCondition == SHOW_NONE){
                        continue;
                    }else if(showCondition == SHOW_BRIEF_DAMAGE || showCondition == SHOW_BRIEF_SELF_DAMAGE){

                        JSONArray jsonArray = new JSONArray();
                        jsonArray.add(UnityCmdUtil.getInfoWindowRet(str3d));
                        MsgUtil.sendMsg(ob.getSocketClient(), jsonArray);
                    }else{

                        JSONArray jsonArray = new JSONArray();
                        jsonArray.add(UnityCmdUtil.getInfoWindowRet(str3f));
                        MsgUtil.sendMsg(ob.getSocketClient(), jsonArray);
                    }
                }
            }
        }
    }

    public void tellRoom(String roomName, JSONArray data){

        RoomObjects roomObjects = UserCacheUtil.getRoomObjectsCache().get(roomName);
        if(roomObjects == null){
            return;
        }

        List<SocketIOClient> clients = new ArrayList<SocketIOClient>();
        for (Player player : roomObjects.getPlayers()){
            clients.add(player.getSocketClient());
        }

        MsgUtil.sendMsg(data, new ArrayList<SocketIOClient>(), clients);
    }
}
