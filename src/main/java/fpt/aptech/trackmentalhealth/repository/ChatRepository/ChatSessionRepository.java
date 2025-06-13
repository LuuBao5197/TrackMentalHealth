package fpt.aptech.trackmentalhealth.repository.ChatRepository;

import fpt.aptech.trackmentalhealth.entities.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatSessionRepository extends JpaRepository<ChatSession,Integer> {
    @Query("select c from ChatSession c")
    List<ChatSession> getChatSessions();

    @Query("select cs from ChatSession cs where cs.psychologistCode.bCode=:PsyId")
    List<ChatSession> getChatSessionByPsyId(int PsyId);

    @Query("select cs from ChatSession cs where cs.id=:id")
    ChatSession getChatSessionById(int id);
}
