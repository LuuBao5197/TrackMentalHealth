package fpt.aptech.trackmentalhealth.repository.chat;

import fpt.aptech.trackmentalhealth.entities.ChatMessage;
import fpt.aptech.trackmentalhealth.entities.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatSessionRepository extends JpaRepository<ChatSession,Integer> {

    @Query("select cs from ChatSession cs where cs.sender.id=:id or cs.receiver.id=:id")
    List<ChatSession> getChatSessionByUserId(int id);

    @Query("select cs from ChatSession cs where cs.id=:id")
    ChatSession getChatSessionById(int id);

    @Query("select cs from ChatSession cs where (cs.receiver.id=:user1 and cs.sender.id=:user2) or (cs.receiver.id=:user2 and cs.sender.id=:user1)")
    ChatSession getChatSessionByTwoUserid(@Param("user1") int user1, @Param("user2") int user2);

    @Query(
            value = "SELECT TOP 1 * FROM chat_messages m WHERE m.session_id = :sessionId ORDER BY m.timestamp DESC",
            nativeQuery = true
    )
    ChatMessage getLatestMessageBySessionId(@Param("sessionId") int sessionId);

}
