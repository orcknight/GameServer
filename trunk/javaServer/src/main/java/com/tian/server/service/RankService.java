package com.tian.server.service;

import com.tian.server.common.Ansi;
import com.tian.server.model.Living;
import com.tian.server.model.Player;

/**
 * Created by PPX on 2017/9/27.
 */
public class RankService {


    public String queryRank(Living ob)
    {

        //Todo:暂时不处理巫师
        /*if(wizardp(ob)&&ob->query("env/own_rank"))
            return NOR"【"HIW+ob->query("env/own_rank")+NOR" 】";*/
        if( ob.getGhost() ) {
            return "【" + Ansi.HIB  + "孤魂野鬼" + Ansi.NOR + "】";
        }

        int budd;
        if(ob.getFamilyName() != null && ob.getFamilyName().equals("少林派")){
            budd = ob.querySkill("buddhism", 1);
        }

        if(ob.query("fight_rank") != null && Integer.parseInt(ob.query("fight_rank").toString()) == 1) {
            if(ob.query("env/my_title") != null && ob.query("env/my_title").toString().length() == 2){
                String set_title = ob.query("env/my_title").toString();
                return "【" + Ansi.HIW + "天下第一" + set_title + Ansi.NOR + " 】";
            }else{
                return "【" + Ansi.HIW + "天下第一" + Ansi.NOR + "】";
            }
        }

        if(ob.query("fight_rank") != null && Integer.parseInt(ob.query("fight_rank").toString()) == 2)
        {
            if(ob.query("env/my_title") != null && ob.query("env/my_title").toString().length() == 2){
                String set_title = ob.query("env/my_title").toString();
                return "【" + Ansi.HIW + "天下第二" + set_title + Ansi.NOR + " 】";
            }else{
                return "【" + Ansi.HIW + "天下第二" + Ansi.NOR + "】";
            }
        }

        if(ob.query("fight_rank") != null && Integer.parseInt(ob.query("fight_rank").toString()) == 3)
        {
            if(ob.query("env/my_title") != null && ob.query("env/my_title").toString().length() == 2){
                String set_title = ob.query("env/my_title").toString();
                return "【" + Ansi.HIW + "天下第三" + set_title + Ansi.NOR + " 】";
            }else{
                return "【" + Ansi.HIW + "天下第三" + Ansi.NOR + "】";
            }
        }

        if(ob.getClassStr().equals("女性")){
            return getFemaleRank(ob);
        }else{
            return getMaleRank(ob);
        }
    }

    String query_respect(Living ob)
    {
        if(ob.query("rank_info/respect") != null){
            return ob.query("rank_info/respect").toString();
        }

        Integer age = ob.getAge();
        switch(ob->query("gender")) {
            case "女性":
                switch(ob->query("class")) {
                    case "bonze":
                        if( age < 18 ) return "小师太";
                        else return "师太";
                        break;
                    case "taoist":
                        if( age < 18 ) return "小仙姑";
                        else return "仙姑";
                        break;
                    default:
                        if( age < 18 ) return "小姑娘";
                        else if( age < 50 ) return "姑娘";
                        else return "婆婆";
                        break;
                }
            case "男性":
            default:
                switch(ob->query("class")) {
                    case "bonze":
                        if( age < 18 ) return "小师父";
                        else return "大师";
                        break;
                    case "taoist":
                        if( age < 18 ) return "道兄";
                        else return "道长";
                        break;
                    case "fighter":
                    case "swordsman":
                        if( age < 18 ) return "小老弟";
                        else if( age < 50 ) return "壮士";
                        else return "老前辈";
                        break;
                    default:
                        if( age < 20 ) return "小兄弟";
                        else if( age < 50 ) return "壮士";
                        else return "老爷子";
                        break;
                }
        }
    }

    string query_rude(object ob)
    {
        int age;
        string str;

        if( stringp(str = ob->query("rank_info/rude")) )
            return str;

        age = ob->query("age");
        switch(ob->query("gender")) {
            case "女性":
                switch(ob->query("class")) {
                    case "bonze":
                        return "贼尼";
                    break;
                    case "taoist":
                        return "妖女";
                    break;
                    default:
                        if( age < 30 ) return "小贱人";
                        else return "死老太婆";
                        break;
                }
            case "男性":
            default:
                switch(ob->query("class")) {
                    case "bonze":
                        if( age < 50 ) return "死秃驴";
                        else return "老秃驴";
                        break;
                    case "taoist":
                        return "死牛鼻子";
                    break;
                    default:
                        if( age < 20 ) return "小王八蛋";
                        if( age < 50 ) return "臭贼";
                        else return "老匹夫";
                        break;
                }
        }
    }

