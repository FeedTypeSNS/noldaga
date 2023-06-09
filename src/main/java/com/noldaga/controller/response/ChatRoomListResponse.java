package com.noldaga.controller.response;

import com.noldaga.domain.chatdto.ChatDto;
import com.noldaga.domain.chatdto.ChatRoomDto;
import com.noldaga.domain.UserSimpleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomListResponse {
    private ChatRoomDto roomInfo; //채팅방 정보
    private List<UserSimpleDto> joinPeoples; //참가자, 방번호
    private ChatDto recentChat; //마지막 채팅
    private int unreadCount;
    private int peopleCount;

    public static ChatRoomListResponse returnResponse(ChatRoomDto roomInfo, List<UserSimpleDto> jp, ChatDto rc,int u, int c){
        return new ChatRoomListResponse(roomInfo, jp, rc,u, c);
    }



}
