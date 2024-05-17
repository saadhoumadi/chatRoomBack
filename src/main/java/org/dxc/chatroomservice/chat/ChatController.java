package org.dxc.chatroomservice.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final RestTemplate restTemplate;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
//        // Update the chatIndex for all users via the User microservice API
//        String updateUserUrl = "http://localhost:8083/identity-service/user/update-chatIndex?recipientId=" + chatMessage.getRecipientId();
//        restTemplate.put(updateUserUrl, null);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
                                                 @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @GetMapping("/lastContent/{senderId}/{recipientId}")
    public List<ChatMessage> findLastContent(@PathVariable String senderId, @PathVariable String recipientId){
        return chatMessageService.findLastContent(senderId,recipientId);
    }

    @GetMapping("/unseen-messages/{senderId}/{recipientId}")
    public List<ChatMessage> getUnseenMessages(@PathVariable String senderId, @PathVariable String recipientId) {
        return chatMessageService.getUnseenMessagesForConversation(senderId, recipientId);
    }

    @PostMapping("/mark-seen/{chatId}/{recipientId}")
    public ResponseEntity<?> markMessagesAsSeen(@PathVariable String chatId, @PathVariable String recipientId) {
        chatMessageService.markMessagesAsSeen(chatId, recipientId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Messages marked as seen");

        // Return the JSON response
        return ResponseEntity.ok(response);
    }
}
