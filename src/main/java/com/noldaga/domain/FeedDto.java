package com.noldaga.domain;

import com.noldaga.domain.entity.Comment;
import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.userdto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class FeedDto {
    private Long id;
    private String title;
    private String content;
    private UserDto userDto;
    private Long groupId;
    private int range;
    private String modDate;
    private String regDate;
    private Long totalView;
    private Long totalLike;
    private Long totalComment;
    private List<CommentDto> commentList;
    private List<FeedTagDto> feedTagDtoList;

    private FeedDto(Long id, String title, String content, UserDto userDto, Long groupId, int range, LocalDateTime modDate, LocalDateTime regDate, Long totalView, Long totalLike, Long totalComment, List<CommentDto> commentList, List<FeedTagDto> feedTagDtoList) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userDto = userDto;
        this.groupId = groupId;
        this.range = range;
        this.modDate = modDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.regDate = regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.totalView = totalView;
        this.totalLike = totalLike;
        this.totalComment = totalComment;
        this.commentList = commentList;
        this.feedTagDtoList = feedTagDtoList;
    }

    private FeedDto(Long id, String title, String content, UserDto userDto, Long groupId, int range, LocalDateTime modDate, LocalDateTime regDate, Long totalView, Long totalLike, Long totalComment, List<FeedTagDto> feedTagDtoList) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userDto = userDto;
        this.groupId = groupId;
        this.range = range;
        this.modDate = modDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.regDate = regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.totalView = totalView;
        this.totalLike = totalLike;
        this.totalComment = totalComment;
        this.feedTagDtoList = feedTagDtoList;
    }

    public static FeedDto fromEntity(Feed feed) {
        return new FeedDto(
                feed.getId(),
                feed.getTitle(),
                feed.getContent(),
                UserDto.fromEntity(feed.getUser()),
                feed.getGroupId(),
                feed.getRange(),
                feed.getModDate(),
                feed.getRegDate(),
                feed.getTotalView(),
                feed.getLikeCount(),
                feed.getCommentCount(),
                CommentDto.listFromEntity(feed.getComment()),
                FeedTagDto.listFromEntity(feed.getFeedTags())
        );
    }

    //지연로딩 반영을 위해 댓글이 없는 dto생성
    public static FeedDto fromEntityWithoutComment(Feed feed) {
        return new FeedDto(
                feed.getId(),
                feed.getTitle(),
                feed.getContent(),
                UserDto.fromEntity(feed.getUser()),
                feed.getGroupId(),
                feed.getRange(),
                feed.getModDate(),
                feed.getRegDate(),
                feed.getTotalView(),
                feed.getLikeCount(),
                feed.getCommentCount(),
                FeedTagDto.listFromEntity(feed.getFeedTags())
        );
    }

}
