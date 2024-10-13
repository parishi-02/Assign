package com.parishi.controller;


import com.parishi.dto.AssessmentDTO;
import com.parishi.dto.BatchDTO;
import com.parishi.dto.TraineeDTO;
import com.parishi.entity.User;
import com.parishi.service.AdminService;
import com.parishi.service.TraineeService;
import com.parishi.service.TrainerService;
import com.parishi.service.UserService;
import com.parishi.utility.LocalDateEditor;
import com.parishi.utility.LocalTimeEditor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
@PreAuthorize("hasRole('TRAINEE')")
@RequestMapping("trainee")
public class TraineeController {

    @Autowired
    private Logger logger;

    @Autowired
    private AdminService adminService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    UserService userService;

    @Autowired
    TraineeService traineeService;

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
    public String traineeDashboard()
    {
        return "traineeDashboard"; // This is the JSP page for trainee
    }

    @GetMapping("showAllBatches")
    public String showAllBatches(Model model)
    {
        // Get the list of all batches
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(user.getUsername()!=null)
        {
            String username = user.getUsername();
            System.out.println("Username = " + username);
            String id = username.replaceAll("\\D+", "");
            BatchDTO batchDTO = traineeService.getBatchByTraineeId(Integer.parseInt(id));
            TraineeDTO traineeDTO = adminService.getTrainee(Integer.parseInt(id));
            Map<LocalDate, String> upperCaseMap = new LinkedHashMap<>();

            if(batchDTO != null) {

                if (batchDTO.getAssessmentDTOS() != null) {
                    Collections.sort(batchDTO.getAssessmentDTOS(), new Comparator<AssessmentDTO>() {
                        @Override
                        public int compare(AssessmentDTO a1, AssessmentDTO a2) {
                            if (a1.getPlannedDate() == null || a2.getPlannedDate() == null) {
                                return 0; // Handle null values as needed
                            }
                            return a1.getPlannedDate().compareTo(a2.getPlannedDate());
                        }
                    });
                }
                // Step 1: Sort the map by LocalDate
                Map<LocalDate, String> sortedMap = null;

                if (traineeDTO.getAttendance() != null) {
                    sortedMap = new TreeMap<>(traineeDTO.getAttendance());

                    for (Map.Entry<LocalDate, String> entry : sortedMap.entrySet()) {
                        upperCaseMap.put(entry.getKey(), entry.getValue().toUpperCase());
                    }
                }
            }

            // Step 2: Convert the values to uppercase



            // Add the sorted list to the model
            model.addAttribute("batch", batchDTO);
            model.addAttribute("traineeAttendance",upperCaseMap);
            model.addAttribute("traineeId",id);
            model.addAttribute("currentDate", LocalDate.of(2024, 10, 10));  // current date krni hai yha prr
            // Return the view name
            return "trainee-dashboard";
        }

        model.addAttribute("errorMessage","User Not Found/Session Timed Out.");
        return "error";
    }
}
