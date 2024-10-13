package com.parishi.dao;

import com.parishi.dto.AssignmentPlanDTO;
import com.parishi.dto.TraineeDTO;

import java.util.List;
import java.util.Map;

public interface AssignmentDao
{
    AssignmentPlanDTO getAssignmentById(Integer assignmentId);
    boolean saveAssignmentMarks(AssignmentPlanDTO assignmentPlanDTO);
    boolean updateAssignmentTotalScore(AssignmentPlanDTO assignmentPlanDTO);
    List<AssignmentPlanDTO> getAllAssignment();


}
