package com.noldaga.domain;

import com.noldaga.domain.entity.Comment;
import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.userdto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter //response 로 변환해줄때 필요 등
public class FeedDto {
    private Long id;
    private String title;
    private String content;
    private UserDto userDto;
    private Long groupId;
    private int range;
    private LocalDateTime modDate;
    private LocalDateTime regDate;
    private Long totalView;
    private List<CommentDto> commentList;

    private FeedDto(Long id, String title, String content, UserDto userDto, Long groupId, int range, LocalDateTime modDate, LocalDateTime regDate, Long totalView, List<CommentDto> commentList) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userDto = userDto;
        this.groupId = groupId;
        this.range = range;
        this.modDate = modDate;
        this.regDate = regDate;
        this.totalView = totalView;
        this.commentList = commentList;
    }

    public static FeedDto fromEntity(Feed feed) {
        return new FeedDto(
                feed.getId(),
                feed.getTitle(),
                feed.getContent(),
                UserDto.fromEntity(feed.getUser()), //user 엔티티가 아니라 userDto 이어야함
                feed.getGroupId(),
                feed.getRange(),
                feed.getModDate(),
                feed.getRegDate(),
                feed.getTotalView(),
                CommentDto.listFromEntity(feed.getComment())
        );
    }

}
