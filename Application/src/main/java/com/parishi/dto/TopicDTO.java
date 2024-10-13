package com.parishi.dto;

import com.parishi.entity.SubTopic;
import com.parishi.entity.Trainer;

import java.util.List;

public class TopicDTO {

    private Integer topicId;

    private String topicName;

    private List<SubTopicDTO> subTopicsDto;


    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public List<SubTopicDTO> getSubTopicsDto() {
        return subTopicsDto;
    }

    public void setSubTopicsDto(List<SubTopicDTO> subTopicsDto) {
        this.subTopicsDto = subTopicsDto;
    }

    @Override
    public String toString() {
        return "TopicDTO{" +
                "topicId='" + topicId + '\'' +
                ", topicName='" + topicName + '\'' +
                ", subTopicsDto=" + subTopicsDto +
                '}';
    }
}
