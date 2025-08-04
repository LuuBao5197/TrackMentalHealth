package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.entities.Topic;

import java.util.List;

public interface TopicService
{
    public List<Topic> getTopic();
    public Topic getTopicById(int id);
    public Topic saveTopic(Topic topic);
    public Topic updateTopic(Topic topic);
    public Boolean deleteTopic(int id);
}
