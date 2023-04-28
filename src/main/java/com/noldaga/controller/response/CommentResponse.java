package com.noldaga.controller.response;

import com.noldaga.domain.CommentDto;
import com.noldaga.domain.FeedDto;
import com.noldaga.domain.FeedTagDto;
import com.noldaga.domain.userdto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Getter
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private UserResponse userResponse;
    private String content;
    private String regDate;
    private String modDate;
    private Long totalLike;

    public static CommentResponse fromCommentDto(CommentDto commentDto) {
        return new CommentResponse(commentDto.getId(),
                UserResponse.fromUserDto(commentDto.getUserDto()),
                commentDto.getContent(),
                commentDto.getModDate(),
                commentDto.getRegDate(),
                commentDto.getTotalLike()
        );
    }
}
