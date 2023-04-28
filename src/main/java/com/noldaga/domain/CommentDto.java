package com.noldaga.domain;

import com.noldaga.domain.entity.Comment;
import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.userdto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private String regDate;
    private String modDate;
    private Long totalLike;

    private CommentDto(Long id, String content, UserDto userDto, LocalDateTime modDate, LocalDateTime regDate, Long totalLike) {
            this.id = id;
            this.content = content;
            //this.feedDto = feedDto;
            this.userDto = userDto;
            this.modDate = modDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.regDate = regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.totalLike = totalLike;
    }

    public static CommentDto fromEntity(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                UserDto.fromEntity(comment.getUser()),
                comment.getModDate(),
                comment.getRegDate(),
                comment.getTotalLike()
        );
    }

    public static List<CommentDto> listFromEntity(List<Comment> commentList) {
        if(commentList == null)
            return null;
        List<CommentDto> commentDtoList = new ArrayList<>();
        commentList.forEach(comment -> {
            CommentDto commentDto = CommentDto.fromEntity(comment);
            commentDtoList.add(commentDto);
        });
        return commentDtoList;
    }
}
