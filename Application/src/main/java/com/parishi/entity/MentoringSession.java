package com.parishi.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "track_mentoring_session")
public class MentoringSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Session_id")
    private Integer sessionId;

    @Column(name = "Session_Description")
    private String sessionDescription;

    @Column(name = "Planned_Date")
    private LocalDate plannedDate;

    @Column(name="Session_Time")
    private LocalTime plannedTime;

    @Column(name = "Actual_Date")
    private LocalDate actualDate;

    @Column(name = "Remarks")
    private String remarks;

    public LocalTime getPlannedTime() {
        return plannedTime;
    }

    public void setPlannedTime(LocalTime plannedTime) {
        this.plannedTime = plannedTime;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }


    public String getSessionDescription() {
        return sessionDescription;
    }

    public void setSessionDescription(String sessionDescription) {
        this.sessionDescription = sessionDescription;
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

    @Override
    public String toString() {
        return "MentoringSession{" +
                "sessionId=" + sessionId +
                ", sessionDescription='" + sessionDescription + '\'' +
                ", plannedDate=" + plannedDate +
                ", plannedTime=" + plannedTime +
                ", actualDate=" + actualDate +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
