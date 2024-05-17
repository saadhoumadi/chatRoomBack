package org.dxc.chatroomservice.chat;

import org.dxc.chatroomservice.chatroom.ChatRoomService;
import org.dxc.chatroomservice.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;

    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow(); // You can create your own dedicated exception
        chatMessage.setChatId(chatId);
        repository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }

    public List<ChatMessage> findLastContent(String senderId, String recipientId){
        return repository.findTopBySenderIdAndRecipientIdOrSenderIdAndRecipientIdOrderByTimestampDesc(senderId,recipientId,recipientId,senderId);
    }

    public List<ChatMessage> getUnseenMessagesForConversation(String senderId, String recipientId) {
        // Query the database for messages with the specified chatId, recipientId, and unseen status
        return repository.findBySenderIdAndRecipientIdAndSeen(senderId, recipientId, false);
    }

    public void markMessagesAsSeen(String senderId, String recipientId) {
        List<ChatMessage> unseenMessages = repository.findBySenderIdAndRecipientIdAndSeen(senderId, recipientId, false);
        for (ChatMessage message : unseenMessages) {
            message.setSeen(true);
        }
        repository.saveAll(unseenMessages);
    }
}
