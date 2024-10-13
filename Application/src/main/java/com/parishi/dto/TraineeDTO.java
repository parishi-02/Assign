package com.parishi.dto;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

public class TraineeDTO {

    @NotNull(message = "Employee Id is required")
    @Min(value = 10000, message = "Trainee ID must be a minimum of 5 digits")
    @Max(value = 1000000, message = "Trainee ID must be a maximum of 7 digits")
    private Integer employeeId;

    @NotNull(message = "Date of joining is required")
    private LocalDate doj;

    @NotBlank(message = "Trainee name is required")
    @Pattern(regexp = "^[A-Za-z ]+$" ,message = "Only alphabets are allowed")
    private String traineeName;

    @NotBlank(message = "Email is required")
    private String officeEmailId;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "\\d{10}", message = "Contact number must be 10 digits")
    private String cellNo;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "College name is required")
    private String college;

    @NotBlank(message = "Branch is required")
    private String branch;

    @Column(name = "Final_Grade")
    private String finalGrade;

    @Column(name = "Final_Marks")
    private Double finalMarks;


    @Column(name = "Tenth_Marks")
    private Double tenthMarks;


    @Column(name = "Twelveth_Marks")
    private Double twelvethMarks;

    @NotBlank(message = "Remarks is required")
    @Column(name = "Remarks")
    private String remarks;



    private Integer statusFlag;

    private Map<LocalDate,String> attendance;


    public TraineeDTO() {
    }

    public void setAttendance(Map<LocalDate, String> attendance) {
        this.attendance = attendance;
    }

    public Map<LocalDate, String> getAttendance() {
        return attendance;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getDoj() {
        return doj;
    }

    public void setDoj(LocalDate doj) {
        this.doj = doj;
    }

    public String getTraineeName() {
        return traineeName;
    }

    public void setTraineeName(String traineeName) {
        this.traineeName = traineeName;
    }

    public String getOfficeEmailId() {
        return officeEmailId;
    }

    public void setOfficeEmailId(String officeEmailId) {
        this.officeEmailId = officeEmailId;
    }

    public String getCellNo() {
        return cellNo;
    }

    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

//    public AttendanceDTO getAttendance() {
//        return attendance;
//    }
//
//    public void setAttendance(AttendanceDTO attendance) {
//        this.attendance = attendance;
//    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(String finalGrade) {
        this.finalGrade = finalGrade;
    }

    public Double getFinalMarks() {
        return finalMarks;
    }

    public void setFinalMarks(Double finalMarks) {
        this.finalMarks = finalMarks;
    }

    public Double getTenthMarks() {
        return tenthMarks;
    }

    public void setTenthMarks(Double tenthMarks) {
        this.tenthMarks = tenthMarks;
    }

    public Double getTwelvethMarks() {
        return twelvethMarks;
    }

    public void setTwelvethMarks(Double twelvethMarks) {
        this.twelvethMarks = twelvethMarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "TraineeDTO{" +
                "employeeId=" + employeeId +
                ", doj=" + doj +
                ", traineeName='" + traineeName + '\'' +
                ", officeEmailId='" + officeEmailId + '\'' +
                ", cellNo='" + cellNo + '\'' +
                ", gender='" + gender + '\'' +
                ", college='" + college + '\'' +
                ", branch='" + branch + '\'' +
                ", attendance=" + attendance +
//                ", batch=" + batch +
                ", statusFlag=" + statusFlag +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraineeDTO that = (TraineeDTO) o;
        return Objects.equals(employeeId, that.employeeId) && Objects.equals(doj, that.doj) && Objects.equals(traineeName, that.traineeName) && Objects.equals(officeEmailId, that.officeEmailId) && Objects.equals(cellNo, that.cellNo) && Objects.equals(gender, that.gender) && Objects.equals(college, that.college) && Objects.equals(branch, that.branch) && Objects.equals(attendance, that.attendance) && Objects.equals(statusFlag, that.statusFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, doj, traineeName, officeEmailId, cellNo, gender, college, branch, attendance);
    }
}

