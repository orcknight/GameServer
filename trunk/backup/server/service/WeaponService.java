package com.tian.server.service;

import com.tian.server.model.Goods;
import com.tian.server.model.Living;
import com.tian.server.model.MudObject;
import com.tian.server.model.SkillAction;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by PPX on 2017/8/16.
 */
public class WeaponService {

    private static Map<String, SkillAction> weaponActions = new HashMap<String, SkillAction>();

    static {

        SkillAction action = new SkillAction();
        action.setAction("$N挥动$w，斩向$n的$l");
        action.setDamageType("割伤");
        action.setParry(20);
        weaponActions.put("slash", action);

        action = new SkillAction();
        action.setAction("$N用$w往$n的$l砍去");
        action.setDamageType("劈伤");
        action.setDodge(20);
        weaponActions.put("slice", action);

        action = new SkillAction();
        action.setAction("$N的$w朝著$n的$l劈将过去");
        action.setDamageType("劈伤");
        action.setParry(-20);
        weaponActions.put("chop", action);

        action = new SkillAction();
        action.setAction("$N挥舞$w，对准$n的$l一阵乱砍");
        action.setDamageType("劈伤");
        action.setDamage(30);
        action.setDodge(30);
        weaponActions.put("hack", action);

        action = new SkillAction();
        action.setAction("$N用$w往$n的$l刺去");
        action.setDamageType("刺伤");
        action.setDodge(15);
        action.setParry(-15);
        weaponActions.put("thrust", action);

        action = new SkillAction();
        action.setAction("$N的$w往$n的$l狠狠地一捅");
        action.setDamageType("刺伤");
        action.setDodge(-30);
        action.setParry(-30);
        weaponActions.put("pierce", action);

        action = new SkillAction();
        action.setAction("$N将$w一扬，往$n的$l抽去");
        action.setDamageType("鞭伤");
        action.setDodge(-20);
        action.setParry(30);
        weaponActions.put("whip", action);

        action = new SkillAction();
        action.setAction("$N用$w往$n的$l直戳过去");
        action.setDamageType("刺伤");
        action.setDodge(-10);
        action.setParry(-10);
        weaponActions.put("impale", action);

        action = new SkillAction();
        action.setAction("$N用$w往$n的$l直戳过去");
        action.setDamageType("刺伤");
        action.setDodge(-10);
        action.setParry(-10);
        weaponActions.put("impale", action);

        action = new SkillAction();
        action.setAction("$N一个大舒臂抡起$w，对着$n的$l往下一砸");
        action.setDamageType("筑伤");
        action.setDodge(-10);
        action.setParry(-10);
        weaponActions.put("strike", action);

        action = new SkillAction();
        action.setAction("$N一个大舒臂抡起$w，对着$n的$l往下一砸");
        action.setDamageType("筑伤");
        action.setDodge(-10);
        action.setParry(-10);
        weaponActions.put("strike", action);

        action = new SkillAction();
        action.setAction("$N挥舞$w，往$n的$l用力一砸");
        action.setDamageType("挫伤");
        action.setPostAction("bashWeapon");
        weaponActions.put("bash", action);

        action = new SkillAction();
        action.setAction("$N高高举起$w，往$n的$l当头砸下");
        action.setDamageType("挫伤");
        action.setPostAction("bashWeapon");
        weaponActions.put("crush", action);

        action = new SkillAction();
        action.setAction("$N手握$w，眼露凶光，猛地对准$n的$l挥了过去");
        action.setDamageType("挫伤");
        action.setPostAction("bashWeapon");
        weaponActions.put("slam", action);

        action = new SkillAction();
        action.setAction("$N将$w对准$n的$l射了过去");
        action.setDamageType("刺伤");
        action.setPostAction("throwWeapon");
        weaponActions.put("throw", action);
    }


    public SkillAction queryAction(MudObject mudObject) {

        Object verbs = mudObject.query("verbs");
        if(verbs == null || !verbs.getClass().toString().equals("String")){
            return weaponActions.get("hit");
        }

        String[] verbsArray = verbs.toString().split(",");
        if(verbsArray == null) {
            return weaponActions.get("hit");
        }

        Random r = new Random();
        int randomIndex = r.nextInt(verbsArray.length - 1);
        String verb = verbsArray[randomIndex];
        if(weaponActions.get("verb") == null){
            return weaponActions.get("hit");
        }

        return weaponActions.get(verb);
    }

    /*public void throwWeapon(Living me, Living victim, Goods weapon, Integer damage) {

        if(weapon == null){
            return;
        }getOwnerPackage

        if (weapon.().getCount() == 1)
        {
            weapon->unequip();
            tell_object(me, "\n你的" + weapon->query("name") + "用完了！\n\n");
        }
        weapon->add_amount(-1);
    }

    public void bashWeapon(object me, object victim, object weapon, int damage)
    {
        object ob;
        int wap, wdp;

        if (objectp(weapon) &&
                damage == RESULT_PARRY &&
                ob = victim->query_temp("weapon"))
        {
            wap = (int)weapon->weight() / 500
                    + (int)weapon->query("rigidity")
                    + (int)me->query("str");
            wdp = (int)ob->weight() / 500
                    + (int)ob->query("rigidity")
                    + (int)victim->query("str");
            wap = random(wap);
            if( wap > 2 * wdp )
            {
                message_vision(HIW "$N只觉得手中" + ob->name() + "把持不定，脱手飞出！"NOR"\n",
                        victim);
                ob->unequip();
                ob->move(environment(victim));
                victim->reset_action();
            } else
            if (wap > wdp)
            {
                message_vision("$N只觉得手中" + ob->name() + "一震，险些脱手！\n",
                        victim);
            } else
            if (wap > wdp / 2 && ! ob->is_item_make())
            {
                message_vision(HIW "只听见「啪」地一声，$N手中的" + ob->name()
                        + "已经断为两截！"NOR"\n", victim );
                ob->unequip();
                ob->move(environment(victim));
                ob->set("name", "断掉的" + ob->query("name"));
                ob->set("value", 0);
                ob->set("weapon_prop", 0);
                victim->reset_action();
            } else
            {
                message_vision("$N的" + weapon->name() + "和$n的" + ob->name()
                        + "相击，冒出点点的火星。\n", me, victim);
            }
        }
    }*/

}
