package com.parishi.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "track_holiday")
@DynamicUpdate
@DynamicInsert
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer holidayId;

    @Column(name = "Holiday_type")
    private String holidayType;

    @Column(name = "Holiday_Name")
    private String holidayName;

    @Column(name = "Holiday_Date")
    private LocalDate holidayDate;

    public Integer getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(Integer holidayId) {
        this.holidayId = holidayId;
    }

    public String getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(String holidayType) {
        this.holidayType = holidayType;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }

    @Override
    public String toString() {
        return "Holiday{" +
                "holidayId=" + holidayId +
                ", holidayType='" + holidayType + '\'' +
                ", holidayName='" + holidayName + '\'' +
                ", holidayDate=" + holidayDate +
                '}';
    }
}
