package com.tian.server.model.Race;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import com.tian.server.entity.PlayerFamilyEntity;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.model.SkillAction;
import com.tian.server.util.LuaBridge;
import com.tian.server.util.UnityCmdUtil;
import com.tian.server.util.UserCacheUtil;
import net.sf.json.JSONArray;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Created by PPX on 2017/7/11.
 */
public class Human extends Living {

    private static List<SkillAction> actions = new ArrayList<SkillAction>();
    private static List<String> limbs = new ArrayList<String>();
    protected PlayerFamilyEntity family = new PlayerFamilyEntity();

    public Human(){

        this.weight = 40000;

        actions.add(new SkillAction("$N挥拳攻击$n的$l", "瘀伤"));
        actions.add(new SkillAction("$N往$n的$l一抓", "抓伤"));
        actions.add(new SkillAction("$N往$n的$l狠狠地踢了一脚", "瘀伤"));
        actions.add(new SkillAction("$N提起拳头往$n的$l捶去", "瘀伤"));
        actions.add(new SkillAction("$N对准$n的$l用力挥出一拳", "瘀伤"));
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

        addButton("给予", "give $ID");
        addButton("查看技能", "skills $ID");
        addButton("跟随", "follow $ID");
        addButton("偷窃", "steal $ID");
        addButton("切磋", "fight $ID");
        addButton("偷袭", "touxi $ID");
        addButton("杀死", "kill $ID");
    }

    public PlayerFamilyEntity getFamily() {
        return family;
    }

    public void setFamily(PlayerFamilyEntity family) {
        this.family = family;
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

    @Override
    public String getRandomLimb(){

        if(actions.size() < 1){

            return "";
        }

        Random r = new Random();
        int randomIndex = r.nextInt(limbs.size()-1);
        return limbs.get(randomIndex);
    }

    public SkillAction createAction(String action, String damageType, Integer damage){

        SkillAction skillAction = new SkillAction(action, damageType);
        skillAction.setDamage(damage);

        return skillAction;
    }

    public boolean accept_fight(Player ob){

        //Todo:
        /*message_vision("$N急忙对$n道：这位" + RANK_D->query_respect(ob) +
                        "，有话好好说，怎么可以这个样子？\n",
                this_object(), ob);*/

        if(!(this instanceof  Human)){


            kill_ob

        }

        int accept_fight(object who)
        {
            String att;
            int perqi;
            int perjing;

            if( !query("can_speak") ) {
                kill_ob(who);
                return 1;
            }

            if( this_object()->is_guarder() )
            return this_object()->check_enemy(who, "fight");

            att = query("attitude");
            perqi = (int)query("qi") * 100 / query("max_qi");
            perjing = (int)query("jing") * 100 / query("max_jing");

            if( is_fighting() ) {
                switch(att)
                {
                    case "heroism":
                        if( perqi >= 50 ) {
                            command("say 哼！出招吧！");
                            return 1;
                        } else {
                            command("say 哼！我小歇片刻再收拾你不迟。");
                            return 0;
                        }
                        break;
                    default:
                        command("say 想倚多为胜，这不是欺人太甚吗！");
                        return 0;
                }
            }

            if( perqi >= 75 && perjing >= 75 ) {
                switch (att)
                {
                    case "friendly":
                        command("say " + RANK_D->query_self(this_object())
                                + "怎么可能是" + RANK_D->query_respect(who)
                                + "的对手？");
                        return 0;
                    case "aggressive":
                    case "killer":
                        command("say 哼！出招吧！");
                        break;
                    default:
                        command("say 既然" + RANK_D->query_respect(who)
                                + "赐教，" + RANK_D->query_self(this_object())
                                + "只好奉陪。");
                        break;
                }
                return 1;
            }

            command("say 今天有些疲惫，改日再战也不迟啊。");
            return 0;
        }

        if(cmdActions != null && cmdActions.get("accept_fight") != null ){

            LuaBridge bridge = new LuaBridge();
            String luaPath = this.getClass().getResource(this.getResource()).getPath();
            Globals globals = JsePlatform.standardGlobals();
            //加载脚本文件login.lua，并编译
            globals.loadfile(luaPath).call();
            //获取带参函数create
            LuaValue createFun = globals.get(LuaValue.valueOf(cmdActions.get("accept_fight")));
            //执行方法初始化数据
            createFun.call(CoerceJavaToLua.coerce(bridge), LuaValue.valueOf(uuid.toString()));



        }else{

            List<SocketIOClient> excludeClients = new ArrayList<SocketIOClient>();
            excludeClients.add(ob.getSocketClient());
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(UnityCmdUtil.getInfoWindowRet("$N急忙对$n道：这位同志，有话好好说，怎么可以这个样子？"));
            Collection<SocketIOClient> clients = ob.getSocketClient().getNamespace().getRoomOperations(ob.getLocation().getName()).getClients();

            for (SocketIOClient client : clients) {

                if(excludeClients.contains(client)){
                    continue;
                }

                List<Object> msgList = new ArrayList<Object>();
                msgList.add(jsonArray);
                Packet packet = new Packet(PacketType.MESSAGE);
                packet.setSubType(PacketType.EVENT);
                packet.setName("stream");
                packet.setData(msgList);
                client.send(packet);
            }
        }

        return true;
    }


}
