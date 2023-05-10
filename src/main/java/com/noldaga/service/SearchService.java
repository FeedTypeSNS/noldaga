package com.noldaga.service;

import com.noldaga.domain.FeedDto;
import com.noldaga.domain.HashTagDto;
import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.HashTag;
import com.noldaga.domain.entity.User;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.Feed.FeedRepository;
import com.noldaga.repository.HashTag.HashTagRepository;
import com.noldaga.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final HashTagRepository hashTagRepository;

    public List<FeedDto> getMatchedFeed(String query,int page,String username){
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        Pageable pageable = PageRequest.of(page,10);
        Page<Feed> feedListPagination = feedRepository.findAllBySearch(query,pageable);

        List<FeedDto> feedDtoList = new ArrayList<>();
        feedListPagination.getContent().forEach(feed -> {
            FeedDto feedDto = FeedDto.fromEntityWithoutComment(feed);
            feedDtoList.add(feedDto);
        });
        return feedDtoList;
    }

    public List<UserDto> getMatchedUser(String query, String username){
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        Pageable pageable = PageRequest.of(0,20);
        List<User> userList = userRepository.findAllBySearch(query);

        List<UserDto> userDtoList = new ArrayList<>();
        userList.forEach(u -> {
            UserDto userDto = UserDto.fromEntity(u);
            userDtoList.add(userDto);
        });
        return userDtoList;
    }

    public List<HashTagDto> getMatchedHashTag(String query, int page, String username){
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        Pageable pageable = PageRequest.of(0,20);
        Page<HashTag> feedListPagination = hashTagRepository.findAllBySearch(query,pageable);

        List<HashTagDto> hashTagDtoList = new ArrayList<>();
        feedListPagination.getContent().forEach(hashTag -> {
            HashTagDto hashTagDto = HashTagDto.fromEntity(hashTag);
            hashTagDtoList.add(hashTagDto);
        });
        return hashTagDtoList;
    }
}
