package com.example.crm.workbench.dao;

import com.example.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int countIds(String[] ids);

    int delete(String[] ids);

    List<ActivityRemark> getRemarkByAid(String aId);

    int deleteRemarkById(String id);

    int addRemark(ActivityRemark activityRemark);

    int updateRemark(ActivityRemark activityRemark);
}