    string query_self(object ob)
    {
        int age;
        string str;

        if( stringp(str = ob->query("rank_info/self")) )
            return str;

        age = ob->query("age");
        switch(ob->query("gender")) {
            case "女性":
                switch(ob->query("class")) {
                    case "bonze":
                        if( age < 50 ) return "贫尼";
                        else return "老尼";
                        break;
                    default:
                        if( age < 30 ) return "小女子";
                        else return "妾身";
                        break;
                }
            case "男性":
            default:
                switch(ob->query("class")) {
                    case "bonze":
                        if( age < 50 ) return "贫僧";
                        else return "老纳";
                        break;
                    case "taoist":
                        return "贫道";
                    break;
                    default:
                        if( age < 50 ) return "在下";
                        else return "老头子";
                        break;
                }
        }
    }

    string query_self_rude(object ob)
    {
        int age;
        string str;

        if( stringp(str = ob->query("rank_info/self_rude")) )
            return str;

        age = ob->query("age");
        switch(ob->query("gender")) {
            case "女性":
                switch(ob->query("class")) {
                    case "bonze":
                        if( age < 50 ) return "贫尼";
                        else return "老尼";
                        break;
                    default:
                        if( age < 30 ) return "本姑娘";
                        else return "老娘";
                        break;
                }
            case "男性":
            default:
                switch(ob->query("class")) {
                    case "bonze":
                        if( age < 50 ) return "大和尚我";
                        else return "老和尚我";
                        break;
                    case "taoist":
                        return "本山人";
                    break;
                    default:
                        if( age < 50 ) return "大爷我";
                        else return "老子";
                        break;
                }
        }
    }

    string query_close(object ob, int age, string rgender)
    {
        int a1, a2;
        string gender;
        if (objectp(ob) )       {
            if( !age )
                a1 = this_player()->query("age");
		else
            a1 = ob->query("age");
            if( !age)
                a2 = ob->query("age");
            else    a2 = age;
        }

        if( !rgender )
            gender = ob->query("gender");
        else    gender = rgender;

        switch ( gender ) {
            case "女性" :
                if (a1 > a2)
                    return "妹妹";
                else
                    return "姐姐";
                break;
            default :
                if (a1 > a2)
                    return "弟弟";
                else
                    return "哥哥";
        }
    }

