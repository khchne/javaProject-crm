package com.example.crm.workbench.dao;

import com.example.crm.workbench.domain.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClueActivityRelationDao {
    List<Activity> getClueActivity(String clueId);

    int unbound(String id);

    List<Activity> getActivityRelationSearch(@Param("clueId") String clueId, @Param("activityName") String activityName);

    int bound(@Param("id") String id, @Param("clueId") String clueId, @Param("activityId") String activityId);

    List<String> getActivityId(String clueId);
}
