package com.noldaga.controller.response;

import com.noldaga.domain.CommentDto;
import com.noldaga.domain.FeedDto;
import com.noldaga.domain.FeedTagDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
public class FeedResponse {

    private Long id;
    private String title;
    private String content;
    private UserProfileResponse userProfileResponse;
    private Long GroupId;
    private int range;
    private String ModDate;
    private String RegDate;
    private Long totalView;
    private Long totalLike;
    private Long totalComment;
    private List<CommentDto> commentList;
    private List<FeedTagDto> feedTagDtoList;

    public static FeedResponse fromFeedDto(FeedDto feedDto) {
        return new FeedResponse(feedDto.getId(),
                feedDto.getTitle(),
                feedDto.getContent(),
                UserProfileResponse.fromUserDto(feedDto.getUserDto()),
                feedDto.getGroupId(),
                feedDto.getRange(),
                feedDto.getModDate(),
                feedDto.getRegDate(),
                feedDto.getTotalView(),
                feedDto.getTotalLike(),
                feedDto.getTotalComment(),
                feedDto.getCommentList(),
                feedDto.getFeedTagDtoList()
        );
    }
}
