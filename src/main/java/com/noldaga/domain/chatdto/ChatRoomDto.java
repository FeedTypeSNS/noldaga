package com.noldaga.domain.chatdto;

import com.noldaga.domain.entity.chat.ChatRoom;
import lombok.Getter;

@Getter
public class ChatRoomDto {
    private Long id;
    private String uuid; //실제 통신은 이걸로..
    private String roomName;
    private String viewRoomName;


    private ChatRoomDto(Long id, String uuid, String roomName, String viewRoomName){
        this.id = id;
        this.uuid = uuid;
        this.roomName = roomName;
        this.viewRoomName = viewRoomName;
    }

    public static ChatRoomDto fromEntity(ChatRoom chatRoom){
        return new ChatRoomDto(
                chatRoom.getId(),
                chatRoom.getUuid(),
                chatRoom.getRoomName(),
                chatRoom.getViewRoomName()
        );
    }
}
