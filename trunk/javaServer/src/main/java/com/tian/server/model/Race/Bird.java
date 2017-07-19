package com.tian.server.model.Race;

import com.tian.server.common.Race;
import com.tian.server.model.SkillAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by PPX on 2017/7/19.
 */
public class Bird implements Race {

    public static final Integer BASE_WEIGHT = 50;
    private static List<SkillAction> actions;

    public Bird(){

        actions = new ArrayList<SkillAction>();
        actions.add(createAction("$N用爪子往$n的$l猛地一抓", "抓伤", 10));
        actions.add(createAction("$N飞过来往$n的$l狠狠地一啄", "刺伤", 30));
        actions.add(createAction("$N用翅膀向$n的$l拍了过去", "刺伤", 30));
    }

    public SkillAction queryAction() {

        Random r = new Random();
        int randomIndex = r.nextInt(actions.size()-1);
        return actions.get(randomIndex);
    }

    public SkillAction createAction(String action, String damageType, Integer damage){

        SkillAction skillAction = new SkillAction(action, damageType);
        skillAction.setDamage(damage);

        return skillAction;
    }
}
