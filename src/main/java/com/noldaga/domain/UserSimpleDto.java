package com.noldaga.domain;

import com.noldaga.domain.entity.User;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.domain.userdto.UserRole;
import lombok.Getter;

@Getter
public class UserSimpleDto {
    private Long id;
    private String username;
    private UserRole role;
    //추후에 여기에 nickname, profile_img_url 등 여러가지 추가되야함

    private UserSimpleDto(Long id, String username, UserRole role){
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public static UserSimpleDto fromEntity(User entity){
        return new UserSimpleDto(
                entity.getId(),
                entity.getUsername(),
                entity.getRole()
        );
    }

    public static UserSimpleDto fromUserDto(UserDto entity){
        return new UserSimpleDto(
                entity.getId(),
                entity.getUsername(),
                entity.getRole()
        );
    }
}
//유저리스트 반환 (프론트에서 간단한 사용될 정보만)