package com.example.crm.workbench.web.controller;

import com.example.crm.settings.domain.User;
import com.example.crm.settings.service.UserService;
import com.example.crm.settings.service.impl.UserServiceImpl;
import com.example.crm.utils.DateTimeUtil;
import com.example.crm.utils.PrintJson;
import com.example.crm.utils.ServiceFactory;
import com.example.crm.utils.UUIDUtil;
import com.example.crm.workbench.domain.Activity;
import com.example.crm.workbench.domain.Clue;
import com.example.crm.workbench.domain.Transaction;
import com.example.crm.workbench.service.ActivityService;
import com.example.crm.workbench.service.ClueService;
import com.example.crm.workbench.service.impl.ActivityServiceImpl;
import com.example.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到线索活动的service集群");
        String path = request.getServletPath();
        System.out.println("path = " + path);
        if ("/workbench/clue/getUserList.do".equals(path)) {
            getUserList(request, response);
        } else if ("/workbench/clue/saveClue.do".equals(path)) {
            saveClue(request, response);
        } else if ("/workbench/clue/detail.do".equals(path)) {
            detailClue(request, response);
        } else if ("/workbench/clue/getClueActivity.do".equals(path)) {
            getClueActivity(request, response);
        } else if ("/workbench/clue/unbound.do".equals(path)) {
            unbound(request, response);
        } else if ("/workbench/clue/activityRelationSearch.do".equals(path)) {
            activityRelationSearch(request, response);
        } else if ("/workbench/clue/bound.do".equals(path)) {
            bound(request, response);
        } else if ("/workbench/clue/convertPage.do".equals(path)) {
            convertPage(request, response);
        } else if ("/workbench/clue/getActivityListByName.do".equals(path)) {
            getActivityListByName(request, response);
        } else if ("/workbench/clue/convert.do".equals(path)) {
            convert(request, response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到线索转换系统");
        String clueId = request.getParameter("clueId");
        String flag = request.getParameter("flag");
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        Transaction transaction = null;
        if ("true".equals(flag)) {
            // 前端勾选了 为客户创建交易
            transaction = new Transaction();
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();
            String activityId = request.getParameter("activityId");
            String money = request.getParameter("money");
            String tradeName = request.getParameter("tradeName");
            String expectedTradedDate = request.getParameter("expectedTradedDate");
            String stage = request.getParameter("stage");
            transaction.setId(id);
            transaction.setCreateTime(createTime);
            transaction.setCreateBy(createBy);
            transaction.setActivityId(activityId);
            transaction.setMoney(money);
            transaction.setName(tradeName);
            transaction.setExpectedDate(expectedTradedDate);
            transaction.setStage(stage);
        }

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean convertFlag = clueService.convert(transaction, clueId, createBy);
        if (convertFlag) {
            //用的是绝对路径 ！！
            response.sendRedirect(request.getContextPath() + "/workbench/clue/index.jsp");
        } else {
            System.out.println("hooo转换失败");
        }
    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到搜索市场活动信息列表");
        String name = request.getParameter("name");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activityList = activityService.getActivityByName(name);
        PrintJson.printJsonObj(response, activityList);
    }

    private void convertPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("即将跳转到转换页面，3，2，1……");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String clueId = request.getParameter("clueId");
        Clue clue = clueService.getClue(clueId);
        request.setAttribute("clue", clue);
        request.getRequestDispatcher("/workbench/clue/convert.jsp").forward(request, response);
    }

    private void bound(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到关联市场活动系统");
        String clueId = request.getParameter("clueId");
        String[] activityIds = request.getParameterValues("activityId");
        System.out.println(activityIds.toString());
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = clueService.bound(clueId, activityIds);
        PrintJson.printJsonFlag(response, flag);
    }

    private void activityRelationSearch(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到关联市场活动查询ing ");
        String clueId = request.getParameter("clueId");
        String activityName = request.getParameter("activityName");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<Activity> activityList = clueService.getActivityRelationSearch(clueId, activityName);
        PrintJson.printJsonObj(response, activityList);
    }

    private void unbound(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到解除关联操作系统");
        String id = request.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = clueService.unbound(id);
        PrintJson.printJsonFlag(response, flag);
    }

    private void getClueActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到clue中的详细信息页中  的 市场 活动  ");
        String clueId = request.getParameter("clueId");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<Activity> activityList = clueService.getClueActivity(clueId);
        PrintJson.printJsonObj(response, activityList);
    }

    private void detailClue(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到detailClue");
        String id = request.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue = clueService.detailClue(id);
        request.setAttribute("clue", clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);
    }

    private void saveClue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到clue中的saveClue");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String appellation = request.getParameter("appellation");
        String name = request.getParameter("name");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String clueState = request.getParameter("clueState");
        String clueSource = request.getParameter("clueSource");
        String clueDescription = request.getParameter("clueDescription");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        Clue clue = new Clue();

        clue.setId(id);
        clue.setFullname(name);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(clueState);
        clue.setSource(clueSource);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(clueDescription);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);
        boolean flag = clueService.saveClue(clue);
        PrintJson.printJsonFlag(response, flag);
    }


    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到clue中的getUserList");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserListForClueOwner();
        PrintJson.printJsonObj(response, userList);
    }
}
