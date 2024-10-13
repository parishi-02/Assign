package com.parishi.dto;

import java.util.Map;

public class AssignmentPlanDTO {

    private Integer id;

    private String description;
    private Double totalScore;
    private Map<Integer,Double> marksObtained;

    public Map<Integer, Double> getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(Map<Integer, Double> marksObtained) {
        this.marksObtained = marksObtained;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }


    @Override
    public String toString() {
        return "AssignmentPlanDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", totalScore=" + totalScore +
                ", marksObtained=" + marksObtained +
                '}';
    }
}
