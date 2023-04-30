package com.noldaga.domain;

import com.noldaga.domain.entity.Comment;
import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.FeedTag;
import com.noldaga.domain.entity.HashTag;
import com.noldaga.domain.userdto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class FeedTagDto {

    private Long id;
    private HashTagDto hashTagDto;

    private FeedTagDto(Long id, HashTagDto hashTagDto) {
        this.id = id;
        this.hashTagDto = hashTagDto;
    }

    public static FeedTagDto fromEntity(FeedTag feedTag) {
        return new FeedTagDto(
                feedTag.getId(),
                HashTagDto.fromEntity(feedTag.getHashTag())
        );
    }

    public static List<FeedTagDto> listFromEntity(List<FeedTag> feedTagList) {
        if(feedTagList == null)
            return null;
        List<FeedTagDto> feedTagDtoList = new ArrayList<>();
        feedTagList.forEach(feedTag -> {
            FeedTagDto feedTagDto = FeedTagDto.fromEntity(feedTag);
            feedTagDtoList.add(feedTagDto);
        });
        return feedTagDtoList;
    }
}
