package com.noldaga.domain;

import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.HashTag;
import com.noldaga.domain.userdto.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.NamedAttributeNode;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
public class HashTagDto {
    private Long id;
    private String tagName;

    private HashTagDto(Long id, String tagName) {
        this.id = id;
        this.tagName = tagName;
    }

    public static HashTagDto fromEntity(HashTag hashTag) {
        return new HashTagDto(
                hashTag.getId(),
                hashTag.getTagName()
        );
    }

}
