package com.tian.server.util;

/**
 * Created by PPX on 2017/6/26.
 */
public class IdUtil {

    private static CustomUUID customUUID = null;
    public static CustomUUID getInstance(){

        if(customUUID == null){

            customUUID = new CustomUUID(1, 1);
        }

        return customUUID;
    }

    public static long getUUID(){

        CustomUUID uuid = getInstance();
        return uuid.generate();
    }

}
