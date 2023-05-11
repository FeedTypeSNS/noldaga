package com.noldaga.domain;

import com.noldaga.domain.entity.Follow;
import com.noldaga.domain.entity.GroupMember;
import com.noldaga.domain.userdto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class GroupMemberDto {

    private Long id;
    private GroupDto groupDto;
    private UserDto userDto;
    private int favor;

    private GroupMemberDto(Long id, GroupDto groupDto, UserDto userDto, int favor){
        this.id = id;
        this.groupDto = groupDto;
        this.userDto = userDto;
        this.favor = favor;
    }

    public static GroupMemberDto fromEntity(GroupMember groupMember){
        return new GroupMemberDto(
                groupMember.getId(),
                GroupDto.fromEntity(groupMember.getGroup()),
                UserDto.fromEntity(groupMember.getUser()),
                groupMember.getFavor()
        );
    }
}
