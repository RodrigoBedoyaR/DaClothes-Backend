package nl.fontys.s3.daclothes.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.domain.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("notification")
public class MessageController{
    private final SimpMessagingTemplate messagingTemplate;

//    @PostMapping
//    public ResponseEntity<Void> sendNotificationToUsers(@RequestBody Message message) {
//        messagingTemplate.convertAndSend("/topic/publicmessages", message);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    
    @MessageMapping("/application")
    @SendTo("/all/message")
    public Message revievePublicMessage( @Payload Message message ) {
        return message;
    }

    @MessageMapping("/private")
    public Message sendPrivateMessage( @Payload Message message ) {
        messagingTemplate.convertAndSendToUser(message.getTo(), "/specific", message);
        return message;
    }

}
