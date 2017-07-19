package com.tian.server.model.Race;

import com.tian.server.common.Race;
import com.tian.server.model.SkillAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PPX on 2017/7/19.
 */
public class Fish implements Race {

    public static final Integer BASE_WEIGHT = 40000;
    private static List<SkillAction> actions;

    public Fish(){

        actions = new ArrayList<SkillAction>();
        actions.add(createAction("$N扑上来张嘴往$n的$l狠狠地一咬", "咬伤", 50));
        actions.add(createAction("$N举起爪子往$n的$l抓了过去", "抓伤", 30));
        actions.add(createAction("$N跃起来用前掌往$n的$l猛地一拍", "瘀伤", 30));
        actions.add(createAction("$N扑上来张嘴往$n的$l狠狠地一咬", "咬伤", 50));
    }


    public SkillAction queryAction() {
        return null;
    }

    public SkillAction createAction(String action, String damageType, Integer damage){

        SkillAction skillAction = new SkillAction(action, damageType);
        skillAction.setDamage(damage);

        return skillAction;
    }
}
