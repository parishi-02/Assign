package com.parishi.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "track_subtopic")
public class SubTopic {
    @Id
    @Column(name = "subtopic_Id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer subtopicId;
    @Column(name = "subtopic_name")
    private String subtopicName;

    public SubTopic()
    {

    }
    public SubTopic(Integer subtopicId, String subtopicName) {
        this.subtopicId = subtopicId;
        this.subtopicName = subtopicName;
    }

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
        return "SubTopic{" +
                "subtopicId='" + subtopicId + '\'' +
                ", subtopicName='" + subtopicName + '\'' +
                '}';
    }
}
