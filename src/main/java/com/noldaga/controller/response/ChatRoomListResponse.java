package com.noldaga.controller.response;

import com.noldaga.domain.chatdto.ChatDto;
import com.noldaga.domain.chatdto.ChatRoomDto;
import com.noldaga.domain.UserSimpleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatRoomListResponse {
    private ChatRoomDto roomInfo; //채팅방 정보
    private List<UserSimpleDto> joinPeoples; //참가자, 방번호
    private ChatDto recentChat; //마지막 채팅

    public static ChatRoomListResponse returnResponse(ChatRoomDto roomInfo, List<UserSimpleDto> jp, ChatDto rc){
        return new ChatRoomListResponse(roomInfo, jp, rc);
    }



}
