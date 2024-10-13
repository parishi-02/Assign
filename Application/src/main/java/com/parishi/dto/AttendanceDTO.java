package com.parishi.dto;

import com.parishi.entity.Trainee;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AttendanceDTO{
    private Integer attendanceId;

    private LocalDate attendanceDate;
    private String attendanceDay;
    private List<TraineeDTO> traineeList;

    public AttendanceDTO() {
    }


    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }


    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getAttendanceDay() {
        return attendanceDay;
    }

    public void setAttendanceDay(String attendanceDay) {
        this.attendanceDay = attendanceDay;
    }

    public List<TraineeDTO> getTraineeList() {
        return traineeList;
    }

    public void setTraineeList(List<TraineeDTO> traineeList) {
        this.traineeList = traineeList;
    }

    @Override
    public int hashCode() {
        return Objects.hash(attendanceId);
    }


    @Override
    public String toString() {
        return "AttendanceDTO{" +
                "attendanceId='" + attendanceId + '\'' +
                ", attendanceDate=" + attendanceDate +
                ", attendanceDay='" + attendanceDay + '\'' +
                ", traineeList=" + traineeList +
                '}';
    }
}
