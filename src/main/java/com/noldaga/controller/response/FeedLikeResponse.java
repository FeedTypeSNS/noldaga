package com.noldaga.controller.response;

import com.noldaga.domain.CommentDto;
import com.noldaga.domain.FeedDto;
import com.noldaga.domain.FeedLikeDto;
import com.noldaga.domain.FeedTagDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
public class FeedLikeResponse {

    private Long id;
    private FeedResponse feedResponse;
    private UserResponse userResponse;

    public static FeedLikeResponse fromFeedLikeDto(FeedLikeDto feedLikeDto) {
        return new FeedLikeResponse(feedLikeDto.getId(),
                FeedResponse.fromFeedDto(feedLikeDto.getFeedDto()),
                UserResponse.fromUserDto(feedLikeDto.getUserDto())
        );
    }
}
