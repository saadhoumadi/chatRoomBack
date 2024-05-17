package org.dxc.chatroomservice.chatroomGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class ChatRoomGroupController {

    @Autowired
    private ChatRoomGroupService chatRoomService;

    @PostMapping("/create")
    public void createChatGroupId(@RequestParam("senderId") String senderId,
                                  @RequestParam("recipientsId") List<String> recipientsId,
                                  @RequestParam("groupName") String groupName){
        chatRoomService.createChatGroupId(senderId,recipientsId,groupName);
    }

    @GetMapping("/allDistinct")
    public List<String> findAllDistinctById(){
        return chatRoomService.findAllDistinctById();
    }

    @GetMapping("/recipients/{chatId}/{senderId}")
    public List<String> findRecipientIdByChatIdAndSenderId(@PathVariable String chatId,
                                                           @PathVariable String senderId){
        return chatRoomService.findRecipientIdByChatIdAndSenderId(chatId,senderId);
    }
}
