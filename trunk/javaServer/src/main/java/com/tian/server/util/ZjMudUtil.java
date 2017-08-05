package com.tian.server.util;

/**
 * Created by PPX on 2017/8/5.
 */
public class ZjMudUtil {

    public static final String ZJ_KEY = "123456789abcd";
    public static final String ZJ_PAY_PORT = "3001";

    public static final String ESA = "\u001B";
    public static final String ZJ_SEP = "$zj#";
    public static final String ZJ_SP2 = "$z2#";
    public static final String ZJ_JBR = "$br#";

    public static String getZjUrl(String w){
        return ESA + "[u:" + w + "]";
    }

    public static String getZjSize(String n){
        return ESA + "[s:" + n + "]";
    }

    public static String getSySy(){
        return ESA + "000";
    }

    public static String getInputTxt(String a, String b){

        return ESA + "001" + a + ZJ_SEP + b;
    }

}
