package com.noldaga.controller.response;

import com.noldaga.domain.UserDto;
import com.noldaga.domain.UserSimpleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowResponse {
    private Long id;
    private String msg;
    private UserSimpleDto follower;
    private UserSimpleDto following;
    private Boolean both;

    public FollowResponse(Long id,String msg, UserDto fer, UserDto fwing, Boolean both){
        this.id = id;
        this.msg = msg;
        this.follower = UserSimpleDto.fromUserDto(fer);
        this.following = UserSimpleDto.fromUserDto(fwing);
        this.both = both;
    }

    public FollowResponse(String msg){
        this.msg = msg;
    }

}
