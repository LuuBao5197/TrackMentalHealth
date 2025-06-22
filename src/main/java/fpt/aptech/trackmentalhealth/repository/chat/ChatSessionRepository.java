package fpt.aptech.trackmentalhealth.repository.chat;

import fpt.aptech.trackmentalhealth.entities.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatSessionRepository extends JpaRepository<ChatSession,Integer> {
    @Query("select c from ChatSession c")
    List<ChatSession> getChatSessions();

    @Query("select cs from ChatSession cs where cs.sender.id=:id")
    List<ChatSession> getChatSessionByUserId(int id);

    @Query("select cs from ChatSession cs where cs.id=:id")
    ChatSession getChatSessionById(int id);
}
