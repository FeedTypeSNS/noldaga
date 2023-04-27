package com.noldaga.controller.response;


import com.noldaga.domain.userdto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String username;

    public static UserResponse fromUserDto(UserDto userDto) {
        return new UserResponse(userDto.getId(), userDto.getUsername());
    }
}
