package com.example.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest rep, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        System.out.println("进入到字符乱码过滤器");
        rep.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        chain.doFilter(rep, resp);
    }
}
