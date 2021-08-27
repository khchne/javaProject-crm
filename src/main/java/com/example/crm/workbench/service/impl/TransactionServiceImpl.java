package com.example.crm.workbench.service.impl;

import com.example.crm.utils.DateTimeUtil;
import com.example.crm.utils.SqlSessionUtil;
import com.example.crm.utils.UUIDUtil;
import com.example.crm.workbench.dao.CustomerDao;
import com.example.crm.workbench.dao.TransactionDao;
import com.example.crm.workbench.dao.TransactionHistoryDao;
import com.example.crm.workbench.domain.Customer;
import com.example.crm.workbench.domain.Transaction;
import com.example.crm.workbench.domain.TransactionHistory;
import com.example.crm.workbench.service.TransactionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionServiceImpl implements TransactionService {
    private TransactionDao transactionDao =
            SqlSessionUtil.getSqlSession().getMapper(TransactionDao.class);
    private TransactionHistoryDao transactionHistoryDao =
            SqlSessionUtil.getSqlSession().getMapper(TransactionHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public void save(Transaction transaction) {
        String customerName = transaction.getCustomerId();
        Customer customer = customerDao.getCustomerByName(customerName);
        if (customer == null) {
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setOwner(transaction.getOwner());
            customer.setContactSummary(transaction.getContactSummary());
            customer.setNextContactTime(transaction.getNextContactTime());
            customer.setDescription(transaction.getDescription());
            customer.setName(customerName);
            customer.setCreateBy(transaction.getCreateBy());
        }

        transaction.setCustomerId(customer.getId());
        transactionDao.save(transaction);
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setId(UUIDUtil.getUUID());
        transactionHistory.setCreateTime(DateTimeUtil.getSysTime());
        transactionHistory.setTranId(transactionHistory.getTranId());
        transactionHistory.setMoney(transaction.getMoney());
        transactionHistory.setExpectedDate(transaction.getExpectedDate());
        transactionHistory.setStage(transaction.getStage());
        transactionHistory.setCreateBy(transaction.getCreateBy());
        transactionHistory.setTranId(transaction.getId());
        transactionHistoryDao.save(transactionHistory);
    }

    @Override
    public Transaction getTran(String id) {
        return transactionDao.getTran(id);
    }

    @Override
    public boolean changeStage(String id, Transaction transaction) {
        boolean flag = false;
        int cnt = transactionDao.changeStage(id, transaction);
        if (cnt == 1) {
            TransactionHistory th = new TransactionHistory();
            th.setTranId(transaction.getId());
            th.setMoney(transaction.getMoney());
            th.setStage(transaction.getStage());
            th.setCreateBy(transaction.getEditBy());
            th.setCreateTime(transaction.getEditTime());
            th.setId(UUIDUtil.getUUID());
            th.setExpectedDate(transaction.getExpectedDate());
            flag = 1 == transactionHistoryDao.save(th);
        }
        return flag;
    }

    @Override
    public Map<String, Object> getEChartsData() {
        Map<String,Object> map = new HashMap<>();
        int total = transactionDao.count();
        List<Map<String, Object>> list = transactionDao.getStageCount();
        map.put("total", total);
        map.put("dataList", list);
        return map;
    }
}
