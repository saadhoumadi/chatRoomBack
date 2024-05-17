package org.dxc.chatroomservice.chatroomGroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ChatRoomGroup {
    @Id
    private String id;
    private String chatId;
    private String senderId;
    private List<String> recipientId;
    private String groupName;
}
