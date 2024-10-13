package com.parishi.dto;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.*;

public class TrainerDTO {

    @NotNull
    @Min(value = 10000, message = "Trainer ID must be a minimum of 5 digits")
    @Max(value = 1000000, message = "Trainer ID must be a maximum of 7 digits")
    private Integer trainerId;
    @NotBlank(message = "Trainer name is required")
    @Pattern(regexp = "^[A-Za-z ]+$" ,message = "Only alphabets are allowed")
    private String trainerName;

    @NotBlank(message = "Trainer type is required")
    private String trainerType;
    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "\\d{10}", message = "Contact number must be 10 digits")
    private String trainerContactNo;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Please provide a valid email address")
    private String trainerEmail;

    private Integer statusFlag;


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
    public String toString() {
        return "TrainerDTO{" +
                "trainerId=" + trainerId +
                ", trainerName='" + trainerName + '\'' +
                ", trainerType='" + trainerType + '\'' +
                ", trainerContactNo='" + trainerContactNo + '\'' +
                ", trainerEmail='" + trainerEmail + '\'' +
                ", statusFlag=" + statusFlag +
                '}';
    }
}
