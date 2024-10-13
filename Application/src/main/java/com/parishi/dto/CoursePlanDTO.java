package com.parishi.dto;

import com.parishi.entity.AssignmentPlan;
import com.parishi.entity.Batch;
import com.parishi.entity.DayWisePlan;
import com.parishi.entity.Topic;

import java.util.ArrayList;
import java.util.List;

public class CoursePlanDTO {

    private Integer coursePlanId;

    private BatchDTO batchDto;

    private List<DayWisePlanDTO> dayWisePlanDTOS = new ArrayList<>();


    public Integer getCoursePlanId() {
        return coursePlanId;
    }

    public void setCoursePlanId(Integer coursePlanId) {
        this.coursePlanId = coursePlanId;
    }

    public BatchDTO getBatchDto() {
        return batchDto;
    }

    public void setBatchDto(BatchDTO batchDto) {
        this.batchDto = batchDto;
    }


    public List<DayWisePlanDTO> getDayWisePlanDTOS() {
        return dayWisePlanDTOS;
    }

    public void setDayWisePlanDTOS(List<DayWisePlanDTO> dayWisePlanDTOS) {
        this.dayWisePlanDTOS = dayWisePlanDTOS;
    }

    @Override
    public String toString() {
        return "CoursePlanDTO{" +
                "coursePlanId=" + coursePlanId +
                ", batchDto=" + batchDto +
                ", dayWisePlanDTOS=" + dayWisePlanDTOS +
                '}';
    }
}
