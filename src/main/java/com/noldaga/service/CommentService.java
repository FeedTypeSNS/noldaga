package com.noldaga.service;


import com.noldaga.controller.request.CommentCreateRequest;
import com.noldaga.domain.CommentDto;
import com.noldaga.domain.entity.Comment;
import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.User;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.CommentRepository;
import com.noldaga.repository.Feed.FeedRepository;
import com.noldaga.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentService{

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentDto create(CommentCreateRequest request, String username) {
        //저장정보
        String content = request.getContent();
        long feedId= request.getFeedId();

        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //피드확인
        Feed feed = feedRepository.findById(feedId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s not founded", feedId)));

        Comment comment = Comment.of(content,feed,user);
        //Comment comment = CommentMapper.INSTANCE.toEntity(commentDto);
        //comment.setUser(user);
        Comment savedComment = commentRepository.save(comment);
        CommentDto savedCommentDto = CommentDto.fromEntity(savedComment);
        return savedCommentDto;
    }


}
