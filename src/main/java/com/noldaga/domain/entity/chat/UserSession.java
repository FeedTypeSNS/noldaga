package com.noldaga.domain.entity.chat;

import com.noldaga.domain.entity.User;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter
@ToString
@Entity
public class UserSession {
    @Id
    private String sessionId; //WebSocket 연결 세션을 식별하는 고유한 식별자

    @ManyToOne
    private User user;  //현재 페이지에 들어와 있는 사용자

    protected UserSession(){}
    public UserSession(String id, User user){
        this.sessionId = id;
        this.user = user;
    }

    public static UserSession of(String id, User user){return new UserSession(id, user);}
}
