package com.parishi.utility.mapper;

import com.parishi.dto.AssessmentDTO;
import com.parishi.dto.AttendanceDTO;
import com.parishi.entity.Assessment;
import com.parishi.entity.Attendance;

public class AssessmentMapper {

    //Method to convert Entity to DTO
    public static AssessmentDTO fromEntityToDTO(Assessment assessment) {
       AssessmentDTO assessmentDTO = new AssessmentDTO();

       assessmentDTO.setAssessmentId(assessment.getAssessmentId());
        assessmentDTO.setSubTopic(assessment.getSubTopic());
        assessmentDTO.setTotalScore(assessment.getTotalScore());
        assessmentDTO.setActualDate(assessment.getActualDate());
        assessmentDTO.setRemarks(assessment.getRemarks());
        assessmentDTO.setPlannedDate(assessment.getPlannedDate());
        assessmentDTO.setMarksObtained(assessment.getMarksObtained());

        return assessmentDTO;
    }


    //Method to convert DTO to Entity
    public static Assessment fromDTOToEntity(AssessmentDTO assessmentDTO) {
        Assessment assessment = new Assessment();

        assessment.setAssessmentId(assessmentDTO.getAssessmentId());
        assessment.setSubTopic(assessmentDTO.getSubTopic());
        assessment.setTotalScore(assessmentDTO.getTotalScore());
        assessment.setRemarks(assessmentDTO.getRemarks());
        assessment.setPlannedDate(assessmentDTO.getPlannedDate());
        assessment.setActualDate(assessmentDTO.getActualDate());
        assessment.setMarksObtained(assessmentDTO.getMarksObtained());

        return assessment;
    }

}
