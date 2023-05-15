package com.noldaga.controller.response;

import com.noldaga.domain.chatdto.ChatDto;
import com.noldaga.domain.chatdto.ChatReadDto;
import com.noldaga.domain.chatdto.ChatRoomDto;
import com.noldaga.domain.UserSimpleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatRoomResponse {
    private ChatRoomDto roomInfo; //채팅방 정보
    private List<UserSimpleDto> joinPeoples; //참가자, 방번호
    private List<ChatDto> pastChat; //과거 채팅 리스트
    private List<ChatReadDto> readList; //읽은 사람 리스트
    private String type;

    public static ChatRoomResponse returnResponse(ChatRoomDto roomInfo, List<UserSimpleDto> jp, List<ChatDto> pc, List<ChatReadDto> read, String type){
        return new ChatRoomResponse(roomInfo, jp, pc, read, type);
    }

}
