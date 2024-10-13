package com.parishi.utility;

import com.parishi.dto.*;
import com.parishi.service.AdminService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;

public class ExcelFileReader {

    public static List<TraineeDTO> traineeToListConverter(MultipartFile file) {


        List<TraineeDTO> traineeDTOList = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            // Assuming the first sheet contains trainee details
            for (Row currentRow : sheet) {
                if (currentRow.getRowNum() == 0) {
                    // Skip header row
                    continue;
                }

                TraineeDTO traineeDTO = new TraineeDTO();
                Date input = (currentRow.getCell(0).getDateCellValue());
                traineeDTO.setDoj(input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                traineeDTO.setTraineeName(currentRow.getCell(1).getStringCellValue());
                traineeDTO.setOfficeEmailId(currentRow.getCell(2).getStringCellValue());
                traineeDTO.setCellNo(String.valueOf((int) currentRow.getCell(3).getNumericCellValue()));
                traineeDTO.setGender(currentRow.getCell(4).getStringCellValue());
                traineeDTO.setCollege(currentRow.getCell(5).getStringCellValue());
                traineeDTO.setBranch(currentRow.getCell(6).getStringCellValue());
                traineeDTO.setEmployeeId((int) currentRow.getCell(7).getNumericCellValue());

                //New attributes
                traineeDTO.setTenthMarks(currentRow.getCell(8).getNumericCellValue());
                traineeDTO.setTwelvethMarks(currentRow.getCell(9).getNumericCellValue());
                traineeDTO.setRemarks(currentRow.getCell(10).getStringCellValue());

                traineeDTO.setFinalGrade(null);
                traineeDTO.setFinalMarks(0.0);
                traineeDTO.setStatusFlag(0);
                traineeDTOList.add(traineeDTO);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return traineeDTOList;
    }

    public static List<DayWisePlanDTO> fetchCoursePlanExcel(MultipartFile file, BatchDTO batchDTO) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            boolean hrSet = false;
            boolean holidaySet = false;
            int serialNumber = 1;

            List<DayWisePlanDTO> dayWisePlanDTOS = new ArrayList<>();
            List<HolidayDTO> holidayDTOS = batchDTO.getHolidayDTOS();

            LocalDate initialDate = batchDTO.getCreationDate();
            String month = String.valueOf(initialDate.getMonth());

            // Get the next month
            LocalDate nextMonthDate = initialDate.plusMonths(1).withDayOfMonth(1);

            // Get the month name
            Month nextMonth = nextMonthDate.getMonth();

            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row currentRow = sheet.getRow(rowIndex);
                // Skip the header row (row index 0)
                if (currentRow != null && rowIndex == 0) {
                    continue;
                }

                DayWisePlanDTO dayWisePlanDTO = new DayWisePlanDTO();

//                if the row is empty in an excel sheet then break the loop
                if(currentRow==null)
                     break;
                else
                {
                    //  set the serial number
                    dayWisePlanDTO.setSerialNumber(serialNumber);
                    serialNumber++;

//                set the planned date
                    LocalDate currentDate = initialDate;
                    dayWisePlanDTO.setPlannedDate(currentDate);
                    initialDate = initialDate.plusDays(1);

//                set the day
                    dayWisePlanDTO.setDay(String.valueOf(currentDate.getDayOfWeek()));

                    System.out.println("Day wise plan dto1 = " + dayWisePlanDTO);

//                check the HR Induction month
//                set the topic and subtopic
                    TopicDTO topicDTO = new TopicDTO();

                    if(holidayDTOS!=null && holidaySet != true)
                    {
                        holidaySet = true;
                        // Find the holiday that matches the planned date
                        Optional<HolidayDTO> holidayOptional = findHolidayByDate(holidayDTOS, currentDate);

                        // Output result
                        if (holidayOptional.isPresent())
                        {
                            HolidayDTO holidayDTO = holidayOptional.get();
                            topicDTO.setTopicName(holidayDTO.getHolidayName() + " " + "Holiday");
                            dayWisePlanDTO.setTopics(topicDTO);
                            rowIndex--;
                        }
                    }
                    else if("Saturday".equalsIgnoreCase(dayWisePlanDTO.getDay()) || "Sunday".equalsIgnoreCase(dayWisePlanDTO.getDay()))
                    {
                        dayWisePlanDTO.setTopics(null);
                        rowIndex--;
                    }
                    else
                    {
                        if(hrSet!=true && (String.valueOf(currentDate.getMonth()).equalsIgnoreCase(String.valueOf(nextMonth))))
                        {
                            topicDTO.setTopicName("HR INDUCTION");
                            dayWisePlanDTO.setTopics(topicDTO);
                            hrSet=true;
                            rowIndex--;

                            System.out.println("Day wise plan 2 = " + dayWisePlanDTO);
                        }

                        else
                        {
                            if(currentRow.getCell(0)!=null && !((currentRow.getCell(0)).toString().isEmpty()))
                            {
                                topicDTO.setTopicName(currentRow.getCell(0).getStringCellValue());
                            }
                            List<SubTopicDTO> subtopics = new ArrayList<>();
                            if(currentRow.getCell(1)!=null && !((currentRow.getCell(1)).toString().isEmpty()))
                            {
                                String mergeSubtopics = currentRow.getCell(1).getStringCellValue();
                                List<String> substrings = Arrays.asList(mergeSubtopics.split("~"));

                                // Print each substring
                                for (String substring : substrings) {
                                    SubTopicDTO subtopic = new SubTopicDTO();
                                    subtopic.setSubtopicName(substring.trim());
                                    subtopics.add(subtopic);
                                }
                                topicDTO.setSubTopicsDto(subtopics);
                            }
                            dayWisePlanDTO.setTopics(topicDTO);

//                        set the actual date and remarks

                            dayWisePlanDTO.setActualDate(null);
                            dayWisePlanDTO.setRemarks(null);

                            System.out.println("Day wise plan 3 = " + dayWisePlanDTO);


//                        set the assignment

                            if(currentRow.getCell(2)!=null)
                            {
                                AssignmentPlanDTO assignmentPlanDTO = new AssignmentPlanDTO();

                                assignmentPlanDTO.setDescription(currentRow.getCell(2).getStringCellValue());
                                dayWisePlanDTO.setAssignmentPlanDTO(assignmentPlanDTO);
                            }
                            System.out.println("Day wise plan 4 = " + dayWisePlanDTO);


//                            dayWisePlanDTO.setAssessmentDTO(null);
                        }
                    }
                }
                dayWisePlanDTOS.add(dayWisePlanDTO);
            }
            return dayWisePlanDTOS;

            }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static Optional<HolidayDTO> findHolidayByDate(List<HolidayDTO> holidayList, LocalDate plannedDate) {
        return holidayList.stream()
                .filter(holiday -> holiday.getHolidayDate().equals(plannedDate))
                .findFirst(); // Finds the first match if any
    }

}



