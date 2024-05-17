package org.dxc.chatroomservice.chatGroup;

import org.dxc.chatroomservice.chatroomGroup.ChatRoomGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageGroupService {

    private final ChatMessageGroupRepository repository;
    private final ChatRoomGroupService chatRoomService;

    public ChatMessageGroup save(ChatMessageGroup chatMessage) {
        System.out.println(chatMessage.getSenderId()+ " "+chatMessage.getRecipientsId());
        var chatId = chatRoomService
                .getChatRoomGroupId(chatMessage.getSenderId(), chatMessage.getRecipientsId())
                .orElseThrow(); // You can create your own dedicated exception
        chatMessage.setChatId(chatId);
        chatMessage.setHasSeenBy(new ArrayList<>());
        repository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessageGroup> findChatMessages(String senderId, List<String> recipientsId) {
        var chatId = chatRoomService.getChatRoomGroupId(senderId, recipientsId);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }

    public List<ChatMessageGroup> getUnseenMessages(String chatId, String senderId) {
        return repository.findByChatIdAndRecipientsIdInAndHasSeenByNotIn(chatId, senderId, senderId);
    }

    public void addToHasSeenBy(String chatId, String recipientId) {
        List<ChatMessageGroup> messages = repository.findByChatId(chatId);
        for (ChatMessageGroup message : messages) {
            if (!message.getHasSeenBy().contains(recipientId)) {
                message.getHasSeenBy().add(recipientId);
                repository.save(message);
            }
        }
    }

    public ChatMessageGroup findTopByChatIdOrderByTimestampDesc(String chatId){
        return repository.findTopByChatIdOrderByTimestampDesc(chatId);
    }
}
