package com.example.crm.workbench.dao;

import com.example.crm.workbench.domain.Transaction;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TransactionDao {
    int save(Transaction transaction);

    Transaction getTran(String id);

    int changeStage(@Param("id") String id, @Param("transaction") Transaction transaction);

    int count();

    List<Map<String, Object>> getStageCount();

}
