package com.parishi.service;

import com.parishi.dao.BatchDao;
import com.parishi.dto.BatchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeServiceImpl implements TraineeService{

    @Autowired
    private BatchDao batchDao;


    @Override
    public BatchDTO getBatchByTraineeId(Integer traineeId) {
        return batchDao.getBatchByTraineeId(traineeId);
    }
}
