package com.tian.server.bll;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.dao.*;
import com.tian.server.entity.*;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.service.RoomService;
import com.tian.server.util.UnityCmdUtil;
import com.tian.server.util.UserCacheUtil;
import com.tian.server.util.XmlUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.*;

/**
 * Created by PPX on 2017/6/15.
 */
public class DefaultBll extends BaseBll {

    public DefaultBll(SocketIOClient socketIOClient) {
        super(socketIOClient);
    }

    public void checkVersion(){

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = UnityCmdUtil.getCheckVersionRet("版本验证成功");
        jsonArray.add(jsonObject);
        socketIOClient.sendEvent("stream", jsonArray);

        //初始化数据
        initData();
    }

    public void sendEmpty(){

        socketIOClient.sendEvent("stream", "");
    }

    private void initData(){

        if(UserCacheUtil.getAllCitys().isEmpty()) {

            initCityCache();
        }

        if(UserCacheUtil.getAllMaps().isEmpty()){

            initRoomCache();
        }

        if(UserCacheUtil.getTaskTrackMap().isEmpty()){

            UserCacheUtil.initTaskTrackMap(XmlUtil.parseXmlToTask());
        }

        //初始化房间内物品
        if(UserCacheUtil.getRoomObjectsCache().isEmpty()){

            RoomContentDao roomContentDao = new RoomContentDao();
            ItemDao itemDao = new ItemDao();
            NpcDao npcDao = new NpcDao();
            List<RoomContentEntity> roomContents = roomContentDao.getList();
            List<ItemEntity> items = itemDao.getList();
            List<NpcEntity> npcs = npcDao.getList();

            /*for(NpcEntity npc : npcs) {

                try {
                    Class cls = Class.forName("com.tian.server.model.Race." + npc.getRace());
                    Living living = (Living) cls.newInstance();

                    Long uuid = IdUtil.getUUID();
                    living.setUuid(uuid);

                    //RoomObjects roomObjects = roomObjectsCache.get(roomContent.getRoomName());
                    //if(roomObjects == null){

                    //roomObjects = new RoomObjects();
                    //}
                    //}
                    LuaBridge bridge = new LuaBridge();
                    String luaPath = this.getClass().getResource(npc.getResource()).getPath();
                    //= "resources/lua/login.lua";   //lua脚本文件所在路径
                    Globals globals = JsePlatform.standardGlobals();
                    //加载脚本文件login.lua，并编译
                    globals.loadfile(luaPath).call();

                    //获取带参函数create
                    LuaValue createFun = globals.get(LuaValue.valueOf("create"));
                    //执行方法初始化数据
                    createFun.call(CoerceJavaToLua.coerce(bridge), LuaValue.valueOf(uuid.toString()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/

            UserCacheUtil.initRoomObjectsCache(roomContents, items, npcs);

            //初始化门
            RoomGateDao roomGateDao = new RoomGateDao();
            List<RoomGateEntity> roomGates = roomGateDao.getList();
            UserCacheUtil.initRoomGates(roomGates);
        }

        //初始完数据以后生成定时器
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {

                //获取player列表
                Map<Integer, Living> playerCacheMap = UserCacheUtil.getPlayers();
                Map<SocketIOClient, Integer> socketCache = UserCacheUtil.getUserSockets();
                if(socketCache.isEmpty()){

                    return;
                }
                for (Map.Entry<SocketIOClient, Integer> entry : socketCache.entrySet()) {


                    Integer userId = entry.getValue();
                    SocketIOClient client = entry.getKey();

                    //获取玩家信息并提取信息
                    Player player = (Player)playerCacheMap.get(userId);
                    if(player == null){

                        continue;
                    }
                    /*if(player.getMaxQi() == null || player.getMaxQi() < 1){

                        continue;
                    }*/

                    player.heartBeat();
                    //准备状态字符串，然后发送消息
                    JSONArray jArray = UnityCmdUtil.getPlayerStatus(player);
                    //client.sendEvent("status",  jArray);
                }

            }
        }, 2 * 1000, 2 * 1000);
    }


    private void initCityCache(){

        CityDao cityDao = new CityDao();
        List<CityEntity> list = cityDao.getList();
        Map<String, CityEntity> cityMaps = UserCacheUtil.getAllCitys();
        for(CityEntity item : list) {

            cityMaps.put(item.getName(), item);
        }
    }

    private void initRoomCache() {

        RoomDao roomDao = new RoomDao();
        List<RoomEntity> list = roomDao.getList();
        Map<String, RoomEntity> rooms = UserCacheUtil.getAllMaps();
        Map<String, Map<String, RoomEntity>> cityedRooms = UserCacheUtil.getCityedRooms();

        for (RoomEntity item : list) {

            rooms.put(item.getName(), item);
            Map<String, RoomEntity> cityed = cityedRooms.get(item.getPname());
            if (cityed == null) {

                cityed = new HashMap<String, RoomEntity>();
                cityedRooms.put(item.getPname(), cityed);
            }

            cityed.put(item.getName(), item);
        }
    }

}
