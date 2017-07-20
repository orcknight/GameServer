package com.tian.server.model.Race;

import com.tian.server.common.Race;
import com.tian.server.model.Living;
import com.tian.server.model.SkillAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by PPX on 2017/7/19.
 */
public class Fish extends Living implements Race {

    private static List<SkillAction> actions;
    private static List<String> limbs;

    public Fish(){

        this.weight = 40000;

        actions = new ArrayList<SkillAction>();
        actions.add(createAction("$N扑上来张嘴往$n的$l狠狠地一咬", "咬伤", 50));
        actions.add(createAction("$N举起爪子往$n的$l抓了过去", "抓伤", 30));
        actions.add(createAction("$N跃起来用前掌往$n的$l猛地一拍", "瘀伤", 30));
        actions.add(createAction("$N扑上来张嘴往$n的$l狠狠地一咬", "咬伤", 50));
        limbs.add(0, "头部");
        limbs.add(1, "腮部");
        limbs.add(2, "背部");
        limbs.add(3, "腹部");
        limbs.add(4, "前鳍");
        limbs.add(5, "后鳍");
        limbs.add(6, "尾巴");
    }

    @Override
    public SkillAction queryAction() {

        if(actions.size() < 1){

            return null;
        }

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
