package com.example.crm.workbench.dao;

import com.example.crm.workbench.domain.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityDao {
    int save(Activity activity);

    List<Activity> getActivityListByCondition(
            @Param("activity") Activity activity, @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

    int getTotalByCondition(@Param("activity") Activity activity);

    int delete(String[] ids);

    Activity getActivityById(String id);

    int update(Activity activity);

    Activity getActivityDetail(String id);

    List<Activity> getActivityByName(String name);
}
