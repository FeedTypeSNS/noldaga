package com.noldaga.domain.chatdto;

import com.noldaga.domain.entity.chat.ChatRoom;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ChatRoomDto {
    private Long id;
    private String uuid; //실제 통신은 이걸로..
    private String roomName;
    @Setter
    private String viewRoomName;
    private int userNum;


    private ChatRoomDto(Long id, String uuid, String roomName, String viewRoomName, int userNum){
        this.id = id;
        this.uuid = uuid;
        this.roomName = roomName;
        this.viewRoomName = viewRoomName;
        this.userNum = userNum;
    }

    public static ChatRoomDto fromEntity(ChatRoom chatRoom){
        return new ChatRoomDto(
                chatRoom.getId(),
                chatRoom.getUuid(),
                chatRoom.getRoomName(),
                chatRoom.getViewRoomName(),
                chatRoom.getUserNum()
        );
    }
}
