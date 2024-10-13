package com.parishi.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "track_trainees")
public class Trainee {
    @Id
    @Column(name = "Employee_ID")
    private Integer employeeId;

    @Column(name = "DOJ")
    private LocalDate doj;

    @Column(name = "Trainee_Name")
    private String traineeName;

    @Column(name ="EmailID")
    private String officeEmailId;

    @Column(name = "ContactNo")
    private String cellNo;

    @Column(name = "Gender")
    private String gender;

    @Column(name = "college")
    private String college;

    @Column(name = "branch")
    private String branch;

    @Column(name = "Final_Grade")
    private String finalGrade;

    @Column(name = "Final_Marks")
    private Double finalMarks;

    @Column(name = "Tenth_Marks")
    private Double tenthMarks;

    @Column(name = "Twelveth_Marks")
    private Double twelvethMarks;

    @Column(name = "Remarks")
    private String remarks;

    @ElementCollection
    @CollectionTable(
            name = "trainee_attendance",
            joinColumns = @JoinColumn(name = "trainee_id")
    )
    @MapKeyColumn(name = "attendance_date")
    @Column(name = "attendance_status")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Map<LocalDate,String> attendance = new HashMap<>();


    @Column(name = "Status_Flag")
    private Integer statusFlag;


    public Trainee() {
    }

    public Trainee(Integer employeeId, LocalDate doj, String traineeName, String officeEmailId, String cellNo, String gender, String college, String branch, Attendance attendance) {
        this.employeeId = employeeId;
        this.doj = doj;
        this.traineeName = traineeName;
        this.officeEmailId = officeEmailId;
        this.cellNo = cellNo;
        this.gender = gender;
        this.college = college;
        this.branch = branch;
//        this.attendance = attendance;
    }

    public Map<LocalDate, String> getAttendance() {
        return attendance;
    }

    public void setAttendance(Map<LocalDate, String> attendance) {
        this.attendance = attendance;
    }

    public Integer getStatusFlag() {
        return statusFlag;
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

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    @Override
    public String toString() {
        return "Trainee{" +
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
}
