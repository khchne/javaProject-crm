package com.example.crm.workbench.service;

import com.example.crm.workbench.domain.TransactionHistory;

import java.util.List;

public interface TransactionHistoryService {
    List<TransactionHistory> getTranHistory(String id);
}
