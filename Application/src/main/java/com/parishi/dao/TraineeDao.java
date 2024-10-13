package com.parishi.dao;

import com.parishi.dto.TraineeDTO;
import com.parishi.dto.TrainerDTO;

import java.util.List;

public interface TraineeDao
{
    List<TraineeDTO> getAllTrainees();
    boolean registerTrainee(TraineeDTO traineeDTO);
    boolean insertBulkTrainee(List<TraineeDTO> traineeDTOList);
    boolean updateTrainee(TraineeDTO traineeDTO);
    public TraineeDTO getTrainee(int empId);
    List<TraineeDTO> getTraineeListByBatchId(String batchId);

}
