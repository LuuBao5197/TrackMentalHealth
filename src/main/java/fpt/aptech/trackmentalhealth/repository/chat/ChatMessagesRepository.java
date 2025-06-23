package fpt.aptech.trackmentalhealth.repository.chat;

import fpt.aptech.trackmentalhealth.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessagesRepository extends JpaRepository<ChatMessage,Integer> {

    @Query("select cs from ChatMessage cs where cs.session.id=:idSession")
    List<ChatMessage> getChatMessagesByIdSession(int idSession);
}
