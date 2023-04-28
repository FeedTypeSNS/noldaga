package com.noldaga.domain;

import com.noldaga.domain.entity.CommentLike;
import com.noldaga.domain.entity.FeedLike;
import com.noldaga.domain.userdto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CommentLikeDto {

    private Long id;
    private CommentDto commentDto;
    private UserDto userDto;

    private CommentLikeDto(Long id, CommentDto commentDto, UserDto userDto) {
        this.id = id;
        this.commentDto = commentDto;
        this.userDto = userDto;
    }

    public static CommentLikeDto fromEntity(CommentLike commentLike) {
        return new CommentLikeDto(
                commentLike.getId(),
                CommentDto.fromEntity(commentLike.getComment()),
                UserDto.fromEntity(commentLike.getUser())
        );
    }
}
