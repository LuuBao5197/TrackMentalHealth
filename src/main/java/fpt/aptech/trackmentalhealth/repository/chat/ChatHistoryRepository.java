package fpt.aptech.trackmentalhealth.repository.chat;

import fpt.aptech.trackmentalhealth.entities.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Integer> {
    List<ChatHistory> findByUserIdOrderByTimestampAsc(int userId);

}
