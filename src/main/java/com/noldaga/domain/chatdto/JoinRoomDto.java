package com.noldaga.domain.chatdto;

import com.noldaga.domain.UserSimpleDto;
import com.noldaga.domain.entity.chat.JoinRoom;
import lombok.Getter;

@Getter
public class JoinRoomDto {
    private Long id;
    private String uuid;
    private UserSimpleDto user;

    private JoinRoomDto(Long id, String room, UserSimpleDto user){
        this.id = id;
        this.uuid = room;
        this.user = user;
    }

    public static JoinRoomDto fromEntity(JoinRoom join){
        return new JoinRoomDto(
                join.getId(),
                join.getUuid(),
                UserSimpleDto.fromEntity(join.getUsers())
        );
    }
}
