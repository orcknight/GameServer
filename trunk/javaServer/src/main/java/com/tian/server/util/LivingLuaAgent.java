package com.tian.server.util;

import com.tian.server.model.Living;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Iterator;

/**
 * Created by PPX on 2017/7/4.
 */
public class LivingLuaAgent {

    public static void info(String msg){

        System.out.println(msg);
    }

    public static void setButtons(String jsonStr){

        JSONArray jsonArray = JSONArray.fromObject(jsonStr);
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = (String)jsonObject.get(key);

                System.out.println(key);
                System.out.println(value);
            }
            //String tile = jsonObject.getString("tile");
            //System.out.println(jsonObject.get("tile"));
            //System.out.println(jsonObject.get("name"));
        }
    }

    public static void setName(String uuid, String name){
       Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
       if(living != null){
           living.setName(name);
       }
    }

    public static void setNickname(String uuid, String nickname){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.setNickname(nickname);
        }
    }

    public static void setCmdName(String uuid, String cmdName){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null) {
            living.setCmdName(cmdName);
        }
    }

    public static void setGender(String uuid, String cmdName){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.setGender(cmdName);
        }
    }

    public static void setAge(String uuid, Integer age){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.setAge(age);
        }
    }

    public static void setLongDesc(String uuid, String longDesc){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.setLongDesc(longDesc);
        }
    }

    public static void setCombatExp(String uuid, Integer combatExp){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.setCombatExp(combatExp);
        }
    }

    public static void setAttitude(String uuid, String attitude){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.setAttitude(attitude);
        }
    }

    public static void setMaxNeili(String uuid, Integer maxNeili){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.setMaxNeili(maxNeili);
        }
    }

    public static void setEffNeili(String uuid, Integer effNeili){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.setEffNeili(effNeili);
        }
    }

    public static void setNeili(String uuid, Integer neili){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.setMaxNeili(neili);
        }
    }

    public static void setMaxQi(String uuid, Integer maxQi){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.setMaxNeili(maxQi);
        }
    }

    public static void setEffQi(String uuid, Integer effQi){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.setEffNeili(effQi);
        }
    }

    public static void setQi(String uuid, Integer qi){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.setQi(qi);
        }
    }

    public static void setMaxJing(String uuid, Integer maxJing){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.setMaxJing(maxJing);
        }
    }

    public static void setEffJing(String uuid, Integer effJing){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.setEffJing(effJing);
        }
    }

    public static void setJing(String uuid, Integer jing){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.setJing(jing);
        }
    }

    public static void setSkill(String uuid, String skillName, Integer level){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.setSkill(skillName, level);
        }
    }

    public static void setSkillMap(String uuid, String skillName1, String skillName2){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.mapSkill(skillName1, skillName2);
        }
    }

    public static void setSkillPrepare(String uuid, String baseSkill, String skillName){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.prepareSkill(baseSkill, skillName);
        }
    }

    public static void setup(String uuid){
        Living living  = (Living)UserCacheUtil.getAllObjects().get(Long.valueOf(uuid));
        if(living != null){
            living.setup();
        }
    }

}
