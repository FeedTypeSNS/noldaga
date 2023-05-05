package com.noldaga.controller.response;

import com.noldaga.domain.chatdto.ChatDto;
import com.noldaga.domain.UserSimpleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatSendResponse {
    private ChatDto chat; //채팅 내용
    private String type;
    /*private String who; //내가 보낸건지 다른사람이 보낸건지 같이 보내줘야 편하게 작업..*/
    //private List<UserSimpleDto> receiverList; //받아야하는 사람들 리스트

    public static ChatSendResponse returnResponse(ChatDto chat /*List<UserSimpleDto> rl*/){
        return new ChatSendResponse(chat, "TALK"/*, rl*/);
    }
}
