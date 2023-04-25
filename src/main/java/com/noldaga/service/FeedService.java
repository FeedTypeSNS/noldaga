package com.noldaga.service;


import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.domain.FeedDto;
import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.User;
import com.noldaga.repository.FeedRepository;
import com.noldaga.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(String title, String content, String username) {

        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //post feed
        feedRepository.save(Feed.of(title, content, user));
    }

    @Transactional
    public FeedDto modify(String title, String content, String username, Long feedId) {
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


        //modify
        feed.setTitle(title);
        feed.setContent(content);

        Feed result = feedRepository.saveAndFlush(feed);
        return FeedDto.fromEntity(result);
    }

    @Transactional
    public void delete(String username, Long feedId) {
        //유저확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", username)));

        //피드확인
        Feed feed = feedRepository.findById(feedId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s is not founded", feedId)));

        //권한확인
        if (feed.getUser() != user) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,String.format("%s ha no permission with %s",username,feedId));
        }

        //delete
        feedRepository.delete(feed);
    }

    public Page<FeedDto> list(Pageable pageable) {
        return feedRepository.findAll(pageable).map(FeedDto::fromEntity);
    }

    public Page<FeedDto> myList(String username,Pageable pageable) {

        //유저확인
        User user= userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        return feedRepository.findAllByUser(user,pageable).map(FeedDto::fromEntity);
    }






    //피드가 존재하는지
    private Feed getFeedOrException(Long feedId){
        return null;
    }

    //유저가 존재하는지
    private User getUserOrException(String username){
        return null;
    }

}
