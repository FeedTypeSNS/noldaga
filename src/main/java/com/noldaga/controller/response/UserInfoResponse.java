package com.noldaga.controller.response;


import com.noldaga.domain.userdto.Gender;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.domain.userdto.UserRole;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserInfoResponse {

    private Long id;
    private String username;
    private String nickname;
    private String email;

    private String profileImageUrl;
    private String profileMessage;

    private Long totalFollower;
    private Long totalFollowing;

    private Gender gender;
    private LocalDate birthday;
    private UserRole role;

    private UserInfoResponse(Long id, String username, String nickname, String email,
                            String profileImageUrl, String profileMessage, Long totalFollower, Long totalFollowing,
                            Gender gender, LocalDate birthday, UserRole role) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.profileMessage = profileMessage;
        this.totalFollower = totalFollower;
        this.totalFollowing = totalFollowing;
        this.gender = gender;
        this.birthday = birthday;
        this.role = role;
    }

    public static UserInfoResponse fromUserDto(UserDto userDto){
        return new UserInfoResponse(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getNickname(),
                userDto.getEmail(),
                userDto.getProfileImageUrl(),
                userDto.getProfileMessage(),
                userDto.getTotalFollower(),
                userDto.getTotalFollowing(),
                userDto.getGender(),
                userDto.getBirthday(),
                userDto.getRole()
                );
    }
}
