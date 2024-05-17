package org.dxc.chatroomservice.chat;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByChatId(String chatId);

    List<ChatMessage> findTopBySenderIdAndRecipientIdOrSenderIdAndRecipientIdOrderByTimestampDesc(
            String senderId1, String recipientId1, String senderId2, String recipientId2
    );

    List<ChatMessage> findBySenderIdAndRecipientIdAndSeen(String senderId, String recipientId, Boolean seen);
}
