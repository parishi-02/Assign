package com.parishi.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "track_DayWisePlan")
public class DayWisePlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Integer planId;


    private Integer serialNumber;

    @Column(name="Day")
    private String day;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Topic topics;


    @Column(name = "Planned_Date")
    private LocalDate plannedDate;

    @Column(name = "Actual_Date")
    private LocalDate actualDate;
    @Column(name = "Remarks")
    private String remarks;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Attendance attendance;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private AssignmentPlan assignmentPlan;

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Topic getTopics() {
        return topics;
    }

    public void setTopics(Topic topics) {
        this.topics = topics;
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


    public AssignmentPlan getAssignmentPlan() {
        return assignmentPlan;
    }

    public void setAssignmentPlan(AssignmentPlan assignmentPlan) {
        this.assignmentPlan = assignmentPlan;
    }

    @Override
    public String toString() {
        return "DayWisePlan{" +
                "planId=" + planId +
                ", serialNumber=" + serialNumber +
                ", day='" + day + '\'' +
                ", topics=" + topics +
                ", plannedDate=" + plannedDate +
                ", actualDate=" + actualDate +
                ", remarks='" + remarks + '\'' +
                ", assignmentPlan=" + assignmentPlan +
                '}';
    }
}
