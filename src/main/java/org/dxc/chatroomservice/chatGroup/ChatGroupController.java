package org.dxc.chatroomservice.chatGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class ChatGroupController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageGroupService chatMessageService;

    @MessageMapping("/chatGroup")
    public void processMessage(@Payload ChatMessageGroup chatMessage) {
        ChatMessageGroup savedMsg = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getChatId(), "/queue/messages",
                new ChatGroupNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        null,
                        savedMsg.getRecipientsId(),
                        savedMsg.getContent()
                )
        );
    }

    @GetMapping("/messagesGrp/{senderId}/{recipientsId}")
    public ResponseEntity<List<ChatMessageGroup>> findChatMessages(@PathVariable String senderId,
                                                                   @PathVariable List<String> recipientsId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientsId));
    }

    @PostMapping("/markAsSeen/{chatId}/{recipientId}")
    public void addToHasSeenBy(@PathVariable String chatId, @PathVariable String recipientId ){
        chatMessageService.addToHasSeenBy(chatId, recipientId);
    }

    @GetMapping("/unseenGroup-messages/{chatId}/{senderId}")
    public List<ChatMessageGroup> findBySenderIdAndRecipientsIdInAndHasSeenByNotIn(@PathVariable String chatId,
                                                                                @PathVariable String senderId){
        return chatMessageService.getUnseenMessages(chatId,senderId);
    }

    @GetMapping("/lastGroupContent/{chatId}")
    public ChatMessageGroup findTopByChatIdOrderByTimestampDesc(@PathVariable String chatId){
        return chatMessageService.findTopByChatIdOrderByTimestampDesc(chatId);
    }

}
