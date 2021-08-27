package com.example.crm.settings.service.impl;

import com.example.crm.exception.LoginException;
import com.example.crm.settings.dao.UserDao;
import com.example.crm.settings.domain.User;
import com.example.crm.settings.service.UserService;
import com.example.crm.utils.DateTimeUtil;
import com.example.crm.utils.SqlSessionUtil;

import java.util.List;

public class UserServiceImpl implements UserService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        User user = userDao.login(loginAct, loginPwd);
        if (user == null) {
            throw new LoginException("帐号或者密码错误");
        }
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (currentTime.compareTo(expireTime) >= 0) {
            throw new LoginException("帐号已失效");
        }
        String allowIps = user.getAllowIps();
        if (!allowIps.contains(ip)) {
            throw new LoginException("IP地址受限");
        }
        String lockState = user.getLockState();
        if ("0".equals(lockState)) {
            throw new LoginException("帐号已锁定");
        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        return userDao.getUserList();
    }

    @Override
    public List<User> getUserListForClueOwner() {
        return userDao.getUserListForClueOwner();
    }
}
