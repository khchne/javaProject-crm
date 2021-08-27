package com.example.crm.workbench.service.impl;

import com.example.crm.settings.dao.UserDao;
import com.example.crm.settings.domain.User;
import com.example.crm.utils.SqlSessionUtil;
import com.example.crm.vo.PaginationVO;
import com.example.crm.workbench.dao.ActivityDao;
import com.example.crm.workbench.dao.ActivityRemarkDao;
import com.example.crm.workbench.domain.Activity;
import com.example.crm.workbench.domain.ActivityRemark;
import com.example.crm.workbench.service.ActivityService;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


    @Override
    public boolean save(Activity activity) {
        boolean flag = true;
        int num = activityDao.save(activity);
        flag = num == 1;
        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(Activity activity, int pageNo, int pageSize) {
        // 取得total
        int total = activityDao.getTotalByCondition(activity);
//        取得activityList
        List<Activity> activities = activityDao.getActivityListByCondition(activity, (pageNo - 1) * pageSize, pageSize);
//        将total 和 activity封装到vo中
        PaginationVO<Activity> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(activities);
//        返回
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        // 删除 市场活动备注表
        int activityRemarkNo = activityRemarkDao.countIds(ids);
        int activityRemarkDeleteNo = activityRemarkDao.delete(ids);
        flag = activityRemarkNo == activityRemarkDeleteNo;
        // 删除 市场活动信息
        int activityNo = ids.length;
        int activityDeleteNo = activityDao.delete(ids);
        flag = activityNo == activityDeleteNo;
        return flag;
    }

    @Override
    public Map<String, Object> getUserAndActivity(String id) {
        Map<String, Object> map = new HashMap<>();
        List<User> userList = userDao.getUserList();
        Activity activity = activityDao.getActivityById(id);
        map.put("userList", userList);
        map.put("activity", activity);
        return map;
    }

    @Override
    public boolean update(Activity activity) {
        boolean flag = true;
        int num = activityDao.update(activity);
        flag = num == 1;
        return flag;
    }

    @Override
    public Activity detail(String id) {
        return activityDao.getActivityDetail(id);
    }

    @Override
    public List<ActivityRemark> getRemarkByAid(String aId) {
        return activityRemarkDao.getRemarkByAid(aId);
    }

    @Override
    public boolean deleteRemark(String id) {

        return activityRemarkDao.deleteRemarkById(id) == 1;
    }

    @Override
    public boolean addRemark(ActivityRemark activityRemark) {
        return 1==activityRemarkDao.addRemark(activityRemark);
    }

    @Override
    public boolean updateRemark(ActivityRemark activityRemark) {
        return activityRemarkDao.updateRemark(activityRemark) == 1;
    }

    @Override
    public List<Activity> getActivityByName(String name) {

        return activityDao.getActivityByName(name);
    }


}
