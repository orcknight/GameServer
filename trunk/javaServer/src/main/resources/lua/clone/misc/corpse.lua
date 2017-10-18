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
    agent:addAttr(uuid, "food_remaining", 4);
    agent:addAttr(z, "food_supply", 40);

    --agent:setButton('[{"关于性格":"ask %s\n + " about character", "name":"偏属"}]')
end


