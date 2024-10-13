package com.parishi.utility.mapper;

import com.parishi.dto.TraineeDTO;
import com.parishi.dto.TrainerDTO;
import com.parishi.entity.Trainee;
import com.parishi.entity.Trainer;

public class TrainerMapper {


    //Method to convert Entity to DTO
    public static TrainerDTO fromEntityToDTO(Trainer trainer) {

        TrainerDTO trainerDTO1 = new TrainerDTO();
        trainerDTO1.setTrainerId(trainer.getTrainerId());
        trainerDTO1.setTrainerName(trainer.getTrainerName());
        trainerDTO1.setTrainerEmail(trainer.getTrainerEmail());
        trainerDTO1.setTrainerType(trainer.getTrainerType());
        trainerDTO1.setTrainerContactNo(trainer.getTrainerContactNo());
        trainerDTO1.setStatusFlag(trainer.getStatusFlag());

        return trainerDTO1;

    }



    //Method to convert DTO to Entity
    public static Trainer fromDTOToEntity(TrainerDTO trainerDTO) {
        Trainer trainer = new Trainer();

        trainer.setTrainerId(trainerDTO.getTrainerId());
        trainer.setTrainerName(trainerDTO.getTrainerName());
        trainer.setTrainerEmail(trainerDTO.getTrainerEmail());
        trainer.setTrainerType(trainerDTO.getTrainerType());
        trainer.setTrainerContactNo(trainerDTO.getTrainerContactNo());
        trainer.setStatusFlag(trainerDTO.getStatusFlag());

        return trainer;
    }

    }
