package com.example.crm.settings.service.impl;

import com.example.crm.settings.dao.DicTypeDao;
import com.example.crm.settings.dao.DicValueDao;
import com.example.crm.settings.domain.DicType;
import com.example.crm.settings.domain.DicValue;
import com.example.crm.settings.service.DicService;
import com.example.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);

    @Override
    public Map<String, List<DicValue>> getDicValueInCatalog() {
        Map<String, List<DicValue>> map = new HashMap<>();
        List<DicType> dicTypeList = dicTypeDao.getDicType();
        for (DicType dicType : dicTypeList) {
            List<DicValue> dicValueList = dicValueDao.getValuesInDicType(dicType.getCode());
            map.put(dicType.getCode(), dicValueList);
        }
        return map;
    }
}
