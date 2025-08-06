package fpt.aptech.trackmentalhealth.repository.chat;

import fpt.aptech.trackmentalhealth.entities.ChatGroup;
import fpt.aptech.trackmentalhealth.entities.ChatMessageGroup;
import fpt.aptech.trackmentalhealth.entities.ChatSession;
import fpt.aptech.trackmentalhealth.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatGroupRepository extends JpaRepository<ChatGroup, Integer> {
    @Query("select cs from ChatGroup cs where cs.id=:id")
    ChatGroup getChatGroupById(int id);

    @Query("SELECT cs FROM ChatGroup cs JOIN cs.members m WHERE m.id = :id")
    List<ChatGroup> getChatGroupsByUserId(@Param("id") int id);

    @Query("select cs from ChatGroup cs where cs.createdBy.id=:id")
    List<ChatGroup> getChatGroupsByUserIdCreated(int id);

    @Query("SELECT DISTINCT m.sender FROM ChatMessageGroup m " +
            "WHERE m.group.id = :groupId AND m.sender.id <> :currentUserId")
    List<Users> findUsersByGroupId(@Param("groupId") int groupId,
                                   @Param("currentUserId") int currentUserId);

    @Query(value = """
                SELECT TOP 1 * 
                FROM chat_message_group m 
                WHERE m.group_id = :groupId 
                ORDER BY m.timestamp DESC
            """, nativeQuery = true)
    ChatMessageGroup findLatestMessageByGroupId(@Param("groupId") int groupId);

}
