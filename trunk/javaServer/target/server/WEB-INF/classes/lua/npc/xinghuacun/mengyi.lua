--
-- Created by IntelliJ IDEA.
-- User: PPX
-- Date: 2017/7/25
-- Time: 12:00
-- To change this template use File | Settings | File Templates.
--
function create(bridge, uuid)

    local agent = bridge:getClass("com.tian.server.util.LivingLuaAgent");

    --调用对象方法
    agent:setName(uuid, "梦一")
    agent:setCmdName(uuid, "mengyi")
    agent:setNickname(uuid, "新手导师")
    agent:setAge(uuid, 30);
    agent:setLongDesc(uuid, "一见梦一误终身，不见梦一终身误。我就是潇洒风流的新手导师梦一。")
    agent:setCombatExp(uuid, 1000);
    agent:setAttitude(uuid, "peaceful")
    agent:setup(uuid)

    --agent:setButton('[{"关于性格":"ask %s\n + " about character", "name":"偏属"}]')
end


