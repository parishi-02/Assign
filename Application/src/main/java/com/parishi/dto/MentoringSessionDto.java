package com.parishi.dto;

import com.parishi.entity.Batch;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

public class MentoringSessionDto {



    private Integer sessionId;

    private String sessionDescription;

    private LocalDate plannedDate;

    private LocalTime plannedTime;


    private LocalDate actualDate;

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
        return "MentoringSessionDto{" +
                "sessionId=" + sessionId +
                ", sessionDescription='" + sessionDescription + '\'' +
                ", plannedDate=" + plannedDate +
                ", plannedTime=" + plannedTime +
                ", actualDate=" + actualDate +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
