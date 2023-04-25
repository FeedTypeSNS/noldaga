package com.noldaga.domain;

import com.noldaga.domain.entity.Feed;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter //response 로 변환해줄때 필요 등
public class FeedDto {
    private Long id;
    private String title;
    private String content;
    private UserDto userDto;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

    private FeedDto(Long id, String title, String content, UserDto userDto, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userDto = userDto;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;
    }

    public static FeedDto fromEntity(Feed feed) {
        return new FeedDto(
                feed.getId(),
                feed.getTitle(),
                feed.getContent(),
                UserDto.fromEntity(feed.getUser()), //user 엔티티가 아니라 userDto 이어야함
                feed.getCreatedAt(),
                feed.getModifiedAt(),
                feed.getDeletedAt()
        );

    }

}
