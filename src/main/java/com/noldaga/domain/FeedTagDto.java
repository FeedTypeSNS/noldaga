package com.noldaga.domain;

import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.FeedTag;
import com.noldaga.domain.entity.HashTag;
import com.noldaga.domain.userdto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

public class FeedTagDto {

    private Long id;
    private Long feedId;
    private HashTagDto hashTagDto;

    private FeedTagDto(Long id, Long feedId, HashTagDto hashTagDto) {
        this.id = id;
        this.feedId = feedId;
        this.hashTagDto = hashTagDto;
    }

    public static FeedTagDto fromEntity(FeedTag feedTag) {
        return new FeedTagDto(
                feedTag.getId(),
                feedTag.getFeedId(),
                HashTagDto.fromEntity(feedTag.getHashTag())
        );
    }
}
