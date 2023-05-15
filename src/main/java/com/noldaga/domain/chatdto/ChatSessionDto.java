package com.noldaga.domain.chatdto;

import com.noldaga.domain.entity.User;
import com.noldaga.domain.entity.chat.ChatSession;
import com.noldaga.domain.userdto.UserDto;
import lombok.Getter;

@Getter
public class ChatSessionDto {
    private String sessionId;
    private ChatRoomDto room;
    private UserDto user;

    private ChatSessionDto(String sessionId, ChatRoomDto room, UserDto user){
        this.sessionId = sessionId;
        this.room = room;
        this.user = user;
    }

    public static ChatSessionDto fromEntity(ChatSession chat){
        return new ChatSessionDto(
                chat.getSessionId(),
                ChatRoomDto.fromEntity(chat.getChatRoom()),
                UserDto.fromEntity(chat.getUser())
        );
    }
}
