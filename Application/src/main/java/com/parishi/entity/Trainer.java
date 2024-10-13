package com.parishi.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "track_trainer")
public class Trainer {
    @Id
    @Column(name="trainer_id")
    private Integer trainerId;
    @Column(name="trainer_name")
    private String trainerName;
    @Column(name="trainer_type")
    private String trainerType;
    @Column(name="trainer_email")
    private String trainerEmail;
    @Column(name="trainer_contact_number")
    private String trainerContactNo;

    @Column(name = "Status_Flag")
    private Integer statusFlag = 0;


    public Trainer() {
    }

    public Trainer(Integer trainerId, String trainerName, String trainerType, String trainerEmail, String trainerContactNo) {
        this.trainerId = trainerId;
        this.trainerName = trainerName;
        this.trainerType = trainerType;
        this.trainerEmail = trainerEmail;
        this.trainerContactNo = trainerContactNo;
    }

    public Integer getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Integer trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public String getTrainerType() {
        return trainerType;
    }

    public void setTrainerType(String trainerType) {
        this.trainerType = trainerType;
    }

    public String getTrainerEmail() {
        return trainerEmail;
    }

    public void setTrainerEmail(String trainerEmail) {
        this.trainerEmail = trainerEmail;
    }

    public String getTrainerContactNo() {
        return trainerContactNo;
    }

    public void setTrainerContactNo(String trainerContactNo) {
        this.trainerContactNo = trainerContactNo;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return trainerId == trainer.trainerId && Objects.equals(trainerName, trainer.trainerName) && Objects.equals(trainerType, trainer.trainerType) && Objects.equals(trainerEmail, trainer.trainerEmail) && Objects.equals(trainerContactNo, trainer.trainerContactNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainerId, trainerName, trainerType, trainerEmail, trainerContactNo);
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "trainerId=" + trainerId +
                ", trainerName='" + trainerName + '\'' +
                ", trainerType='" + trainerType + '\'' +
                ", trainerEmail='" + trainerEmail + '\'' +
                ", trainerContactNo='" + trainerContactNo + '\'' +
                ", statusFlag=" + statusFlag +
                '}';
    }
}
