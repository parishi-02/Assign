package com.parishi.utility;


import com.parishi.dto.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.engine.jdbc.batch.spi.Batch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class UtilityFunctions {
    public static ByteArrayOutputStream downloadTraineeContent(List<TraineeDTO> trainees) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("trainees");

        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));


        // Create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Employee Id");
        header.createCell(1).setCellValue("Trainee Name");
        header.createCell(2).setCellValue("Gender");
        header.createCell(3).setCellValue("Branch");
        header.createCell(4).setCellValue("Date of Joining");
        header.createCell(5).setCellValue("College Name");
        header.createCell(6).setCellValue("Email Address");
        header.createCell(7).setCellValue("Contact Number");
        header.createCell(8).setCellValue("Trainee Batch");

        // Fill data
        int rowNum = 1;
        for (TraineeDTO trainee : trainees) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(trainee.getEmployeeId());
            row.createCell(1).setCellValue(trainee.getTraineeName());
            row.createCell(2).setCellValue(trainee.getGender());
            row.createCell(3).setCellValue(trainee.getBranch());

            Cell dateCell = row.createCell(4);
            if (trainee.getDoj() != null) {
                dateCell.setCellValue(trainee.getDoj());
                dateCell.setCellStyle(dateCellStyle);
            }

            row.createCell(5).setCellValue(trainee.getCollege());
            row.createCell(6).setCellValue(trainee.getOfficeEmailId());
            row.createCell(7).setCellValue(Long.parseLong(trainee.getCellNo()));
