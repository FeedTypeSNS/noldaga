package com.noldaga.controller.response;

import com.noldaga.domain.FeedDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class FeedResponse {

    private Long id;
    private String title;
    private String content;
    private UserResponse userResponse;
    private LocalDateTime ModDate;
    private LocalDateTime RegDate;


    public static FeedResponse fromFeedDto(FeedDto feedDto) {
        return new FeedResponse(feedDto.getId(),
                feedDto.getTitle(),
                feedDto.getContent(),
                UserResponse.fromUserDto(feedDto.getUserDto()),
                feedDto.getModDate(),
                feedDto.getRegDate()
        );
    }
}
