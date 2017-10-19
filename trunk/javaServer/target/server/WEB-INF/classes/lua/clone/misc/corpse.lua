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
end

function init_actions(agent, uuid)
    agent:addAction(uuid, "make_corpse", "make_corpse");
end

function make_corpse(bridge, uuid, params)

    local vctimUuid = params[1];
    local killerUuid = params[2];

    return 1;
end

