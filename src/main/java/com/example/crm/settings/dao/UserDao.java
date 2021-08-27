package com.example.crm.settings.dao;

import com.example.crm.settings.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    User login(@Param("loginAct") String loginAct, @Param("loginPwd") String loginPwd);

    List<User> getUserList();

    List<User> getUserListForClueOwner();

}
