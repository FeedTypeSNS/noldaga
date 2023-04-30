package com.noldaga.domain;

import com.noldaga.domain.entity.User;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.domain.userdto.UserRole;
import lombok.Getter;

@Getter
public class UserSimpleDto {
    private Long id;
    private String username;
    private String nickname;
    private String profileImageUrl;
    private String profileMessage;
    private UserRole role;
    //추후에 여기에 nickname, profile_img_url 등 여러가지 추가되야함

    private UserSimpleDto(Long id, String username, UserRole role, String nickname, String img, String msg){
        this.id = id;
        this.username = username;
        this.role = role;
        this.nickname = nickname;
        this.profileImageUrl = img;
        this.profileMessage = msg;
    }

    public static UserSimpleDto fromEntity(User entity){
        return new UserSimpleDto(
                entity.getId(),
                entity.getUsername(),
                entity.getRole(),
                entity.getNickname(),
                entity.getProfileImageUrl(),
                entity.getProfileMessage()
        );
    }

    public static UserSimpleDto fromUserDto(UserDto entity){
        return new UserSimpleDto(
                entity.getId(),
                entity.getUsername(),
                entity.getRole(),
                entity.getNickname(),
                entity.getProfileImageUrl(),
                entity.getProfileMessage()
        );
    }
}
//유저리스트 반환 (프론트에서 간단한 사용될 정보만)