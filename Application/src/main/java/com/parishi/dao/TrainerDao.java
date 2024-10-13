package com.parishi.dao;

import com.parishi.dto.*;
import com.parishi.entity.Assessment;

import java.time.LocalDate;
import java.util.List;

public interface TrainerDao
{
    List<TrainerDTO> getAllTrainers();
    boolean registerTrainer(TrainerDTO trainerDto);
    boolean updateTrainer(TrainerDTO trainerDTO);
    TrainerDTO getTrainer(int trainerId);
    List<TrainerDTO> getAllTrainerWithStatusFlagZero();



}
