package com.tian.server.bll;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.model.GoodsContainer;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.service.PlayerService;
import com.tian.server.util.UnityCmdUtil;
import com.tian.server.util.UserCacheUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Map;

/**
 * Created by PPX on 2017/9/22.
 */
public class InfoBll extends BaseBll {

    public InfoBll(SocketIOClient socketIOClient) {
        super(socketIOClient);
    }

    public void handleBag(){

        Map<Integer, Living> cacheMap = UserCacheUtil.getPlayers();
        Player player = (Player) cacheMap.get(this.userId);
        PlayerService playerService = new PlayerService();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        Long gold = player.getMoney() / 10000L;
        Long sliver = (player.getMoney() % 10000L) /100L;
        Long coin = player.getMoney() % 100L;

        String moneyStr = String.format("%d金%02d银%02d铜", gold, sliver, coin);
        jsonObject.put("money", moneyStr);
        jsonObject.put("load", Math.ceil(player.getEncumbrance() * 100 / playerService.getMaxEncumbrance(player))  + "%负重");
        jsonObject.put("ticket", player.getTicket().toString() + "元宝");

        JSONArray itemsArray = new JSONArray();
        for(GoodsContainer goodsContainer: player.getPackageList()){

            JSONObject itemObject = new JSONObject();
            itemObject.put("cmd", "look");
            itemObject.put("displayName", goodsContainer.getGoodsEntity().getName());
            itemObject.put("objId", "/goods/goods#" + goodsContainer.getUuid().toString());
            itemsArray.add(itemObject);
        }
        jsonObject.put("items", itemsArray);
        jsonArray.add(UnityCmdUtil.getBagPopRet(jsonObject));
        sendMsg(jsonArray);
    }
}
