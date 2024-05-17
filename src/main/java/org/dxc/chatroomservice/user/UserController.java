package org.dxc.chatroomservice.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public User addUser(
            @Payload User user
    ) {
        userService.saveUser(user);
        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public User disconnectUser(
            @Payload User user
    ) {
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }

    @MessageMapping("/send-message")
//    @SendTo("/user/topic/messages") // This is where the message will be sent to subscribers
    public MessageResponse handleMessage(@Payload Map<String, String> payload) {
        // Process the incoming message here
        String msg = payload.get("message");
        String sender = payload.get("sender");
        String recipient = payload.get("recipient");
        MessageResponse messageResponse= new MessageResponse(msg, sender, recipient);

        messagingTemplate.convertAndSend("/user/topic/messages", messageResponse);

        return messageResponse;
    }

}
