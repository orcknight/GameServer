package com.tian.server.model.Race;

import com.tian.server.common.Race;
import com.tian.server.model.SkillAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PPX on 2017/7/11.
 */
public class Beast implements Race {

    public static final Integer BASE_WEIGHT = 30000;
    List<SkillAction> actions;

    public Beast(){

        actions = new ArrayList<SkillAction>();
        actions.add(new SkillAction("$N扑上来张嘴往$n的$l狠狠地一咬", "瘀伤"));
        actions.add(new SkillAction("$N往$n的$l一抓", "抓伤"));
        actions.add(new SkillAction("$N往$n的$l狠狠地踢了一脚", "瘀伤"));
        actions.add(new SkillAction("$N提起拳头往$n的$l捶去", "瘀伤"));
        actions.add(new SkillAction("$N对准$n的$l用力挥出一拳", "瘀伤"));
    }


    public SkillAction queryAction() {


        return null;
    }
}
