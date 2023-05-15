package com.noldaga.domain.entity.chat;

import com.noldaga.domain.entity.User;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class) //자동으로 시간을 매핑하여 테이블에 넣어주는 jpa auditing 사용
@Getter //dto 생성시 사용
@ToString
@Entity
public class ChatRead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //구별을 위한 id

    @ManyToOne
    @JoinColumn(name = "chat_read_user")
    private User readUser; //채팅을 읽은 사람, 한 사람은 여러개의 채팅확인 가능

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;      //채팅(메시지), 하나의 메시지는 여러개의 채팅확인 가능


    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false , updatable = false)
    private LocalDateTime readDate;  //메시지 읽은 시간

    protected ChatRead(){}

    public ChatRead(Long id, User reader, Chat chatId){
        this.id = id;
        this.readUser = reader;
        this.chat = chatId;
    }

    public static ChatRead of(User reader, Chat chatId){
        return new ChatRead(null, reader, chatId);
    }

}
//채팅을 읽은 사람, 시간 체크하는 테이블
//읽지 않은 마지막 채팅까지만 체크
//만약 모두 다 읽으면 -> 본사람수 0이면 자동 삭제?
