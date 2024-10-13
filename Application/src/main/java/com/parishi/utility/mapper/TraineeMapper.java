package com.parishi.utility.mapper;

import com.parishi.dto.TraineeDTO;

import com.parishi.entity.Trainee;

public class TraineeMapper
{

    //Method to convert Entity to DTO
    public static TraineeDTO fromEntityToDTO(Trainee trainee) {
        TraineeDTO traineeDTO = new TraineeDTO();


        traineeDTO.setEmployeeId(trainee.getEmployeeId());
        traineeDTO.setDoj(trainee.getDoj());
        traineeDTO.setTraineeName(trainee.getTraineeName());
        traineeDTO.setOfficeEmailId(trainee.getOfficeEmailId());
        traineeDTO.setCellNo(trainee.getCellNo());
        traineeDTO.setGender(trainee.getGender());
        traineeDTO.setCollege(trainee.getCollege());
        traineeDTO.setBranch(trainee.getBranch());
        traineeDTO.setStatusFlag(trainee.getStatusFlag());

        traineeDTO.setFinalGrade(trainee.getFinalGrade());
        traineeDTO.setFinalMarks(trainee.getFinalMarks());
        traineeDTO.setRemarks(trainee.getRemarks());
        traineeDTO.setTwelvethMarks(trainee.getTwelvethMarks());
        traineeDTO.setTenthMarks(traineeDTO.getTenthMarks());

//        if(trainee.getAttendance()!=null) {
//            traineeDTO.setAttendance(AttendanceMapper.fromEntityToDTO(trainee.getAttendance()));
//        }

        traineeDTO.setAttendance(trainee.getAttendance());

        return traineeDTO;
    }


    //Method to convert DTO to Entity
    public static Trainee fromDTOToEntity(TraineeDTO traineeDTO) {
        Trainee trainee = new Trainee();

        trainee.setEmployeeId(traineeDTO.getEmployeeId());
        trainee.setDoj(traineeDTO.getDoj());
        trainee.setTraineeName(traineeDTO.getTraineeName());
        trainee.setOfficeEmailId(traineeDTO.getOfficeEmailId());
        trainee.setCellNo(traineeDTO.getCellNo());
        trainee.setGender(traineeDTO.getGender());
        trainee.setCollege(traineeDTO.getCollege());
        trainee.setBranch(traineeDTO.getBranch());

        trainee.setFinalGrade(traineeDTO.getFinalGrade());
        trainee.setFinalMarks(traineeDTO.getFinalMarks());
        trainee.setTenthMarks(traineeDTO.getTenthMarks());
        trainee.setTwelvethMarks(traineeDTO.getTwelvethMarks());
        trainee.setRemarks(traineeDTO.getRemarks());

       trainee.setAttendance(traineeDTO.getAttendance());
        if(traineeDTO.getStatusFlag()!=null)
            trainee.setStatusFlag(traineeDTO.getStatusFlag());

        return trainee;
    }

}
