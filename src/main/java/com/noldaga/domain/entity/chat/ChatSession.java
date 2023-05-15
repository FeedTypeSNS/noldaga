package com.noldaga.domain.entity.chat;

import com.noldaga.domain.entity.User;
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

    @ManyToOne
    private User user;


    protected ChatSession(){}
    public ChatSession(String id, ChatRoom room, User user){
        this.sessionId = id;
        this.chatRoom = room;
        this.user = user;
    }

    public static ChatSession of(String id, ChatRoom room, User user){return new ChatSession(id, room, user);}

}
