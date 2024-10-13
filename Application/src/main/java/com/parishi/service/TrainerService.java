package com.parishi.service;

import com.parishi.dto.*;
import com.parishi.entity.Assessment;
import com.parishi.entity.AssignmentPlan;
import com.parishi.entity.Attendance;
import com.parishi.entity.Trainee;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TrainerService {

    List<BatchDTO> getBatchByTrainerId(Integer trainerId);
    List<TraineeDTO> getTraineeListByBatchId(String batchId);

    AttendanceDTO getAttendanceDto(LocalDate attendanceDate, Map<String,String> traineeStates, String batchId);

    boolean saveAttendance(BatchDTO batchDTO);

    AttendanceDTO getAttendanceDataByDate(LocalDate attendanceDate);

    List<TraineeDTO> setTraineeAttendanceStatus(List<TraineeDTO> traineeDTOList,Map<String, String> traineeStates,LocalDate attendanceDate);
    List<TraineeDTO> updateTraineeAttendanceStatus(List<TraineeDTO> traineeDTOList,Map<String, String> traineeStates,LocalDate attendanceDate);

    AssignmentPlanDTO getAssignmentById(Integer assignmentId);
    boolean saveAssignmentMarks(AssignmentPlanDTO assignmentPlanDTO);

    AssessmentDTO getAssessmentById(Integer assessmentId);

    boolean saveAssessmentMarks(AssessmentDTO assessmentDTO);
    List<AssignmentPlanDTO> getAllAssignment();

    List<AssessmentDTO> getAllAssessment();
    Map<AssignmentPlanDTO,Double>  getAllAssignmentMarksOfTrainee(TraineeDTO traineeDTO);

    Map<AssessmentDTO,Double>  getAllAssessmentMarksOfTrainee(TraineeDTO traineeDTO);

    AttendanceDTO getAttendanceDtoById(Integer attendanceId);

    boolean updateAttendance(AttendanceDTO attendanceDTO);

}