    string query_self_close(object ob, int age)
    {
        int a1, a2;
        string gender;
        if( objectp(ob) )
        {
            if( !age )
                a1 = this_player()->query("age");
		else
            a1 = ob->query("age");
            if( !age)
                a2 = ob->query("age");
            else    a2 = age;
        }

        if( age )
            gender = ob->query("gender");
        else
            gender = this_player()->query("gender");

        switch (gender)
        {

            case "女性" :
                if (a1 > a2)
                    return "姐姐我";
                else
                    return "小妹我";
                break;
            default :
                if (a1 > a2)
                    return "愚兄我";
                else
                    return "小弟我";
        }
    }
    varargs string new_short( object ob ,int withshort)
    {
        mixed tmp;
        string icon="", str;
        if(!objectp(ob))
        {
            CHANNEL_D->do_channel(this_player(),"chat","object参数错误！");
            return "";
        }
        if(!ob->query_temp("apply/id")||!ob->query_temp("apply/name")||!ob->query_temp("apply/short"))
            str = ob->query("name") + "(" +ob->query("id") + ")" +(!withshort?"":ob->short());
	else
        str = ob->query_temp("apply/name")[0] + "(" +ob->query_temp("apply/id")[0] + ")" +(!withshort?"":ob->query_temp("apply/short")[0]);

        //房间内更新人物或物品图象
        //-默认OBJECT的图像--------------------------------------------------------------
        icon="";
        //自制物品的判断
        if(ob->query("icon"))
        {
            icon=ob->query("icon");
        }
        else
        {
            if(living(ob)) icon= "00901";   //默认生物为00901
            else icon ="00961";		 //默认物品为00961
            if(userp(ob) && !ob->query("icon"))
                icon=(ob->query("gender") == "男性" )?"01174":"01173";  //默认玩家头像
            if(living(ob) && ob->query("family")!=0 && ob->query("gender") == "男性") icon = "00744";  //有家族的男性
            if(living(ob) && ob->query("family")!=0 && ob->query("gender") == "女性") icon = "00739";  //有家族的女性
            //---笼统的物品图象--------------------------------------------------
            if(ob->query("id")=="board")  icon="05025";	 //留言板
            if(ob->query("id")=="boy") icon = "02149";   //对经常虐待的男孩致敬
            if(ob->query("id")=="girl") icon = "02111";   //女孩享受同等待遇
            if(ob->query("vendor_goods")) icon = "00988";  //最低优先级为卖货，（解决同时存在问题）
            if(ob->query("teacher")) icon = "05014";	//教书
            if(ob->query("id")=="xiao er" || ob->query("id")=="xiaoer") icon = "05024";   //酒楼
            if(ob->query("dealer")=="bank") icon = "00842"; //钱庄
            if(ob->query("dealer")=="pawn") icon = "00957"; //典当行
            if(ob->query("id")=="zhuang jia") icon = "00923"; //赌场
            if(ob->query("id")=="yahuan" ) icon = "02121"; //丫鬟
            if(ob->query("id")=="guan bing" ) icon = "05016"; //官兵
            if(ob->query("id")=="wu jiang" ) icon = "05010"; //武将
            if(ob->query("unit")=="朵") icon="00942";   //花
            if(ob->query("unit")=="本") icon="00906";   //书本
            //物品图标显示---------------------------------------------
            if(ob->query("id")=="coin") icon="01013";   //钱，银子
            if(ob->query("id")=="silver") icon="05012";   //钱，银子
            if(ob->query("id")=="cash") icon="00955";   //钱，银票
            if(ob->query("id")=="gold") icon="05001";   //金子

            //武器
            if(ob->query("skill_type")=="sword") icon="05035";  //剑类武器
            if(ob->query("skill_type")=="pin") icon="01059";    //针类武器
            //if(ob->query("skill_type")=="dagger") icon="05030"; //矛类武器
            if(ob->query("skill_type")=="axe") icon="05029";    //斧类武器
            if(ob->query("skill_type")=="dagger") icon="05040"; //匕首武器
            if(ob->query("skill_type")=="staff") icon="05042";   //棍类武器
            if(ob->query("skill_type")=="club") icon="05042";   //杖类武器
            if(ob->query("skill_type")=="blade") icon="05038";  //刀类武器
            if(ob->query("skill_type")=="throwing") icon="05049";   //暗器
            if(ob->query("skill_type")=="hammer") icon="05050";     //锤
            if(ob->query("skill_type")=="fork") icon="05034";     //叉
            //防护装备
            if(ob->query("armor_type")=="cloth") icon="05000";   //衣服
            if(ob->query("armor_type")=="armor") icon="05044";   //装甲
            if(ob->query("armor_type")=="pants") icon="05077";   //裤子
            if(ob->query("armor_type")=="boots") icon="03007";   //鞋子
            if(ob->query("armor_type")=="head") icon="03004";   //头
            if(ob->query("armor_type")=="hands") icon="01980";   //手掌类
            if(ob->query("armor_type")=="finger") icon="03002";   //手指类
            if(ob->query("armor_type")=="shield") icon="05017";   //盾牌类
            if(ob->query("armor_type")=="neck") icon="03006";   //项链
            if(ob->query("armor_type")=="wrists") icon="03003";  //护腕
        }
        return str + icon;
    }

