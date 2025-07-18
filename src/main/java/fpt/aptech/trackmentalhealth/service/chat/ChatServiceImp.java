package fpt.aptech.trackmentalhealth.service.chat;

import fpt.aptech.trackmentalhealth.entities.ChatGroup;
import fpt.aptech.trackmentalhealth.entities.ChatMessage;
import fpt.aptech.trackmentalhealth.entities.ChatMessageGroup;
import fpt.aptech.trackmentalhealth.entities.ChatSession;
import fpt.aptech.trackmentalhealth.repository.chat.ChatGroupRepository;
import fpt.aptech.trackmentalhealth.repository.chat.ChatMessageGroupRepository;
import fpt.aptech.trackmentalhealth.repository.chat.ChatMessagesRepository;
import fpt.aptech.trackmentalhealth.repository.chat.ChatSessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImp implements ChatService {

    @Autowired
    ChatMessagesRepository chatMessagesRepository;

    @Autowired
    ChatSessionRepository chatSessionRepository;

    @Autowired
    ChatGroupRepository chatGroupRepository;

    @Autowired
    ChatMessageGroupRepository chatMessageGroupRepository;

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

    @Override
    public List<ChatGroup> getChatGroups() {
        return chatGroupRepository.findAll();
    }

    @Override
    public ChatGroup getChatGroupById(int id) {
        return chatGroupRepository.getChatGroupById(id);
    }

    @Override
    public ChatGroup createChatGroup(ChatGroup chatGroup) {
        return chatGroupRepository.save(chatGroup);
    }

    @Override
    public ChatGroup updateChatGroup(ChatGroup chatGroup) {
        return chatGroupRepository.save(chatGroup);
    }

    @Override
    public void deleteChatGroup(int id) {
        // Bước 1: Xóa tin nhắn
        chatMessageGroupRepository.deleteMessagesByGroupId(id);
        // Bước 3: Xóa nhóm
        chatGroupRepository.deleteById(id);
    }


    @Override
    public List<ChatMessageGroup> getChatMessagesByChatGroupId(int id) {
        return chatMessageGroupRepository.getChatMessagesByGroupId(id);
    }

    @Override
    public ChatMessageGroup sendMessageToGroup(ChatMessageGroup message) {
        message.setSendAt(new Date());
        return chatMessageGroupRepository.save(message);
    }

    @Override
    public List<ChatGroup> getChatGroupsByUserId(int id) {
        return chatGroupRepository.getChatGroupsByUserId(id);
    }

    @Override
    public List<ChatGroup> getChatSessionByUserIdCreated(int id) {
        return chatGroupRepository.getChatGroupsByUserIdCreated(id);
    }

    @Override
    public void changeStatusIsRead(int sessionId, int receiverId) {
        chatMessagesRepository.markAllMessagesAsRead(sessionId, receiverId);
    }

    @Override
    public ChatMessageGroup saveMessageToGroup(ChatMessageGroup message) {
        return chatMessageGroupRepository.save(message);
    }

}