//            row.createCell(8).setCellValue((RichTextString) trainee.getBatch());
        }

        // Adjust column width
        for (int i = 0; i < 9; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream;

    }

    public static ByteArrayOutputStream downloadTrainerContent(List<TrainerDTO> trainers) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("trainers");

        // Create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Trainer Type");
        header.createCell(1).setCellValue("Trainer Id");
        header.createCell(2).setCellValue("Trainer Name");
        header.createCell(3).setCellValue("Email Address");
        header.createCell(4).setCellValue("contact Number");

        // Fill data
        int rowNum = 1;
        for (TrainerDTO trainer : trainers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(trainer.getTrainerType());
            row.createCell(1).setCellValue(trainer.getTrainerId());
            row.createCell(2).setCellValue(trainer.getTrainerName());
            row.createCell(3).setCellValue(trainer.getTrainerEmail());
            row.createCell(4).setCellValue(Long.parseLong(trainer.getTrainerContactNo()));
        }

        // Adjust column width
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream;

    }

    public static ByteArrayOutputStream downloadCoursePlan(CoursePlanDTO coursePlanDTO) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("coursePlanDTO");

        // Create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("S.No");

        header.createCell(1).setCellValue("Topic");
        header.createCell(2).setCellValue("SubTopic");
        header.createCell(3).setCellValue("Planned Date");
        header.createCell(4).setCellValue("Day");
        header.createCell(5).setCellValue("Actual Date");
        header.createCell(6).setCellValue("Remarks");
        header.createCell(7).setCellValue("Assignment");

        // Fill data
        int rowNum = 1;
        for (DayWisePlanDTO dayWisePlanDTO : coursePlanDTO.getDayWisePlanDTOS()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(dayWisePlanDTO.getPlanId());


            if(dayWisePlanDTO.getTopics()!=null)
            {
                row.createCell(1).setCellValue(dayWisePlanDTO.getTopics().getTopicName());
                List<SubTopicDTO> subTopicDTOS = dayWisePlanDTO.getTopics().getSubTopicsDto();
                String subtopicName = "";

                if(subTopicDTOS!=null)
                {
                    int i = 0;

                    for(i=0;i<subTopicDTOS.size()-1;i++)
                    {
                        subtopicName = subtopicName + subTopicDTOS.get(i).getSubtopicName() + " , ";
                    }
                    if(i==subTopicDTOS.size()-1)
                        subtopicName = subtopicName + subTopicDTOS.get(i).getSubtopicName();

                    row.createCell(2).setCellValue(subtopicName);
                }
                else
                    row.createCell(2).setCellValue(subtopicName);
            }
            else
                row.createCell(1).setCellValue(" ");

            row.createCell(3).setCellValue(String.valueOf(dayWisePlanDTO.getPlannedDate()));
            row.createCell(4).setCellValue(dayWisePlanDTO.getDay());
            row.createCell(5).setCellValue(String.valueOf(dayWisePlanDTO.getActualDate()));
            row.createCell(6).setCellValue(dayWisePlanDTO.getRemarks());

            if(dayWisePlanDTO.getAssignmentPlanDTO()!=null)
            {
                row.createCell(7).setCellValue(dayWisePlanDTO.getAssignmentPlanDTO().getDescription());
            }

            else
                row.createCell(7).setCellValue(" ");

        }

        // Adjust column width
        for (int i = 0; i < 8; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream;
    }


    public static ByteArrayOutputStream downloadMentoringSession(List<MentoringSessionDto> mentoringSessionDtos) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("mentoringSessionDtos");

        // Create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Session Description");
        header.createCell(1).setCellValue("Session Time");
        header.createCell(2).setCellValue("Planned Date");
        header.createCell(3).setCellValue("Actual Date");
        header.createCell(4).setCellValue("Remarks");

        // Fill data
        int rowNum = 1;
        for (MentoringSessionDto mentoringSessionDto : mentoringSessionDtos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(mentoringSessionDto.getSessionDescription());
            row.createCell(1).setCellValue(String.valueOf(mentoringSessionDto.getPlannedTime()));
            row.createCell(2).setCellValue(String.valueOf(mentoringSessionDto.getPlannedDate()));
            row.createCell(3).setCellValue(String.valueOf(mentoringSessionDto.getActualDate()));
            row.createCell(4).setCellValue(mentoringSessionDto.getRemarks());
        }

        // Adjust column width
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream;

    }

    public static ByteArrayOutputStream downloadAssessments(BatchDTO batchDTO) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Assessment List");

        List<TraineeDTO> traineeDTOList = batchDTO.getTraineeList();
        Collections.sort(traineeDTOList, new Comparator<TraineeDTO>() {
            @Override
            public int compare(TraineeDTO o1, TraineeDTO o2) {
                return o1.getEmployeeId().compareTo(o2.getEmployeeId());
            }
        });

        LocalDate currenDate = LocalDate.now();

        // Create header row
        Row header = sheet.createRow(0);
        int i = 0;
        header.createCell(i++).setCellValue("Trainee Id");
        header.createCell(i++).setCellValue("Trainee Name");

        System.out.println("Assessment dto down = " + batchDTO.getAssessmentDTOS());

        if(batchDTO.getAssessmentDTOS()!=null)
        {
            for(AssessmentDTO assessmentDTO: batchDTO.getAssessmentDTOS())
            {
                if(assessmentDTO==null)
                    continue;
                if((assessmentDTO.getPlannedDate().compareTo(currenDate) )<= 0)
                    header.createCell(i++).setCellValue(assessmentDTO.getSubTopic());
            }

            System.out.println("i down= " + i);

            int rowNum = 1;

            for(TraineeDTO traineeDTO : traineeDTOList)
            {
                Integer traineeId = traineeDTO.getEmployeeId();
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(String.valueOf(traineeDTO.getEmployeeId()));
                row.createCell(1).setCellValue(traineeDTO.getTraineeName());
                int j = i;
                int k=2;
                for (AssessmentDTO assessmentDTO : batchDTO.getAssessmentDTOS())
                {
                    if(assessmentDTO==null)
                        continue;
                    if((assessmentDTO.getPlannedDate().compareTo(currenDate) )<= 0)
                    {
                        if(k<j)
                        {
                            if (assessmentDTO.getMarksObtained().get(traineeId)!= null)
                                row.createCell(k).setCellValue(assessmentDTO.getMarksObtained().get(traineeId));
                            else
                                row.createCell(k).setCellValue(String.valueOf(0));
                            k++;
                        }
                    }
                    else
                        break;
                }
            }
        }


        // Adjust column width
        for (int h = 0; h < i; h++) {
            sheet.autoSizeColumn(h);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream;

    }

    public static ByteArrayOutputStream downloadAssignmentList(BatchDTO batchDTO) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Assignment List");

        List<TraineeDTO> traineeDTOList = batchDTO.getTraineeList();
        Collections.sort(traineeDTOList, new Comparator<TraineeDTO>() {
            @Override
            public int compare(TraineeDTO o1, TraineeDTO o2) {
                return o1.getEmployeeId().compareTo(o2.getEmployeeId());
            }
        });
        List<DayWisePlanDTO> dayWisePlanDTOS = batchDTO.getCoursePlanDto().getDayWisePlanDTOS();

