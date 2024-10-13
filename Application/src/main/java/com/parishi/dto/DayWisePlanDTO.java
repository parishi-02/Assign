package com.parishi.dto;


import java.time.LocalDate;


public class DayWisePlanDTO {

    private Integer planId;
    private Integer serialNumber;

    private String day;
    private TopicDTO topics;

    private LocalDate plannedDate;

    private LocalDate actualDate;

    private String remarks;

    private AssignmentPlanDTO assignmentPlanDTO;

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public TopicDTO getTopics() {
        return topics;
    }

    public void setTopics(TopicDTO topics) {
        this.topics = topics;
    }

    public AssignmentPlanDTO getAssignmentPlanDTO() {
        return assignmentPlanDTO;
    }

    public void setAssignmentPlanDTO(AssignmentPlanDTO assignmentPlanDTO) {
        this.assignmentPlanDTO = assignmentPlanDTO;
    }

    public LocalDate getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(LocalDate plannedDate) {
        this.plannedDate = plannedDate;
    }

    public LocalDate getActualDate() {
        return actualDate;
    }

    public void setActualDate(LocalDate actualDate) {
        this.actualDate = actualDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "DayWisePlanDTO{" +
                "planId=" + planId +
                ", serialNumber=" + serialNumber +
                ", day='" + day + '\'' +
                ", topics=" + topics +
                ", plannedDate=" + plannedDate +
                ", actualDate=" + actualDate +
                ", remarks='" + remarks + '\'' +
                ", assignmentPlanDTO=" + assignmentPlanDTO +
                '}';
    }
}
