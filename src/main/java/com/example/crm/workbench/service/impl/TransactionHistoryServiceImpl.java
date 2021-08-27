package com.example.crm.workbench.service.impl;

import com.example.crm.utils.SqlSessionUtil;
import com.example.crm.workbench.dao.TransactionHistoryDao;
import com.example.crm.workbench.domain.TransactionHistory;
import com.example.crm.workbench.service.TransactionHistoryService;

import java.util.List;

public class TransactionHistoryServiceImpl implements TransactionHistoryService {
    private TransactionHistoryDao transactionHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TransactionHistoryDao.class);
    @Override
    public List<TransactionHistory> getTranHistory(String id) {
        return transactionHistoryDao.getTranHistory(id);
    }
}
