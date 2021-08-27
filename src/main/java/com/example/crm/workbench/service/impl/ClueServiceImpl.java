package com.example.crm.workbench.service.impl;

import com.example.crm.utils.DateTimeUtil;
import com.example.crm.utils.SqlSessionUtil;
import com.example.crm.utils.UUIDUtil;
import com.example.crm.workbench.dao.*;
import com.example.crm.workbench.domain.*;
import com.example.crm.workbench.service.ClueService;

import java.util.List;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    // 客户
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    // 联系人
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    // 交易
    private TransactionDao transactionDao = SqlSessionUtil.getSqlSession().getMapper(TransactionDao.class);
    private TransactionHistoryDao transactionHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TransactionHistoryDao.class);


    @Override
    public boolean saveClue(Clue clue) {
        return 1 == clueDao.saveClue(clue);
    }

    @Override
    public Clue detailClue(String id) {
        return clueDao.detailClue(id);
    }

    @Override
    public List<Activity> getClueActivity(String clueId) {
        return clueActivityRelationDao.getClueActivity(clueId);
    }

    @Override
    public boolean unbound(String id) {
        return clueActivityRelationDao.unbound(id) == 1;
    }

    @Override
    public List<Activity> getActivityRelationSearch(String clueId, String activityName) {
        return clueActivityRelationDao.getActivityRelationSearch(clueId, activityName);
    }

    @Override
    public boolean bound(String clueId, String[] activityIds) {
        int count = 0;
        for (String activityId : activityIds) {
            String id = UUIDUtil.getUUID();
            count += clueActivityRelationDao.bound(id, clueId, activityId);
        }
        return count == activityIds.length;
    }

    @Override
    public Clue getClue(String clueId) {
        return clueDao.getClue(clueId);
    }

    @Override
    public boolean convert(Transaction transaction, String clueId, String createBy) {
        boolean flag = true;
        String createTime = DateTimeUtil.getSysTime();
        Clue clue = clueDao.getClueByClueId(clueId);
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);
        if (customer == null) {
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setOwner(clue.getOwner());
            customer.setContactSummary(clue.getContactSummary());
            customer.setCreateBy(createBy);
            customer.setName(company);
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setDescription(clue.getDescription());
            customer.setCreateTime(createTime);

            if (1 != customerDao.save(customer)) {
                flag = false;
            }
        }
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setAddress(clue.getAddress());
        contacts.setSource(clue.getSource());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setFullname(clue.getFullname());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(clue.getOwner());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setCreateTime(createTime);
        contacts.setCreateBy(createBy);
        if (1 != contactsDao.save(contacts)) {
            flag = false;
        }

        // 备注转换
        List<ClueRemark> clueRemarkList = clueRemarkDao.getClueRemarkByClueId(clueId);
        for (ClueRemark clueRemark : clueRemarkList) {
            String noteContent = clueRemark.getNoteContent();
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCreateBy(createBy);
            customerRemark.setEditFlag("0");
            customerRemark.setCustomerId(customer.getId());
            if (1 != customerRemarkDao.save(customerRemark)) {
                flag = false;
            }

            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setId(UUIDUtil.getUUID());
            if (1 != contactsRemarkDao.save(contactsRemark)) {
                flag = false;
            }
        }
        // 关系转换
        List<String> activityList = clueActivityRelationDao.getActivityId(clueId);
        for (String activity : activityList) {
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelation.setActivityId(activity);
            if (1 != contactsActivityRelationDao.save(contactsActivityRelation)) {
                flag = false;
            }
        }
        if (transaction != null) {
            transaction.setOwner(clue.getOwner());
            transaction.setCustomerId(customer.getId());
            transaction.setSource(clue.getSource());
            transaction.setContactsId(contacts.getId());
            transaction.setDescription(clue.getDescription());
            transaction.setContactSummary(clue.getContactSummary());
            transaction.setNextContactTime(clue.getNextContactTime());
            if (1 != transactionDao.save(transaction)) {
                flag = false;
            } else {
                TransactionHistory transactionHistory = new TransactionHistory();
                transactionHistory.setId(UUIDUtil.getUUID());
                transactionHistory.setStage(clue.getState());
                transactionHistory.setTranId(transaction.getId());
                transactionHistory.setCreateBy(createBy);
                transactionHistory.setCreateTime(createTime);
                transactionHistory.setMoney(transaction.getMoney());
                transactionHistory.setExpectedDate(transaction.getExpectedDate());
                if (1 != transactionHistoryDao.save(transactionHistory)) {
                    flag = false;
                }
            }
        }

        return flag;
    }
}