    String getFemaleRank(Living ob){

        Integer exp = ob.getLevel();
        String menpai=ob.getFamilyName();
        Integer shen = ob.getShen();
        Integer pks = 0;
        Integer thief = 0;
        if(ob.query("PKS") != null){
            pks = Integer.parseInt(ob.query("PKS").toString());
        }
        if(ob.query("thief") != null){
            thief = Integer.parseInt(ob.query("thief").toString());
        }

        String obHood = wizhood(ob);
        if(obHood.equals("(admin)")){
            return "【" + Ansi.HIW + "九天玄女" + Ansi.NOR + "】";
        }else if(obHood.equals("(wizard)")){
            return "【" + Ansi.HIC + "瑶池仙女" + Ansi.NOR + "】";
        }else if(obHood.equals("(immortal)")){
            return "【" + Ansi.CYN + "梦幻精灵" + Ansi.NOR + "】";
        }else{

            if( pks > 30){
                return  "【" + Ansi.HIB + "饮血女魔" + Ansi.NOR + "】";
            } else if (shen >= 1000000) {
                return "【" + Ansi.HIC + "一代天娇" + Ansi.NOR + "】";
            } else if (shen >= 500000) {
                return "【" + Ansi.HIM + "旷世女侠" + Ansi.NOR + "】";
            } else if (shen >= 100000) {
                return "【" + Ansi.HIY + "巾帼英雄" + Ansi.NOR + "】";
            } else if (shen >= 50000) {
                return  "【" + Ansi.YEL + "女中豪杰" + Ansi.NOR + "】";
            } else if (shen >= 10000) {
                return "【" + Ansi.HIC + "女 少 侠" + Ansi.NOR + "】";
            } else if (shen >= 5000) {
                return "【" + Ansi.HIG + "风尘侠女" + Ansi.NOR + "】";
            } else if (shen <= -1000000) {
                return "【" + Ansi.HIW + "嗜血女魔" + Ansi.NOR + "】";
            } else if (shen <= -500000) {
                return "【" + Ansi.HIR + "旷世魔女" + Ansi.NOR + "】";
            } else if (shen <= -100000) {
                return "【" + Ansi.HIM + "饮血魔女" + Ansi.NOR + "】";
            } else if (shen <= -50000) {
                return "【" + Ansi.GRN + "塞外妖女" + Ansi.NOR + "】";
            } else if (shen <= -10000) {
                return "【" + Ansi.RED + "女 少 魔" + Ansi.NOR + "】";
            } else if (shen <= -5000) {
                return "【" + Ansi.CYN + "刁蛮恶女" + Ansi.NOR + "】";
            } else if( thief > 10 ) {
                return "【" + Ansi.HIW + "女 惯 窃" + Ansi.NOR + "】";
            } else {

                if(menpai.equals("武当派")){

                    if(exp > 99){
                        if(shen >= 0){
                            return "【" + Ansi.HIR + "女 天 师" + Ansi.NOR + "】";
                        }else{
                            return "【" + Ansi.HIR + "女魔天师" + Ansi.NOR + "】";
                        }
                    }

                    if (exp>79) {
                        return "【" + Ansi.RED + "女 国 师" + Ansi.NOR + "】";
                    }
                    if (exp>59) {
                        return "【" + Ansi.GRN  + "女 真 人" + Ansi.NOR + "】";
                    }
                    if (exp>49) {
                        return "【" + Ansi.YEL + "女大法师" + Ansi.NOR + "】";
                    }
                    if (exp>39) {
                        return "【" + Ansi.GRN + "女 法 师" + Ansi.NOR + "】";
                    }
                    if (exp>29) {
                        return "【" + Ansi.CYN + "女 道 长" + Ansi.NOR + "】";
                    }
                    if (exp>24) {
                        return "【" + Ansi.MAG + "女 道 姑" + Ansi.NOR + "】";
                    }
                    if (exp>19) {
                        return "【" + Ansi.HIB + "女 道 姑" + Ansi.NOR + "】";
                    }
                    if (exp>9) {
                        return "【" + Ansi.HIB + " 女  冠 " + Ansi.NOR + "】";
                    }
                    return "【女 少 冠】";
                }else if(menpai.equals("华山派")){

                    if (exp>99) {
                        if (shen >= 0 ) {
                            return "【" + Ansi.HIW + "惊天一剑" + Ansi.NOR + "】";
                        } else {
                            return "【" + Ansi.HIW + "惊魔一剑" + Ansi.NOR + "】";
                        }
                    }
                    if (exp>59) {
                        if (shen >= 0) {
                            return "【" + Ansi.HIR + "女 剑 圣" + Ansi.NOR + "】";
                        } else {
                            return "【" + Ansi.HIM + "女 剑 魔" + Ansi.NOR + "】";
                        }
                    }
                    if (exp>59) {
                        if (shen >= 0) {
                            return "【" + Ansi.GRN + "女 剑 仙" + Ansi.NOR + "】";
                        } else {
                            return "【" + Ansi.GRN + "女 邪 剑" + Ansi.NOR + "】";
                        }
                    }
                    if (exp>49) {
                        return "【" + Ansi.YEL + "女 剑 痴" + Ansi.NOR + "】";
                    }
                    if (exp>39) {
                        return "【" + Ansi.CYN + "女 剑 侠" + Ansi.NOR + "】";
                    }
                    if (exp>29) {
                        return "【" + Ansi.CYN + "女大剑客" + Ansi.NOR + "】";
                    }
                    if (exp>24) {
                        return "【" + Ansi.RED + "女 剑 客" + Ansi.NOR + "】";
                    }
                    if (exp>19) {
                        return "【" + Ansi.MAG + "女大剑士" + Ansi.NOR + "】";
                    }
                    if (exp>9) {
                        return "【" + Ansi.MAG + " 剑  士 " + Ansi.NOR + "】";
                    }
                    return "【女 剑 童】";
                }else if(menpai.equals("丐帮")) {

                    if (exp>99) {
                        return "【" + Ansi.HIR + "女副帮主" + Ansi.NOR  + "】";
                    }
                    if (exp>79) {
                        return "【" + Ansi.HIM + "女大长老" + Ansi.NOR + "】";
                    }
                    if (exp>59) {
                        return "【" + Ansi.HIM + "女 长 老" + Ansi.NOR + "】";
                    }
                    if (exp>49) {
                        return "【" + Ansi.YEL + "女传功使" + Ansi.NOR + "】";
                    }
                    if (exp>39) {
                        return "【" + Ansi.YEL + "女执法使" + Ansi.NOR + "】";
                    }
                    if (exp>29) {
                        return "【" + Ansi.RED + "女 神 丐" + Ansi.NOR + "】";
                    }
                    if (exp>24) {
                        return "【" + Ansi.RED + "女小神丐" + Ansi.NOR + "】";
                    }
                    if (exp>14) {
                        return "【" + Ansi.GRN + "女 乞 丐" + Ansi.NOR + "】";
                    }
                    if (exp>9) {
                        return "【" + Ansi.MAG + "女小乞丐" + Ansi.NOR + "】";
                    }
                    return "【 叫 化 子 】" ;
                }else if(menpai.equals("星宿派") || menpai.equals("桃花岛")){

                    if (exp>99) {
                        if (shen >= 0) {
                            return "【" + Ansi.HIR + "魔 见 愁" + Ansi.NOR + "】";
                        } else {
                            return "【" + Ansi.HIR + " 狂  魔 " + Ansi.NOR + "】";
                        }
                    }
                    if (exp>79) {
                        return "【" + Ansi.HIM + "隐世侠女" + Ansi.NOR + "】";
                    }
                    if (exp>59) {
                        return "【" + Ansi.GRN + "旷世女侠" + Ansi.NOR + "】";
                    }
                    if (exp>49) {
                        return "【" + Ansi.YEL + "大 侠 女" + Ansi.NOR + "】";
                    }
                    if (exp>39) {
                        return "【" + Ansi.RED + "女 侠 客" + Ansi.NOR + "】";
                    }
                    if (exp>29) {
                        return "【" + Ansi.MAG + "女 游 侠" + Ansi.NOR + "】";
                    }
                    if (exp>24) {
                        return "【" + Ansi.MAG + " 女  侠 " + Ansi.NOR + "】";
                    }
                    if (exp>14) {
                        return "【" + Ansi.CYN + " 女  侠 " + Ansi.NOR + "】";
                    }
                    if (exp>9) {
                        return "【" + Ansi.CYN + "小 女 侠" + Ansi.NOR + "】";
                    }
                    return "【风尘女子】" ;
                }else if(menpai.equals("明教")){

                    if (exp>99) {
                        return "【" + Ansi.HIR + "女副教主" + Ansi.NOR + "】";
                    }
                    if (exp>79) {
                        return "【" + Ansi.HIM + " 法  王 " + Ansi.NOR + "】";
                    }
                    if (exp>59) {
                        return "【" + Ansi.GRN + "女总护法" + Ansi.NOR + "】";
                    }
                    if (exp>49) {
                        return "【" + Ansi.YEL + "女 护 法" + Ansi.NOR + "】";
                    }
                    if (exp>39) {
                        return "【" + Ansi.YEL + "女总堂主" + Ansi.NOR + "】";
                    }
                    if (exp>29) {
                        return "【" + Ansi.CYN + "女副堂主" + Ansi.NOR + "】";
                    }
                    if (exp>24) {
                        return "【" + Ansi.CYN + "女 旗 主" + Ansi.NOR + "】";
                    }
                    if (exp>19) {
                        return "【" + Ansi.MAG + "女副旗主" + Ansi.NOR + "】";
                    }
                    if (exp>9) {
                        return "【" + Ansi.MAG + "女 教 众" + Ansi.NOR + "】";
                    }
                    return "【  教  众  】" ;
                }else{

                    if(ob.getClassStr().equals("bonze")){
                        return "【 女  尼 】";
                    }else if(ob.getClassStr().equals("yishi")){
                        return "【女 义 士】";
                    }else if(ob.getClassStr().equals("taoist")){
                        return "【 女  冠 】";
                    }else if(ob.getClassStr().equals("bandit")){
                        return "【女 飞 贼】";
                    }else if(ob.getClassStr().equals("dancer")){
                        return "【绝色舞姬】";
                    }else if(ob.getClassStr().equals("scholar")){
                        return "【遥遥佳人】";
                    }else if(ob.getClassStr().equals("officer")) {
                        return "【 女  差 】";
                    }else if(ob.getClassStr().equals("fighter")) {
                        return "【风尘女子】";
                    }else if(ob.getClassStr().equals("swordsman")) {
                        return "【舞剑少女】";
                    }else if(ob.getClassStr().equals("alchemist")) {
                        return "【女 方 士】";
                    }else if(ob.getClassStr().equals("shaman")) {
                        return "【女 巫 医】";
                    }else if(ob.getClassStr().equals("beggar")) {
                        return "【女叫花子】";
                    }else if(ob.getClassStr().equals("shiny") || ob.getClassStr().equals("huanu")) {
                        return "【移花宫花奴】";
                    }else if(ob.getClassStr().equals("yihuashinv")) {
                        return "【移花宫侍女】";
                    }else if(ob.getClassStr().equals("chuanren")) {
                        return "【移花宫传人】";
                    }else {
                        return "【芊芊民女】";
                    }
                }
            }
        }
    }

