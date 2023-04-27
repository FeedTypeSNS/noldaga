package com.noldaga.domain;

import com.noldaga.domain.entity.Group;
import com.noldaga.domain.userdto.UserDto;
import lombok.*;

@NoArgsConstructor
@Setter
@Getter
public class GroupDto {

    private Long id;
    private String name;
    private int open;
    private String pw;
    private String profile_url;
    private String intro;

    private UserDto userDto;

    private GroupDto(Long id, String name, String profile_url, String pw, String intro, int open, UserDto userDto) {
        this.id = id;
        this.name = name;
        this.profile_url = profile_url;
        this.pw = pw;
        this.intro = intro;
        this.open = open;
        this.userDto = userDto;
    }

    public static GroupDto fromEntity(Group group) {
        return new GroupDto(
                group.getId(),
                group.getName(),
                group.getProfile_url(),
                group.getPw(),
                group.getIntro(),
                group.getOpen(),
                UserDto.fromEntity(group.getUser()) //user 엔티티가 아니라 userDto 이어야함
        );
    }
}