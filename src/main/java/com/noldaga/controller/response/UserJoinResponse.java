package com.noldaga.controller.response;

import com.noldaga.domain.UserDto;
import com.noldaga.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter //스프링이 json 으로 변환해줄때 필요
public class UserJoinResponse {

    private Long id;
    private String username;
    private UserRole role;

    public static UserJoinResponse fromUserDto(UserDto userDto) {
        return new UserJoinResponse(userDto.getId(), userDto.getUsername(), userDto.getRole());
    }


}
