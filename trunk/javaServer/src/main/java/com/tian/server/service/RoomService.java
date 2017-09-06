package com.tian.server.service;

import com.tian.server.dao.RoomDao;
import com.tian.server.entity.CityEntity;
import com.tian.server.entity.PlayerEntity;
import com.tian.server.entity.RoomEntity;
import com.tian.server.entity.RoomGateEntity;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.model.PlayerLocation;
import com.tian.server.model.RoomObjects;
import com.tian.server.util.UnityCmdUtil;
import com.tian.server.util.UserCacheUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/9/1.
 */
public class RoomService {

    private RoomDao roomDao = new RoomDao();

    public void initRoomCache() {

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

    public JSONArray getRoomDesc(PlayerLocation pl) {

        JSONArray jsonArray = new JSONArray();

        String shortMsg = pl.getLocation().getShortDesc();
        CityEntity cityEntity = UserCacheUtil.getAllCitys().get(pl.getLocation().getPname());
        String cityName = cityEntity.getCname();
        String msg = shortMsg + " [" + cityName + "]";
        String longMsg = pl.getLocation().getLongDesc();

        JSONObject shortObject = UnityCmdUtil.getRoomShortRet(msg);
        JSONObject longObject = UnityCmdUtil.getRoomLongRet(longMsg);
        JSONObject roadObject = getRoomRoadDesc(pl);

        jsonArray.add(shortObject);
        jsonArray.add(longObject);
        jsonArray.add(roadObject);

        return jsonArray;
    }

    private JSONObject getRoomRoadDesc(PlayerLocation pl){

        String contact = "$zj#";
        StringBuffer mapBuffer = new StringBuffer();

        if(pl.getNorth() != null){

            mapBuffer .append( "north:" + pl.getNorth().getShortDesc() + contact);
        }
        if(pl.getSouth() != null){

            mapBuffer.append("south:" + pl.getSouth().getShortDesc() + contact);
        }
        if(pl.getEast() != null){

            mapBuffer.append("east:" + pl.getEast().getShortDesc() + contact);
        }
        if(pl.getWest() != null){

            mapBuffer.append("west:" + pl.getWest().getShortDesc() + contact);
        }
        if(pl.getOut() != null){

            mapBuffer.append("out:" + pl.getOut().getShortDesc() + contact);
        }

        int index = mapBuffer.toString().lastIndexOf(contact);
        if(index > 0){

            mapBuffer = mapBuffer.delete(index, index + contact.length());
            if(mapBuffer.length() < 5){

                mapBuffer.setLength(0);
            }
        }

        return UnityCmdUtil.getRoomRoadRet(mapBuffer.toString());
    }
}
