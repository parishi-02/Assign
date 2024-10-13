package com.parishi.utility.mapper;

import com.parishi.dto.BatchDTO;
import com.parishi.dto.SubTopicDTO;
import com.parishi.dto.TopicDTO;
import com.parishi.dto.TraineeDTO;
import com.parishi.entity.Batch;
import com.parishi.entity.SubTopic;
import com.parishi.entity.Topic;
import com.parishi.entity.Trainee;

import java.util.ArrayList;
import java.util.List;

public class TopicMapper {

    public static TopicDTO fromEntityToDTO(Topic topic) {
        TopicDTO topicDTO = new TopicDTO();

        topicDTO.setTopicId(topic.getTopicId());
        topicDTO.setTopicName(topic.getTopicName());

        List<SubTopic> subTopics = topic.getSubTopics();
        List<SubTopicDTO> subTopicDTOS = new ArrayList<>();

        if(subTopics!=null)
        {
            for(SubTopic subTopic : subTopics)
            {
                SubTopicDTO subTopicDTO = SubTopicMapper.fromEntityToDTO(subTopic);
                subTopicDTOS.add(subTopicDTO);
            }
            topicDTO.setSubTopicsDto(subTopicDTOS);
        }

        return topicDTO;
    }



    //Method to convert DTO to Entity
    public static Topic fromDTOToEntity(TopicDTO topicDTO) {

        Topic topic = new Topic();
        topic.setTopicId(topicDTO.getTopicId());

        topic.setTopicName(topicDTO.getTopicName());
        List<SubTopicDTO> subTopicDTOS = topicDTO.getSubTopicsDto();
        List<SubTopic> subTopics = new ArrayList<>();

        if(subTopicDTOS!=null)
        {
            for(SubTopicDTO subTopicDTO : subTopicDTOS){
                SubTopic subTopic = SubTopicMapper.fromDTOToEntity(subTopicDTO);
                subTopics.add(subTopic);
            }
            topic.setSubTopics(subTopics);
        }
        return topic;

    }
}
