package com.tian.server.util;

import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.Helper;

/**
 * Created by PPX on 2017/7/27.
 */
public class LuaBridge {

    public Varargs getClass(String clazzName) {
        try {
            Class clazz = Class.forName(clazzName);
            System.out.println(clazzName);

            Varargs retClass = Helper.forClass(clazz);
            if(retClass == null){

                System.out.println("get class fail");
            }
            return retClass;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
