package fpt.aptech.trackmentalhealth.api.chat;

import fpt.aptech.trackmentalhealth.entities.CallSignal;
import fpt.aptech.trackmentalhealth.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class CallSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    // Khi gửi yêu cầu gọi
    @MessageMapping("/call/{sessionId}")
    public void handleCallSignal(@DestinationVariable int sessionId, @Payload CallSignal signal) {
        System.out.println("📞 Call signal: " + signal.getType() + " từ user " + signal.getCallerId());

        // Gửi tín hiệu đến tất cả client trong session này
        messagingTemplate.convertAndSend("/topic/call/" + sessionId, signal);
    }
}
