package com.parishi.controller;

import com.parishi.dto.*;
import com.parishi.entity.Assessment;
import com.parishi.entity.Attendance;
import com.parishi.entity.User;
import com.parishi.service.AdminService;
import com.parishi.service.TrainerService;
import com.parishi.utility.LocalDateEditor;
import com.parishi.utility.LocalTimeEditor;
import com.parishi.utility.UtilityFunctions;
import oracle.ucp.proxy.annotation.Post;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.expression.spel.ast.Assign;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
@PreAuthorize("hasRole('TRAINER')")
@RequestMapping("trainer")
public class TrainerController {

    @Autowired
    private Logger logger;

    @Autowired
    private AdminService adminService;

    @Autowired
    private TrainerService trainerService;


    private String divType = "null";
    private String batchType="null";


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor());
    }

    @InitBinder
    public void initBinderTime(WebDataBinder binder) {
        binder.registerCustomEditor(LocalTime.class, new LocalTimeEditor());
    }

    @RequestMapping("dashboard")
    public String trainerDashboard()
    {
        return "trainer-dashboard";
    }

    @GetMapping("showAllBatches")
    public String showAllBatches(Model model)
    {
        // Get the list of all batches
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        System.out.println("Username = " + username);
        String id = username.replaceAll("\\D+", "");
        List<BatchDTO> batches = trainerService.getBatchByTrainerId(Integer.parseInt(id));

        // Sort the batches by Batch ID in ascending order
        batches.sort(Comparator.comparing(BatchDTO::getBatchId));

        if(batches != null) {
            for (BatchDTO batch : batches) {
                if (batch.getAssessmentDTOS() != null) {
                    Collections.sort(batch.getAssessmentDTOS(), new Comparator<AssessmentDTO>() {
                        @Override
                        public int compare(AssessmentDTO a1, AssessmentDTO a2) {
                            if (a1.getPlannedDate() == null || a2.getPlannedDate() == null) {
                                return 0; // Handle null values as needed
                            }
                            return a1.getPlannedDate().compareTo(a2.getPlannedDate());
                        }
                    });
                }
            }
        }

        // Add the sorted list to the model
        model.addAttribute("BatchDto", batches);
        model.addAttribute("traineeId",id);
        model.addAttribute("currentDate", LocalDate.now());  // current date krni hai yha prr
        // Return the view name
        return "trainerBatches";
    }

    @GetMapping("markAttendanceForm")
    public String markAttendanceForm(Model model,
                                     @RequestParam("batchId") String batchId,
                                     @RequestParam("attendanceDate") LocalDate attendanceDate,
                                     @RequestParam("divType") String divTypes,
                                     @RequestParam("batchType") String batchTypes) {
        divType = divTypes;
        batchType = batchTypes;
        BatchDTO batchDTO = adminService.getBatchById(batchId);
        List<TraineeDTO> traineeDTOList = batchDTO.getTraineeList();

        System.out.println("List = " + traineeDTOList);

        if (traineeDTOList != null) {
            model.addAttribute("traineeList", traineeDTOList);
            model.addAttribute("attendanceDate", attendanceDate);
            model.addAttribute("batchId", batchId);
        }

        return "mark-trainees-attendance";
    }

    @PostMapping("saveTraineeAttendance")
    public String saveTraineeAttendance(Model model,
                                        @RequestParam Map<String, String> traineeStates,
                                        @RequestParam("attendanceDate") LocalDate attendanceDate,
                                        @RequestParam("batchId") String batchId)
    {
        BatchDTO batchDTO = adminService.getBatchById(batchId);
        List<AttendanceDTO> attendanceDTOS = batchDTO.getAttendances();
        AttendanceDTO attendanceDTO = trainerService.getAttendanceDto(attendanceDate, traineeStates, batchId);
        System.out.println("attendance dto = " + attendanceDTO);
        List<TraineeDTO> traineeDTOList = attendanceDTO.getTraineeList();
        System.out.println("bcxm");
        if (attendanceDTO != null) {
            if(attendanceDTOS!=null)
            {
                attendanceDTOS.add(attendanceDTO);
                batchDTO.setAttendances(attendanceDTOS);
            }
            else
            {
                List<AttendanceDTO> attendanceDTOS1 = new ArrayList<>();
                attendanceDTOS1.add(attendanceDTO);
                batchDTO.setAttendances(attendanceDTOS1);
            }

            System.out.println("batchto = " +batchDTO);
            System.out.println("trainee dto list  = " + traineeDTOList);
            if (trainerService.saveAttendance(batchDTO)) {
                System.out.println("Saved");
                traineeDTOList = trainerService.setTraineeAttendanceStatus(traineeDTOList,traineeStates,attendanceDate);
                System.out.println("attendance trainee dto list = " + traineeDTOList);
                if(traineeDTOList!=null)
                {
                    boolean flag=true;
                    for(TraineeDTO traineeDTO : traineeDTOList)
                        if(!adminService.updateTrainee(traineeDTO))
                            flag=false;
                    if(flag)
                    {
                        model.addAttribute("divType",divType);
                        model.addAttribute("batchType",batchType);
                        model.addAttribute("successMessage", "Attendance for " + attendanceDate + " updated successfully");
                        return "redirect:showAllBatches";
                    }
                }
            }
        }
        model.addAttribute("errorMessage", "Attendance for " + attendanceDate + " not updated successfully");
        return "error";
    }

    @GetMapping("updateAttendanceForm")
    public String updateAttendanceForm(Model model,
                                       @RequestParam("batchId") String batchId,
                                       @RequestParam("attendanceDate") LocalDate attendanceDate,
                                       @RequestParam("divType") String divTypes,
                                       @RequestParam("batchType") String batchTypes
                                       )
    {
        divType = divTypes;
        batchType = batchTypes;
        BatchDTO batchDTO = adminService.getBatchById(batchId);

        if(batchDTO.getAttendances()==null)
        {
            System.out.println(",sndcn");
            return "forward:markAttendanceForm";
        }
        List<AttendanceDTO> attendanceDTOS = batchDTO.getAttendances();
        Optional<AttendanceDTO> matchingAttendance = attendanceDTOS
                                .stream()
                                .filter(attendance->attendance.getAttendanceDate().equals(attendanceDate))
                                .findFirst();

        AttendanceDTO attendanceDTO = null;
        if(!matchingAttendance.isPresent())
        {
            System.out.println("bxcmb");
            return "forward:markAttendanceForm";
        }
        attendanceDTO = matchingAttendance.get();
        System.out.println("attendance dto : " + attendanceDTO);
        System.out.println("update");
        List<TraineeDTO> traineeDTOList = attendanceDTO.getTraineeList();
        List<String> attendanceIdWithStatus = new ArrayList<>();
        for(TraineeDTO traineeDTO : traineeDTOList)
        {
            Map<LocalDate,String> attendance = traineeDTO.getAttendance();
            String status = "";
            for (Map.Entry<LocalDate, String> entry : attendance.entrySet()) {
                LocalDate date = entry.getKey();
                String value = entry.getValue();
                if (date.equals(attendanceDate)) {
                    status += traineeDTO.getEmployeeId() + " " + value;
                }
            }
            attendanceIdWithStatus.add(status);
        }
        model.addAttribute("traineeList",batchDTO.getTraineeList());
        model.addAttribute("attendanceDto",attendanceDTO);
        model.addAttribute("batchId",batchId);
        model.addAttribute("attendanceStatus",attendanceIdWithStatus);
        return "update-trainee-attendance";
    }

    @PostMapping("updateTraineeAttendance")
    public String updateTraineeAttendance(Model model,
                                          @RequestParam Map<String, String> traineeStates,
                                          @RequestParam("attendanceDate") LocalDate attendanceDate,
                                          @RequestParam("batchId") String batchId,
                                          @RequestParam("attendanceId") Integer attendanceId)
    {
        List<TraineeDTO> traineeDTOList = trainerService.getTraineeListByBatchId(batchId);
        List<TraineeDTO> traineeDTOList1 = trainerService.updateTraineeAttendanceStatus(traineeDTOList,traineeStates,attendanceDate);
        AttendanceDTO attendanceDTO = trainerService.getAttendanceDtoById(attendanceId);
        System.out.println("attendance dto = " + attendanceDTO);
        System.out.println("trainee list = " + traineeDTOList1);
        if(traineeDTOList1!=null)
        {
            attendanceDTO.setTraineeList(traineeDTOList1);
            if(trainerService.updateAttendance(attendanceDTO)) {
                System.out.println("Dto list= " + traineeDTOList1);
                boolean flag = true;
                for (TraineeDTO traineeDTO : traineeDTOList1)
                    if (!adminService.updateTrainee(traineeDTO))
                        flag = false;

                if (flag) {
                    model.addAttribute("divType", divType);
                    model.addAttribute("batchType", batchType);
                    model.addAttribute("successMessage", "Trainee Attendance status for " + attendanceDate + " updated successfully\"");
                    return "redirect:showAllBatches";
                }
            }
        }
        model.addAttribute("errorMessage", "Attendance not updated");
        return "error";
    }

    @GetMapping("getAttendancePerDay")
    @ResponseBody
    public Map<String,Object> getAttendancePerDay(Model model,
                                                   @RequestParam("batchId1") String batchId,
                                                  @RequestParam("plannedDate1") LocalDate attendanceDate
    )
    {
        Map<String, Object> response = new HashMap<>();
        BatchDTO batchDTO = adminService.getBatchById(batchId);
        if(batchDTO.getAttendances()==null)
        {
            response.put("error","Attendance is not uploaded");
        }
        else
        {
            List<AttendanceDTO> attendanceDTOS = batchDTO.getAttendances();
            Optional<AttendanceDTO> matchingAttendance = attendanceDTOS
                    .stream()
                    .filter(attendance->attendance.getAttendanceDate().equals(attendanceDate))
                    .findFirst();
            AttendanceDTO attendanceDTO = null;
            if(!matchingAttendance.isPresent())
            {
                response.put("error","Attendance is not uploaded yet for " + attendanceDate);
            }
            else
            {
                attendanceDTO = matchingAttendance.get();
                System.out.println("attendance dto print: " + attendanceDTO);
                List<TraineeDTO> traineeDTOList = attendanceDTO.getTraineeList();
                List<String> attendanceIdWithStatus = new ArrayList<>();
                for(TraineeDTO traineeDTO : traineeDTOList)
                {
                    Map<LocalDate,String> attendance = traineeDTO.getAttendance();
                    String status = "";
                    for (Map.Entry<LocalDate, String> entry : attendance.entrySet()) {
                        LocalDate date = entry.getKey();
                        String value = entry.getValue();
                        if (date.equals(attendanceDate)) {
                            status += traineeDTO.getEmployeeId() + " " + value;
                        }
                    }
                    attendanceIdWithStatus.add(status);
                }
                response.put("traineeList",attendanceDTO.getTraineeList());
                response.put("attendanceDto",attendanceDTO);
                response.put("attendanceStatus",attendanceIdWithStatus);
            }
        }
        return response;
    }

    @GetMapping("getTraineeAttendance")
    @ResponseBody
    public Map<String,Object> getTraineeAttendance(Model model,
                                   @RequestParam("traineeId") Integer traineeId
    )
    {
        TraineeDTO traineeDTO = adminService.getTrainee(traineeId);
        Map<LocalDate,String> attendance = traineeDTO.getAttendance();
        // Convert Map to List of Map.Entry
        List<Map.Entry<LocalDate, String>> entryList = new ArrayList<>(attendance.entrySet());

        // Sort the list by the key (LocalDate)
        entryList.sort(Map.Entry.comparingByKey());

        // (Optional) Rebuild the sorted map
        Map<LocalDate, String> sortedAttendance = new LinkedHashMap<>();
        for (Map.Entry<LocalDate, String> entry : entryList) {
            sortedAttendance.put(entry.getKey(), entry.getValue());
        }

        System.out.println("trainee attendance = " + sortedAttendance);
        Map<String, Object> response = new HashMap<>();

        if(sortedAttendance.isEmpty())
            response.put("error","Attendance is not uploaded yet for " + traineeDTO.getTraineeName());
        response.put("traineeId", traineeDTO.getEmployeeId());
        response.put("traineeName",traineeDTO.getTraineeName());
        response.put("traineeAttendance", sortedAttendance);

        // Return the data as JSON
        return response;
    }


    @GetMapping("getTraineeAssignment")
    @ResponseBody
    public Map<String,Object> getTraineeAssignment(Model model,
                                                   @RequestParam("traineeId") Integer traineeId
    )
    {
        System.out.println("cxbvmn");
        TraineeDTO traineeDTO = adminService.getTrainee(traineeId);

        System.out.println("trainee " + traineeDTO);
        Map<AssignmentPlanDTO,Double> assignmentsMarks= trainerService.getAllAssignmentMarksOfTrainee(traineeDTO);


        Map<String, Object> response = new HashMap<>();

        System.out.println("trainee marks in get trainee assignment = " + assignmentsMarks);
        if(assignmentsMarks.isEmpty())
            response.put("error","Assignment marks is not uploaded yet for " + traineeDTO.getTraineeName());
        response.put("traineeId", traineeDTO.getEmployeeId());
        response.put("traineeName",traineeDTO.getTraineeName());
        response.put("traineeMarks", assignmentsMarks);

        // Return the data as JSON
        return response;
    }

    @GetMapping("getTraineeAssessment")
    @ResponseBody
    public Map<String,Object> getTraineeAssessment(Model model,
                                                   @RequestParam("traineeId") Integer traineeId
    )
    {
        TraineeDTO traineeDTO = adminService.getTrainee(traineeId);

        System.out.println("trainee " + traineeDTO);
        Map<AssessmentDTO,Double> assessmentsMarks= trainerService.getAllAssessmentMarksOfTrainee(traineeDTO);

        System.out.println("trainee marks in get trainee assessment = " + assessmentsMarks);

        Map<String, Object> response = new HashMap<>();
        if(assessmentsMarks.isEmpty())
            response.put("error","Assessment marks is not uploaded yet for " + traineeDTO.getTraineeName());
        response.put("traineeId", traineeDTO.getEmployeeId());
        response.put("traineeName",traineeDTO.getTraineeName());
        response.put("traineeMarks", assessmentsMarks);

        // Return the data as JSON
        return response;
    }

    @GetMapping("viewTraineesAssessmentMarks")
    @ResponseBody
    public Map<String,Object> viewTraineeAssessmentMarks(@RequestParam("assessmentId") Integer assessmentId )
    {
        AssessmentDTO assessmentDTO = adminService.getAssessmentById(assessmentId);
        Map<String, Double> marksObtainedByName = new HashMap<>();

        // Get the map of marks obtained (trainee ID -> marks) from the assignment plan
        Map<Integer, Double> marksObtained = assessmentDTO.getMarksObtained();

        for (Map.Entry<Integer, Double> entry : marksObtained.entrySet()) {
            Integer traineeId = entry.getKey();
            Double marks = entry.getValue();  // pura trainee dto send krdo and then vha ajax mein compare krna hoga
            String traineeName = adminService.getTrainee(traineeId).getTraineeName();
            marksObtainedByName.put(traineeName, marks);
        }
        Map<String, Object> response = new HashMap<>();

        System.out.println("marks in assessment = " + marksObtainedByName);
        if(marksObtainedByName.isEmpty())
            response.put("error","Assessment marks not uploaded yet for " + assessmentDTO.getSubTopic());

        response.put("assessmentDescription", assessmentDTO.getSubTopic());
        response.put("assessmentTotalScore", assessmentDTO.getTotalScore());
        response.put("assessmentMarksObtainedByName", marksObtainedByName);

        return response;
    }



    @GetMapping("viewTraineesAssignmentMarks")
    @ResponseBody
    public Map<String,Object> viewTraineeAssignmentMarks(@RequestParam("assignmentId") Integer assignmentId )
    {
        AssignmentPlanDTO assignmentPlanDTO = adminService.getAssignmentById(assignmentId);

        Map<String, Double> marksObtainedByName = new HashMap<>();
        // Get the map of marks obtained (trainee ID -> marks) from the assignment plan
        Map<Integer, Double> marksObtained = assignmentPlanDTO.getMarksObtained();


        for (Map.Entry<Integer, Double> entry : marksObtained.entrySet()) {
            Integer traineeId = entry.getKey();
            Double marks = entry.getValue();
            String traineeName = adminService.getTrainee(traineeId).getTraineeName();
            marksObtainedByName.put(traineeName, marks);
        }
        System.out.println("marks in assignment = " + marksObtainedByName);

        Map<String, Object> response = new HashMap<>();

        if(marksObtainedByName.isEmpty())
            response.put("error","Assignment marks not uploaded yet for " + assignmentPlanDTO.getDescription());

        response.put("assignmentDescription", assignmentPlanDTO.getDescription());
        response.put("totalScore", assignmentPlanDTO.getTotalScore());
        response.put("marksObtainedByName", marksObtainedByName);
        return response;
    }

    @PostMapping("updateAssignmentTotalMarks")
    public String updateAssignmentTotalMarks(Model model,
                                             @RequestParam("id") Integer id,
                                             @RequestParam("totalMarks") Double totalMarks,
                                             @RequestParam("divType") String divTypes,
                                             @RequestParam("batchType") String batchTypes)
    {
        divType = divTypes;
        batchType = batchTypes;
        System.out.println("id = " + id);
        AssignmentPlanDTO assignmentPlanDTO = adminService.getAssignmentById(id);
        assignmentPlanDTO.setTotalScore(totalMarks);
        if(adminService.updateAssignmentTotalScore(assignmentPlanDTO))
        {
            model.addAttribute("divType",divType);
            model.addAttribute("batchType",batchType);
            model.addAttribute("successMessage","Marks updated successfully for " + assignmentPlanDTO.getDescription());
            return "redirect:showAllBatches";
        }
        model.addAttribute("errorMessage", "Assignment marks not updated for id : " + id);
        return "error";

    }

    @GetMapping("giveAssignmentMarksForm")
    public String giveAssignmentMarksForm(Model model,
                                          @RequestParam("assignmentId") Integer assignmentId,
                                          @RequestParam("batchId") String batchId,
                                          @RequestParam("divType") String divTypes,
                                          @RequestParam("batchType") String batchTypes)
    {
        divType = divTypes;
        batchType = batchTypes;
        List<TraineeDTO> traineeDTOList = trainerService.getTraineeListByBatchId(batchId);
        System.out.println("trainee list ass = " + traineeDTOList);
        AssignmentPlanDTO assignmentPlanDTO = trainerService.getAssignmentById(assignmentId);

        model.addAttribute("assignmentPlanDto",assignmentPlanDTO);
        model.addAttribute("assignmentId",assignmentId);
        model.addAttribute("trainees",traineeDTOList);
        return "mark-assignment-score";
    }

    @PostMapping("saveAssignmentScore")
    public String saveAssignmentScore(@RequestParam Map<String, String> allParams,
                                      @RequestParam("assignmentId") Integer assignmentId,

                                      Model model) {
        System.out.println("params = " +allParams);
        AssignmentPlanDTO assignmentPlanDTO = trainerService.getAssignmentById(assignmentId);
        Map<Integer,Double> assignmentMarks = new HashMap<>();
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            String paramName = entry.getKey();
            String paramValue = entry.getValue();

            // Extract traineeId from the parameter name
            if (paramName.startsWith("marks[")) {
                String traineeId = paramName.substring(6, paramName.length() - 1);
                if("-1".equalsIgnoreCase(paramValue))
                    assignmentMarks.put(Integer.parseInt(traineeId),0.0);
                else
                    assignmentMarks.put(Integer.parseInt(traineeId),Double.valueOf(paramValue));
                System.out.println("Trainee ID: " + traineeId + ", Marks: " + paramValue);
            }
        }
        assignmentPlanDTO.setMarksObtained(assignmentMarks);
        System.out.println("marks = " + assignmentMarks);

        if(trainerService.saveAssignmentMarks(assignmentPlanDTO))
        {
            model.addAttribute("divType",divType);
            model.addAttribute("batchType",batchType);
            model.addAttribute("successMessage", "Assignment marks uploaded successfully for " + assignmentPlanDTO.getDescription());
            return "redirect:showAllBatches";
        }
        model.addAttribute("errorMessage", "Assignment marks not saved for assignment id = " +assignmentId);
        return "error";

    }

    @GetMapping("updateAssignmentMarksForm")
    public String updateAssignmentMarksForm(Model model,
                                            @RequestParam("assignmentId") Integer assignmentId,
                                            @RequestParam("batchId") String batchId,
                                            @RequestParam("divType") String divTypes,
                                            @RequestParam("batchType") String batchTypes)
    {
        divType = divTypes;
        batchType = batchTypes;
        AssignmentPlanDTO assignmentPlanDTO = trainerService.getAssignmentById(assignmentId);
        Map<Integer,Double> assignmentMarksTrainees = assignmentPlanDTO.getMarksObtained();
        System.out.println("marks = " + assignmentMarksTrainees);

        BatchDTO batchDTO = adminService.getBatchById(batchId);
        List<TraineeDTO> traineeDTOList = batchDTO.getTraineeList();
        model.addAttribute("assignmentPlanDto",assignmentPlanDTO);
        model.addAttribute("trainees",traineeDTOList);
        return "update-Assignment-Marks-Form";
    }



    @GetMapping("updateAssignmentMarks")
    public String updateAssignmentMarks(Model model,
                                        @RequestParam("assignmentId") Integer assignmentId,
                                        @RequestParam("batchId") String batchId,
                                        @RequestParam("divType") String divTypes,
                                        @RequestParam("batchType") String batchTypes)
    {
        divType = divTypes;
        batchType = batchTypes;
        System.out.println("Assignment id = " + assignmentId);
        AssignmentPlanDTO assignmentPlanDTO = trainerService.getAssignmentById(assignmentId);

        if(assignmentPlanDTO.getMarksObtained().isEmpty())
        {
            return "forward:giveAssignmentMarksForm";
        }
        else
        {
            return "forward:updateAssignmentMarksForm";
        }
    }