    String getMaleRank(Living ob) {

        Integer exp = ob.getLevel();
        String menpai = ob.getFamilyName();
        Integer shen = ob.getShen();
        Integer pks = 0;
        Integer thief = 0;
        Integer budd = 0;
        if (ob.query("PKS") != null) {
            pks = Integer.parseInt(ob.query("PKS").toString());
        }
        if (ob.query("thief") != null) {
            thief = Integer.parseInt(ob.query("thief").toString());
        }

        if(ob.getFamilyName() != null && ob.getFamilyName().equals("少林派")){
            budd = ob.querySkill("buddhism", 1);
        }

        String obHood = wizhood(ob);
        if(obHood.equals("(admin)")){
            return "【" + Ansi.HIR + "天界总管" + Ansi.NOR + "】";
        }else if(obHood.equals("(wizard)")){
            return "【" + Ansi.HIC + "护法尊者" + Ansi.NOR +"】";
        }else if(obHood.equals("(immortal)")){
            return "【" + Ansi.HIB + "逍遥散仙" + Ansi.NOR + "】";
        }else{

            if(pks > 50) {
                return "【杀人魔王】";
            } else if (shen >= 1000000) {
                return "【" + Ansi.HIG + "武林泰斗" + Ansi.NOR + "】";
            }else if (shen >= 500000) {
                return "【" + Ansi.HIC + "旷世大侠" + Ansi.NOR + "】";
            }else if (shen >= 100000) {
                return "【" + Ansi.HIC + "名满天下" + Ansi.NOR + "】";
            }else if (shen >= 50000) {
                return "【" + Ansi.HIY + "名动一时" + Ansi.NOR + "】";
            }else if (shen >= 10000) {
                return "【" + Ansi.HIY + "江湖豪杰" + Ansi.NOR + "】";
            }else if (shen >= 5000) {
                return "【" + Ansi.HIG + "武林新秀" + Ansi.NOR + "】";
            }else if (shen >= 1000) {
                return "【" + Ansi.HIG + "小有名声" + Ansi.NOR + "】";
            }else if (shen >= 500) {
                return "【" + Ansi.CYN + "小有侠义" + Ansi.NOR + "】";
            }else if (shen <= -1000000) {
                return "【" + Ansi.HIW + "江湖老怪" + Ansi.NOR + "】";
            }else if (shen <= -500000) {
                return "【" + Ansi.HIR + "旷世魔头" + Ansi.NOR + "】";
            }else if (shen <= -100000) {
                return "【" + Ansi.RED + "恶霸一方" + Ansi.NOR + "】";
            }else if (shen <= -50000) {
                return "【" + Ansi.YEL + "恶名远扬" + Ansi.NOR + "】";
            }else if (shen <= -10000) {
                return "【" + Ansi.YEL + "小有恶名" + Ansi.NOR + "】";
            }else if (shen <= -500) {
                return "【" + Ansi.HIB + "走上邪道" + Ansi.NOR + "】";
            }else if(thief > 10) {
                return "【" + Ansi.YEL + " 神  偷 " + Ansi.NOR + "】";
            }else {

                if(menpai.equals("武当派")){

                    if (exp>99){
                        if (shen >= 0 ) {
                            return "【" + Ansi.HIR + " 天  师 " + Ansi.NOR + "】";
                        }else {
                            return "【" + Ansi.HIW + "恶 天 师" + Ansi.NOR + "】";
                        }
                    }
                    if (exp>79) {
                        return "【" + Ansi.HIM + " 国  师 " + Ansi.NOR + "】";
                    }
                    if (exp>59) {
                        return "【" + Ansi.GRN + " 真  人 " + Ansi.NOR + "】";
                    }
                    if (exp>49) {
                        return "【" + Ansi.YEL + "大 法 师" + Ansi.NOR + "】";
                    }
                    if (exp>39) {
                        return "【" + Ansi.YEL + " 法  师 " + Ansi.NOR + "】";
                    }
                    if (exp>29) {
                        return "【" + Ansi.CYN + " 道  长 " + Ansi.NOR + "】";
                    }
                    if (exp>24) {
                        return "【" + Ansi.CYN + " 道  士 " + Ansi.NOR + "】";
                    }
                    if (exp>19) {
                        return "【" + Ansi.HIB + " 道  士 " + Ansi.NOR + "】";
                    }
                    if (exp>9) {
                        return "【" + Ansi.HIB + "大 道 童" + Ansi.NOR + "】";
                    }
                    return "【 道  童 】" ;
                }else if(menpai.equals("华山派")){

                    if (exp>99) {
                        if (shen >= 0) {
                            return "【" + Ansi.HIW + "惊天一剑" + Ansi.NOR + "】";
                        }else {
                            return "【" + Ansi.HIW + "惊魔一剑" + Ansi.NOR + "】";
                        }
                    }
                    if (exp>79) {
                        if (shen >= 0) {
                            return "【 " + Ansi.HIR + "剑  圣" + Ansi.NOR + " 】";
                        }else {
                            return "【 " + Ansi.HIM + "剑  魔" + Ansi.NOR + " 】";
                        }
                    }
                    if (exp>59) {
                        if (shen >= 0) {
                            return "【 " + Ansi.GRN + "剑  仙" + Ansi.NOR + " 】";

                        }else {
                            return "【 " + Ansi.GRN + "邪  剑" + Ansi.NOR + " 】";
                        }
                    }
                    if (exp>49) {
                        return "【 " + Ansi.YEL + "剑  痴" + Ansi.NOR + " 】";
                    }
                    if (exp>39) {
                        return "【 " + Ansi.CYN + "剑  侠" + Ansi.NOR + " 】";
                    }
                    if (exp>29) {
                        return "【" + Ansi.CYN + "大 剑 客" + Ansi.NOR + "】";
                    }
                    if (exp>24) {
                        return "【 " + Ansi.RED + "剑  客" + Ansi.NOR + " 】";
                    }
                    if (exp>19) {
                        return "【" + Ansi.MAG + "大 剑 士" + Ansi.NOR + "】";
                    }
                    if (exp>9) {
                        return "【" + Ansi.MAG + " 剑  士 " + Ansi.NOR + "】";
                    }
                    return "【 剑  童 】";
                }else if(menpai.equals("丐帮")){

                    if (exp>99) {
                        return "【" + Ansi.HIR + "副 帮 主" + Ansi.NOR + "】";
                    }
                    if (exp>79) {
                        return "【" + Ansi.HIW + "十袋长老" + Ansi.NOR + "】";
                    }
                    if (exp>59) {
                        return "【" + Ansi.HIM + "九袋长老" + Ansi.NOR + "】";
                    }
                    if (exp>49) {
                        return "【" + Ansi.YEL + "传功长老" + Ansi.NOR + "】";
                    }
                    if (exp>39) {
                        return "【" + Ansi.YEL + "执法长老" + Ansi.NOR + "】";
                    }
                    if (exp>29) {
                        return "【" + Ansi.RED + " 神  丐 " + Ansi.NOR + "】";
                    }
                    if (exp>24) {
                        return "【" + Ansi.CYN + "小 神 丐" + Ansi.NOR + "】";
                    }
                    if (exp>19) {
                        return "【" + Ansi.CYN + "  乞  丐 " + Ansi.NOR + "】";
                    }
                    if (exp>9) {
                        return "【" + Ansi.HIB + "小 乞 丐" + Ansi.NOR + "】";
                    }
                    return "【叫 化 子】";
                }else if(menpai.equals("星宿派") || menpai.equals("桃花岛")){

                    if (exp>99) {
                        if (shen >= 0) {
                            return "【" + Ansi.HIR + "魔 见 愁" + Ansi.NOR + "】";
                        }else {
                            return "【" + Ansi.HIR + " 狂  魔 " + Ansi.NOR + "】";
                        }
                    }
                    if (exp>79){
                        return "【" + Ansi.CYN + "世外高人" + Ansi.NOR + "】";
                    }
                    if (exp>59){
                        return "【" + Ansi.GRN + "旷世奇侠" + Ansi.NOR + "】";
                    }
                    if (exp>49){
                        return "【" + Ansi.YEL + "大 豪 侠" + Ansi.NOR + "】";
                    }
                    if (exp>39) {
                        return "【" + Ansi.YEL + " 豪  侠 " + Ansi.NOR + "】";
                    }
                    if (exp>29) {
                        return "【" + Ansi.GRN + " 大  侠 " + Ansi.NOR + "】";
                    }
                    if (exp>24) {
                        return "【" + Ansi.GRN + " 侠  士 " + Ansi.NOR + "】";
                    }
                    if (exp>19) {
                        return "【" + Ansi.CYN + " 游  侠 " + Ansi.NOR + "】";
                    }
                    if (exp>9) {
                        return "【" + Ansi.HIC + " 少  侠 " + Ansi.NOR + "】";
                    }
                    return "【武林人物】";
                }else if(menpai.equals("明教")){

                    if (exp>99) {
                        return "【" + Ansi.HIR + "副 教 主" + Ansi.NOR + "】";
                    }
                    if (exp>79) {
                        return "【" + Ansi.HIM + " 法  王 " + Ansi.NOR + "】";
                    }
                    if (exp>59) {
                        return "【" + Ansi.GRN + "总 护 法" + Ansi.NOR + "】";
                    }
                    if (exp>49) {
                        return "【" + Ansi.YEL + " 护  法 " + Ansi.NOR + "】";
                    }
                    if (exp>39) {
                        return "【" + Ansi.YEL + "总 堂 主" + Ansi.NOR + "】";
                    }
                    if (exp>29) {
                        return "【" + Ansi.CYN + "副 堂 主" + Ansi.NOR + "】";
                    }
                    if (exp>24) {
                        return "【" + Ansi.CYN + " 旗  主 " + Ansi.NOR + "】";
                    }
                    if (exp>19){
                        return "【" + Ansi.GRN + "副 旗 主" + Ansi.NOR + "】";
                    }
                    if (exp>9) {
                        return "【" + Ansi.HIB + " 教  众 " + Ansi.NOR + "】";
                    }
                    return "【 教  众 】" ;
                }else{

                    if(ob.getClassStr().equals("bonze")){

                        if (budd >= 150) {
                            return "【" + Ansi.HIY + " 长  老" + Ansi.NOR + " 】";
                        }else if (budd >= 120) {
                            return "【" + Ansi.HIY + " 圣  僧" + Ansi.NOR + " 】";
                        }else if (budd >= 90) {
                            return "【" + Ansi.HIY + " 罗  汉" + Ansi.NOR + " 】";
                        }else if (budd >= 60) {
                            return "【" + Ansi.YEL + " 尊  者" + Ansi.NOR + " 】";
                        }else if (budd >= 40) {
                            return "【 " + Ansi.YEL + "禅  师" + Ansi.NOR + " 】";
                        }else if (budd >= 30) {
                            return "【" + Ansi.YEL + " 比  丘" + Ansi.NOR + " 】";
                        }else {
                            return "【 僧  人 】";
                        }
                    }else if(ob.getClassStr().equals("yishi")){
                        return "【江湖义士】";
                    }else if(ob.getClassStr().equals("taoist")){
                        return "【云游道士】";
                    }else if(ob.getClassStr().equals("bandit")){
                        return "【梁上君子】";
                    }else if(ob.getClassStr().equals("scholar")){
                        return "【翩翩才子】";
                    }else if(ob.getClassStr().equals("officer")){
                        return "【差    人】";
                    }else if(ob.getClassStr().equals("swordsman")){
                        return "【多情剑客】";
                    }else if(ob.getClassStr().equals("alchemist")){
                        return "【 方  士 】";
                    }else if(ob.getClassStr().equals("shaman")){
                        return "【 巫  医 】";
                    }else if(ob.getClassStr().equals("beggar")){
                        return "【穷叫花子】";
                    }else if(ob.getClassStr().equals("shiny")){
                        return "【光明使者】";
                    }else{
                        return "【布衣平民】";
                    }
                }
            }
        }

    }

    //Todo:暂时用一个伪函数实现
    private String wizhood(Living ob){
        return "player";
    }



}
