package com.tian.server.model.Race;

import com.tian.server.common.Race;
import com.tian.server.model.Living;
import com.tian.server.model.SkillAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by PPX on 2017/7/11.
 */
public class Beast extends Living implements Race {

    private static List<SkillAction> actions;
    private static List<String> limbs;

    public Beast(){

        this.weight = 30000;

        actions = new ArrayList<SkillAction>();
        actions.add(new SkillAction("$N扑上来张嘴往$n的$l狠狠地一咬", "瘀伤"));
        actions.add(new SkillAction("$N往$n的$l一抓", "抓伤"));
        actions.add(new SkillAction("$N往$n的$l狠狠地踢了一脚", "瘀伤"));
        actions.add(new SkillAction("$N提起拳头往$n的$l捶去", "瘀伤"));
        actions.add(new SkillAction("$N对准$n的$l用力挥出一拳", "瘀伤"));
        limbs.add(0, "头部");
        limbs.add(1, "颈部");
        limbs.add(2, "胸部");
        limbs.add(3, "后背");
        limbs.add(4, "腹部");
        limbs.add(5, "前腿");
        limbs.add(6, "后腿");
        limbs.add(7, "前爪");
        limbs.add(8, "后爪");
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
}
