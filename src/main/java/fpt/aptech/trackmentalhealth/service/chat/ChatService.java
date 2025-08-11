package fpt.aptech.trackmentalhealth.service.chat;

import fpt.aptech.trackmentalhealth.entities.*;

import java.util.List;

public interface ChatService {

    ChatSession getChatSessionById(int id);

    List<ChatMessage> getChatMessagesByChatSessionId(int id);

    ChatMessage sendMessage(ChatMessage message);

    List<ChatSession> getChatSessionByUserId(int id);

    ChatSession createChatSession(ChatSession chatSession);

    ChatSession getChatSessionByFromAndTo(int user1, int user2);

    List<ChatGroup> getChatGroups();

    ChatGroup getChatGroupById(int id);

    ChatGroup createChatGroup(ChatGroup chatGroup);

    ChatGroup updateChatGroup(ChatGroup chatGroup);

    void deleteChatGroup(int id);

    List<ChatMessageGroup> getChatMessagesByChatGroupId(int id);

    ChatMessageGroup sendMessageToGroup(ChatMessageGroup message);

    List<ChatGroup> getChatGroupsByUserId(int id);

    List<ChatGroup> getChatSessionByUserIdCreated(int id);

    void changeStatusIsRead(int sessionId, int receiverId );

    ChatMessageGroup saveMessageToGroup(ChatMessageGroup message);

    List<Users> findUserByGroupId(int groupId,int currentUserId);

    Users findUserById(int id);

    boolean hasUnreadMessages(int receiverId);

    ChatMessage getLatestMessage(int sessionId);

    ChatMessageGroup getLatestMessageGroup(int groupId);
}
