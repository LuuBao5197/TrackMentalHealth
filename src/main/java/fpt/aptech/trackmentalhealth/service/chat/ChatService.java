package fpt.aptech.trackmentalhealth.service.chat;

import fpt.aptech.trackmentalhealth.entities.ChatMessage;
import fpt.aptech.trackmentalhealth.entities.ChatSession;

import java.util.List;

public interface ChatService {
    List<ChatSession> getChatSessions();

    ChatSession getChatSessionById(int id);

    List<ChatMessage> getChatMessagesByChatSessionId(int id);

    ChatMessage sendMessage(ChatMessage message);

    List<ChatSession> getChatSessionByUserId(int id);

    ChatSession getChatSessionByFromAndTo(int user1, int user2);
}
