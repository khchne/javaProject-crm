package com.example.crm.workbench.service;

import com.example.crm.workbench.domain.Activity;
import com.example.crm.workbench.domain.Clue;
import com.example.crm.workbench.domain.Transaction;

import java.util.List;

public interface ClueService {
    boolean saveClue(Clue clue);

    Clue detailClue(String id);

    List<Activity> getClueActivity(String clueId);

    boolean unbound(String id);

    List<Activity> getActivityRelationSearch(String clueId, String activityName);

    boolean bound(String clueId, String[] activityIds);

    Clue getClue(String clueId);

    boolean convert(Transaction transaction, String clueId, String createBy);
}
