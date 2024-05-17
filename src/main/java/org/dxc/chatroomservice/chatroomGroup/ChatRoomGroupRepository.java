package org.dxc.chatroomservice.chatroomGroup;

import org.dxc.chatroomservice.chatroom.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomGroupRepository extends MongoRepository<ChatRoomGroup,String> {

    Optional<ChatRoom> findByChatIdAndSenderId(String chatId, String senderId);
    @Query(value = "{}", fields = "{ 'chatId' : 1 }")
    List<ChatRoom> findAllDistinctById();


    List<ChatRoomGroup> findRecipientIdByChatIdAndSenderId(String chatId, String senderId);
}
