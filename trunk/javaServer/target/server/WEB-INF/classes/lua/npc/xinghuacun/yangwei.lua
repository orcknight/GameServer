--
-- Created by IntelliJ IDEA.
-- User: PPX
-- Date: 2017/8/7
-- Time: 16:46
-- To change this template use File | Settings | File Templates.
--
function create(bridge, uuid)

    local agent = bridge:getClass("com.tian.server.util.LivingLuaAgent");

    --调用对象方法
    agent:setName(uuid, "杨威")
    agent:setCmdName(uuid, "yangwei")
    agent:setAge(uuid, 30);
    agent:setLongDesc(uuid, "生性淫荡，操狗被咬断，从此背负杨威之名。")
    agent:setCombatExp(uuid, 1000);
    agent:setAttitude(uuid, "peaceful")

    --agent:setButton('[{"关于性格":"ask %s\n + " about character", "name":"偏属"}]')
end

