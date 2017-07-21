package com.tian.server.model.Race;

import com.tian.server.common.Race;
import com.tian.server.model.Living;
import com.tian.server.model.SkillAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by PPX on 2017/7/20.
 */
public class FlyInsect extends Living implements Race {

    private static List<SkillAction> actions = new ArrayList<SkillAction>();
    private static List<String> limbs = new ArrayList<String>();

    public FlyInsect() {

        this.weight = 1;

        actions.add(createAction("$N飞过来张嘴往$n的$l狠狠地一咬", "咬伤", 30));
        actions.add(createAction("$N飞上来猛地往$n的$l叮了一下", "刺伤", 10));
        actions.add(createAction("$N反转身用尾巴尖对准$n的$l一刺", "刺伤", 20));
        limbs.add(0, "头部");
        limbs.add(1, "背部");
        limbs.add(2, "腹部");
        limbs.add(3, "翅膀");
        limbs.add(4, "前腿");
        limbs.add(5, "后腿");
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
