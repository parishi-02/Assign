package com.parishi.entity;


import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "track_topic")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "topic_Id")
    private Integer topicId;

    @Column(name = "topic_name")
    private String topicName;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<SubTopic> subTopics;

    public Topic()
    {
    }

    public Topic(Integer topicId, String topicName, List<SubTopic> subTopics) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.subTopics = subTopics;
    }

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

    public List<SubTopic> getSubTopics() {
        return subTopics;
    }

    public void setSubTopics(List<SubTopic> subTopics) {
        this.subTopics = subTopics;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "topicId='" + topicId + '\'' +
                ", topicName='" + topicName + '\'' +
                ", subTopics=" + subTopics +
                '}';
    }
}
