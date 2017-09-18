package com.tian.server.service;

import com.tian.server.dao.PlayerDao;
import com.tian.server.entity.PlayerEntity;
import com.tian.server.entity.RoomGateEntity;
import com.tian.server.model.Living;
import com.tian.server.model.MudObject;
import com.tian.server.model.Player;
import com.tian.server.util.UnityCmdUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/8/31.
 */
public class PlayerService {

    public JSONArray getLoginBoradcastLine(PlayerEntity player){

        JSONArray jsonArray = new JSONArray();
        JSONObject infoObject = UnityCmdUtil.getInfoWindowRet(player.getName() + "连线进入这个世界。");
        JSONArray objectsArray = new JSONArray();
        JSONObject msgObject = new JSONObject();
        msgObject.put("cmd", "look");
        msgObject.put("displayName", player.getName());
        msgObject.put("objId", "/user/user#" + player.getUuid());
        objectsArray.add(msgObject);
        JSONObject enterObject = UnityCmdUtil.getObjectEnterRet(objectsArray);
        jsonArray.add(infoObject);
        jsonArray.add(enterObject);

        return jsonArray;
    }

    public JSONArray getLogoutBoradcastLine(Player player){

        JSONArray jsonArray = new JSONArray();
        JSONObject infoObject = UnityCmdUtil.getInfoWindowRet(player.getName() + "离开了这个世界。");
        JSONObject msgObject = new JSONObject();
        msgObject.put("cmd", "look");
        msgObject.put("displayName", player.getName());
        msgObject.put("objId", "/user/user#" + player.getUuid());

        JSONObject enterObject = UnityCmdUtil.getObjectOutRet(msgObject);
        jsonArray.add(infoObject);
        jsonArray.add(enterObject);

        return jsonArray;
    }

    public JSONObject getLookLivingProto(List<Living> livingList, String livingType){

        if(livingType == null || livingType.length() < 1){

            livingType = "user";
        }

        JSONArray livingArrays = new JSONArray();
        for(Living living : livingList) {

            JSONObject msgObject = new JSONObject();
            msgObject.put("cmd", "look");
            msgObject.put("displayName", living.getNickname() + "\n" + living.getName());
            msgObject.put("objId", "/" + livingType + "/" + livingType + "#" + living.getUuid());
            livingArrays.add(msgObject);
        }

        JSONObject enterObject = UnityCmdUtil.getObjectEnterRet(livingArrays);
        return enterObject;
    }

    public JSONObject getLookLivingProto(Living living, String livingType){

        if(livingType == null || livingType.length() < 1){

            livingType = "user";
        }

        JSONArray livingArrays = new JSONArray();

        JSONObject msgObject = new JSONObject();
        msgObject.put("cmd", "look");
        msgObject.put("displayName", living.getName());
        msgObject.put("objId", "/" + livingType + "/" + livingType + "#" + living.getUuid());
        livingArrays.add(msgObject);

        JSONObject enterObject = UnityCmdUtil.getObjectEnterRet(livingArrays);
        return enterObject;
    }

    public JSONObject getLookLivingProto(Map<String, RoomGateEntity> roomGates, String livingType){

        if(livingType == null || livingType.length() < 1){

            livingType = "user";
        }

        JSONArray livingArrays = new JSONArray();
        for(RoomGateEntity gate : roomGates.values()) {

            JSONObject msgObject = new JSONObject();
            msgObject.put("cmd", "look");
            msgObject.put("displayName", gate.getName());
            msgObject.put("objId", "/" + livingType + "/" + livingType + "#" + gate.getName());
            livingArrays.add(msgObject);
        }

        JSONObject enterObject = UnityCmdUtil.getObjectEnterRet(livingArrays);
        return enterObject;
    }

    public JSONArray getEnterRoomLine(String eqpt, Player player){

        String eqptDesc = eqpt.length() == 0 ? "一丝不挂的" : "身着" + eqpt;
        String msg = player.getName() + eqptDesc + "走了过来。";

        JSONArray jsonArray = new JSONArray();
        JSONObject infoObject = UnityCmdUtil.getInfoWindowRet(msg);
        JSONArray objectsArray = new JSONArray();
        JSONObject msgObject = new JSONObject();
        msgObject.put("cmd", "look");
        msgObject.put("displayName", player.getName());
        msgObject.put("objId", "/user/user#" + player.getUuid());
        objectsArray.add(msgObject);
        JSONObject enterObject = UnityCmdUtil.getObjectEnterRet(objectsArray);
        jsonArray.add(infoObject);
        jsonArray.add(enterObject);

        return jsonArray;
    }

    public JSONArray getLeaveRoomLine(String directionInfo, Player player){

        String msg = player.getName() + "往" + directionInfo + "离开。";

        JSONArray jsonArray = new JSONArray();
        JSONObject infoObject = UnityCmdUtil.getInfoWindowRet(msg);
        JSONObject msgObject = new JSONObject();
        msgObject.put("cmd", "look");
        msgObject.put("displayName", player.getName());
        msgObject.put("objId", "/user/user#" + player.getUuid());

        JSONObject enterObject = UnityCmdUtil.getObjectOutRet(msgObject);
        jsonArray.add(infoObject);
        jsonArray.add(enterObject);

        return jsonArray;
    }





}
