package com.example.crm.workbench.dao;

import com.example.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {
    Customer getCustomerByName(String company);

    int save(Customer customer);

    List<Customer> getCustomerName(String name);
}
