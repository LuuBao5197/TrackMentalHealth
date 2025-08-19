package fpt.aptech.trackmentalhealth.api.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class CallSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Khi gửi yêu cầu gọi
    @MessageMapping("/call/{sessionId}")
    public void handleCallSignal(@DestinationVariable int sessionId, @Payload String signalJson) {
        System.out.println("📞 Call signal raw: " + signalJson);

        // Forward nguyên JSON qua topic
        messagingTemplate.convertAndSend("/topic/call/" + sessionId, signalJson);
    }
}
