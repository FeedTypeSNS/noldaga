package com.noldaga.controller.response;

import com.noldaga.domain.FeedDto;
import com.noldaga.domain.FeedTagDto;
import com.noldaga.domain.ImageDto;
import com.noldaga.domain.UserSimpleDto;
import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.Image;
import com.noldaga.domain.userdto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@AllArgsConstructor
public class InterestFeedResponse {
    private Long id;
    private String title;
    private String content;
    private String modiDate;
    private LocalDateTime date;
    private UserSimpleDto userSimpleDto;
    private Long totalView;
    private Long totalLike;
    private Long totalComment;
    private String imageUrl;
    private List<FeedTagDto> feedTagDtoList;
    private String end;

/*    public static InterestFeedResponse fromFeedDto(FeedDto feedDto){
        return new InterestFeedResponse(
                feedDto.getId(),
                feedDto.getTitle(),
                UserSimpleDto.fromUserDto(feedDto.getUserDto()),
                feedDto.getTotalView(),
                feedDto.getTotalLike(),
                feedDto.getTotalComment(),
                feedDto.getImageDtoList()
        );
    }*/

    public static InterestFeedResponse fromEntity(Feed feedDto, String image, List<FeedTagDto> feedTagDtoList, String end){
        return new InterestFeedResponse(
                feedDto.getId(),
                feedDto.getTitle(),
                feedDto.getContent(),
                feedDto.getModDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                feedDto.getModDate(),
                UserSimpleDto.fromEntity(feedDto.getUser()),
                feedDto.getTotalView(),
                feedDto.getTotalView(),
                feedDto.getLikeCount(),
                image,
                feedTagDtoList,
                end
        );
    }
}
