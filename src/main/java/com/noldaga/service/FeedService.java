package com.noldaga.service;


import com.noldaga.controller.request.FeedCreateRequest;
import com.noldaga.controller.request.FeedModifyRequest;
import com.noldaga.domain.UserSimpleDto;
import com.noldaga.domain.entity.Comment;
import com.noldaga.domain.entity.Follow;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.domain.FeedDto;
import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.User;
import com.noldaga.repository.Feed.FeedRepository;
import com.noldaga.repository.FollowRepository;
import com.noldaga.repository.StoreFeedRepository;
import com.noldaga.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final HashTagService hashTagService;


    @Transactional
    public FeedDto create(FeedCreateRequest request, String username) {
        //저장정보
        String title = request.getTitle();
        String content = request.getContent();
        long groupId = request.getGroupId();
        int range = request.getRange();

        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //post feed
        Feed feed = feedRepository.save(Feed.of(title, content, groupId, range, user));
        FeedDto feedDto = FeedDto.fromEntity(feed);
        //해시태그저장
        hashTagService.extractHashTag(feedDto.getContent(), feedDto.getId());

        return feedDto;
    }

    @Transactional
    public List<FeedDto> getMainFeed(int page,String username){
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        Pageable pageable = PageRequest.of(page,5);
        Page<Feed> feedListPagination = feedRepository.MainFeedWithFollow(user.getId(), pageable);

        List<FeedDto> feedDtoList = new ArrayList<>();

        feedListPagination.getContent().forEach(feed -> {
            FeedDto feedDto = FeedDto.fromEntityWithoutComment(feed);
            feedDtoList.add(feedDto);
        });
        return feedDtoList;
    }

    @Transactional
    public List<FeedDto> getGroupFeed(int page,String username, Long groupId){
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        Pageable pageable = PageRequest.of(page,5);
        Page<Feed> feedListPagination = feedRepository.GroupPageFeed(groupId, pageable);

        List<FeedDto> feedDtoList = new ArrayList<>();

        feedListPagination.getContent().forEach(feed -> {
            FeedDto feedDto = FeedDto.fromEntity(feed);
            feedDtoList.add(feedDto);
        });
        return feedDtoList;
    }

    @Transactional
    public List<FeedDto> getMyPageFeed(int page, Long pageOwnerId, String username){
        //회원가입된 user인지 확인
        User loginUser = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //lmit은 변경예정
        Pageable pageable = PageRequest.of(page,5);
        Page<Feed> feedListPagination = null;

        //공개범위 1(부분공개),0(전체)
        if(loginUser.getId() == pageOwnerId) {
            feedListPagination = feedRepository.MyPageFeed(pageOwnerId, pageable);
        }
        //다른 사람의 마이페이지 방문이라면
        else {
            Optional<Follow> follow = followRepository.findByFollowerAndFollowing(pageOwnerId,loginUser.getId());
            if(follow.isPresent()) feedListPagination = feedRepository.MyPageFeed(pageOwnerId, pageable);
            else feedListPagination = feedRepository.MyPageFeedOnlyPublic(pageOwnerId, pageable);
        }
        List<FeedDto> feedDtoList = new ArrayList<>();

        feedListPagination.getContent().forEach(feed -> {
            FeedDto feedDto = FeedDto.fromEntity(feed);
            feedDtoList.add(feedDto);
        });
        return feedDtoList;
    }

    @Transactional
    public List<FeedDto> getExploreFeed(int page,String username){
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        Pageable pageable = PageRequest.of(page,100);
        Page<Feed> feedListPagination = feedRepository.ExplorePageFeed(pageable);

        List<FeedDto> feedDtoList = new ArrayList<>();

        feedListPagination.getContent().forEach(feed -> {
            FeedDto feedDto = FeedDto.fromEntity(feed);
            feedDtoList.add(feedDto);
        });
        return feedDtoList;
    }

    @Transactional
    public FeedDto getDetailFeed(Long feedId,String username){
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //피드 확인
        Feed feed = feedRepository.findByIdWithComment(feedId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s not founded", feedId)));
        feed.plusViewCount();

        FeedDto feedDto = FeedDto.fromEntity(feed);
        return feedDto;
    }

    @Transactional
    public List<FeedDto> getMySavedFeed(int page,String username){
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        Pageable pageable = PageRequest.of(page,100);
        Page<Feed> feedListPagination = feedRepository.MyStoredFeed(user.getId(),pageable);

        List<FeedDto> feedDtoList = new ArrayList<>();

        feedListPagination.getContent().forEach(feed -> {
            FeedDto feedDto = FeedDto.fromEntity(feed);
            feedDtoList.add(feedDto);
        });
        return feedDtoList;
    }

    @Transactional
    public FeedDto modify(FeedModifyRequest request, Long feedId, String username) {
        //유저확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //피드확인
        Feed feed = feedRepository.findById(feedId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s not founded", feedId)));

        //권한있나확인
        if (feed.getUser() != user) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", username, feedId));
        }

        feed.change(request.getTitle(), request.getContent(), request.getGroupId(), request.getRange());

        feedRepository.save(feed);
        FeedDto feedDto = FeedDto.fromEntity(feed);

        //해시태그 지우기
        hashTagService.deleteHashTag(feedId);
        //다시저장하기
        hashTagService.extractHashTag(request.getContent(), feedId);
        return feedDto;
    }

    @Transactional
    public void delete(Long feedId,String username) {
        //유저확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", username)));

        //피드확인
        Feed feed = feedRepository.findById(feedId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s is not founded", feedId)));

//        //권한확인
//        if (feed.getUser() != user) {
//            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,String.format("%s ha no permission with %s",username,feedId));
//        }

        //해시태그 지우기
        hashTagService.deleteHashTag(feedId);
        //delete
        feedRepository.delete(feed);
    }




}
