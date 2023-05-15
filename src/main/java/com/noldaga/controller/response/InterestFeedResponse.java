package com.noldaga.controller.response;

import com.noldaga.domain.FeedDto;
import com.noldaga.domain.ImageDto;
import com.noldaga.domain.UserSimpleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InterestFeedResponse {
    private Long id;
    private String title;
    private UserSimpleDto userSimpleDto;
    private Long totalView;
    private Long totalLike;
    private Long totalComment;
    private String imgUrl;

    public static InterestFeedResponse fromFeedDto(FeedDto feedDto){
        return new InterestFeedResponse(
                feedDto.getId(),
                feedDto.getTitle(),
                UserSimpleDto.fromUserDto(feedDto.getUserDto()),
                feedDto.getTotalView(),
                feedDto.getTotalLike(),
                feedDto.getTotalComment(),
                feedDto.getImageDtoList().get(0).getUrl()
        );
    }
}
