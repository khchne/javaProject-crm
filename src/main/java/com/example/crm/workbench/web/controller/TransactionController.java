package com.example.crm.workbench.web.controller;

import com.example.crm.settings.domain.User;
import com.example.crm.settings.service.UserService;
import com.example.crm.settings.service.impl.UserServiceImpl;
import com.example.crm.utils.DateTimeUtil;
import com.example.crm.utils.PrintJson;
import com.example.crm.utils.ServiceFactory;
import com.example.crm.utils.UUIDUtil;
import com.example.crm.workbench.domain.Customer;
import com.example.crm.workbench.domain.Transaction;
import com.example.crm.workbench.domain.TransactionHistory;
import com.example.crm.workbench.service.CustomerService;
import com.example.crm.workbench.service.TransactionHistoryService;
import com.example.crm.workbench.service.TransactionService;
import com.example.crm.workbench.service.impl.CustomerServiceImpl;
import com.example.crm.workbench.service.impl.TransactionHistoryServiceImpl;
import com.example.crm.workbench.service.impl.TransactionServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到交易市场活动的控制器");
        String path = request.getServletPath();
        if ("/workbench/transaction/createTran.do".equals(path)) {
            createPath(request, response);
        } else if ("/workbench/transaction/getCustomerName.do".equals(path)) {
            getCustomerName(request, response);
        } else if ("/workbench/transaction/saveTran.do".equals(path)) {
            saveTran(request, response);
        } else if ("/workbench/transaction/detail.do".equals(path)) {
            detail(request, response);
        } else if ("/workbench/transaction/getTranHistory.do".equals(path)) {
            getTranHistory(request, response);
        } else if ("/workbench/transaction/changeStage.do".equals(path)) {
            changeStage(request, response);
        } else if ("/workbench/transaction/getEChartsData.do".equals(path)) {
            getEChartsData(request, response);
        }
    }

    private void getEChartsData(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取ECHARTS图表数据的控制器");
        TransactionService transactionService = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        Map<String, Object> map = transactionService.getEChartsData();
        PrintJson.printJsonObj(response, map);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到修改状态阶段");
        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String expectedDate = request.getParameter("expectedDate");
        String money = request.getParameter("money");
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setStage(stage);
        transaction.setEditBy(editBy);
        transaction.setEditTime(editTime);
        transaction.setExpectedDate(expectedDate);
        transaction.setMoney(money);
        TransactionService transactionService = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        boolean flag = transactionService.changeStage(id, transaction);
        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("tran", transaction);
        PrintJson.printJsonObj(response, map);
    }

    private void getTranHistory(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取交易历史ing ");
        String id = request.getParameter("id");
        TransactionHistoryService transactionHistoryService = (TransactionHistoryService) ServiceFactory.getService(new TransactionHistoryServiceImpl());
        List<TransactionHistory> transactionHistoryList = transactionHistoryService.getTranHistory(id);
        PrintJson.printJsonObj(response, transactionHistoryList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("即将进入到交易的详细信息页");
        String id = request.getParameter("id");
        TransactionService transactionService = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        Transaction transaction = transactionService.getTran(id);
        request.setAttribute("tran", transaction);
        System.out.println(transaction);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);
    }

    private void saveTran(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("进入到交易活动保存系统……");
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("TransactionName");
        String expectedClosingDate = request.getParameter("expectedClosingDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activitySrc");
        String contactId = request.getParameter("contactsName");
        String description = request.getParameter("description");
        String contactSummery = request.getParameter("contactsSummery");
        String nextContactTime = request.getParameter("nextContactTime");
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setOwner(owner);
        transaction.setMoney(money);
        transaction.setName(name);
        transaction.setExpectedDate(expectedClosingDate);
        transaction.setCustomerId(customerName);
        transaction.setStage(stage);
        transaction.setType(type);
        transaction.setSource(source);
        transaction.setActivityId(activityId);
        transaction.setContactsId(contactId);
        transaction.setDescription(description);
        transaction.setContactSummary(contactSummery);
        transaction.setNextContactTime(nextContactTime);
        transaction.setCreateBy(createBy);
        transaction.setCreateTime(createTime);

        TransactionService transactionService = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        transactionService.save(transaction);
        response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到getCustomerName中");
        String name = request.getParameter("name");
        CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<Customer> customerList = customerService.getCustomerName(name);
        PrintJson.printJsonObj(response, customerList);
    }

    private void createPath(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到创建按钮的地盘");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();
        request.setAttribute("userList", userList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);
    }
}
