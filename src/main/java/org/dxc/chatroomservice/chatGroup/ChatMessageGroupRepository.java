package org.dxc.chatroomservice.chatGroup;

import org.dxc.chatroomservice.chatroom.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageGroupRepository extends MongoRepository<ChatMessageGroup,String> {
    List<ChatMessageGroup> findByChatId(String chatId);

    List<ChatMessageGroup> findByChatIdAndRecipientsIdInAndHasSeenByNotIn(String chatId, String senderId, String recipientId1);

    ChatMessageGroup findTopByChatIdOrderByTimestampDesc(String chatId);

    Optional<ChatRoom> findByChatIdAndSenderId(String chatId, String senderId);

}
