package com.example.crm.workbench.dao;

import com.example.crm.workbench.domain.Clue;

public interface ClueDao {
    int saveClue(Clue clue);

    Clue detailClue(String id);

    Clue getClue(String clueId);

    Clue getClueByClueId(String clueId);
}
