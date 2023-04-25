package com.noldaga.domain.Mapper.Feed;


import com.noldaga.domain.FeedDto;
import com.noldaga.domain.Mapper.GenericMapper;
import com.noldaga.domain.entity.Feed;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FeedMapper extends GenericMapper<Feed, FeedDto> {
    FeedMapper INSTANCE = Mappers.getMapper(FeedMapper.class);

    @Mapping(source="user.id", target="userId")
    FeedDto toDto(Feed feed);

    List<FeedDto> toDtoList(List<Feed> feedList);

    @Mapping(target="user", ignore = true)
    Feed toEntity(FeedDto feedDto);

    List<Feed> toEntityList(List<FeedDto> feedDtoList); //users정보는...어떻게..
}
