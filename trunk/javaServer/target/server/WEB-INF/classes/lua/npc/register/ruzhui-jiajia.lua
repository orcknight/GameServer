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
    agent:setName(uuid, "入赘假假")
    agent:setCmdName(uuid, "ruzhui-jiajia")
    agent:setAge(uuid, 30);
    agent:setLongDesc(uuid, "本是一个无知之辈，后入赘豪门捐了个官职；平生胆小懦弱，死后阎王看他可怜，赏他一个引路判官的官职。")
    agent:setCombatExp(uuid, 1000);
    agent:setAttitude(uuid, "peaceful")

    --agent:setButton('[{"关于性格":"ask %s\n + " about character", "name":"偏属"}]')
end


