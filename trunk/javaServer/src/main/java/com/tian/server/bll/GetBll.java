package com.tian.server.bll;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.common.GoodsType;
import com.tian.server.dao.PlayerDao;
import com.tian.server.dao.PlayerPackageDao;
import com.tian.server.entity.PlayerEntity;
import com.tian.server.model.*;
import com.tian.server.util.UnityCmdUtil;
import com.tian.server.util.UserCacheUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/9/20.
 */
public class GetBll extends BaseBll {

    private PlayerPackageDao playerPackageDao = new PlayerPackageDao();

    public GetBll(SocketIOClient socketIOClient) {
        super(socketIOClient);
    }

    public void handleGet(String target){

        String type = target.split("/")[1];
        String id = target.split("#")[1];
        JSONArray jsonArray = new JSONArray();

        if (type.equals("goods")) {

            Map<Long, MudObject> allObjects = UserCacheUtil.getAllObjects();
            GoodsContainer goodsContainer = (GoodsContainer) allObjects.get(Long.valueOf(id));
            if(!goodsContainer.getGoodsEntity().getPickable()){
                jsonArray.add(UnityCmdUtil.getInfoWindowRet("这个物品不能被捡起。"));
                sendMsg(jsonArray);
                return;
            }

            Map<Integer, Living> cacheMap = UserCacheUtil.getPlayers();
            Player player = (Player) cacheMap.get(this.userId);
            if (player == null) {
                jsonArray.add(UnityCmdUtil.getInfoWindowRet("你无法捡起这个物品。"));
                sendMsg(jsonArray);
                return;
            }

            if(goodsContainer.getGoodsEntity().getType() == GoodsType.MONEY.toInteger()){
                //计算金钱金额
                Integer amount = goodsContainer.getGoodsEntity().getValue() * goodsContainer.getCount();
                player.setMoney(player.getMoney() + amount);

                //持久化金钱
                PlayerDao playerDao = new PlayerDao();
                PlayerEntity playerEntity = playerDao.getById(player.getPlayerId());
                playerEntity.setMoney(player.getMoney());
                playerDao.update(playerEntity);

                //把物品从房间移除
                RoomObjects roomObjects = UserCacheUtil.getRoomObjectsCache().get(player.getLocation().getName());
                if(roomObjects != null){

                    roomObjects.getGoods().remove(goodsContainer);
                }

                String msg = "你捡起了" + goodsContainer.getCount().toString() +
                        goodsContainer.getGoodsEntity().getUnit() + goodsContainer.getGoodsEntity().getName();

                JSONObject infoObject = UnityCmdUtil.getInfoWindowRet(msg);
                JSONObject msgObject = new JSONObject();
                msgObject.put("cmd", "look");
                msgObject.put("displayName", goodsContainer.getGoodsEntity().getName());
                msgObject.put("objId", "/goods/goods#" + goodsContainer.getUuid());

                JSONObject outObject = UnityCmdUtil.getObjectOutRet(msgObject);
                jsonArray.add(infoObject);
                jsonArray.add(outObject);
                //发送移除物品的命令
                sendMsg(jsonArray);

                //把物品从AllObjects移除
                allObjects.remove(goodsContainer);
                return;

            }else{

                //把物品赋值给用户
                if(goodsContainer.getGoodsEntity().getStackable()){

                    List<GoodsContainer> goodsContainerList = player.getPackageList();


                }else{


                }

            }
        }


    }

}
