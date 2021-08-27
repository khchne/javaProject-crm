package com.example.crm.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入到拦截系统");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI();
        System.out.println("uri = " + path);
        HttpSession session = request.getSession(false);
        /**
         * 重定向
         *      1、重定向的路径怎么写
         *      在实际项目中，对于路径的使用，不论是操作前端还是后端，应该一律使用绝对路径
         *          关于转发和重定向的路径写法：
         *          转发：
         *              使用的是一种特殊的绝对路径的使用方式，这种绝对路径前面 不加 /项目名，这种路径也称之为内部路径
         *              /login.jsp
         *          重定向：
         *              使用的是传统绝对路径的写法前面 必须以 /项目名开头，后面跟具体的资源路径
         *              /crm/login.jsp
         *
         *      2、为什么使用重定向，使用转发不行吗？
         *          转发之后，浏览器中的地址会停留在老路径上，而不是跳转之后的最新资源路径
         *          我们应该在为用户跳转到登录页（最新页面）的同时，将浏览器的地址栏设置为 当前登录页（当前最新页面的路径）
         *
         */
        if (path.contains("login")) {
            filterChain.doFilter(request, response);
        } else if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
//            动态难道当前项目名的路径！！
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
