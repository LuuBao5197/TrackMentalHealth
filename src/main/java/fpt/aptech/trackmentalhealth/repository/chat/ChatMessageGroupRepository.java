package fpt.aptech.trackmentalhealth.repository.chat;

import fpt.aptech.trackmentalhealth.entities.ChatGroup;
import fpt.aptech.trackmentalhealth.entities.ChatMessageGroup;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface ChatMessageGroupRepository extends JpaRepository<ChatMessageGroup,Integer> {
    @Query("select cs from ChatMessageGroup cs where cs.group.id=:id")
    List<ChatMessageGroup> getChatMessagesByGroupId(int id);


    @Modifying
    @Transactional
    @Query("DELETE FROM ChatMessageGroup cs WHERE cs.group.id = :groupId")
    void deleteMessagesByGroupId(int groupId);
}
