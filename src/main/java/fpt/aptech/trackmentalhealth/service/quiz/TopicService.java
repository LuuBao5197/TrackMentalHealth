package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.dto.quiz.TopicDTO;
import fpt.aptech.trackmentalhealth.entities.Topic;

import java.util.List;

public interface TopicService
{
    public List<TopicDTO> getTopic();
    public Topic getTopicById(Integer id);
    public Topic saveTopic(Topic topic);
    public Topic updateTopic(Topic topic);
    public Boolean deleteTopic(int id);
}
