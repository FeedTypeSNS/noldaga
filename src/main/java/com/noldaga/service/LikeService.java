package com.noldaga.service;

import com.noldaga.domain.CommentLikeDto;
import com.noldaga.domain.FeedLikeDto;
import com.noldaga.domain.entity.*;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.Comment.CommentRepository;
import com.noldaga.repository.CommentLikeRepository;
import com.noldaga.repository.Feed.FeedRepository;
import com.noldaga.repository.FeedLikeRepository;
import com.noldaga.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public int feedLikeCheck(Long feedId, String username){
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        return feedLikeRepository.findByFeedIdAndUsername(feedId,user.getId());
    }

    @Transactional
    public int commentLikeCheck(Long commentId, String username){
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        return commentLikeRepository.findByFeedIdAndUsername(commentId,user.getId());
    }

    @Transactional
    public FeedLikeDto feedLikeRegister(Long feedId, String username){
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));
        //피드확인
        Feed feed = feedRepository.findById(feedId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s not founded", feedId)));

        FeedLike feedLike = FeedLike.of(feed,user);
        FeedLike feedLiked = feedLikeRepository.save(feedLike);

        //피드 좋아요+1
        feed.plusLikeCount();

        return FeedLikeDto.fromEntity(feedLiked);
    }

    @Transactional
    public void feedLikeDelete(Long feedId, String username){
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));
        //피드확인
        Feed feed = feedRepository.findById(feedId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s not founded", feedId)));

        feedLikeRepository.deleteByFeedIdAndUserId(feedId,user.getId());

        //피드 좋아요-1
        feed.minusLikeCount();
    }

    @Transactional
    public CommentLikeDto commentLikeRegister(Long commentId, String username){
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));
        //답글 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->
                new SnsApplicationException(ErrorCode.COMMENT_NOT_FOUND, String.format("%s not founded", commentId)));

        CommentLike commentLike = CommentLike.of(comment,user);
        CommentLike commentLiked = commentLikeRepository.save(commentLike);

        //댓글 좋아요+1
        comment.plusLikeCount();

        return CommentLikeDto.fromEntity(commentLiked);
    }

    @Transactional
    public void commentLikeDelete(Long commentId, String username){
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));
        //답글 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->
                new SnsApplicationException(ErrorCode.COMMENT_NOT_FOUND, String.format("%s not founded", commentId)));

        //댓글 좋아요-1
        comment.minusLikeCount();

        commentLikeRepository.deleteByFeedIdAndUserId(commentId,user.getId());
    }
}
