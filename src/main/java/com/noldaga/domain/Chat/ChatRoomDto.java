package com.noldaga.domain.Chat;

import com.noldaga.domain.entity.Chat.ChatRoom;
import lombok.Getter;

@Getter
public class ChatRoomDto {
    private Long id;
    private String roomName;
    private String viewRoomName;

    private ChatRoomDto(Long id, String roomName, String viewRoomName){
        this.id = id;
        this.roomName = roomName;
        this.viewRoomName = viewRoomName;
    }

    public static ChatRoomDto fromEntity(ChatRoom chatRoom){
        return new ChatRoomDto(
                chatRoom.getId(),
                chatRoom.getRoomName(),
                chatRoom.getViewRoomName()
        );
    }
}
