package com.parishi.service;

import com.parishi.dao.*;
import com.parishi.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    private AssessmentDao assessmentDao;
    @Autowired
    private AssignmentDao assignmentDao;
    @Autowired
    private AttendanceDao attendanceDao;
    @Autowired
    private BatchDao batchDao;
    @Autowired
    private MentoringSessionDao mentoringSessionDao;
    @Autowired
    private TraineeDao traineeDao;
    @Autowired
    private TrainerDao trainerDao;

    @Override
    public List<TraineeDTO> getAllTrainees()
    {
        return traineeDao.getAllTrainees();
    }

    @Override
    public List<TraineeDTO> getUnAssignedTrainees()
    {
        List<TraineeDTO> trainees=traineeDao.getAllTrainees();

        System.out.println("Abhishek Verma Checking trainees in service"+trainees);

        return trainees.stream()
                .filter(trainee -> trainee.getStatusFlag() == 0)
                .collect(Collectors.toList());
    }
    @Override
    public boolean registerTrainee(TraineeDTO traineeDTO) {
        return traineeDao.registerTrainee(traineeDTO);
    }

    @Override
    public boolean insertBulkTrainee(List<TraineeDTO> traineeDTOList) {
        if(traineeDTOList==null || traineeDTOList.isEmpty()){
            return false;
        }
        return traineeDao.insertBulkTrainee(traineeDTOList);
    }

    @Override
    public List<TrainerDTO> getAllTrainers() {
        return trainerDao.getAllTrainers();
    }

    @Override
    public List<TrainerDTO> getAllTrainerWithStatusFlagZero()
    {
        return trainerDao.getAllTrainerWithStatusFlagZero();
    }

    @Override
    public boolean registerTrainer(TrainerDTO trainerDto) {
        return trainerDao.registerTrainer(trainerDto);
    }

    @Override
    public boolean updateTrainee(TraineeDTO traineeDTO) {
        if(traineeDTO==null){
            return false;
        }
        return traineeDao.updateTrainee(traineeDTO);
    }

    @Override
    public TraineeDTO getTrainee(int empId) {
        return traineeDao.getTrainee(empId);
    }

    @Override
    public boolean updateTrainer(TrainerDTO trainerDTO) {
        return trainerDao.updateTrainer(trainerDTO);
    }

    @Override
    public TrainerDTO getTrainer(int trainerId) {
        return trainerDao.getTrainer(trainerId);
    }

    @Override
    public boolean createBatch(BatchDTO batchDTO) {
        return batchDao.createBatch(batchDTO);
    }

    @Override
    public List<BatchDTO> getAllBatches() {
        return batchDao.getAllBatches();
    }

    @Override
    public BatchDTO getBatchById(String batchId) {
        return batchDao.getBatchById(batchId);
    }

    @Override
    public List<MentoringSessionDto> getAllMentoringSession() {
       return mentoringSessionDao.getAllMentoringSession();
    }

    @Override
    public boolean addMentoringSession(BatchDTO batchDTO) {
        return mentoringSessionDao.addMentoringSession(batchDTO);
    }

    @Override
    public List<HolidayDTO> createHolidayList(List<String> holidayType, List<String> holidayName, List<String> holidayDate) {

        if(holidayType.isEmpty() || holidayName.isEmpty() || holidayDate.isEmpty())
        {
            return null;
        }
        List<HolidayDTO> holidayDTOS = new ArrayList<>();
        for(int i=0;i<holidayType.size();i++)
        {
            HolidayDTO holidayDTO = new HolidayDTO();
            holidayDTO.setHolidayType(holidayType.get(i));
            holidayDTO.setHolidayName(holidayName.get(i));
            holidayDTO.setHolidayDate(LocalDate.parse(holidayDate.get(i)));
            holidayDTOS.add(holidayDTO);
        }
        return holidayDTOS;
    }

    @Override
    public boolean updateCoursePlanDates(String batchId, String topicName, LocalDate newDate)
    {
        return batchDao.updateCoursePlanDates(batchId,topicName,newDate);
    }

    @Override
    public boolean updateTopicPlannedDate(String batchId, Integer planId, LocalDate newDate,String remarks) {

        return batchDao.updateTopicPlannedDate(batchId,planId,newDate,remarks);
    }

    @Override
    public boolean updateBatch(BatchDTO batchDTO)
    {
        return batchDao.updateBatch(batchDTO);
    }

    @Override
    public boolean addAssessment(BatchDTO batchDTO) {

        return batchDao.addAssessment(batchDTO);
    }

    @Override
    public MentoringSessionDto getMentoringSessionById(Integer sessionId) {
        return mentoringSessionDao.getMentoringSessionById(sessionId);
    }

    @Override
    public boolean updateMentoringSession(MentoringSessionDto mentoringSessionDto) {
        return mentoringSessionDao.updateMentoringSession(mentoringSessionDto);
    }

    @Override
    public AssessmentDTO getAssessmentById(Integer assessmentId) {
        return assessmentDao.getAssessmentById(assessmentId);
    }

    @Override
    public boolean updateAssessment(AssessmentDTO assessmentDTO) {
        return assessmentDao.updateAssessment(assessmentDTO);
    }

    @Override
    public AssignmentPlanDTO getAssignmentById(Integer id) {
        return assignmentDao.getAssignmentById(id);
    }

    @Override
    public boolean updateAssignmentTotalScore(AssignmentPlanDTO assignmentPlanDTO) {
        return assignmentDao.updateAssignmentTotalScore(assignmentPlanDTO);
    }
    @Override
    public Map<Double, Double> totalAssignmentMarksForGivenTopic(List<DayWisePlanDTO> dayWisePlans, String selectedTopic, Integer traineeId)
    {
        Double totalScore = 0.0;
        Double marksScored = 0.0;
        AssignmentPlanDTO assignment;

        for (DayWisePlanDTO dayWisePlan : dayWisePlans)
        {
            if(dayWisePlan.getAssignmentPlanDTO()==null)
                continue;

            assignment = dayWisePlan.getAssignmentPlanDTO();
            String[] parts = assignment.getDescription().split("\\s*[-–]\\s*", 2);
            if (parts.length > 0) {
                String topic = parts[0].trim().toUpperCase();

                if (topic.contains(selectedTopic.toUpperCase()))
                {
                    if (assignment.getMarksObtained().get(traineeId) != null)
                    {
                        marksScored += assignment.getMarksObtained().get(traineeId);
                    }
                    if(assignment.getTotalScore()==null)
                        continue;
                    totalScore += assignment.getTotalScore();
                }
            }
        }

        // Prepare and return the result as a Map
        Map<Double, Double> result = new HashMap<>();
        result.put(totalScore, marksScored);

        return result;
    }
    @Override
    public Map<Double, Double> totalAssignmentMarksForAllTopics(List<DayWisePlanDTO> dayWisePlans, Integer traineeId)
    {
        Double totalScore = 0.0;
        double marksScored = 0.0;


        for (DayWisePlanDTO dayWisePlan : dayWisePlans)
        {
            if(dayWisePlan.getAssignmentPlanDTO()==null)
                continue;

            AssignmentPlanDTO assignment = dayWisePlan.getAssignmentPlanDTO();
            String[] parts = assignment.getDescription().split("\\s*[-–]\\s*", 2);
            if (parts.length > 0)
            {
                    if (assignment.getMarksObtained().get(traineeId)!= null)
                    {
                        marksScored += assignment.getMarksObtained().get(traineeId);;
                    }

                    if(assignment.getTotalScore()==null)
                        continue;
                    totalScore += assignment.getTotalScore();
            }
        }

        // Prepare and return the result as a Map
        Map<Double, Double> result = new HashMap<>();
        result.put(totalScore, marksScored);

        return result;
    }

    @Override
    public Map<Double,Double> totalAssessmentMarksForGivenTopic(String selectedTopic, List<AssessmentDTO> assessments, Integer traineeId)
    {
        Double totalScore = 0.0;
        double marksScored = 0.0;

        for(AssessmentDTO assessmentDTO : assessments)
        {
            System.out.println("devilService 1"+assessmentDTO);
            String[] parts = assessmentDTO.getSubTopic().split("\\s*[-–]\\s*",2);
            System.out.println("devilservice 2" + Arrays.toString(parts));
            if (parts.length > 0)
            {
                String topic = parts[0].trim().toUpperCase();
                System.out.println("devilservice 3" +topic);
                if (topic.contains(selectedTopic.toUpperCase()))
                {
                    if (assessmentDTO.getMarksObtained().get(traineeId) != null)
                    {
                        marksScored += assessmentDTO.getMarksObtained().get(traineeId);
                    }
                    System.out.println("devilservice " +assessmentDTO.getTotalScore());
                    if(assessmentDTO.getTotalScore()!=null)
                        totalScore += assessmentDTO.getTotalScore();
                }
            }
        }
        Map<Double, Double> result = new HashMap<>();
        result.put(totalScore, marksScored);

        return result;
    }

    @Override
    public Map<Double,Double> totalAssessmentMarksForAllTopics(List<AssessmentDTO> assessments, Integer traineeId)
    {
        Double totalScore = 0.0;
        double marksScored = 0.0;

        for(AssessmentDTO assessmentDTO : assessments)
        {
            String[] parts = assessmentDTO.getSubTopic().split("\\s*[-–]\\s*",2);
            if (parts.length > 0)
            {
                if (assessmentDTO.getMarksObtained().get(traineeId) != null)
                {
                    marksScored += assessmentDTO.getMarksObtained().get(traineeId);
                }

                if(assessmentDTO.getTotalScore()==null)
                    continue;
                totalScore += assessmentDTO.getTotalScore();
            }
        }
        Map<Double, Double> result = new HashMap<>();
        result.put(totalScore, marksScored);

        return result;
    }

    @Override
    public Map<LocalDate,Double> extractPlanDateForAllTopic(List<DayWisePlanDTO> dayWisePlanDTOList)
    {
        Map<LocalDate,Double> plannedDates=new HashMap<>();

        for(DayWisePlanDTO dayWisePlanDTO : dayWisePlanDTOList)
        {
            if(dayWisePlanDTO.getTopics().getTopicName()!=null)
            {
                if (dayWisePlanDTO.getPlannedDate() == null)
                    continue;
                LocalDate valueDate = dayWisePlanDTO.getPlannedDate();
                plannedDates.put(valueDate, null);
            }
        }

        return  plannedDates;
    }
    @Override
    public Map<LocalDate,Double> extractPlanDateForGivenTopic(List<DayWisePlanDTO> dayWisePlanDTOList, String selectedTopic)
    {
        Map<LocalDate,Double> plannedDates=new HashMap<>();

        for(DayWisePlanDTO dayWisePlanDTO : dayWisePlanDTOList)
        {
            if(dayWisePlanDTO.getAssignmentPlanDTO()==null)
                continue;

            AssignmentPlanDTO assignment = dayWisePlanDTO.getAssignmentPlanDTO();
            String[] parts = assignment.getDescription().split("\\s*[-–]\\s*", 2);
            if (parts.length > 0)
            {
                String topic = parts[0].trim().toUpperCase();
                if (topic.contains(selectedTopic.toUpperCase()))
                {
                    if(dayWisePlanDTO.getPlannedDate()!=null)
                    {
                        LocalDate valueDate = dayWisePlanDTO.getPlannedDate();
                        plannedDates.put(valueDate, null);
                    }
                }
            }
        }
        return  plannedDates;
    }

    @Override
    public Map<Double,Double> calculateAttendance(TraineeDTO traineeDTO, Map<LocalDate, Double> plannedDates)
    {
        Map<Double, Double> attendanceScoreMap = new HashMap<>();

        // Get the trainee's attendance record (date -> "Present"/"Absent"/"Half-day")
        Map<LocalDate, String> attendance = traineeDTO.getAttendance();
        System.out.println("devilService 0"+attendance);

        // Initialize counters for total planned days and attendance score
        double totalPlannedDays = 0.0;
        double totalAttendanceScore = 0.0;

        // Iterate over the planned dates and compare with trainee's attendance dates
        for (LocalDate plannedDate : plannedDates.keySet())
        {
            // Check if the planned date exists in the trainee's attendance map
            if (attendance.containsKey(plannedDate)) {
                totalPlannedDays++; // Increment for each planned day with a matching attendance record

                // Retrieve the attendance status for the matching date
                String status = attendance.get(plannedDate);

                // Assign score based on attendance status
                switch (status.toLowerCase()) {
                    case "present":
                        totalAttendanceScore += 1.0; // Full score for present
                        break;
                    case "half-day":
                        totalAttendanceScore += 0.5; // Half score for half-day
                        break;
                    // No score for absent
                    default:
                        totalAttendanceScore += 0.0; // Treat unknown status as absent
                        break;
                }
            }
        }

        // Store the result in the map: totalPlannedDays as key and totalAttendanceScore as value
        attendanceScoreMap.put(totalPlannedDays, totalAttendanceScore);

        return attendanceScoreMap;

    }

    @Override
    public String calculateGrade(Double finalScore)
    {
        if (finalScore >= 95) {
            return "A+";
        } else if (finalScore >= 85) {
            return "A";
        } else if (finalScore >= 70) {
            return "B";
        } else if (finalScore >= 60) {
            return "C";
        } else {
            return "D";
        }
    }

}
