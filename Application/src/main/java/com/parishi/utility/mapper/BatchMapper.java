package com.parishi.utility.mapper;

import com.parishi.dto.*;
import com.parishi.entity.*;

import java.util.ArrayList;
import java.util.List;

public class BatchMapper {


    public static BatchDTO fromEntityToDTO(Batch batch) {
        BatchDTO batchDTO = new BatchDTO();


        //Method to convert Entity to DTO

        batchDTO.setBatchId(batch.getBatchId());
        batchDTO.setCreationDate(batch.getCreationDate());


        List<Trainee> traineeList = batch.getTraineeList();
        List<TraineeDTO> traineeDTOList = new ArrayList<>();

        if(traineeList!=null)
        {
            for(Trainee trainee : traineeList)
            {
                TraineeDTO traineeDTO = TraineeMapper.fromEntityToDTO(trainee);
                traineeDTOList.add(traineeDTO);
            }
            batchDTO.setTraineeList(traineeDTOList);
        }

        List<MentoringSession> mentoringSessions = batch.getMentoringSessions();
        List<MentoringSessionDto> mentoringSessionDtos = new ArrayList<>();
        if(mentoringSessions!=null)
        {
            for(MentoringSession mentoringSession : mentoringSessions) {
                MentoringSessionDto mentoringSessionDto = MentoringSessionMapper.fromEntityToDTO(mentoringSession);
                mentoringSessionDtos.add(mentoringSessionDto);
            }
            batchDTO.setMentoringSessionDtos(mentoringSessionDtos);
        }

        if(batch.getCoursePlan()!=null)
            batchDTO.setCoursePlanDto(CoursePlanMapper.fromEntityToDTO(batch.getCoursePlan()));


        if(batch.getTrainer()!=null)
            batchDTO.setTrainerDto(TrainerMapper.fromEntityToDTO(batch.getTrainer()));

        List<Holiday>holidays = batch.getHolidays();
        List<HolidayDTO> holidayDTOS = new ArrayList<>();

        if(holidays!=null && !holidays.isEmpty())
        {
            for(Holiday holiday : holidays)
            {
                HolidayDTO holidayDTO = HolidayMapper.fromEntityToDTO(holiday);
                holidayDTOS.add(holidayDTO);
            }
            batchDTO.setHolidayDTOS(holidayDTOS);
        }


        List<Assessment>assessments = batch.getAssessments();
        List<AssessmentDTO> assessmentDTOS = new ArrayList<>();

        if(assessments!=null && !assessments.isEmpty())
        {
            for(Assessment assessment : assessments)
            {
                AssessmentDTO assessmentDTO = AssessmentMapper.fromEntityToDTO(assessment);
               assessmentDTOS.add(assessmentDTO);
            }
            batchDTO.setAssessmentDTOS(assessmentDTOS);
        }

        List<Attendance> attendances = batch.getAttendances();
        List<AttendanceDTO> attendanceDTOS = new ArrayList<>();
        if(attendances!=null && !attendances.isEmpty())
        {
            for(Attendance attendance : attendances)
            {
                AttendanceDTO attendanceDTO = AttendanceMapper.fromEntityToDTO(attendance);
                attendanceDTOS.add(attendanceDTO);
            }
            batchDTO.setAttendances(attendanceDTOS);
        }

        return batchDTO;

    }



    //Method to convert DTO to Entity
    public static Batch fromDTOToEntity(BatchDTO batchDTO) {

        Batch batch = new Batch();
        batch.setBatchId(batchDTO.getBatchId());
        batch.setTrainer(TrainerMapper.fromDTOToEntity(batchDTO.getTrainerDto()));
        batch.setCreationDate(batchDTO.getCreationDate());


        List<TraineeDTO> traineeDTOList = batchDTO.getTraineeList();
        List<Trainee> traineeList = new ArrayList<>();
        if(traineeDTOList!=null)
        {
            for(TraineeDTO traineeDTO : traineeDTOList)
            {
                Trainee trainee = TraineeMapper.fromDTOToEntity(traineeDTO);
                traineeList.add(trainee);
            }
            batch.setTraineeList(traineeList);
        }

        List<MentoringSessionDto> mentoringSessionDtos = batchDTO.getMentoringSessionDtos();
        List<MentoringSession> mentoringSessions = new ArrayList<>();

        if(mentoringSessionDtos!=null)
        {
            for(MentoringSessionDto mentoringSessionDto : mentoringSessionDtos)
            {
                MentoringSession mentoringSession = MentoringSessionMapper.fromDTOToEntity(mentoringSessionDto);
                mentoringSessions.add(mentoringSession);
            }
            batch.setMentoringSessions(mentoringSessions);
        }

        if(batchDTO.getCoursePlanDto()!=null)
            batch.setCoursePlan(CoursePlanMapper.fromDTOToEntity(batchDTO.getCoursePlanDto()));


        List<Holiday>holidays = new ArrayList<>();
        List<HolidayDTO> holidayDTOS = batchDTO.getHolidayDTOS();

        if(holidayDTOS!=null && !holidayDTOS.isEmpty())
        {
            for(HolidayDTO holidayDTO : holidayDTOS)
            {
                Holiday holiday = HolidayMapper.fromDTOToEntity(holidayDTO);
                holidays.add(holiday);
            }
            batch.setHolidays(holidays);
        }

        List<Assessment>assessments = new ArrayList<>();
        List<AssessmentDTO> assessmentDTOS = batchDTO.getAssessmentDTOS();

        if(assessmentDTOS!=null && !assessmentDTOS.isEmpty())
        {
            for(AssessmentDTO assessmentDTO : assessmentDTOS)
            {
                Assessment assessment = AssessmentMapper.fromDTOToEntity(assessmentDTO);
                assessments.add(assessment);
            }
            batch.setAssessments(assessments);
        }

        List<AttendanceDTO> attendanceDTOS = batchDTO.getAttendances();
        List<Attendance> attendances = new ArrayList<>();
        if(attendanceDTOS!=null && !attendanceDTOS.isEmpty())
        {
            for(AttendanceDTO attendanceDTO : attendanceDTOS)
            {
                Attendance attendance = AttendanceMapper.fromDTOToEntity(attendanceDTO);
                attendances.add(attendance);
            }
            batch.setAttendances(attendances);
        }


        return batch;

    }
}
