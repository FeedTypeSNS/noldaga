package com.noldaga.controller.response;

import com.noldaga.domain.FeedLikeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class FeedLikeResponse {

    private Long id;
    private FeedResponse feedResponse;
    private UserProfileResponse userProfileResponse;

    public static FeedLikeResponse fromFeedLikeDto(FeedLikeDto feedLikeDto) {
        return new FeedLikeResponse(feedLikeDto.getId(),
                FeedResponse.fromFeedDto(feedLikeDto.getFeedDto()),
                UserProfileResponse.fromUserDto(feedLikeDto.getUserDto())
        );
    }
}
