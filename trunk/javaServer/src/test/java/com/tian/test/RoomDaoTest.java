package com.tian.test;

import com.tian.server.dao.ItemDao;
import com.tian.server.entity.ItemEntity;
import junit.framework.TestCase;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by PPX on 2017/6/22.
 */
public class RoomDaoTest extends TestCase {
    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    @Test
    public void testGetList() throws Exception {


        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);

        ItemDao itemDao = new ItemDao();
        List<ItemEntity> list = itemDao.getList();
        for(ItemEntity item : list){

            System.out.println(item.getDesc());
            JSONArray jsonArray = JSONArray.fromObject(item.getJsonAttr());
            for(int i = 0; i < jsonArray.size(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //String tile = jsonObject.getString("tile");
                //System.out.println(jsonObject.get("tile"));
                //System.out.println(jsonObject.get("name"));
            }
        }

    }

}