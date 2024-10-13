package com.parishi.utility.mapper;

import com.parishi.dto.AssignmentPlanDTO;
import com.parishi.dto.BatchDTO;
import com.parishi.dto.TraineeDTO;
import com.parishi.entity.AssignmentPlan;
import com.parishi.entity.Batch;
import com.parishi.entity.Trainee;

import java.util.ArrayList;
import java.util.List;

public class AssignmentPlanMapper {

    public static AssignmentPlanDTO fromEntityToDTO(AssignmentPlan assignmentPlan) {

        AssignmentPlanDTO assignmentPlanDTO = new AssignmentPlanDTO();
        assignmentPlanDTO.setId(assignmentPlan.getId());
        assignmentPlanDTO.setDescription(assignmentPlan.getDescription());
        assignmentPlanDTO.setTotalScore(assignmentPlan.getTotalScore());
        assignmentPlanDTO.setMarksObtained(assignmentPlan.getMarksObtained());
        return assignmentPlanDTO;
    }



    //Method to convert DTO to Entity
    public static AssignmentPlan fromDTOToEntity(AssignmentPlanDTO assignmentPlanDTO) {

        AssignmentPlan assignmentPlan = new AssignmentPlan();
        assignmentPlan.setId(assignmentPlanDTO.getId());
        assignmentPlan.setDescription(assignmentPlanDTO.getDescription());
        assignmentPlan.setTotalScore(assignmentPlanDTO.getTotalScore());
        assignmentPlan.setMarksObtained(assignmentPlanDTO.getMarksObtained());

        return assignmentPlan;

    }
}
