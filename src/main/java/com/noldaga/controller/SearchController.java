package com.noldaga.controller;

import com.noldaga.controller.response.FeedResponse;
import com.noldaga.controller.response.HashTagResponse;
import com.noldaga.controller.response.Response;
import com.noldaga.controller.response.UserResponse;
import com.noldaga.domain.FeedDto;
import com.noldaga.domain.GroupDto;
import com.noldaga.domain.HashTagDto;
import com.noldaga.domain.UserSimpleDto;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/api/search/feed/{query}/{page}")
    public Response<List<FeedResponse>> searchFeed(@PathVariable String query,@PathVariable int page, Authentication authentication){
        List<FeedDto> feedDtoList = searchService.getMatchedFeed(query,page,authentication.getName());

        List<FeedResponse> feedResponseList = new ArrayList<>();
        feedDtoList.forEach(feedDto->{
            feedResponseList.add(FeedResponse.fromFeedDto(feedDto));
        });

        return Response.success(feedResponseList);
    }

    @GetMapping("/api/search/user/{query}")
    public Response<List<UserResponse>> searchUser(@PathVariable String query, Authentication authentication){
        List<UserDto> userDtoList = searchService.getMatchedUser(query,authentication.getName());

        List<UserResponse> userResponseList = new ArrayList<>();
        userDtoList.forEach(userDto->{
            userResponseList.add(UserResponse.fromUserDto(userDto));
        });

        return Response.success(userResponseList);
    }

    @GetMapping("/api/search/hashTag/{query}/{page}")
    public Response<List<HashTagResponse>> searchHashTag(@PathVariable String query, @PathVariable int page, Authentication authentication){
        List<HashTagDto> hashTagDtoList = searchService.getMatchedHashTag(query,page,authentication.getName());

        List<HashTagResponse> hashTagResponseList = new ArrayList<>();
        hashTagDtoList.forEach(hashTagDto->{
            hashTagResponseList.add(HashTagResponse.fromHashTagDto(hashTagDto));
        });

        return Response.success(hashTagResponseList);
    }

    @GetMapping("/api/search/group/{query}")
    public Response<List<GroupDto>> searchHashTag(@PathVariable String query, Authentication authentication){
        List<GroupDto> groupDtoList = searchService.getMatchedGroup(query,authentication.getName());
        return Response.success(groupDtoList);
    }

}
