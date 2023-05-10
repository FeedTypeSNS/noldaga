package com.noldaga.service;


import com.noldaga.controller.request.CommentCreateRequest;
import com.noldaga.controller.request.CommentModifyRequest;
import com.noldaga.domain.CommentDto;
import com.noldaga.domain.alarm.*;
import com.noldaga.domain.entity.Alarm;
import com.noldaga.domain.entity.Comment;
import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.User;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.AlarmRepository;
import com.noldaga.repository.Comment.CommentRepository;
import com.noldaga.repository.Feed.FeedRepository;
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

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    private final AlarmRepository alarmRepository;

    @Transactional
    public CommentDto create(CommentCreateRequest request, String username) {
        //저장정보
        String content = request.getContent();
        long feedId = request.getFeedId();

        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //피드확인
        Feed feed = feedRepository.findById(feedId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s not founded", feedId)));

        Comment comment = Comment.of(content, feed, user);
        Comment savedComment = commentRepository.save(comment);

        //피드 댓글 수+1
        feed.plusCommentCount();

        CommentDto savedCommentDto = CommentDto.fromEntity(savedComment);


        Long commentWriterId = user.getId();
        Long feedWriterId = feed.getUser().getId();
        if (commentWriterId != feedWriterId) { //내가 내 피드 댓글 할때는 알람 x
            Alarm alarm = Alarm.of(feedWriterId, AlarmType.NEW_COMMENT_ON_FEED,
                    AlarmArgs.of(UserObject.from(user), CommentObject.from(comment,feed.getTitle())));
            alarmRepository.save(alarm);
        }


        return savedCommentDto;
    }

    @Transactional
    public List<CommentDto> getFeedComments(Long feedId, String username) {
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //피드확인
        Feed feed = feedRepository.findById(feedId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s not founded", feedId)));

        Pageable pageable = PageRequest.of(0, 5);
        Page<Comment> commentPageList = commentRepository.FindWithFeed(feedId, pageable);

        List<CommentDto> commentDtoList = new ArrayList<>();

        commentPageList.getContent().forEach(comment -> {
            CommentDto commentDto = CommentDto.fromEntity(comment);
            commentDtoList.add(commentDto);
        });
        return commentDtoList;
    }

    @Transactional
    public CommentDto getOneComment(Long commentId, String username) {
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //답글 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.COMMENT_NOT_FOUND, String.format("%s not founded", commentId)));

        return CommentDto.fromEntity(comment);
    }

    @Transactional
    public CommentDto modifyComment(CommentModifyRequest request, Long commentId, String username) {
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //답글 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.COMMENT_NOT_FOUND, String.format("%s not founded", commentId)));

//        //권한있나확인 -> 권한이 있어야만 삭제 버튼이 보이므로 재확인 필요 없음
//        if (comment.getUser().getId() != user.getId()) {
//            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", username, commentId));
//        }

        comment.change(request.getContent());
        Comment changedComment = commentRepository.save(comment);

        CommentDto changedCommentDto = CommentDto.fromEntity(changedComment);
        return changedCommentDto;
    }

    @Transactional
    public void deleteComment(Long commentId, String username) {
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //답글 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.COMMENT_NOT_FOUND, String.format("%s not founded", commentId)));
//
//        //권한있나확인 -> 권한이 있어야만 삭제 버튼이 보이므로 재확인 필요 없음
//        if (comment.getUser().getId() != user.getId()) {
//            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", username, commentId));
//        }

        commentRepository.deleteById(commentId);

        //피드확인
        Feed feed = feedRepository.findById(comment.getFeed().getId()).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s not founded", comment.getFeed().getId())));
        //피드 댓글 수 -1
        feed.minusCommentCount();
    }

}
