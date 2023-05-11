package com.noldaga.controller.response;

import com.noldaga.domain.FeedLikeDto;
import com.noldaga.domain.HashTagDto;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class HashTagResponse {

    private Long id;
    private String tagName;

    public static HashTagResponse fromHashTagDto(HashTagDto hashTagDto) {
        return new HashTagResponse(hashTagDto.getId(), hashTagDto.getTagName());
    }
}
