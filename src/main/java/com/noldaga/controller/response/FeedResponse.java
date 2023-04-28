package com.noldaga.controller.response;

import com.noldaga.domain.CommentDto;
import com.noldaga.domain.FeedDto;
import com.noldaga.domain.FeedTagDto;
import com.noldaga.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Getter
@AllArgsConstructor
public class FeedResponse {

    private Long id;
    private String title;
    private String content;
    private UserResponse userResponse;
    private Long GroupId;
    private int range;
    private String ModDate;
    private String RegDate;
    private Long totalView;
    private List<CommentDto> commentList;
    private List<FeedTagDto> feedTagDtoList;

    public static FeedResponse fromFeedDto(FeedDto feedDto) {
        return new FeedResponse(feedDto.getId(),
                feedDto.getTitle(),
                feedDto.getContent(),
                UserResponse.fromUserDto(feedDto.getUserDto()),
                feedDto.getGroupId(),
                feedDto.getRange(),
                feedDto.getModDate(),
                feedDto.getRegDate(),
                feedDto.getTotalView(),
                feedDto.getCommentList(),
                feedDto.getFeedTagDtoList()
        );
    }
}
