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
public class Snake extends Living implements Race {

    private static List<SkillAction> actions = new ArrayList<SkillAction>();
    private static List<String> limbs = new ArrayList<String>();

    public Snake() {

        this.weight = 1;

        actions.add(createAction("$N爬上来张嘴往$n的$l狠狠地一咬", "咬伤", 30));
        actions.add(createAction("$N支起身猛地往$n的$l卷了上来", "瘀伤", 30));
        limbs.add(0, "头部");
        limbs.add(1, "身体");
        limbs.add(2, "七寸");
        limbs.add(3, "尾巴");
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
