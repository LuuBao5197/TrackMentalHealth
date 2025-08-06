package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.dto.quiz.TopicDTO;
import fpt.aptech.trackmentalhealth.entities.Topic;
import fpt.aptech.trackmentalhealth.repository.quiz.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicRepository topicRepository;
    @Override
    public List<TopicDTO> getTopic() {
        List<Topic> allTopics = topicRepository.findAll();
        List<TopicDTO> topicDTOs = new ArrayList<>();
        for (Topic topic : allTopics) {
            TopicDTO topicDTO = new TopicDTO();
            topicDTO.setId(topic.getId());
            topicDTO.setName(topic.getName());
            topicDTOs.add(topicDTO);
        }
        return topicDTOs;
    }

    @Override
    public Topic getTopicById(Integer id) {
        return topicRepository.findById(id).orElse(null);
    }

    @Override
    public Topic saveTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    @Override
    public Topic updateTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    @Override
    public Boolean deleteTopic(int id) {
        Topic topic = topicRepository.findById(id).orElse(null);
        if (topic != null) {
            topic.setDeleted(true);
            topic.setDeletedAt(LocalDateTime.now());
            topicRepository.save(topic);
            return true;
        }
        return false;

    }
}
