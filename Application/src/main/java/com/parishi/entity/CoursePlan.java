package com.parishi.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "track_coursePlan")
public class CoursePlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_plan_Id")
    private Integer coursePlanId;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Batch batch;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DayWisePlan> dayWisePlans;

    public CoursePlan() {
    }

    public CoursePlan(Integer coursePlanId, Batch batch, List<DayWisePlan> dayWisePlans) {
        this.coursePlanId = coursePlanId;
        this.batch = batch;
        this.dayWisePlans = dayWisePlans;
    }

    public Integer getCoursePlanId() {
        return coursePlanId;
    }

    public void setCoursePlanId(Integer coursePlanId) {
        this.coursePlanId = coursePlanId;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public List<DayWisePlan> getDayWisePlans() {
        return dayWisePlans;
    }

    public void setDayWisePlans(List<DayWisePlan> dayWisePlans) {
        this.dayWisePlans = dayWisePlans;
    }

    @Override
    public String toString() {
        return "CoursePlan{" +
                "coursePlanId='" + coursePlanId + '\'' +
                ", batch=" + batch +
                ", dayWisePlans=" + dayWisePlans +
                '}';
    }
}
