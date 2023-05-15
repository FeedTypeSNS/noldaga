package com.noldaga.domain;

import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.FeedLike;
import com.noldaga.domain.entity.Image;
import com.noldaga.domain.userdto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ImageDto {

    private Long id;
    private String url;
    private int seq;

    private ImageDto(Long id, String url, int seq) {
        this.id = id;
        this.url = url;
        this.seq = seq;
    }

    public static ImageDto fromEntity(Image image) {
        return new ImageDto(
                image.getId(),
                image.getUrl(),
                image.getSeq()
        );
    }
}
