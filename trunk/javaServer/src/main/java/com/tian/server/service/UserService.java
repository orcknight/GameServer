package com.tian.server.service;

import com.tian.server.common.Ansi;
import com.tian.server.model.Player;
import com.tian.server.util.LevelUtil;
import com.tian.server.util.MapGetUtil;
import com.tian.server.util.UnityCmdUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by PPX on 2017/10/27.
 */
public class UserService extends  BaseService {

    public JSONArray getPlayerAttribute(Player me){

        JSONObject attrObject = new JSONObject();
        JSONArray buttonArray = new JSONArray();
        StringBuffer sb = new StringBuffer();

        sb.append(String.format("姓名：%s (" + Ansi.HIG + "%s" + Ansi.NOR + ")  等级：%d\n",
                me.getName(), me.getCmdName(), me.getLevel()));
        sb.append("────────────────────────\n");
        sb.append(String.format(Ansi.HIR + "〖气血〗：" + Ansi.NOR + "%s%s\n",
                getCurrentEffStr(me.getQi(), me.getEffQi()), getEffMaxPercentStr(me.getEffQi(), me.getMaxQi())));
        sb.append(String.format(Ansi.HIR + "〖内力〗：" + Ansi.NOR + "%s\n",
                getCurrentMaxStr(me.getNeili(), me.getMaxNeili(), MapGetUtil.queryInteger(me, "jiali"))));
        sb.append(String.format(Ansi.HIR + "〖精神〗：" + Ansi.NOR + "%s%s\n",
                getCurrentEffStr(me.getJing(), me.getEffJing()), getEffMaxPercentStr(me.getEffJing(), me.getMaxJing())));
        sb.append(String.format(Ansi.HIR + "〖精力〗：" + Ansi.NOR + "%s\n",
                getCurrentMaxStr(me.getJingLi(), me.getMaxJingLi(), MapGetUtil.queryInteger(me, "jiajing"))));
        sb.append(String.format(Ansi.CYN + "〖食物〗：" + Ansi.NOR + "%s\n", getFoodWaterStr(me.getFood(), me.getMaxFood())));
        sb.append(String.format(Ansi.CYN + "〖饮水〗：" + Ansi.NOR + "%s\n", getFoodWaterStr(me.getWater(), me.getMaxWater())));
        sb.append(String.format(Ansi.CYN + "〖潜能〗：" + Ansi.NOR + "%s(max)\n",
                getCurrentEffStr(me.getPotential() - MapGetUtil.queryTempInteger(me, "learned_points"), me.queryPotentialLimit() - MapGetUtil.queryTempInteger(me, "learned_points"))));
        sb.append(String.format(Ansi.HIC + "〖经验〗：" + Ansi.NOR + Ansi.HIM + "%07d/%07d(up)\n" + Ansi.NOR, me.getCombatExp(), LevelUtil.levelMaxExp(me.getLevel())));
        sb.append("────────────────────────\n");

        attrObject.put("desc", sb.toString());
        attrObject.put("buttons", buttonArray);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(UnityCmdUtil.getObjectInfoPopRet(attrObject));
        return jsonArray;
    }

    private String getFoodWaterStr(int current, int max){

        String colorStr = statusColor(current, max);
        int percent = 100;
        if(max > 0) {
            percent = current * 100 / max;
        }
        String retStr = String.format(colorStr + "%d/%d[%d%%]" + Ansi.NOR, current, max, percent);
        return retStr;
    }

    private String getCurrentEffStr(int current, int eff){
        String colorStr = statusColor(current, eff);
        String retStr = String.format(colorStr + "%d/%d" + Ansi.NOR, current, eff);
        return retStr;
    }

    private String getEffMaxPercentStr(int eff, int max){
        String colorStr = statusColor(eff, max);
        int percent = 100;
        if(max > 0) {
            percent = eff * 100 / max;
        }
        String retStr = colorStr + "(" + percent + "%%)" + Ansi.NOR;
        return retStr;
    }

    private String getCurrentMaxStr(int current, int max, int addn){
        String colorStr = statusColor(current, max);
        String retStr = String.format(colorStr + "%d/%d(+%d)" + Ansi.NOR, current, max, addn);
        return retStr;
    }

    private String statusColor(int current, int max) {
        int percent;
        if( max>0 ) percent = current * 100 / max;
        else percent = 100;
        if( percent > 100 ) return Ansi.HIC;
        if( percent >= 90 ) return  Ansi.HIG;
        if( percent >= 60 ) return Ansi.HIY;
        if( percent >= 30 ) return Ansi.YEL;
        if( percent >= 10 ) return Ansi.HIR;
        return Ansi.RED;
    }
}
