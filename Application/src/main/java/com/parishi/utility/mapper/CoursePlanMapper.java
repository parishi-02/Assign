package com.parishi.utility.mapper;

import com.parishi.dto.*;
import com.parishi.entity.*;

import java.util.ArrayList;
import java.util.List;

public class CoursePlanMapper {

    public static CoursePlanDTO fromEntityToDTO(CoursePlan coursePlan) {
       CoursePlanDTO coursePlanDTO = new CoursePlanDTO();

       coursePlanDTO.setCoursePlanId(coursePlan.getCoursePlanId());

       if(coursePlan.getBatch()!=null)
            coursePlanDTO.setBatchDto(BatchMapper.fromEntityToDTO(coursePlan.getBatch()));

       List<DayWisePlan> dayWisePlans = coursePlan.getDayWisePlans();
       List<DayWisePlanDTO> dayWisePlanDTOS = new ArrayList<>();

       if(dayWisePlans!=null)
       {
           for(DayWisePlan dayWisePlan : dayWisePlans)
           {
               DayWisePlanDTO dayWisePlanDTO = DayWisePlanMapper.fromEntityToDTO(dayWisePlan);
               dayWisePlanDTOS.add(dayWisePlanDTO);
           }
           coursePlanDTO.setDayWisePlanDTOS(dayWisePlanDTOS);
       }

       return coursePlanDTO;
    }



    //Method to convert DTO to Entity
    public static CoursePlan fromDTOToEntity(CoursePlanDTO coursePlanDTO) {
       CoursePlan coursePlan = new CoursePlan();

       coursePlan.setCoursePlanId(coursePlanDTO.getCoursePlanId());

       if(coursePlanDTO.getBatchDto()!=null)
            coursePlan.setBatch(BatchMapper.fromDTOToEntity(coursePlanDTO.getBatchDto()));

       List<DayWisePlanDTO> dayWisePlanDTOS = coursePlanDTO.getDayWisePlanDTOS();
       List<DayWisePlan> dayWisePlans = new ArrayList<>();
       if(dayWisePlanDTOS!=null)
       {
           for(DayWisePlanDTO dayWisePlanDTO : dayWisePlanDTOS)
           {
               DayWisePlan dayWisePlan = DayWisePlanMapper.fromDTOToEntity(dayWisePlanDTO);
               dayWisePlans.add(dayWisePlan);
           }
           coursePlan.setDayWisePlans(dayWisePlans);
       }
       return coursePlan;
    }
}
