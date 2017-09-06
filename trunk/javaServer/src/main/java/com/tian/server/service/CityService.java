package com.tian.server.service;

import com.tian.server.dao.CityDao;
import com.tian.server.entity.CityEntity;
import com.tian.server.util.UserCacheUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/9/1.
 */
public class CityService {

    private CityDao cityDao = new CityDao();

    public void initCityCache(){

        List<CityEntity> list = cityDao.getList();
        Map<String, CityEntity> cityMaps = UserCacheUtil.getAllCitys();
        for(CityEntity item : list) {

            cityMaps.put(item.getName(), item);
        }
    }

}
