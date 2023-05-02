package com.noldaga.domain;

import com.noldaga.domain.entity.FeedLike;
import com.noldaga.domain.entity.StoreFeed;
import com.noldaga.domain.userdto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class StoreFeedDto {

    private Long id;
    private FeedDto feedDto;
    private UserDto userDto;

    private StoreFeedDto(Long id, FeedDto feedDto, UserDto userDto) {
        this.id = id;
        this.feedDto = feedDto;
        this.userDto = userDto;
    }

    public static StoreFeedDto fromEntity(StoreFeed storeFeed) {
        return new StoreFeedDto(
                storeFeed.getId(),
                FeedDto.fromEntity(storeFeed.getFeed()),
                UserDto.fromEntity(storeFeed.getUser())
        );
    }
}
