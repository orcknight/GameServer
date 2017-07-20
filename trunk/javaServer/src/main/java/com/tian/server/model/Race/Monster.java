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
public class Monster extends Living implements Race {

    private static List<SkillAction> actions;
    private static List<String> limbs;

    public Monster() {

        this.weight = 10000;

        actions = new ArrayList<SkillAction>();
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
