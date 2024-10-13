package com.parishi.utility.mapper;

import com.parishi.dto.AssessmentDTO;
import com.parishi.dto.MentoringSessionDto;
import com.parishi.entity.Assessment;
import com.parishi.entity.MentoringSession;

public class MentoringSessionMapper {

    //Method to convert Entity to DTO
    public static MentoringSessionDto fromEntityToDTO(MentoringSession mentoringSession) {

        MentoringSessionDto mentoringSessionDto = new MentoringSessionDto();

        mentoringSessionDto.setSessionId(mentoringSession.getSessionId());
        mentoringSessionDto.setSessionDescription(mentoringSession.getSessionDescription());
        mentoringSessionDto.setPlannedDate(mentoringSession.getPlannedDate());
        mentoringSessionDto.setActualDate(mentoringSession.getActualDate());
        mentoringSessionDto.setRemarks(mentoringSession.getRemarks());
        mentoringSessionDto.setPlannedTime(mentoringSession.getPlannedTime());

        return mentoringSessionDto;

    }


    //Method to convert DTO to Entity
    public static MentoringSession fromDTOToEntity(MentoringSessionDto mentoringSessionDto) {

        MentoringSession mentoringSession = new MentoringSession();

        mentoringSession.setSessionId(mentoringSessionDto.getSessionId());
        mentoringSession.setPlannedDate(mentoringSessionDto.getPlannedDate());
        mentoringSession.setActualDate(mentoringSessionDto.getActualDate());
        mentoringSession.setSessionDescription(mentoringSessionDto.getSessionDescription());
        mentoringSession.setRemarks(mentoringSessionDto.getRemarks());
        mentoringSession.setPlannedTime(mentoringSessionDto.getPlannedTime());
        return mentoringSession;


    }

}
