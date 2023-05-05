package com.noldaga.domain.chatdto;

import com.noldaga.domain.entity.chat.ChatSession;
import lombok.Getter;

@Getter
public class ChatSessionDto {
    private String sessionId;
    private ChatRoomDto room;

    private ChatSessionDto(String sessionId, ChatRoomDto room){
        this.sessionId = sessionId;
        this.room = room;
    }

    public static ChatSessionDto fromEntity(ChatSession chat){
        return new ChatSessionDto(
                chat.getSessionId(),
                ChatRoomDto.fromEntity(chat.getChatRoom())
        );
    }
}
