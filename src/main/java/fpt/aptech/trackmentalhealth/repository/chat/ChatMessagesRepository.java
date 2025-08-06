package fpt.aptech.trackmentalhealth.repository.chat;

import fpt.aptech.trackmentalhealth.entities.ChatMessage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessagesRepository extends JpaRepository<ChatMessage, Integer> {

    @Query("select cs from ChatMessage cs where cs.session.id=:idSession")
    List<ChatMessage> getChatMessagesByIdSession(int idSession);

    @Modifying
    @Transactional
    @Query("UPDATE ChatMessage m SET m.isRead = true WHERE m.session.id = :sessionId AND m.receiver.id = :receiverId AND m.isRead = false")
    void markAllMessagesAsRead(@Param("sessionId") int sessionId, @Param("receiverId") int receiverId);

    @Query("""
                SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END
                FROM ChatMessage m
                WHERE m.receiver.id = :receiverId
                  AND m.isRead = false
            """)
    boolean existsUnreadMessagesByReceiver(@Param("receiverId") int receiverId);

}
