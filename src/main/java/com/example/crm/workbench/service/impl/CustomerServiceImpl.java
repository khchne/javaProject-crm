package com.example.crm.workbench.service.impl;

import com.example.crm.utils.SqlSessionUtil;
import com.example.crm.workbench.dao.CustomerDao;
import com.example.crm.workbench.domain.Customer;
import com.example.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    @Override
    public List<Customer> getCustomerName(String name) {
        return customerDao.getCustomerName(name);
    }
}
