package com.parishi.dto;

import java.time.LocalDate;

public class HolidayDTO {

    private Integer holidayId;
    private String holidayType;
    private String holidayName;
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
        return "HolidayDTO{" +
                "holidayId=" + holidayId +
                ", holidayType='" + holidayType + '\'' +
                ", holidayName='" + holidayName + '\'' +
                ", holidayDate=" + holidayDate +
                '}';
    }
}