//    assessment score
    @GetMapping("giveAssessmentMarksForm")
    public String giveAssessmentMarksForm(Model model,
                                          @RequestParam("assessmentId") Integer assessmentId,
                                          @RequestParam("batchId") String batchId,
                                          @RequestParam("divType") String divTypes,
                                          @RequestParam("batchType") String batchTypes)
    {
        divType = divTypes;
        batchType = batchTypes;
        List<TraineeDTO> traineeDTOList = trainerService.getTraineeListByBatchId(batchId);
        System.out.println("trainee list ass = " + traineeDTOList);
        AssessmentDTO assessmentDTO = trainerService.getAssessmentById(assessmentId);

        model.addAttribute("assessmentPlanDto",assessmentDTO);
        model.addAttribute("assessmentId",assessmentId);
        model.addAttribute("trainees",traineeDTOList);
        return "mark-assessment-score";
    }

    @PostMapping("saveAssessmentScore")
    public String saveAssessmentScore(@RequestParam Map<String, String> allParams,
                                      @RequestParam("assessmentId") Integer assessmentId,
                                      Model model) {
        System.out.println("params = " +allParams);
        AssessmentDTO assessmentDTO = trainerService.getAssessmentById(assessmentId);
        Map<Integer,Double> assessmentMarks = new HashMap<>();
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            String paramName = entry.getKey();
            String paramValue = entry.getValue();

            // Extract traineeId from the parameter name
            if (paramName.startsWith("marks[")) {
                String traineeId = paramName.substring(6, paramName.length() - 1);
                if("-1".equalsIgnoreCase(paramValue))
                    assessmentMarks.put(Integer.parseInt(traineeId),0.0);
                else
                    assessmentMarks.put(Integer.parseInt(traineeId),Double.valueOf(paramValue));
                System.out.println("Trainee ID: " + traineeId + ", Marks: " + paramValue);
            }
        }
        assessmentDTO.setMarksObtained(assessmentMarks);
        System.out.println("marks = " + assessmentMarks);

        if(trainerService.saveAssessmentMarks(assessmentDTO))
        {
            model.addAttribute("divType",divType);
            model.addAttribute("batchType",batchType);
            model.addAttribute("successMessage", "Assessment marks uploaded successfully for " + assessmentDTO.getSubTopic());
            return "redirect:showAllBatches";
        }
        model.addAttribute("errorMessage", "Assessment marks not saved for assignment id = " +assessmentId);
        return "error";

    }

    @GetMapping("updateAssessmentMarksForm")
    public String updateAssessmentMarksForm(Model model,
                                            @RequestParam("assessmentId") Integer assessmentId,
                                            @RequestParam("batchId") String batchId,
                                            @RequestParam("divType") String divTypes,
                                            @RequestParam("batchType") String batchTypes)
    {
        divType = divTypes;
        batchType = batchTypes;
        AssessmentDTO assessmentDTO = trainerService.getAssessmentById(assessmentId);
        Map<Integer,Double> assessmentMarksTrainees = assessmentDTO.getMarksObtained();
        System.out.println("marks = " + assessmentMarksTrainees);
        BatchDTO batchDTO = adminService.getBatchById(batchId);
        List<TraineeDTO> traineeDTOList = batchDTO.getTraineeList();
        model.addAttribute("assessmentPlanDto",assessmentDTO);
        model.addAttribute("trainees",traineeDTOList);
        return "update-Assessment-Marks-Form";
    }

    @GetMapping("updateAssessmentMarks")
    public String updateAssessmentMarks(Model model,
                                        @RequestParam("assessmentId") Integer assessmentId,
                                        @RequestParam("batchId") String batchId,
                                        @RequestParam("divType") String divTypes,
                                        @RequestParam("batchType") String batchTypes)
    {
        divType = divTypes;
        batchType = batchTypes;
        System.out.println("Assessment id = " + assessmentId);
        AssessmentDTO assessment = trainerService.getAssessmentById(assessmentId);

        if(assessment.getMarksObtained().isEmpty())
        {
            return "forward:giveAssessmentMarksForm";
        }
        else
        {
            return "forward:updateAssessmentMarksForm";
        }
    }


    @GetMapping("assessment-form")
    public String goToAssessmentForm(Model model,
                                     @RequestParam("batchId") String batchId,
                                     @RequestParam("batchStartDate") LocalDate batchStartDate,
                                     @RequestParam("divType") String divTypes,
                                     @RequestParam("batchType") String batchTypes)
    {
        divType = divTypes;
        batchType = batchTypes;
        model.addAttribute("batchStartDate",batchStartDate);
        model.addAttribute("batchId",batchId);
        model.addAttribute("assessmentDto",new AssessmentDTO());
        return "assessment-form";

    }

    @PostMapping("addAssessment")
    public String addAssessment(@ModelAttribute("assessmentDto") AssessmentDTO assessmentDTO,
                                @RequestParam("batchId") String batchId,
                                Model model)
    {
        BatchDTO batchDTO = adminService.getBatchById(batchId);

        List<AssessmentDTO> assessmentDTOS = batchDTO.getAssessmentDTOS();
        if(assessmentDTOS!=null)
        {
            assessmentDTOS.add(assessmentDTO);
            batchDTO.setAssessmentDTOS(assessmentDTOS);
        }

        else
        {
            List<AssessmentDTO> assessmentDTOS1 = new ArrayList<>();
            assessmentDTOS1.add(assessmentDTO);
            batchDTO.setAssessmentDTOS(assessmentDTOS1);
        }


        if(adminService.addAssessment(batchDTO)) {
            model.addAttribute("divType",divType);
            model.addAttribute("batchType",batchType);
            model.addAttribute("successMessage","Assignment (" + assessmentDTO.getSubTopic() +") added successfully");
            return "redirect:showAllBatches";
        }

        model.addAttribute("errorMessage","Assessment not added");
        return "error";
    }

    @RequestMapping("updateAssessmentForm")
    public String updateAssessmentForm(Model modal,
                                       @RequestParam("assessmentId") Integer assessmentId,
                                       @RequestParam("divType") String divTypes,
                                       @RequestParam("batchType") String batchTypes)
    {
        System.out.println("form trainer");
        divType = divTypes;
        batchType = batchTypes;
        AssessmentDTO assessmentDTO = adminService.getAssessmentById(assessmentId);
        modal.addAttribute("assessmentDto",assessmentDTO);
        return "update-assessment-form";
    }

    @PostMapping("saveUpdateAssessment")
    public String saveUpdateAssessment(Model model,
                                       @RequestParam("assessmentId") Integer assessmentId,
                                       @RequestParam("totalScore") Double totalScore,
                                       @RequestParam("actualDate") LocalDate actualDate,
                                       @RequestParam("remarks") String remarks)
    {
        System.out.println("zbmcnm");
        AssessmentDTO assessmentDTO = adminService.getAssessmentById(assessmentId);
        assessmentDTO.setActualDate(actualDate);
        assessmentDTO.setRemarks(remarks);
        assessmentDTO.setTotalScore(totalScore);


        if(adminService.updateAssessment(assessmentDTO))
        {
            model.addAttribute("divType",divType);
            model.addAttribute("batchType",batchType);
            model.addAttribute("successMessage","Assignment (" + assessmentDTO.getSubTopic() +") updated successfully");
            return "redirect:showAllBatches";
        }


        model.addAttribute("errorMessage", "Assessment not updated for id : " + assessmentId);
        return "error";
    }

    @RequestMapping("updateHRInduction")
    public String updateInduction(Model model,
                                  @RequestParam("batchid") String batchId,
                                  @RequestParam("topicName") String topicName,
                                  @RequestParam("plannedDate") LocalDate plannedDate,
                                  @RequestParam("divType") String divTypes,
                                  @RequestParam("batchType") String batchTypes)
    {
        divType = divTypes;
        batchType = batchTypes;
        model.addAttribute("batchId",batchId);
        model.addAttribute("topicName",topicName);
        model.addAttribute("plannedDate",plannedDate);
        return "update-hr-induction";
    }

    @PostMapping("saveUpdatedHRInduction")
    public String saveUpdatedHRInduction(Model model,
                                         @RequestParam("batchId") String batchId,
                                         @RequestParam("topicName") String topicName,
                                         @RequestParam("plannedDate") LocalDate plannedDate)
    {
        System.out.println("batch id = " + batchId);
        System.out.println("topic name = " + topicName);
        System.out.println("planned date = " + plannedDate);
        if(adminService.updateCoursePlanDates(batchId,topicName,plannedDate))
        {
            model.addAttribute("divType",divType);
            model.addAttribute("batchType",batchType);
            model.addAttribute("successMessage", "Hr induction date updated successfully for " + batchId);
            return "redirect:showAllBatches";
        }
        model.addAttribute("errorMessage","Hr induction date not updated successfully for " + batchId);
        return "error";
    }


    @RequestMapping("updatePlannedDate")
    public String updatePlannedDate(Model model,
                                    @RequestParam("batchid") String batchId,
                                    @RequestParam("planId") Integer planId,
                                    @RequestParam("topicName") String topicName,
                                    @RequestParam("plannedDate") LocalDate plannedDate,
                                    @RequestParam("divType") String divTypes,
                                    @RequestParam("batchType") String batchTypes
    )
    {
        divType = divTypes;
        batchType = batchTypes;
        model.addAttribute("batchid",batchId);
        model.addAttribute("planId",planId);
        model.addAttribute("plannedDate",plannedDate);
        model.addAttribute("topicName",topicName);
        return "update-topic-planned-date";
    }

    @PostMapping("saveUpdatedPlannedDate")
    public String saveUpdatedPlannedDate(Model model,
                                         @RequestParam("batchId") String batchId,
                                         @RequestParam("planId") Integer planId,
                                         @RequestParam("plannedDate") LocalDate plannedDate,
                                         @RequestParam("actualDate") LocalDate actualDate,
                                         @RequestParam("remarks") String remarks)
    {
        if(adminService.updateTopicPlannedDate(batchId,planId,actualDate,remarks))
        {
            model.addAttribute("divType",divType);
            model.addAttribute("batchType",batchType);
            model.addAttribute("successMessage", "Planned Date for " + batchId + " updated successfully");
            return "redirect:showAllBatches";
        }

        else {
            model.addAttribute("errorMessage","Planned date not updated");
            return "error";
        }

    }

    @GetMapping("downloadCoursePlan")
    public ResponseEntity<InputStreamResource> downloadCoursePlan(@RequestParam("batchId") String batchId)
    {
        BatchDTO batchDTO = adminService.getBatchById(batchId);
        CoursePlanDTO coursePlanDTO = batchDTO.getCoursePlanDto();
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = UtilityFunctions.downloadCoursePlan(coursePlanDTO);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=coursePlan.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);

        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @GetMapping("downloadMentoringSession")
    public ResponseEntity<InputStreamResource> downloadMentoringSession(@RequestParam("batchId") String batchId)
    {
        BatchDTO batchDTO = adminService.getBatchById(batchId);
        List<MentoringSessionDto> mentoringSessionDtos = batchDTO.getMentoringSessionDtos();
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = UtilityFunctions.downloadMentoringSession(mentoringSessionDtos);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mentoringSessionList.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);

        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
        return null;
    }
    @GetMapping("downloadAttendance")
    public ResponseEntity<InputStreamResource> downloadAttendance(@RequestParam("batchId") String batchId,
                                                                  @RequestParam("fromDate") LocalDate fromDate,
                                                                  @RequestParam("toDate") LocalDate toDate)
    {
        System.out.println("From date = " + fromDate);
        System.out.println("to date = " + toDate);
        BatchDTO batchDTO = adminService.getBatchById(batchId);

        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = UtilityFunctions.downloadTraineesAttendance(batchDTO,fromDate,toDate);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attendance.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @GetMapping("downloadAssignmentList")
    public ResponseEntity<InputStreamResource> downloadAssignmentList(@RequestParam("batchId") String batchId)
    {
        BatchDTO batchDTO = adminService.getBatchById(batchId);
        List<DayWisePlanDTO> dayWisePlanDTOS = batchDTO.getCoursePlanDto().getDayWisePlanDTOS();
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = UtilityFunctions.downloadAssignmentList(batchDTO);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=assignmentsList.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);

        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @GetMapping("downloadAssessments")
    public ResponseEntity<InputStreamResource> downloadAssessments(@RequestParam("batchId") String batchId)
    {
        BatchDTO batchDTO = adminService.getBatchById(batchId);
        List<AssessmentDTO> assessmentDTOS = batchDTO.getAssessmentDTOS();

        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = UtilityFunctions.downloadAssessments(batchDTO);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=assessmentsList.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

}
