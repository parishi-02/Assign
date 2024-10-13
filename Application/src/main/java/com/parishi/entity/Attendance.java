package com.parishi.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "track_attendance")
@DynamicInsert
@DynamicUpdate
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer attendanceId;

    private LocalDate attendanceDate;
    private String attendanceDay;

    @ManyToMany
    @JoinTable(
            name = "attendance_trainee",
            joinColumns = @JoinColumn(name = "attendanceId"),
            inverseJoinColumns = @JoinColumn(name = "employeeId")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Trainee> traineeList;

    public Attendance() {
    }

//    public Attendance(Map<LocalDate, Boolean> attendanceList) {
//        this.attendanceList = attendanceList;
//    }


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

    public List<Trainee> getTraineeList() {
        return traineeList;
    }

    public void setTraineeList(List<Trainee> traineeList) {
        this.traineeList = traineeList;
    }


//    public Map<LocalDate, Boolean> getAttendanceList() {
//        return attendanceList;
//    }

//    public void setAttendanceList(Map<LocalDate, Boolean> attendanceList) {
//        this.attendanceList = attendanceList;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return Objects.equals(attendanceId, that.attendanceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attendanceId);
    }

//    @Override
    public String toString() {
        return "Attendance{" +
                "attendanceId='" + attendanceId + '\'' +
                ", attendanceDate=" + attendanceDate +
                ", attendanceDay='" + attendanceDay + '\'' +
                ", traineeList=" + traineeList +
                '}';
    }
}
