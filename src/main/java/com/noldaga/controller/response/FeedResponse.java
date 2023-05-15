package com.noldaga.controller.response;

import com.noldaga.domain.CommentDto;
import com.noldaga.domain.FeedDto;
import com.noldaga.domain.FeedTagDto;
import com.noldaga.domain.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
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
    private LocalDateTime DelDate;
    private Long totalView;
    private Long totalLike;
    private Long totalComment;
    private List<CommentResponse> commentList;
    private List<FeedTagDto> feedTagDtoList;
    private List<ImageDto> imageDtoList;

    public static FeedResponse fromFeedDto(FeedDto feedDto) {
        return new FeedResponse(
                feedDto.getId(),
                feedDto.getTitle(),
                feedDto.getContent(),
                UserResponse.fromUserDto(feedDto.getUserDto()),
                feedDto.getGroupId(),
                feedDto.getRange(),
                feedDto.getModDate(),
                feedDto.getRegDate(),
                feedDto.getDelDate(),
                feedDto.getTotalView(),
                feedDto.getTotalLike(),
                feedDto.getTotalComment(),
                CommentResponse.fromCommentDtoList(feedDto.getCommentList()),
                feedDto.getFeedTagDtoList(),
                feedDto.getImageDtoList()
        );
    }

}
