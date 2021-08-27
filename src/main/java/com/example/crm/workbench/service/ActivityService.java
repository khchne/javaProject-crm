package com.example.crm.workbench.service;

import com.example.crm.vo.PaginationVO;
import com.example.crm.workbench.domain.Activity;
import com.example.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean save(Activity activity);

    PaginationVO<Activity> pageList(Activity activity, int pageNo, int pageSize);

    boolean delete(String[] ids);

    Map<String, Object> getUserAndActivity(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkByAid(String aId);

    boolean deleteRemark(String id);

    boolean addRemark(ActivityRemark activityRemark);

    boolean updateRemark(ActivityRemark activityRemark);

    List<Activity> getActivityByName(String name);
}
