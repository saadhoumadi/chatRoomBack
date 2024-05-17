package org.dxc.chatroomservice.chatGroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ChatMessageGroup {
    @Id
    private String id;
    private String chatId;
    private String senderId;
    private List<String> recipientsId;
    private String content;
    private Date timestamp;
    private List<String> hasSeenBy;
}
