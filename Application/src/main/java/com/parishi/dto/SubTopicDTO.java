package com.parishi.dto;

public class SubTopicDTO {

    private Integer subtopicId;
    private String subtopicName;


    public Integer getSubtopicId() {
        return subtopicId;
    }

    public void setSubtopicId(Integer subtopicId) {
        this.subtopicId = subtopicId;
    }

    public String getSubtopicName() {
        return subtopicName;
    }

    public void setSubtopicName(String subtopicName) {
        this.subtopicName = subtopicName;
    }

    @Override
    public String toString() {
        return "SubTopicDTO{" +
                "subtopicId='" + subtopicId + '\'' +
                ", subtopicName='" + subtopicName + '\'' +
                '}';
    }
}
