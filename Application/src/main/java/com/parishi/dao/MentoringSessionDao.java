package com.parishi.dao;

import com.parishi.dto.BatchDTO;
import com.parishi.dto.MentoringSessionDto;

import java.util.List;

public interface MentoringSessionDao
{
    MentoringSessionDto getMentoringSessionById(Integer sessionId);

    boolean updateMentoringSession(MentoringSessionDto mentoringSessionDto);

    List<MentoringSessionDto> getAllMentoringSession();
    boolean addMentoringSession(BatchDTO batchDTO);
}
