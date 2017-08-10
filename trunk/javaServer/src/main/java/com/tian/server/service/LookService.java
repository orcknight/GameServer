package com.tian.server.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import com.tian.server.entity.RoomGateEntity;
import com.tian.server.model.Living;
import com.tian.server.model.MudObject;
import com.tian.server.model.Player;
import com.tian.server.model.Race.Human;
import com.tian.server.model.RoomObjects;
import com.tian.server.util.UserCacheUtil;
import com.tian.server.util.ZjMudUtil;

import java.lang.annotation.Target;
import java.util.*;

/**
 * Created by PPX on 2017/6/29.
 */
public class LookService extends BaseService {

    public LookService(SocketIOClient socketIOClient) {
        super(socketIOClient);
    }


    public void look(String msg) {

        String type = msg.split("/")[1];
        String id = msg.split("#")[1];

        //存储观察id
        Map<Integer, Living> cacheMap = UserCacheUtil.getPlayers();
        Player player = (Player) cacheMap.get(this.userId);
        if (player == null) {
            return;
        }
        player.setLookId(new StringBuffer(msg).toString());


        if (type.equals("user")) {

            Map<Long, MudObject> allLivings = UserCacheUtil.getAllObjects();
            Living npc = (Living) allLivings.get(Long.valueOf(id));
            lookLiving(getMe(), npc);
            return;
            /*if (npc != null) {
                sendMsg(npc.getLookStr() + getAct(getMe(), npc));
            }*/
        } else if (type.equals("npc")) {

            Map<Long, MudObject> allLivings = UserCacheUtil.getAllObjects();
            Living npc = (Living) allLivings.get(Long.valueOf(id));
            if (npc != null) {
                sendMsg(npc.getLookStr() + getAct(getMe(), npc));
            }
        } else if (type.equals("gate")) {

            String retMsg = getLookGateStr(id);
            sendMsg(retMsg);
        }
    }

    public void openGate(String direction) {

        openOrCloseGate(direction, "打开");
    }

    public void closeGate(String direction) {

        openOrCloseGate(direction, "关闭");
    }

    private String getPlayerButtonStr(String userName, String playerName) {

        return null;
    }

    private String getLookGateStr(String name) {

        Map<Integer, Living> cacheMap = UserCacheUtil.getPlayers();
        Player player = (Player) cacheMap.get(this.userId);
        String roomName = player.getLocation().getName();

        Map<String, RoomObjects> roomObjectsCache = UserCacheUtil.getRoomObjectsCache();
        RoomObjects roomObjects = roomObjectsCache.get(roomName);
        if (roomObjects == null) {

            return "";
        }

        for (Map.Entry<String, RoomGateEntity> entry : roomObjects.getGates().entrySet()) {

            RoomGateEntity gate = entry.getValue();

            if (name.equals(gate.getName())) {

                //去掉名称的特殊字符
                name = name.replaceAll("【", "");
                name = name.replaceAll("】", "");
                StringBuffer desc = new StringBuffer();
                StringBuffer button = new StringBuffer();

                desc.append("这个" + name + "是");

                if (gate.getStatus() == 1) {

                    desc.append("开着的。");
                    button.append("关门:close " + entry.getKey());
                } else {

                    desc.append("关着的。");
                    button.append("开门:open " + entry.getKey());
                }

                String msg = ZjMudUtil.getHuDongDescLine(desc.toString()) + ZjMudUtil.getHuDongButtonLine(button.toString());
                return msg;
            }
        }

        return "";
    }

    private String lookLiving(Living me, Living target) {

        Integer targetShen = target.getShen();
        Integer meSHen = me.getShen();
        Integer age = target.getAge();

        String pro = (me.getUuid() == target.getUuid()) ? genderSelf(target.getGender()) : genderPronoun(target.getGender());

        if (target instanceof Player) {

            if (((Player) target).getBornFamily().length() < 1) {

                sendMsg(pro + "还没有投胎，只有一股元神，什么都看不到耶！\n" );
            }

            if (me.getUuid() != target.getUuid())
            {

                List<SocketIOClient> excludeClients = new ArrayList<SocketIOClient>();
                excludeClients.add(((Player)me).getSocketClient());
                excludeClients.add(((Player)target).getSocketClient());
                sendMsg(((Player) target).getSocketClient(), me.getName() + "正盯著你看，不知道在打什么主意。\n");
                sendMsg( me.getName() + "盯着" + target.getName() +
                                "看了一会儿，不知道在打什么主意。\n",
                        excludeClients, socketIOClient.getNamespace().getRoomOperations(me.getLocation().getName()).getClients());
            }
        }



        List<Object> msgList = new ArrayList<Object>();
        msgList.add("你大爷喊你回家吃饭");
        Packet packet = new Packet(PacketType.MESSAGE);
        packet.setSubType(PacketType.EVENT);
        packet.setName("stream");
        packet.setData(msgList);

        socketIOClient.send(packet);

        return "";

    }

