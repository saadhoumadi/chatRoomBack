package org.dxc.chatroomservice.chatGroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatGroupNotification {
    private String id;
    private String senderId;
    private String recipientId;
    private List<String> recipientsId;
    private String content;
}
