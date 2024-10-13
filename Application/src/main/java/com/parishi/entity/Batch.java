package com.parishi.entity;

import com.parishi.dto.MentoringSessionDto;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "track_batch")
public class Batch {
    @Id
    @GeneratedValue(generator = "customBatchIdGenerator")
    @GenericGenerator(name = "customBatchIdGenerator", strategy = "com.parishi.utility.CustomIdentifierGenerator")
    @Column(name = "batch_id")
    private String batchId;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Trainee> traineeList;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private CoursePlan coursePlan;

    @OneToOne(cascade = CascadeType.ALL)
    private Trainer trainer;

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<MentoringSession> mentoringSessions;

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Holiday> holidays;

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Assessment> assessments;

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Attendance> attendances;

    public Batch() {
    }

    public Batch(String batchId, LocalDate creationDate, List<Trainee> traineeList, CoursePlan coursePlan, Trainer trainer) {
        this.batchId = batchId;
        this.creationDate = creationDate;
        this.traineeList = traineeList;
        this.coursePlan = coursePlan;
        this.trainer = trainer;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }

    public List<Holiday> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }

    public List<MentoringSession> getMentoringSessions() {
        return mentoringSessions;
    }

    public void setMentoringSessions(List<MentoringSession> mentoringSessions) {
        this.mentoringSessions = mentoringSessions;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public List<Trainee> getTraineeList() {
        return traineeList;
    }

    public void setTraineeList(List<Trainee> traineeList) {
        this.traineeList = traineeList;
    }

    public CoursePlan getCoursePlan() {
        return coursePlan;
    }

    public void setCoursePlan(CoursePlan coursePlan) {
        this.coursePlan = coursePlan;
    }



    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }


    @Override
    public String toString() {
        return "Batch{" +
                "batchId='" + batchId + '\'' +
                ", creationDate=" + creationDate +
                ", traineeList=" + traineeList +
                ", coursePlan=" + coursePlan +
                ", trainer=" + trainer +
                ", mentoringSessions=" + mentoringSessions +
                ", holidays=" + holidays +
                ", assessments=" + assessments +
                '}';
    }
}