package com.tian.server.util;

import com.tian.server.common.Ansi;

import java.lang.reflect.Field;

/**
 * Created by PPX on 2017/10/23.
 */
public class AnsiLuaAgent {

    public static String getAnsiByName(String key) {
        try {
            Class clazz = Class.forName("com.tian.server.common.Ansi");
            Field field = clazz.getField(key.toUpperCase());
            return field.get(clazz).toString();
        } catch (Exception e) {
            return "";
        }
    }

}
