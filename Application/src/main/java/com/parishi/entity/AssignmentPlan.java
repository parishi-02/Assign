package com.parishi.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Map;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "track_assignment_plan")
public class AssignmentPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "assignment_plan_Id")
    private Integer id;

    @Column(name = "Assignment_description")
    private String description;

    @Column(name = "Total_score")
    private Double totalScore;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private Map<Integer,Double> marksObtained;


    public AssignmentPlan() {
    }

    public AssignmentPlan(Integer id, String description, Double totalScore) {
        this.id = id;
        this.description = description;
        this.totalScore = totalScore;
    }

    public Map<Integer, Double> getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(Map<Integer, Double> marksObtained) {
        this.marksObtained = marksObtained;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }


    @Override
    public String toString() {
        return "AssignmentPlan{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", totalScore=" + totalScore +
                ", marksObtained=" + marksObtained +
                '}';
    }
}
