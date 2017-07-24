package com.tian.server.util;

/**
 * Created by PPX on 2017/7/24.
 */
public class StringUtil {


    public static String rtrim(String str) {

        int num=str.length();
        for (int i =  num- 1; i > -1; i--) {
            if (!(str.substring(i,i+1).equals(" "))) {

                return str.substring(0, i+1);
            }
        }
        return str;
    }
}
