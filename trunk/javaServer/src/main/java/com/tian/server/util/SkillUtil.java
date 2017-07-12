package com.tian.server.util;

import com.tian.server.entity.PlayerSkillEntity;
import com.tian.server.entity.SkillEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/7/12.
 */
public class SkillUtil {

    private static Map<String, SkillEntity> allSkills; //存放的是技能的输出参数等 ({ attack, dodge, parry, damage, force, difficult, rank, attribute });
    //如：#技能/攻击/躲闪/招架/伤害/内功/难度/级别/属性 #终极前四项COMBAT之和1100
    //yinyang-shiertian/600/600/600/600/600/6000/ultimate/public


    public static Map<String, SkillEntity> getAllSkills(){

        if(allSkills == null){

            allSkills = new HashMap<String, SkillEntity>();
        }
        return allSkills;
    }

    public static void initAllSkills(List<SkillEntity> skillList){

        Map<String, SkillEntity> refAllSkills = getAllSkills();
        for(SkillEntity skill : skillList){

            refAllSkills.put(skill.getName(), skill);
        }
    }

 }
