package com.parishi.service;

import com.parishi.dao.*;
import com.parishi.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrainerServiceImpl implements TrainerService{


    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private AttendanceDao attendanceDao;

    @Autowired
    private BatchDao batchDao;
    @Autowired
    private AssessmentDao assessmentDao;
    @Autowired
    private AssignmentDao assignmentDao;

    @Override
    public List<BatchDTO> getBatchByTrainerId(Integer trainerId) {
        return batchDao.getBatchByTrainerId(trainerId);
    }

    @Override
    public List<TraineeDTO> getTraineeListByBatchId(String batchId) {
        return traineeDao.getTraineeListByBatchId(batchId);
    }

    @Override
    public AttendanceDTO getAttendanceDto(LocalDate attendanceDate, Map<String, String> params, String batchId) {

        System.out.println("date = " +attendanceDate);
        List<TraineeDTO>traineeDTOList = getTraineeListByBatchId(batchId);

        System.out.println("service list = " + traineeDTOList);
        AttendanceDTO attendanceDTO = new AttendanceDTO();

        attendanceDTO.setAttendanceDate(attendanceDate);
        attendanceDTO.setAttendanceDay(String.valueOf(attendanceDate.getDayOfWeek()));
        attendanceDTO.setTraineeList(traineeDTOList);
        System.out.println("attendance in service= "+ attendanceDTO);
        return attendanceDTO;
    }

    @Override
    public boolean saveAttendance(BatchDTO batchDTO) {
        return attendanceDao.saveAttendance(batchDTO);
    }

    @Override
    public AttendanceDTO getAttendanceDataByDate(LocalDate attendanceDate) {
        return attendanceDao.getAttendanceDataByDate(attendanceDate);
    }

    @Override
    public List<TraineeDTO> setTraineeAttendanceStatus(List<TraineeDTO> traineeDTOList,Map<String, String> params,LocalDate attendanceDate) {
        // Create a map to hold filtered trainee states
        Map<String, String> traineeStates = new HashMap<>();

        // Filter the parameters to include only those related to traineeStates
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // Check if the key matches the pattern for trainee states
            if (key.startsWith("traineeStates[") && key.endsWith("]")) {
                // Extract the trainee ID
                String traineeId = key.substring("traineeStates[".length(), key.length() - 1);
                traineeStates.put(traineeId, value);
            }
        }

        System.out.println("trainee dto list parishi = " + traineeDTOList);
        if(traineeDTOList!=null)
        {
            System.out.println("States in parishi= " + traineeStates);
            List<TraineeDTO> traineeDTOList1 = new ArrayList<>();
            for(TraineeDTO traineeDTO : traineeDTOList)
            {
                Map<LocalDate,String> traineeAttendance = traineeDTO.getAttendance();
                for (Map.Entry<String, String> entry : traineeStates.entrySet()) {
                    String traineeId = entry.getKey();
                    String status = entry.getValue();

                   if(traineeId.equalsIgnoreCase(String.valueOf(traineeDTO.getEmployeeId())) && (status!=null && !status.isEmpty()))
                            {traineeAttendance.put(attendanceDate, status);
                   }
                }
                traineeDTO.setAttendance(traineeAttendance);
                traineeDTOList1.add(traineeDTO);

                System.out.println("List sto = " + traineeDTOList1);
            }
            return traineeDTOList1;
        }
        return Collections.emptyList();
    }

    @Override
    public List<TraineeDTO> updateTraineeAttendanceStatus(List<TraineeDTO> traineeDTOList, Map<String, String> params, LocalDate attendanceDate) {
        // Create a map to hold filtered trainee states

        System.out.println("traineedTolist = " + traineeDTOList);
        Map<String, String> traineeStates = new HashMap<>();

        // Filter the parameters to include only those related to traineeStates
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // Check if the key matches the pattern for trainee states
            if (key.startsWith("traineeStates[") && key.endsWith("]")) {
                // Extract the trainee ID
                String traineeId = key.substring("traineeStates[".length(), key.length() - 1);
                traineeStates.put(traineeId, value);
            }
        }

        System.out.println("trainee start = " + traineeStates);

        if(traineeDTOList!=null)
        {
            System.out.println("States = " + traineeStates);
            List<TraineeDTO> traineeDTOList1 = new ArrayList<>();
            for(TraineeDTO traineeDTO : traineeDTOList)
            {
                for (Map.Entry<String, String> entry : traineeStates.entrySet()) {
                    String traineeId = entry.getKey();
                    String status = entry.getValue();

                    System.out.println("trainee id states= " + traineeId);
                    System.out.println("status = " + status);
                    System.out.println("trainee id dto = " + traineeDTO.getEmployeeId());

                    if(traineeId.equalsIgnoreCase(String.valueOf(traineeDTO.getEmployeeId())))
                    {
                        Map<LocalDate,String> traineeAttendance = traineeDTO.getAttendance();
                        Map<LocalDate, String> updateTraineeAttendance = new HashMap<>();
                        if(traineeAttendance.isEmpty())
                        {
                            updateTraineeAttendance.put(attendanceDate,status);
                            System.out.println("update trainee attendance = " + updateTraineeAttendance);
                        }
                        else
                        {
                            boolean flagFound = false;
                            for(Map.Entry<LocalDate,String> entry1 : traineeAttendance.entrySet())
                            {
                                if((entry1.getKey()).equals(attendanceDate))
                                {
                                    updateTraineeAttendance.put(attendanceDate,status);
                                    System.out.println("update trainee1  = " + updateTraineeAttendance);
                                    flagFound = true;
                                }
                                else
                                    updateTraineeAttendance.put(entry1.getKey(), entry1.getValue());
                                System.out.println("update trainee 2 = " + updateTraineeAttendance);
                            }
                            if(!flagFound)
                            {
                                updateTraineeAttendance.put(attendanceDate,status);
                                System.out.println("update trainee3  = " + updateTraineeAttendance);
                            }
                        }
                        traineeDTO.setAttendance(updateTraineeAttendance);
                    }
                }
                traineeDTOList1.add(traineeDTO);
            }
            System.out.println("List sto = " + traineeDTOList1);
            return traineeDTOList1;
        }
        return Collections.emptyList();
    }

    @Override
    public AssignmentPlanDTO getAssignmentById(Integer assignmentId) {
        return assignmentDao.getAssignmentById(assignmentId);
    }

    @Override
    public boolean saveAssignmentMarks(AssignmentPlanDTO assignmentPlanDTO) {
        return assignmentDao.saveAssignmentMarks(assignmentPlanDTO);
    }

    @Override
    public AssessmentDTO getAssessmentById(Integer assessmentId) {
        return assessmentDao.getAssessmentById(assessmentId);
    }

    @Override
    public boolean saveAssessmentMarks(AssessmentDTO assessmentDTO) {
        return assessmentDao.saveAssessmentMarks(assessmentDTO);
    }

    @Override
    public List<AssignmentPlanDTO> getAllAssignment()
    {
        return assignmentDao.getAllAssignment();
    }

    @Override
    public List<AssessmentDTO> getAllAssessment()
    {
        return assessmentDao.getAllAssessment();
    }

    @Override
    public Map<AssignmentPlanDTO, Double> getAllAssignmentMarksOfTrainee(TraineeDTO traineeDTO) {
        System.out.println("assignment = ");
        List<AssignmentPlanDTO> assignmentPlanDTOS = getAllAssignment();
        System.out.println("Assignment list = " + assignmentPlanDTOS);
        Integer traineeId = traineeDTO.getEmployeeId();
        Map<AssignmentPlanDTO, Double> marksAssignments = new HashMap<>();


        for(AssignmentPlanDTO assignmentPlanDTO : assignmentPlanDTOS)
        {
            Map<Integer,Double> marksObtained = assignmentPlanDTO.getMarksObtained();
            for(Map.Entry<Integer,Double> marks : marksObtained.entrySet())
            {
                Integer traineeId1 = marks.getKey();
                Double mark = marks.getValue();
                if(traineeId1.equals(traineeId))
                {
                    marksAssignments.put(assignmentPlanDTO,mark);
                }
            }
        }

        System.out.println("marks of trainee = " + marksAssignments);
        return marksAssignments;
    }

    @Override
    public Map<AssessmentDTO, Double> getAllAssessmentMarksOfTrainee(TraineeDTO traineeDTO) {
        System.out.println("assessment = ");
        List<AssessmentDTO> assessmentDTOS = getAllAssessment();
        System.out.println("assessment list = " + assessmentDTOS);
        Integer traineeId = traineeDTO.getEmployeeId();
        Map<AssessmentDTO, Double> marksAssessments = new HashMap<>();


        for(AssessmentDTO assessmentDTO : assessmentDTOS)
        {
            Map<Integer,Double> marksObtained = assessmentDTO.getMarksObtained();
            for(Map.Entry<Integer,Double> marks : marksObtained.entrySet())
            {
                Integer traineeId1 = marks.getKey();
                Double mark = marks.getValue();
                if(traineeId1.equals(traineeId))
                {
                    marksAssessments.put(assessmentDTO,mark);
                }
            }
        }

        System.out.println("marks of trainee = " + marksAssessments);
        return marksAssessments;
    }

    @Override
    public AttendanceDTO getAttendanceDtoById(Integer attendanceId) {
        return attendanceDao.getAttendanceDtoById(attendanceId);
    }

    @Override
    public boolean updateAttendance(AttendanceDTO attendanceDTO) {
        return attendanceDao.updateAttendance(attendanceDTO);
    }
}
