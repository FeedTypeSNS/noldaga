package com.noldaga.domain.chatdto;

import com.noldaga.domain.UserSimpleDto;
import com.noldaga.domain.entity.chat.JoinRoom;
import lombok.Getter;

@Getter
public class JoinRoomDto {
    private Long id;
    private ChatRoomDto room;
    private UserSimpleDto user;

    private JoinRoomDto(Long id, ChatRoomDto room, UserSimpleDto user){
        this.id = id;
        this.room = room;
        this.user = user;
    }

    public static JoinRoomDto fromEntity(JoinRoom join){
        return new JoinRoomDto(
                join.getId(),
                ChatRoomDto.fromEntity(join.getRoom()),
                UserSimpleDto.fromEntity(join.getUsers())
        );
    }
}