    private void openOrCloseGate(String direction, String action) {

        Map<Integer, Living> cacheMap = UserCacheUtil.getPlayers();
        Player player = (Player) cacheMap.get(this.userId);
        String roomName = player.getLocation().getName();

        Map<String, RoomObjects> roomObjectsCache = UserCacheUtil.getRoomObjectsCache();
        RoomObjects roomObjects = roomObjectsCache.get(roomName);
        if (roomObjects == null) {

            return;
        }

        RoomGateEntity gate = roomObjects.getGates().get(direction);
        //去掉名称的特殊字符
        String name = gate.getName().replaceAll("【", "");
        name = name.replaceAll("】", "");
        String msg = "将" + name + action + "。";

        sendMsg(ZjMudUtil.getScreenLine("你" + msg));
        Collection<SocketIOClient> cl = socketIOClient.getNamespace().getRoomOperations(gate.getEnterRoom()).getClients();
        socketIOClient.getNamespace().getRoomOperations(player.getLocation().getName()).sendEvent("stream", socketIOClient,
                ZjMudUtil.getScreenLine(player.getName() + msg));
        socketIOClient.getNamespace().getRoomOperations(gate.getExitRoom()).sendEvent("stream", socketIOClient,
                ZjMudUtil.getScreenLine(player.getName() + msg));

        if (action.equals("打开")) {

            gate.setStatus(Byte.valueOf("1"));
        } else {

            gate.setStatus(Byte.valueOf("0"));
        }
    }

    private String getAct(Living me, Living target) {

        StringBuffer sb = new StringBuffer();
        if (!target.getInquirys().isEmpty()) {
            sb.append(ZjMudUtil.getObActs2() + ZjMudUtil.getZjMenuF(2, 3, 9, 28));
        } else {
            sb.append(ZjMudUtil.getObActs2() + ZjMudUtil.getZjMenuF(3, 3, 9, 28));
        }

        if (!target.getVendorGoods().isEmpty()) {
            sb.append("购物:list" + +target.getUuid() + ZjMudUtil.ZJ_SEP);
        }

        if (target.getLocation().getNoFight() != 1) {

            sb.append("切磋:fight " + target.getUuid() + ZjMudUtil.ZJ_SEP);
            sb.append("杀死:kill " + target.getUuid() + ZjMudUtil.ZJ_SEP);
        }

        sb.append("偷窃:steal " + target.getUuid() + ZjMudUtil.ZJ_SEP);

        if (target instanceof Human) {
            sb.append("给予:give " + target.getUuid() + ZjMudUtil.ZJ_SEP);
        }

        sb.append("跟随:follow " + target.getUuid());
        if (!target.getSkills().isEmpty()) {
            sb.append(ZjMudUtil.ZJ_SEP + "查看技能:skills " + target.getUuid());
        }
        sb.append("\n");

        return sb.toString();
    }

    private Living getMe() {

        Map<Integer, Living> playerCacheMap = UserCacheUtil.getPlayers();
        Player player = (Player) playerCacheMap.get(this.userId);

        return player;
    }

    private String genderSelf(String sex) {
        if (sex.equals("女性")) {

            return "你";
        } else {
            return "你";
        }
    }

    private String genderPronoun(String sex) {
        if (sex.equals("中性神") || sex.equals("男性") || sex.equals("无性")) {
            return "他";
        } else if (sex.equals("女性")) {
            return "她";
        } else if (sex.equals("雄性") || sex.equals("雌性")) {
            return "它";
        } else {
            return "它";
        }
    }
}
