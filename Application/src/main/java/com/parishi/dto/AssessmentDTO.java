package com.parishi.dto;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.Map;

public class AssessmentDTO {

    private Integer assessmentId;
    private String subTopic;
    private LocalDate plannedDate;

    private LocalDate actualDate;

    private Double totalScore;

    private String remarks;

    private Map<Integer,Double> marksObtained;

    public Map<Integer, Double> getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(Map<Integer, Double> marksObtained) {
        this.marksObtained = marksObtained;
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

    public Integer getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Integer assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(String subTopic) {
        this.subTopic = subTopic;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }


    @Override
    public String toString() {
        return "AssessmentDTO{" +
                "assessmentId=" + assessmentId +
                ", subTopic='" + subTopic + '\'' +
                ", plannedDate=" + plannedDate +
                ", actualDate=" + actualDate +
                ", totalScore=" + totalScore +
                ", remarks='" + remarks + '\'' +
                ", marksObtained=" + marksObtained +
                '}';
    }
}
