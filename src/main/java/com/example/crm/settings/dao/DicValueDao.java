package com.example.crm.settings.dao;

import com.example.crm.settings.domain.DicType;
import com.example.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getValuesInDicType(String dicType);
}
