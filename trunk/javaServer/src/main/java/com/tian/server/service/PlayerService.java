package com.tian.server.service;

import com.tian.server.dao.PlayerDao;
import com.tian.server.entity.PlayerEntity;
import com.tian.server.entity.RoomGateEntity;
import com.tian.server.model.GoodsContainer;
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
        msgObject.put("objId", "/user/user#" + player.getUuid().toString());
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
        msgObject.put("objId", "/user/user#" + player.getUuid().toString());

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

        for (Living living : livingList) {

            String displayName = living.getNickname().length() > 0 ?
                    (living.getNickname() + "\n" + living.getName()) : (living.getName());
            JSONObject msgObject = new JSONObject();
            msgObject.put("cmd", "look");
            msgObject.put("displayName", displayName);
            msgObject.put("objId", "/" + livingType + "/" + livingType + "#" + living.getUuid().toString());
            livingArrays.add(msgObject);
        }

        JSONObject enterObject = UnityCmdUtil.getObjectEnterRet(livingArrays);
        return enterObject;
    }

    public JSONObject getLookLivingProto(Map<Long, Living> livingList, String livingType){

        if(livingType == null || livingType.length() < 1){

            livingType = "user";
        }

        JSONArray livingArrays = new JSONArray();

        for (Living living : livingList.values()) {

            String displayName = living.getNickname().length() > 0 ?
                    (living.getNickname() + "\n" + living.getName()) : (living.getName());
            JSONObject msgObject = new JSONObject();
            msgObject.put("cmd", "look");
            msgObject.put("displayName", displayName);
            msgObject.put("objId", "/" + livingType + "/" + livingType + "#" + living.getUuid().toString());
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
        String displayName = living.getNickname().length() > 0 ?
                (living.getNickname() + "\n" + living.getName()) : (living.getName());
        msgObject.put("cmd", "look");
        msgObject.put("displayName", displayName);
        msgObject.put("objId", "/" + livingType + "/" + livingType + "#" + living.getUuid().toString());
        livingArrays.add(msgObject);

        JSONObject enterObject = UnityCmdUtil.getObjectEnterRet(livingArrays);
        return enterObject;
    }

    public JSONObject getLookGateProto(Map<String, RoomGateEntity> roomGates, String livingType){

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

    public JSONObject getLookGoodsProto(Map<Long, GoodsContainer> goodsContainerList){

        JSONArray goodsArrays = new JSONArray();
        JSONObject msgObject = new JSONObject();
        String goodsType = "goods";
        for(GoodsContainer goodsContainer : goodsContainerList.values()) {

            msgObject.put("cmd", "look");
            msgObject.put("displayName", goodsContainer.getGoodsEntity().getName());
            msgObject.put("objId", "/" + goodsType + "/" + goodsType + "#" + goodsContainer.getUuid().toString());
            goodsArrays.add(msgObject);
        }

        JSONObject enterObject = UnityCmdUtil.getObjectEnterRet(goodsArrays);
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
        msgObject.put("objId", "/user/user#" + player.getUuid().toString());
        objectsArray.add(msgObject);
        JSONObject enterObject = UnityCmdUtil.getObjectEnterRet(objectsArray);
        jsonArray.add(infoObject);
        //jsonArray.add(enterObject);

        return jsonArray;
    }

    public JSONArray getLeaveRoomLine(String directionInfo, Player player){

        String msg = player.getName() + "往" + directionInfo + "离开。";

        JSONArray jsonArray = new JSONArray();
        JSONObject infoObject = UnityCmdUtil.getInfoWindowRet(msg);
        JSONObject msgObject = new JSONObject();
        msgObject.put("cmd", "look");
        msgObject.put("displayName", player.getName());
        msgObject.put("objId", "/user/user#" + player.getUuid().toString());

        JSONObject enterObject = UnityCmdUtil.getObjectOutRet(msgObject);
        jsonArray.add(infoObject);
        //jsonArray.add(enterObject);

        return jsonArray;
    }

    public Integer getMaxEncumbrance(Player player){

        //Todo:判断是否是会员，这里暂时不不判断　
        // ob->set_max_encumbrance(80000 + ob->query("str") * 8000 + ob->query_str() * 1200);
        Integer base = 40000;
        Integer innate = player.getStr() * 4000;
        Integer postnatal = queryStr(player) * 600;

        return base + innate + postnatal;
    }

    private Integer queryStr(Player player)
    {

        int str;
        int improve = 0;
        int lx;
        str = player.getStr();
        Map<String, Integer> skills = player.getSkills();
        if (skills.size() < 1) {
            return str;
        }

        if( player.query("jingmai/finish") != null ) {
            //Todo:周天加成效果，后续再添加
            //str += ZHOUTIAN_D -> query_jingmai_effect("str");
        }
        str += Integer.parseInt(player.query("jingmai/str") == null ? "0" : player.query("jingmai/str").toString());
        str += Integer.parseInt(player.query("apply/str") == null ? "0" : player.query("apply/str").toString());

        improve += (skills.get("unarmed") == null ? 0 : skills.get("unarmed"))/30;
        improve += (skills.get("cuff") == null ? 0 : skills.get("cuff"))/30;
        improve += (skills.get("finger") == null ? 0 : skills.get("finger"))/30;
        improve += (skills.get("strike") == null ? 0 : skills.get("strike"))/30;
        improve += (skills.get("hand") == null ? 0 : skills.get("hand"))/30;
        improve += (skills.get("claw") == null ? 0 : skills.get("claw"))/30;
        improve += Integer.parseInt(player.queryTemp("suit_skill/unarmed") == null ? "0" : player.queryTemp("suit_skill/unarmed").toString()) / 30; //套装技能加成
        improve += Integer.parseInt(player.queryTemp("suit_skill/cuff") == null ? "0" : player.queryTemp("suit_skill/cuff").toString()) / 30;
        improve += Integer.parseInt(player.queryTemp("suit_skill/finger") == null ? "0" : player.queryTemp("suit_skill/finger").toString()) / 30;
        improve += Integer.parseInt(player.queryTemp("suit_skill/strike") == null ? "0" : player.queryTemp("suit_skill/strike").toString()) / 30;
        improve += Integer.parseInt(player.queryTemp("suit_skill/hand") == null ? "0" : player.queryTemp("suit_skill/hand").toString()) / 30;
        improve += Integer.parseInt(player.queryTemp("suit_skill/claw") == null ? "0" : player.queryTemp("suit_skill/claw").toString()) / 30;
        lx = (skills.get("longxiang") == null ? 0 : skills.get("claw"))/30;
        if (lx >= 13) lx = 15;

        Integer strength = Integer.parseInt(player.queryTemp("suit_eff/strength") == null ? "0" : player.queryTemp("suit_eff/strength").toString());
        Integer applyStr =Integer.parseInt(player.query("apply/str") == null ? "0" : player.query("apply/str").toString());
        return str + improve + lx + strength + applyStr;

    }


}
