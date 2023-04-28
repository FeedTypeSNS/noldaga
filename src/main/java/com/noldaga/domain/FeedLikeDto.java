package com.noldaga.domain;

import com.noldaga.domain.entity.FeedLike;
import com.noldaga.domain.entity.FeedTag;
import com.noldaga.domain.userdto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class FeedLikeDto {

    private Long id;
    private FeedDto feedDto;
    private UserDto userDto;

    private FeedLikeDto(Long id, FeedDto feedDto, UserDto userDto) {
        this.id = id;
        this.feedDto = feedDto;
        this.userDto = userDto;
    }

    public static FeedLikeDto fromEntity(FeedLike feedLike) {
        return new FeedLikeDto(
                feedLike.getId(),
                FeedDto.fromEntity(feedLike.getFeed()),
                UserDto.fromEntity(feedLike.getUser())
        );
    }
}
