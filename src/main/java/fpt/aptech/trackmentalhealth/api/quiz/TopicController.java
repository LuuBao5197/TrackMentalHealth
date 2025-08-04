package fpt.aptech.trackmentalhealth.api.quiz;

import fpt.aptech.trackmentalhealth.entities.Topic;
import fpt.aptech.trackmentalhealth.service.quiz.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/topic")
public class TopicController {
    @Autowired
    private TopicService topicService;
    
    @GetMapping
    public ResponseEntity<List<Topic>> getAllTopics() {
        List<Topic> topics = new ArrayList<>();
        topics = topicService.getTopic();
        return new ResponseEntity<>(topics, HttpStatus.OK);
    }
    @PostMapping("/")
    public ResponseEntity<Topic> createTopic(@RequestBody Topic topic) {
        return new ResponseEntity<>(topicService.saveTopic(topic), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")

    public ResponseEntity<Topic> updateTopic(@PathVariable int id, @RequestBody Topic topic) {
        if (topic.getId() != id) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Topic existTopic = topicService.getTopicById(topic.getId());
        if (existTopic != null) {
            return new ResponseEntity<>(topicService.saveTopic(topic), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTopic(@PathVariable int id) {
        Boolean existTopic = topicService.deleteTopic(id);
        if (existTopic) {
            return new ResponseEntity<>("Deleted topic success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Deleted topic fail", HttpStatus.NOT_FOUND);
        }
    }

}
