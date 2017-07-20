package com.tian.server.model.Race;

import com.tian.server.common.Race;
import com.tian.server.model.Living;
import com.tian.server.model.SkillAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by PPX on 2017/7/11.
 */
public class Human extends Living implements Race {

    private static List<SkillAction> actions;
    private static List<String> limbs;

    public Human(){

        this.weight = 40000;
        actions = new ArrayList<SkillAction>();
        actions.add(createAction("$N扑上来张嘴往$n的$l狠狠地一咬", "咬伤", 50));
        actions.add(createAction("$N举起爪子往$n的$l抓了过去", "抓伤", 30));
        actions.add(createAction("$N跃起来用前掌往$n的$l猛地一拍", "瘀伤", 30));
        actions.add(createAction("$N扑上来张嘴往$n的$l狠狠地一咬", "咬伤", 50));
        limbs.add(0, "头部");
        limbs.add(1, "颈部");
        limbs.add(2, "胸口");
        limbs.add(3, "后心");
        limbs.add(4, "左肩");
        limbs.add(5, "右肩");
        limbs.add(6, "左臂");
        limbs.add(7, "右臂");
        limbs.add(8, "左手");
        limbs.add(9, "右手");
        limbs.add(10, "两肋");
        limbs.add(11, "左脸");
        limbs.add(12, "腰间");
        limbs.add(13, "小腹");
        limbs.add(14, "左腿");
        limbs.add(15, "右腿");
        limbs.add(16, "右脸");
        limbs.add(17, "左脚");
        limbs.add(18, "右脚");
        limbs.add(19, "左脸");
        limbs.add(20, "左耳");
        limbs.add(21, "右耳");
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
