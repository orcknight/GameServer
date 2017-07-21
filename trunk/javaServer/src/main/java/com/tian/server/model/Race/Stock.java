package com.tian.server.model.Race;

import com.tian.server.common.Race;
import com.tian.server.model.Living;
import com.tian.server.model.SkillAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by PPX on 2017/7/20.
 */
public class Stock extends Living implements Race {

    private static List<SkillAction>  actions = new ArrayList<SkillAction>();
    private static List<String> limbs = new ArrayList<String>();

    public Stock() {

        this.weight = 30000;

        actions.add(createAction("$N用后腿往$n的$l用力一蹬", "瘀伤", 30));
        actions.add(createAction("$N低下头往$n的$l猛地一撞", "瘀伤", 30));
        actions.add(createAction("$N抬起前腿往$n的$l狠狠地一踢", "瘀伤", 20));
        limbs.add(0, "头部");
        limbs.add(1, "颈部");
        limbs.add(2, "胸部");
        limbs.add(3, "后背");
        limbs.add(4, "腹部");
        limbs.add(5, "前腿");
        limbs.add(6, "后腿");
        limbs.add(7, "前蹄");
        limbs.add(8, "后蹄");
        limbs.add(9, "尾巴");
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
