package com.noldaga.controller.response;

import com.noldaga.domain.CommentLikeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CommentLikeResponse {

    private Long id;
    private CommentResponse commentResponse;
    private UserProfileResponse userProfileResponse;

    public static CommentLikeResponse fromCommentLikeDto(CommentLikeDto commentLikeDto) {
        return new CommentLikeResponse(commentLikeDto.getId(),
                CommentResponse.fromCommentDto(commentLikeDto.getCommentDto()),
                UserProfileResponse.fromUserDto(commentLikeDto.getUserDto())
        );
    }
}
