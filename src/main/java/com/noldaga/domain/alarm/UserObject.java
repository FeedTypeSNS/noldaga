package com.noldaga.domain.alarm;

import com.noldaga.domain.entity.User;
import com.noldaga.domain.userdto.UserDto;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class UserObject implements AlarmObject{


    private Long id;
    private String username;
    private String nickname;

    private final String OBJECT_TYPE = "USER";

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getNameOnAlarm() {
        return this. nickname + " (" + this. username + ")";

    }

    @Override
    public String getObjectType() {
        return this.OBJECT_TYPE;
    }

    public static UserObject from(User user){
        return new UserObject(user.getId(), user.getUsername(), user.getNickname());
    }

    public static UserObject from(UserDto userDto){
        return new UserObject(userDto.getId(), userDto.getUsername(), userDto.getNickname());
    }

}
