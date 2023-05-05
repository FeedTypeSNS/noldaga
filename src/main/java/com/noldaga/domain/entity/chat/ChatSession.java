package com.noldaga.domain.entity.chat;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;


@Getter //dto 생성시 사용
@ToString
@Entity
public class ChatSession {
    @Id
    private String sessionId; //WebSocket 연결 세션을 식별하는 고유한 식별자

    @ManyToOne
    private ChatRoom chatRoom;


    protected ChatSession(){}
    public ChatSession(String id, ChatRoom room){
        this.sessionId = id;
        this.chatRoom = room;
    }

    public static ChatSession of(String id, ChatRoom room){return new ChatSession(id, room);}

}
