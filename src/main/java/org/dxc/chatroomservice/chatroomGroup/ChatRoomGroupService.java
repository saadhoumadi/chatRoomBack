package org.dxc.chatroomservice.chatroomGroup;

import org.dxc.chatroomservice.chatroom.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomGroupService {

    private final ChatRoomGroupRepository chatRoomRepository;

    public Optional<String> getChatRoomGroupId(String senderId, List<String> recipientIds) {
        List<String> allParticipantIds = new ArrayList<>(recipientIds);
        allParticipantIds.add(senderId);
        Collections.sort(allParticipantIds);
        String chatRoomId = String.join("_", allParticipantIds).replace("[", "").replace("]", "");

        System.out.println("chatId:"+chatRoomId);
        return chatRoomRepository.findByChatIdAndSenderId(chatRoomId, senderId)
                .map(ChatRoom::getChatId);

    }

    public String createChatGroupId(String senderId, List<String> recipientsId, String groupName) {
        List<String> allParticipantIds = new ArrayList<>(recipientsId);
        allParticipantIds.add(senderId);
        Collections.sort(allParticipantIds);
        String chatId = String.join("_", allParticipantIds).replace("[", "").replace("]", "");

        ChatRoomGroup senderRecipients = ChatRoomGroup
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientsId)
                .groupName(groupName)
                .build();
        chatRoomRepository.save(senderRecipients);

        recipientsId.forEach(s -> {
            allParticipantIds.remove(allParticipantIds.indexOf(s));
            ChatRoomGroup senderRecipient = ChatRoomGroup
                    .builder()
                    .chatId(chatId)
                    .senderId(s.replace("[", "").replace("]", ""))
                    .recipientId(allParticipantIds)
                    .groupName(groupName)
                    .build();
            chatRoomRepository.save(senderRecipient);
            allParticipantIds.add(s);
        });

        return chatId;
    }

    public List<String> findAllDistinctById() {
        List<String> distinctChatIds = chatRoomRepository.findAllDistinctById()
                .stream()
                .map(ChatRoom::getChatId)
                .distinct()
                .collect(Collectors.toList());
        return distinctChatIds;
    }

    public List<String> findRecipientIdByChatIdAndSenderId(String chatId, String senderId){
        List<String> recipients= chatRoomRepository.findRecipientIdByChatIdAndSenderId(chatId,senderId)
                .stream()
                .flatMap(chatRoomGroup -> chatRoomGroup.getRecipientId().stream())
                .collect(Collectors.toList());
        return  recipients;
    }
}
