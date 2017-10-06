package com.tian.server.util;

/**
 * Created by PPX on 2017/10/6.
 */
public class GenderUtil {

    public static String genderSelf(String sex) {

        if(sex.equals("女性")){
            return "你";
        }else{
            return "你";
        }
    }

    public static String genderPronoun(String sex) {

        if(sex.equals("中性神")){
            return "他";
        }else if(sex.equals("男性")){
            return "他";
        }else if(sex.equals("无性")){
            return "他";
        }else if(sex.equals("女性")){
            return "她";
        } else if(sex.equals("雄性") || sex.equals("雌性")){
            return "它";
        }else{
            return "它";
        }
    }
}