//        sort the day wise plan list

        Collections.sort(dayWisePlanDTOS, new Comparator<DayWisePlanDTO>() {
            @Override
            public int compare(DayWisePlanDTO o1, DayWisePlanDTO o2) {
                return o1.getPlanId().compareTo(o2.getPlanId());
            }
        });
        LocalDate currenDate = LocalDate.now();

        // Create header row
        Row header = sheet.createRow(0);
        int i = 0;
        header.createCell(i++).setCellValue("Trainee Id");
        header.createCell(i++).setCellValue("Trainee Name");

        for(DayWisePlanDTO dayWisePlanDTO:dayWisePlanDTOS)
        {
            if(dayWisePlanDTO.getAssignmentPlanDTO()==null)
                continue;
            if((dayWisePlanDTO.getPlannedDate().compareTo(currenDate) )<= 0)
                header.createCell(i++).setCellValue(dayWisePlanDTO.getAssignmentPlanDTO().getDescription());
        }

        System.out.println("i down= " + i);

        int rowNum = 1;

        for(TraineeDTO traineeDTO : traineeDTOList)
        {
            Integer traineeId = traineeDTO.getEmployeeId();
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(String.valueOf(traineeDTO.getEmployeeId()));
            row.createCell(1).setCellValue(traineeDTO.getTraineeName());
            int j = i;
            int k=2;
            for (DayWisePlanDTO dayWisePlan : dayWisePlanDTOS)
            {
                if(dayWisePlan.getAssignmentPlanDTO()==null)
                    continue;
                if((dayWisePlan.getPlannedDate().compareTo(currenDate) )<= 0)
                {
                    AssignmentPlanDTO assignment = dayWisePlan.getAssignmentPlanDTO();
                    if(k<j)
                    {
                        if (assignment.getMarksObtained().get(traineeId)!= null)
                            row.createCell(k).setCellValue(assignment.getMarksObtained().get(traineeId));
                        else
                            row.createCell(k).setCellValue(String.valueOf(0));
                        k++;
                    }
                }
                else
                    break;
            }
        }

        // Adjust column width
        for (int h = 0; h < i; h++) {
            sheet.autoSizeColumn(h);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream;

    }

    public static ByteArrayOutputStream downloadTraineesAttendance(BatchDTO batchDTO,LocalDate fromDate, LocalDate toDate) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Attendance List");
        int rowNum = 1;

        LocalDate currentDate = fromDate;
        LocalDate finalDate = toDate;

        List<TraineeDTO> traineeDTOList = batchDTO.getTraineeList();
        List<AttendanceDTO> attendanceDTOS = batchDTO.getAttendances();

        System.out.println("attendance  = " + attendanceDTOS);

        // Create header row
        Row header = sheet.createRow(0);
        int i = 0;
        header.createCell(i++).setCellValue("Trainee Id");
        header.createCell(i++).setCellValue("Trainee Name");

        while (currentDate.compareTo(finalDate) <= 0) {
            System.out.println(" i = " + i);
            header.createCell(i++).setCellValue(String.valueOf(currentDate));

            currentDate = currentDate.plusDays(1);
            System.out.println("intiial adte = " + currentDate);
        }

        System.out.println("i down= " + i);




        for (TraineeDTO traineeDTO : traineeDTOList) {
            LocalDate intialDate = fromDate;

            System.out.println("tarinee id = " + traineeDTO.getEmployeeId());
            System.out.println("trainee name = " + traineeDTO.getTraineeName());

            Map<LocalDate, String> attendanceStatus = traineeDTO.getAttendance();
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(String.valueOf(traineeDTO.getEmployeeId()));
            row.createCell(1).setCellValue(traineeDTO.getTraineeName());


            System.out.println("Attendance status = " + attendanceStatus);

            Map<LocalDate, String> dateRangeAttendance = new HashMap<>();
            int j = i;
            int k = 2;

            System.out.println("initial date before while loop : - " + intialDate);
            // Loop through the date range
            while (intialDate.compareTo(finalDate) <= 0) {
                System.out.println("initial adte = " + intialDate);
                if (k < j) {
                    // Fetch the status for the current date
                    String status = attendanceStatus.getOrDefault(intialDate, "No Record");

                    if(intialDate.getDayOfWeek()== DayOfWeek.SATURDAY)
                        status = "SATURDAY";
                    else if(intialDate.getDayOfWeek() == DayOfWeek.SUNDAY)
                        status = "SUNDAY";

                    System.out.println("status = " + status);
                    status = status.toUpperCase();
                    row.createCell(k).setCellValue(status);
                    // Store the status in the dateRangeAttendance map
                    dateRangeAttendance.put(intialDate, status);

                    // Increment the date by one day
                    intialDate = intialDate.plusDays(1);
                    k++;
                }
            }
            System.out.println("new map = " + dateRangeAttendance);
        }

            // Adjust column width
            for (int h = 0; h < i; h++) {
                sheet.autoSizeColumn(h);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            return outputStream;
        }


    public  static ByteArrayOutputStream downloadTraineeReport(List<String> selectedColumns,List<TraineeDTO> trainees) throws IOException
    {
        // Create Excel workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Trainee/s Report");

        // Create header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < selectedColumns.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(selectedColumns.get(i));
        }

        // Fill data rows
        int rowNum = 1;
        for (TraineeDTO trainee : trainees)
        {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < selectedColumns.size(); i++)
            {
                String column = selectedColumns.get(i);
                Cell cell = row.createCell(i);
                switch (column)
                {
                    case "TraineeId":
                        cell.setCellValue(trainee.getEmployeeId());
                        break;
                    case "TraineeName":
                        cell.setCellValue(trainee.getTraineeName());
                        break;
                    case "Date Of Joining":
                        cell.setCellValue(trainee.getDoj().toString());
                        break;
                    case "OfficeEmailId":
                        cell.setCellValue(trainee.getOfficeEmailId());
                        break;
                    case "MobileNo":
                        cell.setCellValue(trainee.getCellNo());
                        break;
                    case "Gender":
                        cell.setCellValue(trainee.getGender());
                        break;
                    case "College":
                        cell.setCellValue(trainee.getCollege());
                        break;
                    case "Branch":
                        cell.setCellValue(trainee.getBranch());
                        break;
                    case "FinalGrade":
                        cell.setCellValue(trainee.getFinalGrade());
                        break;
                    case "FinalMarks":
                        cell.setCellValue(trainee.getFinalMarks());
                        break;
                    case "TenthMarks":
                        cell.setCellValue(trainee.getTenthMarks());
                        break;
                    case "TwelvethMarks":
                        cell.setCellValue(trainee.getTwelvethMarks());
                        break;
                    case "Remarks":
                        cell.setCellValue(trainee.getRemarks());
                        break;
                    default:
                        System.out.println("Unexpected value: " + column);
                }
            }
        }

        // Write the output to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream;
    }
}
