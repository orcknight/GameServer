package org.luaj.vm2.lib.jse;

/**
 * Created by PPX on 2017/7/27.
 */
public class Helper {

    public static JavaClass forClass(Class c) {
        return JavaClass.forClass(c);
    }

    public Class<JavaClass> huskClass() {
        return JavaClass.class;
    }
}
