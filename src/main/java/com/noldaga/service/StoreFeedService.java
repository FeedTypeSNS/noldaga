package com.noldaga.service;

import com.noldaga.domain.CommentLikeDto;
import com.noldaga.domain.FeedLikeDto;
import com.noldaga.domain.StoreFeedDto;
import com.noldaga.domain.entity.*;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.Comment.CommentRepository;
import com.noldaga.repository.CommentLikeRepository;
import com.noldaga.repository.Feed.FeedRepository;
import com.noldaga.repository.FeedLikeRepository;
import com.noldaga.repository.StoreFeedRepository;
import com.noldaga.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class StoreFeedService {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final StoreFeedRepository storeFeedRepository;

    @Transactional
    public int feedSaveChech(Long feedId, String username){
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));
        //피드확인
        Feed feed = feedRepository.findById(feedId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s not founded", feedId)));

        return storeFeedRepository.findByFeedIdAndUsername(feedId,user.getId());
    }

    @Transactional
    public StoreFeedDto feedSave(Long feedId, String username){
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));
        //피드확인
        Feed feed = feedRepository.findById(feedId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s not founded", feedId)));

        StoreFeed storeFeed = StoreFeed.of(feed,user);
        StoreFeed storedFeed = storeFeedRepository.save(storeFeed);

        return StoreFeedDto.fromEntity(storedFeed);
    }

    @Transactional
    public void feedSaveDelete(Long feedId, String username){
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));
        //피드확인
        Feed feed = feedRepository.findById(feedId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s not founded", feedId)));

        storeFeedRepository.deleteByFeedIdAndUserId(feedId,user.getId());
    }


}
