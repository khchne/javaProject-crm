package com.example.crm.workbench.dao;

import com.example.crm.workbench.domain.TransactionHistory;

import java.util.List;

public interface TransactionHistoryDao {
    int save(TransactionHistory transactionHistory);

    List<TransactionHistory> getTranHistory(String id);
}
