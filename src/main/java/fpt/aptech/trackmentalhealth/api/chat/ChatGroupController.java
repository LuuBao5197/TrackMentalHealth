package fpt.aptech.trackmentalhealth.api.chat;

import fpt.aptech.trackmentalhealth.entities.ChatGroup;
import fpt.aptech.trackmentalhealth.entities.ChatMessageGroup;
import fpt.aptech.trackmentalhealth.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/chatgroup")
public class ChatGroupController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/findAll")
    public List<ChatGroup> findAll() {
        return chatService.getChatGroups();
    }


    @GetMapping("/findAll/{userId}")
    public List<ChatGroup> findByUserId(@PathVariable int userId) {
        return chatService.getChatGroupsByUserId(userId);
    }


    @GetMapping("/{id}")
    public ChatGroup findById(@PathVariable int id) {
        return chatService.getChatGroupById(id);
    }

    @GetMapping("/createdBy/{userId}")
    public List<ChatGroup> findByCreatorId(@PathVariable int userId) {
        return chatService.getChatSessionByUserIdCreated(userId);
    }

    @PostMapping("/create")
    public ChatGroup saveGroup(@RequestBody ChatGroup chatGroup) {
        chatGroup.setCreatedAt(new Date());
        return chatService.createChatGroup(chatGroup);
    }

    @PutMapping("/edit/{id}")
    public ChatGroup editGroup(@RequestBody ChatGroup updatedGroup, @PathVariable int id) {
        ChatGroup existingGroup = chatService.getChatGroupById(id);
        if (existingGroup == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        }
        existingGroup.setName(updatedGroup.getName());
        existingGroup.setDes(updatedGroup.getDes());
        existingGroup.setAvt(updatedGroup.getAvt());
        return chatService.updateChatGroup(existingGroup);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteGroup(@PathVariable int id) {
        chatService.deleteChatGroup(id);
    }

    @GetMapping("/messages/{id}")
    public List<ChatMessageGroup> getMessagesByGroupId(@PathVariable int id){
        ChatGroup chatGroup = chatService.getChatGroupById(id);
        if (chatGroup == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        }
        return chatService.getChatMessagesByChatGroupId(id);
    }
}
