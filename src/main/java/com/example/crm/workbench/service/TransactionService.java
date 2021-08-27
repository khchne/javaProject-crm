package com.example.crm.workbench.service;

import com.example.crm.workbench.domain.Transaction;

import java.util.Map;

public interface TransactionService {
    void save(Transaction transaction);

    Transaction getTran(String id);

    boolean changeStage(String id, Transaction transaction);

    Map<String, Object> getEChartsData();

}
