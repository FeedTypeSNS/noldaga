package com.noldaga.domain;

import com.noldaga.domain.entity.Follow;
import lombok.Getter;

@Getter
public class FollowDto {
    private Long id;
    private UserDto following;
    private UserDto follower;
    private Boolean bothFollow;

    private FollowDto(Long id, UserDto following, UserDto follower, Boolean both){
        this.id = id;
        this.following = following;
        this.follower = follower;
        this.bothFollow = both;
    }

    public static FollowDto fromEntity(Follow follow){
        return new FollowDto(
                follow.getId(),
                UserDto.fromEntity(follow.getFollowing()),
                UserDto.fromEntity(follow.getFollower()),
                follow.isBothFollow()
        );
    }
}
