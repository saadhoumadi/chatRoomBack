package org.dxc.chatroomservice.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponse {
    private String content;
    private String senderId;
    private String recipientId;
}
