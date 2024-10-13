package com.parishi.controller;


import com.parishi.dto.*;
import com.parishi.service.AdminService;
import com.parishi.service.TrainerService;
import com.parishi.service.UserService;
import com.parishi.utility.ExcelFileReader;
import com.parishi.utility.LocalDateEditor;
import com.parishi.utility.LocalTimeEditor;
import com.parishi.utility.UtilityFunctions;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private Logger logger;

    @Autowired
    private AdminService adminService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private UserService userService;

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


    @GetMapping("viewTrainee")
    public String showAllTrainees(Model model) {

        List<TraineeDTO> trainees = adminService.getAllTrainees();
        model.addAttribute("trainees", trainees);
        logger.info("Trainees found");
        return "viewAllTrainees";
    }

    @GetMapping("trainee-form")
    public String traineeForm(Model model)
    {
        TraineeDTO traineeDTO = new TraineeDTO();
        model.addAttribute("traineeDTO",traineeDTO);
        return "traineeRegistrationForm";
    }

    @PostMapping("registerTrainee")
    public String registerTrainee(@Valid @ModelAttribute("traineeDTO") TraineeDTO traineeDto, BindingResult bindingResult, RedirectAttributes model)
    {
        traineeDto.setStatusFlag(0);
        if (bindingResult.hasErrors()) {
            return "traineeRegistrationForm";
        }

        if(adminService.registerTrainee(traineeDto))
        {
            String username=traineeDto.getTraineeName().toLowerCase().replace(" ","")+traineeDto.getEmployeeId();
            userService
                    .registerUser(username,
                            "TRAINEE",
                            String.valueOf(traineeDto.getEmployeeId()));
            model.addAttribute("successMessage", "Trainee (" + traineeDto.getEmployeeId() + ") registered successfully");
        }
        return "redirect:viewTrainee";
    }


    @PostMapping("/bulkUpload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes model) {
        try {
            List<TraineeDTO> traineeDTOList = null;
            if (!file.isEmpty()) {
                traineeDTOList= ExcelFileReader.traineeToListConverter(file);
            }

            if (adminService.insertBulkTrainee(traineeDTOList))
            {
                for (TraineeDTO trainee : traineeDTOList)
                {
                    try
                    {
                        String username=trainee.getTraineeName().toLowerCase().replace(" ","")+trainee.getEmployeeId();
                        userService
                                .registerUser(username,
                                        "TRAINEE",
                                        String.valueOf(trainee.getEmployeeId()));
                        model.addAttribute("successMessage", "Trainee  list uploaded successfully");
                    } catch (Exception e)
                    {
                        model.addAttribute("errorMessage", "Failed to register some trainees.");
                    }
                }
                return "redirect:viewTrainee";

            } else
            {
                model.addAttribute("errorMessage","Trainees could not be registered.");
                return "error";
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage","Trainees could not be registered.");
            logger.error("Trainees could not be registered");
            return "error";
        }

    }
    @PostMapping(path = "/updateTraineeForm")
    public String updateTraineeForm(@RequestParam("employeeId") String employeeId, Model model) {
        try {
            TraineeDTO trainee = adminService.getTrainee(Integer.parseInt(employeeId));

            if(trainee==null){
                model.addAttribute("errorMessage", "Trainee not found with given id");
                return "error";
            }

            model.addAttribute("traineeDTO",trainee);

            return "updateTraineeForm";

        }catch (Exception e){
            model.addAttribute("errorMessage", "Trainee not found with given id");
            logger.error("Trainee not found with given id");
            return "error";
        }
    }

    @PostMapping(path = "/updatingTrainee")
    public String updatingTrainee(@ModelAttribute("traineeDTO")  @Valid TraineeDTO traineeDTO , BindingResult result , RedirectAttributes model ) {
        TraineeDTO trainee = adminService.getTrainee(traineeDTO.getEmployeeId());
        traineeDTO.setStatusFlag(trainee.getStatusFlag());
        if (result.hasErrors()) {
            return "updateTraineeForm";
        }
        if(adminService.updateTrainee(traineeDTO))
        {
            model.addAttribute("successMessage", "Trainee (" + traineeDTO.getEmployeeId() + ") updated successfully");
            return "redirect:viewTrainee";
        }

        model.addAttribute("errorMessage", "Trainee (" + traineeDTO.getEmployeeId() + ") not updated successfully");
        logger.error("Trainee (" + traineeDTO.getEmployeeId() + ") not updated successfully");
        return "error";
    }

    @PostMapping(path = "downloadTrainees")
    public ResponseEntity<InputStreamResource> downloadTrainees() {
        List<TraineeDTO> trainees = adminService.getAllTrainees();
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = UtilityFunctions.downloadTraineeContent(trainees);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=trainees.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);

        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @GetMapping("viewTrainer")
    public String showAllTrainers(Model model) {

        List<TrainerDTO> trainers = adminService.getAllTrainers();
        model.addAttribute("trainers", trainers);
        logger.info("Trainers found");
        return "viewAllTrainers";
    }

    @GetMapping("trainer-form")
    public String trainerForm(Model model)
    {
        TrainerDTO trainerDto = new TrainerDTO();
        model.addAttribute("trainerDto",trainerDto);
        return "trainerRegistrationForm";
    }

    @PostMapping("registerTrainer")
    public String registerTrainer(@ModelAttribute("trainerDto") @Valid TrainerDTO trainerDto, BindingResult bindingResult, RedirectAttributes model)
    {
        if (bindingResult.hasErrors()) {
            return "trainerRegistrationForm";
        }

        if(adminService.registerTrainer(trainerDto))
        {
            String username = trainerDto.getTrainerName().toLowerCase().replace(" ", "")+trainerDto.getTrainerId();
            userService.registerUser(username,
                    "TRAINER",
                    String.valueOf(username.substring(0,3) + "@" + trainerDto.getTrainerId()));
            model.addAttribute("successMessage", "Trainer (" + trainerDto.getTrainerId() + ") register successfully");
            return "redirect:viewTrainer";
        }
        model.addAttribute("errorMessage", "Trainer (" + trainerDto.getTrainerId() + ") not register successfully");
        logger.error("Trainer (" + trainerDto.getTrainerId() + ") not register successfully");
        return "error";
    }

    @PostMapping("update-Trainer")
    public String updateTrainerForm(@RequestParam("trainerId") String trainerId, Model model) {
        try {
            TrainerDTO trainer = adminService.getTrainer(Integer.parseInt(trainerId));
            model.addAttribute("trainerDTO", trainer);

            return "updateTrainerForm";
        }catch (Exception e){
            logger.error("Trainer not found with given id");
            return "error";
        }
    }

    @PostMapping(path = "/updatingTrainer")
    public String updatingTrainer(@ModelAttribute("trainerDTO") @Valid TrainerDTO trainerDTO, BindingResult bindingResult,RedirectAttributes model) {

        trainerDTO.setStatusFlag(0);
        if (bindingResult.hasErrors()) {
            return "updateTrainerForm";
        }

        if(adminService.updateTrainer(trainerDTO))
        {
            model.addAttribute("successMessage", "Trainer (" + trainerDTO.getTrainerId() + ") updated successfully");
            return "redirect:viewTrainer";
        }
        model.addAttribute("errorMessage", "Trainer (" + trainerDTO.getTrainerId() + ") not updated successfully");
        logger.error("Trainer (" + trainerDTO.getTrainerId() + ") not updated");
        return "error";
    }

    @PostMapping(path = "downloadTrainers")
    public ResponseEntity<InputStreamResource> downloadTrainers() {
        List<TrainerDTO> trainers = adminService.getAllTrainers();
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = UtilityFunctions.downloadTrainerContent(trainers);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=trainers.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);

        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
        return null;
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



    @GetMapping("showAllBatches")
    public String showAllBatches(Model model)
    {
        model.addAttribute("divType",divType);
        model.addAttribute("batchType",batchType);
        // Get the list of all batches
        List<BatchDTO> batches = adminService.getAllBatches();

        // Sort the batches by Batch ID in ascending order
        batches.sort(Comparator.comparing(BatchDTO::getBatchId));

        for (BatchDTO batch : batches) {
            if (batch.getAssessmentDTOS() != null) {
                Collections.sort(batch.getAssessmentDTOS(), new Comparator<AssessmentDTO>()
                {
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
        // Add the sorted list to the model
        model.addAttribute("BatchDto", batches);
        model.addAttribute("currentDate", LocalDate.now());

        // Return the view name
        return "viewAllBatches";
    }

    @GetMapping("batch-form")
    public String batchForm(Model model,
                            @RequestParam("divType") String divTypes,
                            @RequestParam("batchType") String batchTypes)
    {
        divType = divTypes;
        batchType = batchTypes;
        model.addAttribute("trainerList",adminService.getAllTrainerWithStatusFlagZero());
        model.addAttribute("batchDto",new BatchDTO());
        return "batchCreationForm";
    }



    @PostMapping("createNewBatch")
    public String createBatch(@ModelAttribute("batchDto") BatchDTO batchDTO,
                              @RequestParam("coursePlan") MultipartFile file,
                              @RequestParam("holidayType") List<String> holidayType,
                              @RequestParam("holidayName")  List<String>  holidayName,
                              @RequestParam("holidayDate")  List<String>  holidayDate,
                              Model model)
    {
        System.out.println("holiday type = " + holidayType);
        System.out.println("holiday name = " + holidayName);
        System.out.println("holiday date = " + holidayDate);
        List<HolidayDTO> holidayDTOS = adminService.createHolidayList(holidayType,holidayName,holidayDate);
        batchDTO.setHolidayDTOS(holidayDTOS);


        System.out.println("Holiday = " + holidayDTOS);
        try {
            if (!file.isEmpty()) {
                List<DayWisePlanDTO> dayWisePlanDTOS = ExcelFileReader.fetchCoursePlanExcel(file,batchDTO);
                CoursePlanDTO coursePlanDTO = new CoursePlanDTO();
                coursePlanDTO.setDayWisePlanDTOS(dayWisePlanDTOS);

                System.out.println("day wise plan dto in create batch= " + coursePlanDTO.getDayWisePlanDTOS());

                batchDTO.setCoursePlanDto(coursePlanDTO);
                TrainerDTO trainerDTO = adminService.getTrainer(batchDTO.getTrainerDto().getTrainerId());
                trainerDTO.setStatusFlag(1);
                batchDTO.setTrainerDto(trainerDTO);

                System.out.println("batch dto while creating batch = " + batchDTO);


                if(adminService.createBatch(batchDTO))
                {model.addAttribute("divType",divType);
                    model.addAttribute("batchType",batchType);
                    model.addAttribute("successMessage", "Batch created successfully");
                    return "redirect:showAllBatches";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage","Batch could not be created.");
            logger.error("Batch could not be created");
            return "error";
        }
        model.addAttribute("errorMessage", "Batch not created successfully");
        return "error";
    }

    @GetMapping("mentoring-session-form")
    public String mentoringSessionForm(Model model,
                                       @RequestParam("batchId") String batchId,
                                       @RequestParam("batchStartDate") LocalDate batchStartDate,
                                       @RequestParam("divType") String divTypes,
                                       @RequestParam("batchType") String batchTypes)
    {
        divType = divTypes;
        batchType = batchTypes;
        model.addAttribute("batchStartDate",batchStartDate);
        model.addAttribute("batchId",batchId);
        model.addAttribute("mentoringSessionDto",new MentoringSessionDto());
        return "mentoring-session-form";
    }

    @PostMapping("addMentoringSession")
    public String addMentoringSession(@ModelAttribute("mentoringSessionDto") MentoringSessionDto mentoringSessionDto,
                                      @RequestParam("batchId") String batchId,
                                      Model model)
    {
        BatchDTO batchDTO = adminService.getBatchById(batchId);
        batchDTO.getMentoringSessionDtos().add(mentoringSessionDto);
        if(adminService.addMentoringSession(batchDTO))
        {
            model.addAttribute("successMessage", "(" + mentoringSessionDto.getSessionDescription() + ") session for " + batchId + " added successfully");
            model.addAttribute("divType",divType);
            model.addAttribute("batchType",batchType);
            return "redirect:showAllBatches";
        }
        model.addAttribute("errorMessage", "Mentoring session not added successfully");
        return "error";
    }

    @RequestMapping("updateMentoringSession")
    public String updateMentoringSession(Model modal,
                                         @RequestParam("sessionId") Integer sessionId,
                                         @RequestParam("divType") String divTypes,
                                         @RequestParam("batchType") String batchTypes)
    {
        divType = divTypes;
        batchType = batchTypes;
        MentoringSessionDto mentoringSessionDto = adminService.getMentoringSessionById(sessionId);
        modal.addAttribute("mentoringSessionDto",mentoringSessionDto);
        return "update-mentoring-session";
    }

    @PostMapping("saveUpdateMentoringSession")
    public String saveUpdateMentoringSession(Model model,
                                             @RequestParam("sessionId") Integer sessionId,
                                             @RequestParam("plannedTime") LocalTime plannedTime,
                                             @RequestParam("actualDate") LocalDate actualDate,
                                             @RequestParam("remarks") String remarks)
    {
        MentoringSessionDto mentoringSessionDto = adminService.getMentoringSessionById(sessionId);
        mentoringSessionDto.setPlannedTime(plannedTime);
        mentoringSessionDto.setActualDate(actualDate);
        mentoringSessionDto.setRemarks(remarks);
        if(adminService.updateMentoringSession(mentoringSessionDto))
        {
            model.addAttribute("divType",divType);
            model.addAttribute("batchType",batchType);
            model.addAttribute("successMessage", "Mentoring session (" + mentoringSessionDto.getSessionDescription() + ") updated successfully");
            return "redirect:showAllBatches";
        }
        else
        {
            model.addAttribute("errorMessage","Mentoring session not updated");
            return "error";
        }
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

    @GetMapping("addTrainee")
    public ModelAndView showAddTraineesPage(@RequestParam("batchId") String batchId,
                                            @RequestParam("divType") String divTypes,
                                            @RequestParam("batchType") String batchTypes)
    {
        divType = divTypes;
        batchType = batchTypes;
        // Retrieve all trainees to display on the JSP page
        List<TraineeDTO> traineeList = adminService.getUnAssignedTrainees();

        ModelAndView mv = new ModelAndView("traineeselect");
        // Add these lists to the model
        mv.addObject("batchId", batchId);
        mv.addObject("trainees", traineeList);

        return mv;
    }

    @PostMapping("addTraineesToBatch")
    public String addTraineesToBatch(@RequestParam String batchId,
                                     @RequestParam List<Integer> selectedTrainees,
                                     Model model) {
        // Retrieve the batch by ID
        BatchDTO batchDTO = adminService.getBatchById(batchId);

        List<TraineeDTO> trainees=new ArrayList<>();
        // Add selected trainees to the batch
        for ( int traineeId : selectedTrainees)
        {
            TraineeDTO traineeDTO = adminService.getTrainee(traineeId);
            traineeDTO.setStatusFlag(1);
            trainees.add(traineeDTO);
        }

        batchDTO.getTraineeList().addAll(trainees);
        if(adminService.updateBatch(batchDTO))
        {
            model.addAttribute("divType",divType);
            model.addAttribute("batchType",batchType);
            model.addAttribute("successMessage", "Trainees added successfully in batch " + batchId);
            return "redirect:showAllBatches";
        }
        // Redirect to the batch list or show a success message
        model.addAttribute("errorMessage", "Trainees is not successfully added to the batch.");
        return "error";
    }


//    Assessment

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

    @GetMapping("updatebatchtrainee")
    public ModelAndView showUpdateBatchTraineesPage(@RequestParam("batchId") String batchId,
                                                    @RequestParam("divType") String divTypes,
                                                    @RequestParam("batchType") String batchTypes)
    {

        divType = divTypes;
        batchType = batchTypes;
        BatchDTO batchDTO = adminService.getBatchById(batchId);
        List<TraineeDTO> traineeList = batchDTO.getTraineeList();

        ModelAndView mv = new ModelAndView("updatebatchtrainee");
        mv.addObject("batchId", batchId);
        mv.addObject("trainees", traineeList);
        return mv;
    }

    @PostMapping("removeTraineesFromBatch")
    public String removeTraineesFromBatch(
            @RequestParam("batchId") String batchId,
            @RequestParam("selectedTrainees") List<Integer> selectedTrainees,
            Model model) {

        BatchDTO batchDTO = adminService.getBatchById(batchId);
        List<TraineeDTO> trainees = batchDTO.getTraineeList();

        // Create a list to store the remaining trainees
        List<TraineeDTO> remainingTrainees = new ArrayList<>();

        // Iterate through the current list of trainees in the batch
        for (TraineeDTO trainee : trainees) {
            if (selectedTrainees.contains(trainee.getEmployeeId())) {
                // Set statusFlag to 0 for trainees being removed
                trainee.setStatusFlag(0);
                adminService.updateTrainee(trainee);
            } else {
                // Add trainees not selected for removal to the remaining list
                remainingTrainees.add(trainee);
            }
        }

        // Update the batch with the remaining trainees
        batchDTO.setTraineeList(remainingTrainees);
        if(adminService.updateBatch(batchDTO))
        {
            model.addAttribute("divType",divType);
            model.addAttribute("batchType",batchType);
            model.addAttribute("successMessage","Trainees removed successfully from batch " + batchId);
            return "redirect:showAllBatches";
        }
        model.addAttribute("errorMessage", "Trainees does not removed successfully from batch " + batchId);
        return "error";
    }

    //    Assignment
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

        System.out.println("trainee dto list = " + traineeDTOList);
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

        System.out.println("assignmentPlanDto = " + assignmentPlanDTO);

        model.addAttribute("batchId",batchId);
        if(assignmentPlanDTO.getMarksObtained().isEmpty())
        {
            return "forward:giveAssignmentMarksForm";
        }
        else
        {
            return "forward:updateAssignmentMarksForm";
        }
    }
    @PostMapping("updateAssignmentTotalMarks")
    public String updateAssignmentTotalMarks(Model model,
                                             @RequestParam("id") Integer id,
                                             @RequestParam("totalMarks") Double totalMarks,
                                             @RequestParam("divType") String divTypes,
                                             @RequestParam("batchType") String batchTypes) {

        AssignmentPlanDTO assignmentPlanDTO = adminService.getAssignmentById(id);
        assignmentPlanDTO.setTotalScore(totalMarks);
        divType = divTypes;
        batchType = batchTypes;

        if (adminService.updateAssignmentTotalScore(assignmentPlanDTO)) {
            model.addAttribute("divType",divType);
            model.addAttribute("batchType",batchType);
            model.addAttribute("successMessage", "Total Marks for " + assignmentPlanDTO.getDescription() + " updated successfully");
            return "redirect:showAllBatches";
        }
        model.addAttribute("errorMessage", "Total Marks for " + assignmentPlanDTO.getDescription() + "not updated successfully");
        return "error";
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

        Map<String, Object> response = new HashMap<>();
        if(marksObtainedByName.isEmpty())
            response.put("error","Assignment marks not uploaded yet for " + assignmentPlanDTO.getDescription());
        response.put("assignmentDescription", assignmentPlanDTO.getDescription());
        response.put("totalScore", assignmentPlanDTO.getTotalScore());
        response.put("marksObtainedByName", marksObtainedByName);
        return response;
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
    public ResponseEntity<Map<String,Object>> getTraineeAttendance(Model model,
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

        System.out.println("Attendance = " + sortedAttendance);
        Map<String, Object> response = new HashMap<>();
        if(sortedAttendance.isEmpty()) {
            System.out.println("mbds");
            response.put("error", "Attendance is not uploaded yet for " + traineeDTO.getTraineeName());
        }
        response.put("traineeId", traineeDTO.getEmployeeId());
        response.put("traineeName",traineeDTO.getTraineeName());
        response.put("traineeAttendance", sortedAttendance);

        // Return the data as JSON
        return ResponseEntity.ok(response);
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
            Double marks = entry.getValue();
            String traineeName = adminService.getTrainee(traineeId).getTraineeName();
            marksObtainedByName.put(traineeName, marks);
        }

        Map<String, Object> response = new HashMap<>();
        if(marksObtainedByName.isEmpty())
            response.put("error","Assessment marks not uploaded yet for " + assessmentDTO.getSubTopic());
        response.put("assessmentDescription", assessmentDTO.getSubTopic());
        response.put("assessmentTotalScore", assessmentDTO.getTotalScore());
        response.put("assessmentMarksObtainedByName", marksObtainedByName);

        return response;
    }

    @GetMapping("traineeReport")
    public ModelAndView showTraineeReport(@RequestParam("batchId") String batchId,
                                          @RequestParam("divType") String divTypes,
                                          @RequestParam("batchType") String batchTypes)
    {
        divType=divTypes;
        batchType=batchTypes;
        BatchDTO batchDTO=adminService.getBatchById(batchId);
        List<TraineeDTO> traineeDTOList=batchDTO.getTraineeList();

        ModelAndView mv =new ModelAndView("trainee-select-report");
        mv.addObject("batchId",batchId);
        mv.addObject("trainees",traineeDTOList);

        return mv;
    }


    @PostMapping("selectHeader")
    public ModelAndView selectHeader(@RequestParam String batchId,
                                     @RequestParam List<Integer> selectedTrainees)
    {
        List<String> availableColumns = Arrays.asList
                ("TraineeId","TraineeName", "Date Of Joining", "OfficeEmailId", "MobileNo",
                "Gender", "College", "Branch", "FinalGrade",
                "FinalMarks", "TenthMarks", "TwelvethMarks", "Remarks");

        BatchDTO batchDTO=adminService.getBatchById(batchId);
        List<DayWisePlanDTO> dayWisePlanDTOS= batchDTO.getCoursePlanDto().getDayWisePlanDTOS();
        List<AssignmentPlanDTO> assignmentPlanDTOList = new ArrayList<>();
        List<String> availableTopics = new ArrayList<>();

        for (DayWisePlanDTO dayWisePlan: dayWisePlanDTOS)
        {
            if(dayWisePlan.getAssignmentPlanDTO()!=null)
            {
                assignmentPlanDTOList.add(dayWisePlan.getAssignmentPlanDTO());
            }
        }

        for (AssignmentPlanDTO assignment: assignmentPlanDTOList)
        {
            String[] parts = assignment.getDescription().split("\\s*[-–]\\s*", 2);
            if (parts.length > 0) {
                String topic = parts[0].trim().toUpperCase();

                // Normalize PLSQL and SQL as the same topic
                if (topic.equals("PLSQL"))
                {
                    topic = "SQL";
                }
                if(!availableTopics.contains(topic))
                {
                    availableTopics.add(topic);
                }
            }
        }
        availableTopics.add("ALL/FINAL Report");// For final report generation

        ModelAndView mv = new ModelAndView("header-selection");
        mv.addObject("batchId",batchId);
        mv.addObject("trainees",selectedTrainees);
        mv.addObject("availableColumns",availableColumns);
        mv.addObject("availableTopics",availableTopics);

        return mv;
    }

    @PostMapping("/generateReport")
    public ResponseEntity<byte[]> generateReport(@RequestParam List<Integer> trainees,
                                                 @RequestParam List<String> selectedColumns,
                                                 @RequestParam String batchId,
                                                 @RequestParam String selectedTopic)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BatchDTO batchDTO=adminService.getBatchById(batchId);
        List<TraineeDTO> trainee = new ArrayList<>();
        double assignmentMarks=0.0;
        double assessmentMarks=0.0;
        double attendanceMarks=0.0;
        double totalMarks=0.0;



        for(Integer traineeId : trainees)
        {
            trainee.add(adminService.getTrainee(traineeId));
        }
        for (TraineeDTO traineeDTO : trainee)
        {
            Map<Double, Double> assignmentMark;
            Map<Double, Double> assessmentMark;
            Map<LocalDate, Double> extractPlanDate;

            if(selectedTopic.equalsIgnoreCase("ALL/FINAL"))
            {
                assignmentMark = adminService.totalAssignmentMarksForAllTopics(batchDTO.getCoursePlanDto().getDayWisePlanDTOS(), traineeDTO.getEmployeeId());

                assessmentMark = adminService.totalAssessmentMarksForAllTopics(batchDTO.getAssessmentDTOS(), traineeDTO.getEmployeeId());

                extractPlanDate = adminService.extractPlanDateForAllTopic(batchDTO.getCoursePlanDto().getDayWisePlanDTOS());

                Map<Double,Double> calculateAttendance=adminService.calculateAttendance(traineeDTO,extractPlanDate);

                assignmentMarks=(assignmentMark.values().iterator().next() / assignmentMark.keySet().iterator().next())*100;

                assessmentMarks=(assessmentMark.values().iterator().next()/assessmentMark.keySet().iterator().next())*100;

                attendanceMarks=(calculateAttendance.values().iterator().next()/calculateAttendance.keySet().iterator().next())*100;

                totalMarks=(assessmentMarks * 0.75)+(assignmentMarks*0.20)+(attendanceMarks*0.05);

                System.out.println("DevilRDX 0 "+totalMarks);
                traineeDTO.setFinalMarks(totalMarks);
                System.out.println("DevilRDX 1 "+adminService.calculateGrade(totalMarks));
                traineeDTO.setFinalGrade(adminService.calculateGrade(totalMarks));
            }
            else
            {
                assignmentMark = adminService.totalAssignmentMarksForGivenTopic(batchDTO.getCoursePlanDto().getDayWisePlanDTOS(), selectedTopic, traineeDTO.getEmployeeId());
                System.out.println("DevilRDX 0 "+assignmentMark);

                assessmentMark = adminService.totalAssessmentMarksForGivenTopic(selectedTopic, batchDTO.getAssessmentDTOS(), traineeDTO.getEmployeeId());
                System.out.println("DevilRDX 1 "+assessmentMark);

                extractPlanDate = adminService.extractPlanDateForGivenTopic(batchDTO.getCoursePlanDto().getDayWisePlanDTOS(), selectedTopic);
                System.out.println("DevilRDX 2 "+extractPlanDate);

                Map<Double,Double> calculateAttendance=adminService.calculateAttendance(traineeDTO,extractPlanDate);
                System.out.println("DevilRDX 3 "+calculateAttendance);

                if(assignmentMark.keySet().iterator().next()!=0.0)
                {
                    assignmentMarks = (assignmentMark.values().iterator().next() / assignmentMark.keySet().iterator().next()) * 100;
                    System.out.println("DevilRDX 4 " + assignmentMarks);
                }

                if(assessmentMark.keySet().iterator().next()!=0.0)
                {
                    assessmentMarks = (assessmentMark.values().iterator().next() / assessmentMark.keySet().iterator().next()) * 100;
                    System.out.println("DevilRDX 5 " + assessmentMarks);
                }

                if(calculateAttendance.keySet().iterator().next()!=0.0)
                {
                    attendanceMarks = (calculateAttendance.values().iterator().next() / calculateAttendance.keySet().iterator().next()) * 100;
                    System.out.println("DevilRDX 6 " + attendanceMarks);
                }

                totalMarks=(assessmentMarks * 0.75)+(assignmentMarks*0.20)+(attendanceMarks*0.05);

                System.out.println("DevilRDX 7 "+totalMarks);

                traineeDTO.setFinalMarks(totalMarks);
                traineeDTO.setFinalGrade(adminService.calculateGrade(totalMarks));
            }
            adminService.updateTrainee(traineeDTO);
        }
        try
        {
            outputStream=UtilityFunctions.downloadTraineeReport(selectedColumns,trainee);
        }
        catch (IOException e)
        {
            logger.info("Error Creating Report");
        }

        // Set response headers and content
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=trainee_report.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }
}
