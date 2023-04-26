package com.noldaga.controller.response;

import com.noldaga.domain.CommentDto;
import com.noldaga.domain.FeedDto;
import com.noldaga.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@AllArgsConstructor
public class FeedResponse {

    private Long id;
    private String title;
    private String content;
    private UserResponse userResponse;
    private LocalDateTime ModDate;
    private LocalDateTime RegDate;
    private List<CommentDto> commentList;

    public static FeedResponse fromFeedDto(FeedDto feedDto) {
        return new FeedResponse(feedDto.getId(),
                feedDto.getTitle(),
                feedDto.getContent(),
                UserResponse.fromUserDto(feedDto.getUserDto()),
                feedDto.getModDate(),
                feedDto.getRegDate(),
                feedDto.getCommentList()
        );
    }
}
