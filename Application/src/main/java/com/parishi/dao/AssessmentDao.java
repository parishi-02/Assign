package com.parishi.dao;


import com.parishi.dto.AssessmentDTO;

import java.util.List;

public interface AssessmentDao
{
    AssessmentDTO getAssessmentById(Integer assessmentId);
    boolean saveAssessmentMarks(AssessmentDTO assessmentDTO);
    boolean updateAssessment(AssessmentDTO assessmentDTO);
    List<AssessmentDTO> getAllAssessment();


}
