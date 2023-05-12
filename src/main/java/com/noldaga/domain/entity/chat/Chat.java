package com.noldaga.domain.entity.chat;

import com.noldaga.domain.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class) //자동으로 시간을 매핑하여 테이블에 넣어주는 jpa auditing 사용
@Getter
@ToString
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //채팅 순서 = 메시지 순서

    @OneToOne //기본 fetchtype eger -> 메시지 보내줄때마다 필요
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    @JoinColumn(name = "send_user_id")
    @ManyToOne //회원 정보를 늘 가져올 필요 없음
    private User sender; //채팅 보내는 사람
    //사용자 한명이 편지를 여러개 쓸 수 있음,
    // chat(메시지) 기준으로 사용자는 한명이고, 자신이 여러개니 ManyToOne
    // 생각해볼 부분.. 만약 회원이 탈퇴를 한다면 메세기 모두 삭제 시키면 (onDelte 설정)
    // 아니면, 그냥 회원이 null 일 경우 이름 없음 이런식으로..?

    @Column(name = "msg")
    private String msg; //메세지 내용 -> 이미지도 포함할 수 있나? 이게 문제..

    @Setter
    @Column(nullable = false)
    private int unread;   //메세지 안 읽은 메시지 수 -> 안읽음 채팅방 참가 인원 수-1 , 읽음 0

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false , updatable = false)
    @Setter
    private LocalDateTime createdAt; //메시지 작성 시간

    protected Chat(){}

    public Chat(Long id, ChatRoom room, User sender, String msg, int unread){
        this.id = id;
        this.room = room;
        this.sender = sender;
        this.msg = msg;
        this.unread = unread;
    }

    public static Chat of (ChatRoom room, User sender, String msg, int unread){
        return new Chat(null, room, sender, msg, unread);
    }



}
