package com.noldaga.controller.response;

import com.noldaga.domain.CommentLikeDto;
import com.noldaga.domain.FeedLikeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CommentLikeResponse {

    private Long id;
    private CommentResponse commentResponse;
    private UserResponse userResponse;

    public static CommentLikeResponse fromCommentLikeDto(CommentLikeDto commentLikeDto) {
        return new CommentLikeResponse(commentLikeDto.getId(),
                CommentResponse.fromCommentDto(commentLikeDto.getCommentDto()),
                UserResponse.fromUserDto(commentLikeDto.getUserDto())
        );
    }
}
