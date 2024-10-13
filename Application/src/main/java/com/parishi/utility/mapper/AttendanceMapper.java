package com.parishi.utility.mapper;

import com.parishi.dto.AttendanceDTO;
import com.parishi.dto.TraineeDTO;
import com.parishi.entity.Attendance;
import com.parishi.entity.Trainee;

import java.util.ArrayList;
import java.util.List;


public class AttendanceMapper {
        //Method to convert Entity to DTO
        public static AttendanceDTO fromEntityToDTO(Attendance attendance) {
            AttendanceDTO attendanceDTO = new AttendanceDTO();

            attendanceDTO.setAttendanceId(attendance.getAttendanceId());
            attendanceDTO.setAttendanceDate(attendance.getAttendanceDate());
            attendanceDTO.setAttendanceDay(attendance.getAttendanceDay());

            List<Trainee> traineeList = attendance.getTraineeList();
            if(traineeList!=null)
            {
                List<TraineeDTO> traineeDTOList = new ArrayList<>();
                for(Trainee trainee : traineeList)
                {
                    TraineeDTO traineeDTO = TraineeMapper.fromEntityToDTO(trainee);
                    traineeDTOList.add(traineeDTO);
                }
                attendanceDTO.setTraineeList(traineeDTOList);
            }
//            attendanceDTO.setAttendanceList(attendance.getAttendanceList());

            return attendanceDTO;
        }

        //Method to convert DTO to Entity
        public static Attendance fromDTOToEntity(AttendanceDTO attendanceDTO) {
            Attendance attendance = new Attendance();

            attendance.setAttendanceId(attendanceDTO.getAttendanceId());
            attendance.setAttendanceDate(attendanceDTO.getAttendanceDate());
            attendance.setAttendanceDay(attendanceDTO.getAttendanceDay());

            List<TraineeDTO> traineeDTOList = attendanceDTO.getTraineeList();
            if(traineeDTOList!=null)
            {
                List<Trainee> traineeList = new ArrayList<>();
                for(TraineeDTO traineeDTO : traineeDTOList)
                {
                    Trainee trainee = TraineeMapper.fromDTOToEntity(traineeDTO);
                    traineeList.add(trainee);
                }
                attendance.setTraineeList(traineeList);
            }

//            attendance.setAttendanceList(attendanceDTO.getAttendanceList());

            return attendance;
        }

    }


