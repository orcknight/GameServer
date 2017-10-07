package com.tian.server.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PPX on 2017/8/11.
 */
public class ChineseUtil {


    private static String[] c_digit = { "零","十","百","千","万","亿","兆" };
    private static String[] c_num = {"零","一","二","三","四","五","六","七","八","九","十"};
    private static String[] sym_tian = { "甲","乙","丙","丁","戊","己","庚","辛","壬","癸" };
    private static String[] sym_di = { "子","丑","寅","卯","辰","巳","午","未","申","酉","戌","亥" };
    private static Map<String, String> dict = new HashMap<String, String>();

    public static String toChinese(String str) {
        if (dict.get(str) != null)
            return dict.get(str);
        else
            return str;
    }

    public static String chinese_number(Integer i) {
        if (i < 0){
            return "负" + chinese_number(-i);
        }
        if (i < 11){
            return c_num[i];
        }
        if (i < 20) {
            return c_digit[1] + c_num[i - 10];
        }
        if (i < 100) {
            if (i % 10 != 0) {
                return c_num[i / 10] + c_digit[1] + c_num[i % 10];
            } else {
                return c_num[i / 10] + c_digit[1];
            }
        }
        if (i < 1000) {
            if (i % 100 == 0) {
                return c_num[i / 100] + c_digit[2];
            } else if (i % 100 < 10) {
                return c_num[i / 100] + c_digit[2] +
                        c_num[0] + chinese_number(i % 100);
            } else if (i % 100 < 10) {
                return c_num[i / 100] + c_digit[2] +
                        c_num[1] + chinese_number(i % 100);
            } else {
                return c_num[i / 100] + c_digit[2] +
                        chinese_number(i % 100);
            }
        }
        if (i < 10000) {
            if (i % 1000 == 0) {
                return c_num[i / 1000] + c_digit[3];
            } else if (i % 1000 < 100) {
                return c_num[i / 1000] + c_digit[3] +
                        c_num[0] + chinese_number(i % 1000);
            } else {
                return c_num[i / 1000] + c_digit[3] +
                        chinese_number(i % 1000);
            }
        }
        if (i < 100000000) {
            if (i % 10000 == 0) {
                return chinese_number(i / 10000) + c_digit[4];
            } else if (i % 10000 < 1000) {
                return chinese_number(i / 10000) + c_digit[4] +
                        c_num[0] + chinese_number(i % 10000);
            } else {
                return chinese_number(i / 10000) + c_digit[4] +
                        chinese_number(i % 10000);
            }
        }
        if (i < 1000000000000L) {
            if (i % 100000000 == 0) {
                return chinese_number(i / 100000000) + c_digit[5];
            } else if (i % 100000000 < 1000000) {
                return chinese_number(i / 100000000) + c_digit[5] +
                        c_num[0] + chinese_number(i % 100000000);
            } else {
                return chinese_number(i / 100000000) + c_digit[5] +
                        chinese_number(i % 100000000);
            }
        }
        if (i % 1000000000000L == 0) {
            Integer temp = ((Long)(i / 1000000000000L)).intValue();
            return chinese_number(temp) + c_digit[6];
        } else if (i % 1000000000000L < 100000000) {
            Integer temp = ((Long)(i / 1000000000000L)).intValue();
            return chinese_number(temp) + c_digit[6] +
                    c_num[0] + chinese_number(temp);
        } else {
            Integer temp = ((Long)(i / 1000000000000L)).intValue();
            return chinese_number(temp) + c_digit[6] +
                    chinese_number(temp);
        }
    }
}
