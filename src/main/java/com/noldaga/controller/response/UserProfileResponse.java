package com.noldaga.controller.response;

import com.noldaga.domain.userdto.UserDto;
import lombok.Getter;


@Getter
public class UserProfileResponse {

    private Long id;
    private String username;

    private String nickname;
    private String profileImageUrl;
    private String profileMessage;
    private Long totalFollower;
    private Long totalFollowing;

    private UserProfileResponse(Long id, String username, String nickname, String profileImageUrl, String profileMessage, Long totalFollower, Long totalFollowing) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.profileMessage = profileMessage;
        this.totalFollower = totalFollower;
        this.totalFollowing = totalFollowing;
    }

    public static UserProfileResponse fromUserDto(UserDto userDto){
        return new UserProfileResponse(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getNickname(),
                userDto.getProfileImageUrl(),
                userDto.getProfileMessage(),
                userDto.getTotalFollower(),
                userDto.getTotalFollowing()
        );
    }


}
