package com.noldaga.domain;

import com.noldaga.domain.entity.Comment;
import com.noldaga.domain.entity.Feed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDto {
    private Long id;
    //private FeedDto feedDto;
    private UserDto userDto;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    private CommentDto(Long id, String content, UserDto userDto, LocalDateTime modDate, LocalDateTime regDate) {
            this.id = id;
            this.content = content;
            //this.feedDto = feedDto;
            this.userDto = userDto;
            this.modDate = modDate;
            this.regDate = regDate;
    }

    public static CommentDto fromEntity(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                UserDto.fromEntity(comment.getUser()),
                comment.getModDate(),
                comment.getRegDate()
        );
    }

    public static List<CommentDto> listFromEntity(List<Comment> commentList) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        commentList.forEach(comment -> {
            CommentDto commentDto = CommentDto.fromEntity(comment);
            commentDtoList.add(commentDto);
        });
        return commentDtoList;
    }
}
