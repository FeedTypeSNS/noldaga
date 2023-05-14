package com.noldaga.domain.chatdto;

import com.noldaga.domain.UserSimpleDto;
import com.noldaga.domain.entity.chat.ChatRead;
import com.noldaga.domain.entity.chat.ChatRoom;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatReadDto {
    private Long id;
    private UserSimpleDto reader;
    private ChatDto chat;
    private LocalDateTime readDate;

    private ChatReadDto(Long id, UserSimpleDto reader, ChatDto chat, LocalDateTime readDate){
        this.id = id;
        this.reader = reader;
        this.chat = chat;
        this.readDate = readDate;
    }

    public static ChatReadDto fromEntity(ChatRead check, ChatRoom room){
        return new ChatReadDto(
                check.getId(),
                UserSimpleDto.fromEntity(check.getReadUser()),
                ChatDto.fromEntity(check.getChat(), room),
                check.getReadDate()
        );
    }
}
