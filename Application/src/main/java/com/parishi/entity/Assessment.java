package com.parishi.entity;


import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "track_assessment")
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Assessment_Id")
    private Integer assessmentId;

    @Column(name = "Assessment_Description")
    private String subTopic;

    @Column(name = "Planned_Date")
    private LocalDate plannedDate;

    @Column(name = "Actual_Date")
    private LocalDate actualDate;

    @Column(name="Total_Score")
    private Double totalScore;

    @Column(name = "Remarks")
    private String remarks;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private Map<Integer,Double> marksObtained;

    public Map<Integer, Double> getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(Map<Integer, Double> marksObtained) {
        this.marksObtained = marksObtained;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
        return "Assessment{" +
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
