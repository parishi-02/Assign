package com.parishi.utility.mapper;

import com.parishi.dto.BatchDTO;
import com.parishi.dto.SubTopicDTO;
import com.parishi.dto.TraineeDTO;
import com.parishi.entity.Batch;
import com.parishi.entity.SubTopic;
import com.parishi.entity.Trainee;

import java.util.ArrayList;
import java.util.List;

public class SubTopicMapper {

    public static SubTopicDTO fromEntityToDTO(SubTopic subTopic) {
       SubTopicDTO subTopicDTO = new SubTopicDTO();

       subTopicDTO.setSubtopicId(subTopic.getSubtopicId());
       subTopicDTO.setSubtopicName(subTopic.getSubtopicName());

       return subTopicDTO;
    }



    //Method to convert DTO to Entity
    public static SubTopic fromDTOToEntity(SubTopicDTO subTopicDTO) {
        SubTopic subTopic = new SubTopic();
        subTopic.setSubtopicId(subTopicDTO.getSubtopicId());
        subTopic.setSubtopicName(subTopicDTO.getSubtopicName());

        return subTopic;
    }
}
