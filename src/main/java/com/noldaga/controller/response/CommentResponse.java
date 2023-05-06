package com.noldaga.controller.response;

import com.noldaga.domain.CommentDto;
import com.noldaga.domain.FeedTagDto;
import com.noldaga.domain.entity.FeedTag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
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

    public static List<CommentResponse> fromCommentDtoList(List<CommentDto> commentDtoList) {
        if(commentDtoList == null)
            return null;
        List<CommentResponse> commentResponseList = new ArrayList<>();
        commentDtoList.forEach(commentDto -> {
            CommentResponse commentResponse = CommentResponse.fromCommentDto(commentDto);
            commentResponseList.add(commentResponse);
        });
        return commentResponseList;
    }
}
