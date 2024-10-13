package com.parishi.dto;

import com.parishi.entity.Attendance;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class BatchDTO {

    private String batchId;

    private LocalDate creationDate;

    private List<TraineeDTO> traineeList;

    private CoursePlanDTO coursePlanDto;

    private TrainerDTO trainerDto=new TrainerDTO();

    private List<MentoringSessionDto> mentoringSessionDtos;

    private List<HolidayDTO> holidayDTOS;

    private List<AssessmentDTO> assessmentDTOS;
    private List<AttendanceDTO> attendances;

    public List<AttendanceDTO> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<AttendanceDTO> attendances) {
        this.attendances = attendances;
    }

    public List<AssessmentDTO> getAssessmentDTOS() {
        return assessmentDTOS;
    }

    public void setAssessmentDTOS(List<AssessmentDTO> assessmentDTOS) {
        this.assessmentDTOS = assessmentDTOS;
    }

    public List<MentoringSessionDto> getMentoringSessionDtos() {
        return mentoringSessionDtos;
    }

    public void setMentoringSessionDtos(List<MentoringSessionDto> mentoringSessionDtos) {
        this.mentoringSessionDtos = mentoringSessionDtos;
    }

    public List<HolidayDTO> getHolidayDTOS() {
        return holidayDTOS;
    }

    public void setHolidayDTOS(List<HolidayDTO> holidayDTOS) {
        this.holidayDTOS = holidayDTOS;
    }

    public CoursePlanDTO getCoursePlanDto() {
        return coursePlanDto;
    }

    public void setCoursePlanDto(CoursePlanDTO coursePlanDto) {
        this.coursePlanDto = coursePlanDto;
    }



    public TrainerDTO getTrainerDto() {
        return trainerDto;
    }

    public void setTrainerDto(TrainerDTO trainerDto) {
        this.trainerDto = trainerDto;
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

    public List<TraineeDTO> getTraineeList() {
        return traineeList;
    }

    public void setTraineeList(List<TraineeDTO> traineeList) {
        this.traineeList = traineeList;
    }

    @Override
    public String toString() {
        return "BatchDTO{" +
                "batchId='" + batchId + '\'' +
                ", creationDate=" + creationDate +
                ", traineeList=" + traineeList +
                ", coursePlanDto=" + coursePlanDto +
                ", trainerDto=" + trainerDto +
                ", mentoringSessionDtos=" + mentoringSessionDtos +
                ", holidayDTOS=" + holidayDTOS +
                ", assessmentDTOS=" + assessmentDTOS +
                '}';
    }
}
