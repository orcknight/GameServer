--
-- Created by IntelliJ IDEA.
-- User: PPX
-- Date: 2017/10/23
-- Time: 16:45
-- To change this template use File | Settings | File Templates.
--
function get_death_msg(bridge)

    local ansiAgent = bridge:getClass("com.tian.server.util.AnsiLuaAgent");
    local death_msg = {
        ansiAgent:getAnsiByName("HIW") .. "白无常说道：喂！新来的，你叫什么名字？\n" .. ansiAgent:getAnsiByName("NOR"),
        ansiAgent:getAnsiByName("HIW") .. "白无常用奇异的眼光盯著你，好像要看穿你的一切似的。\n" .. ansiAgent:getAnsiByName("NOR"),
        ansiAgent:getAnsiByName("HIW") .. "白无常「哼」的一声，从袖中掏出一本像帐册的东西翻看著。\n" .. ansiAgent:getAnsiByName("NOR"),
        ansiAgent:getAnsiByName("HIW") .. "白无常合上册子，说道：咦？阳寿未尽？怎么可能？\n\n" .. ansiAgent:getAnsiByName("NOR"),
        ansiAgent:getAnsiByName("HIW") .. "白无常搔了搔头，叹道：罢了罢了，你走吧。\n一股阴冷的浓雾突然出现，很快地包围了你。\n" .. ansiAgent:getAnsiByName("NOR")
    };
    return death_msg;

end

function create(bridge, uuid)

    local agent = bridge:getClass("com.tian.server.util.LivingLuaAgent");
    local ansiAgent = bridge:getClass("com.tian.server.util.AnsiLuaAgent");

    --调用对象方法
    agent:setName(uuid, "白无常");
    agent:setCmdName(uuid, "baiwuchang");
    agent:setTitle(uuid, "冥府地藏王殿前");
    agent:setAge(uuid, 217);
    agent:setLongDesc(uuid, "白无常瞪著你，焦黄的脸上看不出任何喜怒哀乐。")
    agent:setCombatExp(uuid, 50000000);
    agent:setAttitude(uuid, "peaceful");
    agent:set(uuid, "chat_chance", 15);
    agent:set(uuid, "chat_msg", {
        ansiAgent:getAnsiByName("CYN") .. "白无常狠狠的敲了敲你的脑袋，你觉得头晕晕的。\n".. ansiAgent:getAnsiByName("NOR"),
        ansiAgent:getAnsiByName("CYN") .. "白无常嘿嘿奸笑两声。\n" .. ansiAgent:getAnsiByName("NOR")
    });

    agent:setQi(uuid, 9999);
    agent:setEffQi(uuid, 9999);
    agent:setMaxQi(uuid, 9999);
    agent:setJing(uuid, 9999);
    agent:setEffJing(uuid, 9999);
    agent:setMaxJing(uuid, 9999);
    agent:setNeili(uuid, 9999);
    agent:setEffNeili(uuid, 9999);
    agent:setMaxNeili(uuid, 9999);
    agent:set(uuid, "jiali", 250);

    init_actions(agent, uuid);

    agent:setup(uuid);

--[[    set_skill("unarmed", 500);
    set_skill("sword", 500);
    set_skill("blade", 500);
    set_skill("staff", 500);
    set_skill("hammer", 500);
    set_skill("club", 500);
    set_skill("whip", 500);
    set_skill("dagger", 500);
    set_skill("throwing", 500);
    set_skill("parry", 500);
    set_skill("dodge", 500);
    set_skill("force", 500);
    set_skill("never-defeated", 1000);
    set_skill("magic", 1000);

    map_skill("sword", "never-defeated");
    map_skill("blade", "never-defeated");
    map_skill("staff", "never-defeated");
    map_skill("hammer", "never-defeated");
    map_skill("club", "never-defeated");
    map_skill("whip", "never-defeated");
    map_skill("dagger", "never-defeated");
    map_skill("parry", "never-defeated");
    map_skill("dodge", "never-defeated");
    map_skill("force", "never-defeated");

    set("inquiry", ([
    "地狱道" : (: ask_diyudao :),
        "锦镧袈裟" : "此乃袈裟中的上品，穿戴者可不坠地狱，不入轮回！\n",
    ]));
    setup();

    carry_object(__DIR__"obj/cloth1")->wear();
    carry_object(__DIR__"obj/suolian")->wield();
    set_temp("handing", carry_object(__DIR__"obj/book"));]]

    --agent:setButton('[{"关于性格":"ask %s\n + " about character", "name":"偏属"}]')
end

function init_actions(agent, uuid)
    agent:addAction(uuid, "init", "init");
    agent:addAction(uuid, "death_stage", "death_stage");
end

function init(bridge, uuid, params)
    local agent = bridge:getClass("com.tian.server.util.LivingLuaAgent");
    local ob = params[1];
    print("call init");
    if (agent:getGhost(ob) == 0) then
        return;
    end
    --[[call_out("death_stage", 5, previous_object(), 0);]]
    agent:createScheduleTask(uuid, "death_stage", {5, ob, 0});
    print("call init");
end

function death_stage(bridge, uuid, params)

    local agent = bridge:getClass("com.tian.server.util.LivingLuaAgent");
    local death_msg = get_death_msg(bridge);
    local ob = params[1];
    local stage = tonumber(params[2]);
    local number = #death_msg;
    agent:tellObject(ob, death_msg[stage + 1]);

    if(stage + 1 < number) then
        agent:createScheduleTask(uuid, "death_stage", {5, ob, stage + 1});
        return;
    else
        agent:reincarnate(ob);
    end

    agent:move(ob, "death/gate", "xinghuacun/guangchang");
end