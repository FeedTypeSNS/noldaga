package com.noldaga.controller;

import com.noldaga.controller.request.ChatSendRequest;
import com.noldaga.controller.response.ChatRoomResponse;
import com.noldaga.controller.response.ChatSendResponse;
import com.noldaga.domain.chatdto.ChatRoomDto;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations sendingOperations;
    private final ChatService chatService;

    /*@MessageMapping("chat/{roomId}/message") //실제 프론트는 /app/chat/1/message
    public void enter(Authentication authentication, @RequestBody ChatSendRequest request, @PathVariable Long roomId){
        sendingOperations.convertAndSendToUser("/topic/chat/room/"+message.getSender());
    }*/

    @Transactional
    @MessageMapping("chat/{roomId}/message")
    public void enter(Principal principal, Authentication authentication, @RequestBody ChatSendRequest request, @PathVariable Long roomId){
        Boolean invite = chatService.confirmInvitation(authentication.getName(), roomId);

        if(invite){ //초대되어 있다면 메시지 보내기 및 접속 모두 가능
            ChatRoomDto room = chatService.getChatRoomFromId(authentication.getName(), roomId);

            if (request.getType().equals(ChatSendRequest.MessageType.ENTER)) { //처음 입장하면
                log.info(authentication.getName() + "님이 입장했습니다.");
                ChatRoomResponse enter = chatService.findOneChatRoom(authentication.getName(), roomId);
                sendingOperations.convertAndSend("/user/" + authentication.getName() + "/topic/chat/room/" + room.getUuid(), enter);
                log.info("지난 기록 전송 완료:{}", enter);
            } else {
                ChatSendResponse msg = chatService.saveChat(authentication.getName(), request, roomId); //메시지 보내기전 저장하고
                sendingOperations.convertAndSend("/topic/chat/room/" + room.getUuid(), msg); //모두에게 저장
            }
        }else {
            throw new SnsApplicationException(ErrorCode.NOT_ALLOW_IN_ROOM); //초대 안되어있으면 오류 발생
        }
    }
}
