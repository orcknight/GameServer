--
-- Created by IntelliJ IDEA.
-- User: PPX
-- Date: 2017/9/21
-- Time: 15:49
-- To change this template use File | Settings | File Templates.
--
function create(bridge, uuid)

    local agent = bridge:getClass("com.tian.server.util.GoodsLuaAgent");

    --调用对象方法
    agent:addTypes(uuid, "food");
    agent:addAttr(uuid, "food_remaining", 4);
    agent:addAttr(uuid, "food_supply", 40);

    --agent:setButton('[{"关于性格":"ask %s\n + " about character", "name":"偏属"}]')
end

