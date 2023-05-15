package com.noldaga.domain.chatdto;

import com.noldaga.domain.UserSimpleDto;
import com.noldaga.domain.entity.chat.Chat;
import com.noldaga.domain.entity.chat.ChatRoom;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatDto {
    private Long id;
    private ChatRoomDto room;
    private UserSimpleDto sender;
    private String msg;
    private int unread_count;
    private LocalDateTime createAt;

    private ChatDto(Long id, ChatRoomDto room, UserSimpleDto sender, String msg, int unread_count, LocalDateTime createdAt){
        this.id = id;
        this.room = room;
        this.sender = sender;
        this.msg = msg;
        this.unread_count = unread_count;
        this.createAt = createdAt;
    }

    public static ChatDto fromEntity(Chat chat, ChatRoom room){
        return new ChatDto(
                chat.getId(),
                ChatRoomDto.fromEntity(room),
                UserSimpleDto.fromEntity(chat.getSender()),
                chat.getMsg(),
                chat.getUnread(),
                chat.getCreatedAt()
        );
    }
}
