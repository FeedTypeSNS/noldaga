package com.noldaga.controller.response;

import com.noldaga.domain.FeedLikeDto;
import com.noldaga.domain.StoreFeedDto;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class StoreFeedResponse {

    private Long id;
    private FeedResponse feedResponse;
    private UserResponse userResponse;

    public static StoreFeedResponse fromFeedLikeDto(StoreFeedDto storeFeedDto) {
        return new StoreFeedResponse(storeFeedDto.getId(),
                FeedResponse.fromFeedDto(storeFeedDto.getFeedDto()),
                UserResponse.fromUserDto(storeFeedDto.getUserDto())
        );
    }
}
