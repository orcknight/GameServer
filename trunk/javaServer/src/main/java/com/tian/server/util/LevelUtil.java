package com.tian.server.util;

/**
 * Created by PPX on 2017/9/13.
 */
public class LevelUtil {

    public static Integer levelMaxExp(Integer lvl) {
        Integer maxExp = 0;
        if (lvl < 5)
            maxExp = lvl * 10 + 10000;
        if (lvl < 10 && lvl > 4)
            maxExp = lvl * 15 + 200000;
        if (lvl < 20 && lvl > 9)
            maxExp = lvl * 20 + 500000;
        if (lvl < 30 && lvl > 19)
            maxExp = lvl * 30 + 1000000;
        if (lvl < 40 && lvl > 29)
            maxExp = lvl * 40 + 2000000;
        if (lvl < 50 && lvl > 39)
            maxExp = lvl * 50 + 3000000;
        if (lvl < 60 && lvl > 49)
            maxExp = lvl * 100 + 5000000;
        if (lvl < 70 && lvl > 59)
            maxExp = lvl * 150 + 8000000;
        if (lvl < 80 && lvl > 69)
            maxExp = lvl * 200 + 9000000;
        if (lvl < 90 && lvl > 79)
            maxExp = lvl * 300 + 10000000;
        if (lvl < 100 && lvl > 89)
            maxExp = lvl * 400 + 15000000;
        if (lvl < 200 && lvl > 99)
            maxExp = lvl * 500000 + 50000000;
        if (lvl < 2000 && lvl > 199)
            maxExp = lvl * 600000 + 1000000000;
        return maxExp;
    }

}