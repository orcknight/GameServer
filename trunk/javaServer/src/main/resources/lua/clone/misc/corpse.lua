--
-- Created by IntelliJ IDEA.
-- User: PPX
-- Date: 2017/10/18
-- Time: 17:49
-- To change this template use File | Settings | File Templates.
--
function create(bridge, uuid)

    local agent = bridge:getClass("com.tian.server.util.GoodsLuaAgent");

    --调用对象方法
    agent:addTypes(uuid, "food");
    agent:setCuttable(uuid, 1);
    agent:setName(uuid, "无名尸体");
    agent:setCmdName(uuid, "corpse");
    agent:setDefaultClone(uuid, "/lua/clone/misc/part");
    agent:setUnit(uuid, "具");
    agent:setLongDesc(uuid, "这是一具无名尸体。");
    agent:addAttr(uuid, "no_sell", "我的天...这...这你也拿来卖...官...官府呢？");
    agent:addAttr(uuid, "no_drop", "疯了？谁会乱收尸体。");
    agent:setParts(uuid, "head", '{ "level":0,'
        ..'"unit":"颗",'
        ..'"name":"头",'
        ..'"leftName":"人头",'
        ..'"leftId":"head",'
        ..'"components":{'
        ..'"left eye"  : "left eye",'
        ..'"right eye" : "right eye",'
        ..'"nose"      : "nose",'
        ..'"left ear"  : "left ear",'
        ..'"right ear" : "right ear",'
        ..'"hair"      : "hair",'
        ..'"tongue"    : "tongue" },'
        ..'"verbOfPart": "砍下",'
        ..'"cloneObject":"/clone/misc/head" }');


    init_actions(agent, uuid);
    --agent:setButton('[{"关于性格":"ask %s\n + " about character", "name":"偏属"}]')
    agent:createDecayTask("decay", 120, 1);
end

function init_actions(agent, uuid)
    agent:addAction(uuid, "make_corpse", "make_corpse");
    agent:addAction(uuid, "")
end

function make_corpse(bridge, uuid, params)

    local vctimUuid = params[1];
    local killerUuid = params[2];

    local livingAgent = bridge:getClass("com.tian.server.util.LivingLuaAgent");
    local goodsAgent = bridge:getClass("com.tian.server.util.GoodsLuaAgent");
    local name = livingAgent:getName(vctimUuid) .. "的尸体";
    local cmdName = livingAgent:getCmdName(vctimUuid) .. " corpse";
    local classStr = livingAgent:getClassStr(vctimUuid);
    local longDesc = livingAgent:getLongDesc(vctimUuid);
    goodsAgent:setName(uuid, name);
    goodsAgent:setCmdName(uuid, cmdName);
    goodsAgent:setLongDesc(uuid, longDesc);

    return 1;
end

function decay(bridge, uuid, phase)

    local goodsAgent = bridge:getClass("com.tian.server.util.GoodsLuaAgent");
    local decayed = phase;
    local msg;
    local gender = goodsAgent:queryString(uuid, "gender");

    if(phase == 1) then
        goodsAgent:set(uuid, "owner_id", "unknow");
        msg =  goodsAgent:getName(uuid) .. "开始腐烂了，发出一股难闻的恶臭。\n";
        goodsAgent:delete(uuid, "victim_name");
        goodsAgent:delete(uuid, "owner_id");
        if(gender == "男性") then
            goodsAgent:setName(uuid, "腐烂的男尸");
            goodsAgent:setCmdName(uuid, "corpse");
        elseif(gender == "女性") then
            goodsAgent:setName(uuid, "腐烂的女尸");
            goodsAgent:setCmdName(uuid, "corpse");
        else
            goodsAgent:setName(uuid, "腐烂的尸体");
            goodsAgent:setCmdName(uuid, "corpse");
        end
        goodsAgent:setLongDesc(uuid, "这具尸体显然已经躺在这里有一段时间了，正散发著一股腐尸的味道。\n");
        goodsAgent:createDecayTask("decay", 120, phase + 1);
    elseif(phase == 2) then
        goodsAgent:delete("parts");
        msg = goodsAgent:getName(uuid) .. "被风吹乾了，变成一具骸骨。\n";
        goodsAgent:setName(uuid, "枯乾的骸骨");
        goodsAgent:setCmdName(uuid, "skeleton");
        goodsAgent:setLongDesc("long", "这副骸骨已经躺在这里很久了。\n");
        goodsAgent:createDecayTask("decay", 60, phase + 1);
    elseif(phase == 3) then
        msg = "一阵风吹过，把" .. goodsAgent:getName(uuid) .. "化成骨灰吹散了。\n";
        goodsAgent:destructGoods(uuid);
        return;
        --[[if( env )
        all_inventory(this_object())->move(env);
        tell_room(env, msg);
        destruct(this_object());
        return;
        end]]

    end



   --[[ if (env ~= "") then
        tell_room(env, msg);
    end]]


end

