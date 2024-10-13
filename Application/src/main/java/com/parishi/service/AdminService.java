package com.parishi.service;

import com.parishi.dto.*;
import com.parishi.entity.MentoringSession;
import com.parishi.entity.Trainee;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AdminService {
    List<TraineeDTO> getAllTrainees();

    List<TraineeDTO> getUnAssignedTrainees();

    boolean registerTrainee(TraineeDTO traineeDTO);

    boolean insertBulkTrainee(List<TraineeDTO> traineeDTOList);

    List<TrainerDTO> getAllTrainers();

    List<TrainerDTO> getAllTrainerWithStatusFlagZero();

    boolean registerTrainer(TrainerDTO trainerDto);
    boolean updateTrainee(TraineeDTO traineeDTO );
    TraineeDTO getTrainee(int empId);

    boolean updateTrainer(TrainerDTO trainerDTO);

    TrainerDTO getTrainer(int trainerId);

    boolean createBatch(BatchDTO batchDTO);

    List<BatchDTO> getAllBatches();

    BatchDTO getBatchById(String batchId);

    List<MentoringSessionDto> getAllMentoringSession();

    boolean addMentoringSession(BatchDTO batchDTO);

    List<HolidayDTO> createHolidayList(List<String> holidayType, List<String> holidayName, List<String> holidayDate);

    boolean updateCoursePlanDates(String batchId, String topicName, LocalDate newDate);

    boolean updateTopicPlannedDate(String batchId,Integer planId,LocalDate newDate,String remarks);

    boolean updateBatch(BatchDTO batchDTO);

    boolean addAssessment(BatchDTO batchDTO);

    MentoringSessionDto getMentoringSessionById(Integer sessionId);

    boolean updateMentoringSession(MentoringSessionDto mentoringSessionDto);

    AssessmentDTO getAssessmentById(Integer assessmentId);

    boolean updateAssessment(AssessmentDTO assessmentDTO);

    AssignmentPlanDTO getAssignmentById(Integer id);

    boolean updateAssignmentTotalScore(AssignmentPlanDTO assignmentPlanDTO);

    Map<Double, Double> totalAssignmentMarksForGivenTopic(List<DayWisePlanDTO> dayWisePlans, String selectedTopic, Integer traineeId);

    Map<Double, Double> totalAssignmentMarksForAllTopics(List<DayWisePlanDTO> dayWisePlans, Integer traineeId);

    Map<Double,Double> totalAssessmentMarksForGivenTopic(String selectedTopic, List<AssessmentDTO> assessments, Integer traineeId);

    Map<Double,Double> totalAssessmentMarksForAllTopics(List<AssessmentDTO> assessments, Integer traineeId);

    Map<LocalDate,Double> extractPlanDateForAllTopic(List<DayWisePlanDTO> dayWisePlanDTOList);

    Map<LocalDate,Double> extractPlanDateForGivenTopic(List<DayWisePlanDTO> dayWisePlanDTOList, String selectedTopic);

    Map<Double,Double> calculateAttendance(TraineeDTO traineeDTO, Map<LocalDate, Double> plannedDates);

    String calculateGrade(Double finalScore);
}
