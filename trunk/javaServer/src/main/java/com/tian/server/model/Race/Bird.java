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
public class Bird extends Living implements Race {

    private static List<SkillAction> actions;
    private static List<String> limbs;

    public Bird(){

        this.weight = 50;

        actions = new ArrayList<SkillAction>();
        actions.add(createAction("$N用爪子往$n的$l猛地一抓", "抓伤", 10));
        actions.add(createAction("$N飞过来往$n的$l狠狠地一啄", "刺伤", 30));
        actions.add(createAction("$N用翅膀向$n的$l拍了过去", "刺伤", 30));
        limbs.add(0, "头部");
        limbs.add(1, "颈部");
        limbs.add(2, "后背");
        limbs.add(3, "腹部");
        limbs.add(4, "脚爪");
        limbs.add(5, "翅膀");
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
