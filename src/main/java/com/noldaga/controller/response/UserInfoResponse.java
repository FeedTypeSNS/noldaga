package com.noldaga.controller.response;

import com.noldaga.domain.userdto.Gender;
import com.noldaga.domain.userdto.UserDto;
import lombok.Getter;

import java.time.LocalDate;


@Getter//이거 없으면 못보냄 역직렬화 못함
public class UserInfoResponse {

    private Long id;
    private String username;
    private String nickname;
    private Gender gender;
    private LocalDate birthday;
    private String email;


    private UserInfoResponse(Long id, String username, String nickname, Gender gender, LocalDate birthday, String email) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
    }

    public static UserInfoResponse fromUserDto(UserDto userDto) {
        return new UserInfoResponse(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getNickname(),
                userDto.getGender(),
                userDto.getBirthday(),
                userDto.getEmail()
        );
    }

}
