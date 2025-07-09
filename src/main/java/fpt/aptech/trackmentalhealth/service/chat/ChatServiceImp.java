package fpt.aptech.trackmentalhealth.service.chat;

import fpt.aptech.trackmentalhealth.entities.ChatMessage;
import fpt.aptech.trackmentalhealth.entities.ChatSession;
import fpt.aptech.trackmentalhealth.repository.chat.ChatMessagesRepository;
import fpt.aptech.trackmentalhealth.repository.chat.ChatSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImp implements ChatService {

    @Autowired
    ChatMessagesRepository chatMessagesRepository;

    @Autowired
    ChatSessionRepository chatSessionRepository;


    @Override
    public List<ChatSession> getChatSessions() {
        return List.of();
    }

    @Override
    public ChatSession getChatSessionById(int id) {
        return chatSessionRepository.getChatSessionById(id);
    }


    @Override
    public List<ChatMessage> getChatMessagesByChatSessionId(int id) {
        return chatMessagesRepository.getChatMessagesByIdSession(id);
    }

    @Override
    public ChatMessage sendMessage(ChatMessage message) {
        ChatMessage saved = chatMessagesRepository.save(message);
        saved.getSession().getSender().getFullname();
        return saved;
    }

    @Override
    public List<ChatSession> getChatSessionByUserId(int id) {
        return chatSessionRepository.getChatSessionByUserId(id);
    }

    @Override
    public ChatSession getChatSessionByFromAndTo(int user1, int user2) {
        return chatSessionRepository.getChatSessionByTwoUserid(user1, user2);
    }
}
