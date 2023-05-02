package com.noldaga.domain.entity.chat;

import com.noldaga.domain.entity.User;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@EntityListeners(AuditingEntityListener.class) //자동으로 시간을 매핑하여 테이블에 넣어주는 jpa auditing 사용
@Getter //dto 생성시 사용
@ToString
@Entity
public class JoinRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //기본 인덱스 추가 (편하게 할려고 무의미..)

    @ManyToOne
    @JoinColumn(name = "join_room_id")
    private ChatRoom room; //참여된 채팅방 -> 어디 채팅방?

    @ManyToOne
    @JoinColumn(name = "join_users_id")
    private User users; //방에 참가한 사람

    protected JoinRoom(){}

    public JoinRoom(Long id, ChatRoom room, User users){
        this.id = id;
        this.room = room;
        this.users = users;
    }

    public static JoinRoom of(ChatRoom room, User users){
        return new JoinRoom(null, room, users);
    }
}
