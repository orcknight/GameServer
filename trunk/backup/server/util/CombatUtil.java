package com.tian.server.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PPX on 2017/7/21.
 */
public class CombatUtil {

    private static Map<String, Integer> limbDamage = new HashMap<String, Integer>();
    private static String[] dangerLimbs = new String[] {"头部", "颈部", "胸口", "后心", "小腹",};

    static {
        // 人类身体部位
        limbDamage.put("头部", 100);
        limbDamage.put("颈部", 90);
        limbDamage.put("胸口", 90);
        limbDamage.put("后心", 80);
        limbDamage.put("左肩", 50);
        limbDamage.put("右肩", 55);
        limbDamage.put("左臂", 40);
        limbDamage.put("右臂", 45);
        limbDamage.put("左手", 20);
        limbDamage.put("右手", 30);
        limbDamage.put("腰间", 60);
        limbDamage.put("小腹", 70);
        limbDamage.put("左腿", 40);
        limbDamage.put("右腿", 50);
        limbDamage.put("左脚", 35);
        limbDamage.put("右脚", 40);

        // 动物身体部位
        limbDamage.put("身体", 70);
        limbDamage.put("前脚", 40);
        limbDamage.put("后脚", 50);
        limbDamage.put("左脚", 35);
        limbDamage.put("腿部", 40);
        limbDamage.put("尾巴", 40);
        limbDamage.put("翅膀", 40);
        limbDamage.put("爪子", 40);
    }

    public static Integer getDamageWithLimb(String limb){

        return limbDamage.get(limb);
    }

    public static Boolean isDangerLimb(String limb){

        return Arrays.asList(dangerLimbs).contains(limb);
    }

}
