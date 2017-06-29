package com.tian.server.util;

/**
 * Created by PPX on 2017/6/26.
 */
public class IdUtil {

    private static byte[] idPool = new byte[3000];

    public static int getUnUsedId(){

        idPool[0] =1;
        for(int index = 0; index < 3000; index++){

            if(idPool[index] == 0){

                return index;
            }
        }

        return 0;
    }

    public static void releaseId(int index){

        idPool[index] = 0;
    }


}
