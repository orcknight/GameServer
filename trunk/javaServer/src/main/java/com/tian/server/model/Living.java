package com.tian.server.model;

import java.util.Map;

/**
 * Created by PPX on 2017/7/19.
 */
public class Living {

    //生物属性
    protected String name; //名字
    protected String title; //称号
    protected String nickname; //昵称
    protected String surname; //姓
    protected String bornFamily; //出生世家
    protected String familyName; //门派名称
    protected String bunchName; //帮派名称
    protected String cmdName; //命令名、英文名
    protected Integer age; //年龄
    protected String gender; //性别
    protected String longDesc; //描述
    protected String classStr; //身份 官差 和尚尼姑喇嘛等
    protected String attitude; //态度 和平好战还是别的

    //基本属性
    protected Integer str; //膂力
    protected Integer wux; //悟性 //mud里用int由于与java关键字冲突改名
    protected Integer con; //根骨
    protected Integer dex; //身法
    protected Integer kar; //福缘
    protected Integer per; //容貌

    //气血等信息
    protected Integer maxQi;
    protected Integer effQi;
    protected Integer qi;
    protected Integer maxNeili;
    protected Integer effNeili;
    protected Integer neili;
    protected Integer maxJing;
    protected Integer effJing;
    protected Integer jing;
    protected Integer level;
    protected Integer combatExp; //实战经验
    protected Integer score; //功劳点

    protected Map<String, Integer> skills; //存放的是 技能名：等级
    protected Map<String, Integer> learned; //存放的是玩家已经学习过的技能 技能名：等级
    protected Map<String, String> skillMap; //存放的连招 技能名：技能名
    protected Map<String, String> skillPrepare; //为基本武功设置激发武功 基本技能名字：技能名 如： prepare_skill("strike", "dragon-strike");

    protected void heartBeat() {}

}
