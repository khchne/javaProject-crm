package com.example.settings.test;

import com.example.crm.utils.DateTimeUtil;
import com.example.crm.utils.MD5Util;
import org.junit.Test;

public class TestDateTime {
    @Test
    public void testDateTime() {
        String expireTime = "2019-10-10 10:10:10";
        String currenTime = DateTimeUtil.getSysTime();
        int res = expireTime.compareTo(currenTime);
        /**
         * A.compareTo(B)
         * 字符串A和B进行比较
         * 如果A > B 则返回正数，如果A < B则返回负数，相等返回0
         */
        System.out.println(res);
    }

    @Test
    public void testMD5() {
        String pwd = "kjk12MV_3456";
        pwd = MD5Util.getMD5(pwd);
        System.out.println(pwd);
    }
}
