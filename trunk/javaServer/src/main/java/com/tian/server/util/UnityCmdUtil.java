package com.tian.server.util;

import com.tian.server.model.Player;
import com.tian.server.model.PlayerLocation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.awt.*;


/**
 * Created by PPX on 2017/8/30.
 */
public class UnityCmdUtil {

    public static final String CHECK_VERSION_CODE = "001";
    public static final String CLEAR_SCREEN_CODE = "0002"; //清屏代码
    public static final String EMPTY_CODE = "0003"; //什么都不做的代码
    public static final String INFO_WINDOW_CODE = "015";
    public static final String CREATE_ROLE_CODE = "0000008";
    public static final String POP_WINDOW_CODE = "000100";
    public static final String ROLE_CREATED_CODE = "0000007";
    public static final String ROOM_SHORT_CODE = "002";
    public static final String ROOM_ROAD_CODE = "003";
    public static final String ROOM_LONG_CODE = "004";
    public static final String OBJECT_ENTER_CODE = "005";
    public static final String OBJECT_LEAVE_CODE = "905";
    public static final String OBJECT_CLEAR_CODE = "906";
    public static final String OBJECT_INFO_POP_CODE = "009";
    public static final String GAME_STORY_CODE = "0010";
    public static final String BAG_POP_CODE = "0011";

    public static JSONObject getCheckVersionRet(String msg) {
        return getCodeMsgRet(CHECK_VERSION_CODE, msg);
    }

    //清空屏幕
    public static JSONObject getClearScreenRet(){
        return getCodeMsgRet(CLEAR_SCREEN_CODE, "");
    }

    public static JSONObject getEmptyRet(){
        return getCodeMsgRet(EMPTY_CODE, "");
    }

    //创建角色字符串
    public static JSONObject getCreateRoleRet(String msg){
        return getCodeMsgRet(CREATE_ROLE_CODE, msg);
    }

    public static JSONObject getPopWindowRet(String msg){
        return getCodeMsgRet(POP_WINDOW_CODE, msg);
    }

    public static JSONObject getRoleCreatedRet(String msg){
        return getCodeMsgRet(ROLE_CREATED_CODE, msg);
    }

    public static JSONObject getInfoWindowRet(String msg) {
        return getCodeMsgRet(INFO_WINDOW_CODE, msg);
    }

    public static JSONObject getRoomShortRet(String msg){
        return getCodeMsgRet(ROOM_SHORT_CODE, msg);
    }

    public static JSONObject getRoomLongRet(String msg){
        return getCodeMsgRet(ROOM_LONG_CODE, msg);
    }

    public static JSONObject getRoomRoadRet(String msg){
        return getCodeMsgRet(ROOM_ROAD_CODE, msg);
    }

    public static JSONObject getObjectEnterRet(JSONArray msg) {
        return getCodeMsgRet(OBJECT_ENTER_CODE, msg);
    }

    public static JSONObject getObjectOutRet(JSONObject msg) {
        return getCodeMsgRet(OBJECT_LEAVE_CODE, msg);
    }

    public static JSONObject getObjectClearRet(JSONObject msg){
        return getCodeMsgRet(OBJECT_CLEAR_CODE, msg);
    }

    public static JSONObject getObjectInfoPopRet(JSONObject msg){
        return getCodeMsgRet(OBJECT_INFO_POP_CODE, msg);
    }

    public static JSONObject getGameStoryRet(JSONObject msg) {
        return getCodeMsgRet(GAME_STORY_CODE, msg);
    }

    public static JSONObject getBagPopRet(JSONObject msg) {
        return getCodeMsgRet(BAG_POP_CODE, msg);
    }

    public static JSONObject getCodeMsgRet(String code, Object msg) {

        if(msg instanceof  String){
            msg = ((String)msg).replaceAll("\n", "\\\\n");
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);

        return jsonObject;
    }

    public static JSONArray getPlayerStatus(Player player) {

        JSONArray jArray = new JSONArray();
        JSONObject outData = new JSONObject();

        outData.put("name", "玩家名");
        outData.put("value", player.getName());
        outData.put("max", 0);
        outData.put("eff", 0);
        outData.put("cur", 0);
        jArray.add(outData);

        outData = new JSONObject();
        outData.put("name", "气血");
        outData.put("value", "气血." + player.getQi().toString());
        outData.put("max", player.getMaxQi());
        outData.put("eff", player.getEffQi());
        outData.put("cur", player.getQi());
        outData.put("color", Color.red.getRGB());
        jArray.add(outData);

        outData = new JSONObject();
        outData.put("name", "内力");
        outData.put("value", "内力." + player.getNeili().toString());
        outData.put("max", player.getMaxNeili());
        outData.put("eff", player.getEffNeili());
        outData.put("cur", player.getNeili());
        outData.put("color", Color.blue.getRGB());
        jArray.add(outData);

        outData = new JSONObject();
        outData.put("name", "精神");
        outData.put("value", "精神." + player.getJing().toString());
        outData.put("max", player.getMaxJing());
        outData.put("eff", player.getEffJing());
        outData.put("cur", player.getJing());
        outData.put("color", new Color(0x00,0x80 , 0xFF, 255).getRGB());
        jArray.add(outData);

        outData = new JSONObject();
        outData.put("name", "精力");
        outData.put("value", "精力." + player.getJingLi().toString());
        outData.put("max", player.getMaxJingLi());
        outData.put("eff", player.getEffJingLi());
        outData.put("cur", player.getJingLi());
        outData.put("color", new Color(0x23,0x8E , 0x23, 255).getRGB());
        jArray.add(outData);

        outData = new JSONObject();
        outData.put("name", "等级");
        outData.put("value", "等级:" + player.getLevel().toString());
        outData.put("max", 0);
        outData.put("eff", 0);
        outData.put("cur", 0);
        jArray.add(outData);

        outData = new JSONObject();
        outData.put("name", "食物");
        outData.put("value", "食物." + player.getFood().toString());
        outData.put("max", player.getMaxFood());
        outData.put("eff", player.getMaxFood());
        outData.put("cur", player.getFood());
        outData.put("color", new Color(0xFF,0x7F , 0x00, 255).getRGB());
        jArray.add(outData);

        outData = new JSONObject();
        outData.put("name", "饮水");
        outData.put("value", "饮水." + player.getWater().toString());
        outData.put("max", player.getMaxWater());
        outData.put("eff", player.getMaxWater());
        outData.put("cur", player.getWater());
        outData.put("color", new Color(0x00,0x00 , 0xFF, 255).getRGB());
        jArray.add(outData);

        outData = new JSONObject();
        outData.put("name", "经验");
        outData.put("value", "经验." + player.getCombatExp().toString());
        outData.put("max", player.getCombatExp());
        outData.put("eff", player.getCombatExp());
        outData.put("cur", player.getCombatExp());
        outData.put("color", new Color(0xFF,0x1C , 0xAE, 255).getRGB());
        jArray.add(outData);

        return jArray;
    }

}
