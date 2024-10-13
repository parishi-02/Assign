package com.parishi.dao;

import com.parishi.dto.BatchDTO;

import java.time.LocalDate;
import java.util.List;

public interface BatchDao
{
    boolean createBatch(BatchDTO batchDTO);
    public List<BatchDTO> getAllBatches();
    BatchDTO getBatchById(String batchId);
    boolean updateBatch(BatchDTO batchDTO);
    List<BatchDTO> getBatchByTrainerId(Integer trainerId);
    boolean updateCoursePlanDates(String batchId, String topicName, LocalDate newDate);
    public boolean updateTopicPlannedDate(String batchId, Integer planId, LocalDate newDate,String remarks);
    boolean addAssessment(BatchDTO batchDTO);

    BatchDTO getBatchByTraineeId(Integer traineeId);
}
