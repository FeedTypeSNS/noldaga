package com.noldaga.domain.chatdto;

import com.noldaga.domain.entity.User;
import com.noldaga.domain.entity.chat.ChatSession;
import com.noldaga.domain.entity.chat.UserSession;
import com.noldaga.domain.userdto.UserDto;
import lombok.Getter;

@Getter
public class UserSessionDto {
    private String sessionId;
    private UserDto user;

    private UserSessionDto(String sessionId, UserDto user){
        this.sessionId = sessionId;
        this.user = user;
    }

    public static UserSessionDto fromEntity(UserSession user){
        return new UserSessionDto(
                user.getSessionId(),
                UserDto.fromEntity(user.getUser())
        );
    }
}
