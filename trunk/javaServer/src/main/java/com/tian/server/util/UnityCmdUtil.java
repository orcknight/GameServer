package com.tian.server.util;

import com.tian.server.model.Player;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.awt.*;


/**
 * Created by PPX on 2017/8/30.
 */
public class UnityCmdUtil {

    public static final String CHECK_VERSION_CODE = "001";


    public static JSONObject getCheckVersionRet(String msg){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", CHECK_VERSION_CODE);
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
