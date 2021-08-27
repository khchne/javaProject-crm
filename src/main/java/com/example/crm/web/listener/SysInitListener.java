package com.example.crm.web.listener;

import com.example.crm.settings.domain.DicValue;
import com.example.crm.settings.service.DicService;
import com.example.crm.settings.service.impl.DicServiceImpl;
import com.example.crm.utils.ServiceFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /**
         * sce：该参数能够取得监听的对象
         *      监听的是什么对象，就可以通过该参数取得什么对象
         *      e.g.现在监听的是application，那通过该参数就可以取得application
         */
        System.out.println("ServletContextListener init");
        System.out.println("正在初始化数据字典……");
        ServletContext application = sce.getServletContext();
        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> dicValueMap = dicService.getDicValueInCatalog();
        Set<String> types = dicValueMap.keySet();
        for (String type : types) {
            application.setAttribute(type, dicValueMap.get(type));
        }
        System.out.println("初始化数据字典完成");
        System.out.println("+++++++++++++++++++++++++++++++++++++");
        System.out.println("正在初始化 阶段-可能性对应表 ");
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Stage2Possibility");
        Map<String, String> spMap = new HashMap<>();
        Enumeration<String> ele = resourceBundle.getKeys();

        while (ele.hasMoreElements()) {
            String key = ele.nextElement();
            String value = resourceBundle.getString(key);
            System.out.println("key = " + key + ", value = " + value);
            spMap.put(key, value);
        }
        String possibility = "{}";
        try {
            possibility = new ObjectMapper().writeValueAsString(spMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        application.setAttribute("possibility", possibility);
        System.out.println("初始化 阶段-可能性映射表完成");
    }
}
