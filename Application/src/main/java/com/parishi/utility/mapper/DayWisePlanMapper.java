package com.parishi.utility.mapper;

import com.parishi.dto.AssignmentPlanDTO;
import com.parishi.dto.CoursePlanDTO;
import com.parishi.dto.DayWisePlanDTO;
import com.parishi.dto.TopicDTO;
import com.parishi.entity.AssignmentPlan;
import com.parishi.entity.CoursePlan;
import com.parishi.entity.DayWisePlan;
import com.parishi.entity.Topic;

import java.util.ArrayList;
import java.util.List;

public class DayWisePlanMapper {


    public static DayWisePlanDTO fromEntityToDTO(DayWisePlan dayWisePlan) {
        DayWisePlanDTO dayWisePlanDTO = new DayWisePlanDTO();

        dayWisePlanDTO.setPlanId(dayWisePlan.getPlanId());
        dayWisePlanDTO.setSerialNumber(dayWisePlan.getSerialNumber());
        dayWisePlanDTO.setDay(dayWisePlan.getDay());
        dayWisePlanDTO.setPlannedDate(dayWisePlan.getPlannedDate());
        dayWisePlanDTO.setActualDate(dayWisePlan.getActualDate());
        dayWisePlanDTO.setRemarks(dayWisePlan.getRemarks());

        if(dayWisePlan.getTopics()!=null)
            dayWisePlanDTO.setTopics(TopicMapper.fromEntityToDTO(dayWisePlan.getTopics()));


        if(dayWisePlan.getAssignmentPlan()!=null)
            dayWisePlanDTO.setAssignmentPlanDTO(AssignmentPlanMapper.fromEntityToDTO(dayWisePlan.getAssignmentPlan()));
        return dayWisePlanDTO;
    }


    //Method to convert DTO to Entity
    public static DayWisePlan fromDTOToEntity(DayWisePlanDTO dayWisePlanDTO) {

        DayWisePlan dayWisePlan = new DayWisePlan();

        dayWisePlan.setPlanId(dayWisePlanDTO.getPlanId());
        dayWisePlan.setSerialNumber(dayWisePlanDTO.getSerialNumber());
        dayWisePlan.setDay(dayWisePlanDTO.getDay());
        dayWisePlan.setPlannedDate(dayWisePlanDTO.getPlannedDate());
        dayWisePlan.setActualDate(dayWisePlanDTO.getActualDate());
        dayWisePlan.setRemarks(dayWisePlan.getRemarks());

        if(dayWisePlanDTO.getTopics()!=null)
            dayWisePlan.setTopics(TopicMapper.fromDTOToEntity(dayWisePlanDTO.getTopics()));

        if(dayWisePlanDTO.getAssignmentPlanDTO()!=null)
            dayWisePlan.setAssignmentPlan(AssignmentPlanMapper.fromDTOToEntity(dayWisePlanDTO.getAssignmentPlanDTO()));


        return dayWisePlan;

    }
}
