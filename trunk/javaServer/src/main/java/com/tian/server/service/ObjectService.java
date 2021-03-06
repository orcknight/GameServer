package com.tian.server.service;

import com.tian.server.dao.PlayerPackageDao;
import com.tian.server.model.*;
import com.tian.server.util.UnityCmdUtil;
import com.tian.server.util.UserCacheUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by PPX on 2017/10/12.
 */
public class ObjectService {

    PlayerPackageDao playerPackageDao = new PlayerPackageDao();

    public void destruct(MudObject ob) {

        if(UserCacheUtil.getAllObjects().containsKey(ob.getUuid())){
            UserCacheUtil.getAllObjects().remove(ob.getUuid());
        }

        if(UserCacheUtil.getAllLivings().contains(ob)){
            UserCacheUtil.getAllLivings().remove(ob);
        }

        //如果存在房间中，就要把他从房间中删除
        if(ob.getLocation() != null){
            RoomObjects roomObjects = UserCacheUtil.getRoomObjectsCache().get(ob.getLocation().getName());
            if(roomObjects != null){
                if(roomObjects.getGoods().containsValue(ob)){
                    roomObjects.getGoods().remove(ob.getUuid());
                }
                if(roomObjects.getNpcs().containsValue(ob)){
                    roomObjects.getNpcs().remove(ob.getUuid());
                }
            }
        }

        MessageService messageService = new MessageService();
        JSONArray jsonArray = new JSONArray();
        String type = "npc";
        if(ob instanceof Player){

            type = "user";
        }else if(ob instanceof  Living){
            type = "npc";
        } else {
            type = "goods";

            //Todo:数据库中删除
            //如果是物品，并且属于某个人，执行删除命令
            GoodsContainer goodsContainer = (GoodsContainer)ob;
            if(goodsContainer.getBelongsId() != null && goodsContainer.getBelongsId() > 0){
                Player player = UserCacheUtil.getPlayerById(goodsContainer.getBelongsId());
                if(player != null) {
                    if (player.getPackageList().contains(goodsContainer)) {
                        playerPackageDao.delete(goodsContainer.getBelongsInfo());
                        player.getPackageList().remove(goodsContainer);
                    }
                }
            }
        }

        JSONObject msgObject = new JSONObject();
        msgObject.put("cmd", "look");
        msgObject.put("displayName", ob.getUuid().toString());
        msgObject.put("objId", "/" + type + "/" + type + "#" + ob.getUuid().toString());
        jsonArray.add(UnityCmdUtil.getObjectOutRet(msgObject));
        if(ob.getLocation() != null) {
            messageService.tellRoom(ob.getLocation().getName(), jsonArray);
        }

        ob = null;
    }
}
