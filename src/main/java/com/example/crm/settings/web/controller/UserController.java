package com.example.crm.settings.web.controller;

import com.example.crm.settings.domain.User;
import com.example.crm.settings.service.UserService;
import com.example.crm.settings.service.impl.UserServiceImpl;
import com.example.crm.utils.MD5Util;
import com.example.crm.utils.PrintJson;
import com.example.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入成功---");
        String path = request.getServletPath();
        if ("/settings/user/login.do".equals(path)) {
            login(request, response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("这里是登录验证的操作");
        String loginAct = request.getParameter("loginAct");
        String loginPwd = MD5Util.getMD5(request.getParameter("loginPwd"));
        String ip = request.getRemoteAddr();//获取客户端的IP
        System.out.println("ip = " + ip);
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        // 自定义异常，如果登录失败，抛出异常
        // 如果登录成功，则保存到session中
        try {
            User user = us.login(loginAct, loginPwd, ip);
            request.getSession().setAttribute("user", user);
//            登录成功，给前端返回一个 js {success:true}
            PrintJson.printJsonFlag(response, true);
        } catch (Exception e) {
            String msg = e.getMessage();
            /**
             * 作为controller 需要为ajax提供多项信息，有以下 2 种方式 ：
             *  1、将多项信息打包成一个map集合，解析map为json串
             *  2、创建一个vo类，将信息给vo对象，转化为json串
             *
             *  选择方式：
             *      如果对于展现的信息将来还要 多次使用，那创建一个vo类，使用方便；
             *      如果对于展现的信息，仅在一次（少量）需求中用，那 用个map就可以了
             */
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("msg", msg);
            System.out.println("===========msg = " + msg);
            PrintJson.printJsonObj(response, map);
        }
    }
}


/**
 * request.getServletPath()  获取 url-pattern 中的路径地址，不包括 * 的部分
 * /settings/user/login.do
 * <p>
 * request.getPathInfo()    与getServletPath()获取的路径互补，能够得到的是“url-pattern”中*d的路径部分
 * null
 * <p>
 * request.getContextPath() 获取项目的 路径
 * /crm
 * <p>
 * request.getRequestURI()  uri
 * /crm/settings/user/login.do
 * <p>
 * request.getRequestURL()  url
 * http://localhost:8080/crm/settings/user/login.do
 * <p>
 * request.getServletContext().getRealPath() 获取“/”在机器中的实际地址
 * D:\java\javaProject\workspaceForCrm\crm\target\crm-1.0-SNAPSHOT\
 */