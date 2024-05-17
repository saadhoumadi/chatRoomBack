package org.dxc.chatroomservice.chatroom;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);

    Optional<ChatRoom> findByChatIdAndSenderId(String chatId, String senderId);

    @Query(value = "{ 'recipientId' : null }", fields = "{ 'chatId' : 1 }")
    List<ChatRoom> findAllDistinctById();
}
