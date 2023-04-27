package com.noldaga.service;


import com.noldaga.controller.request.CommentCreateRequest;
import com.noldaga.controller.request.CommentModifyRequest;
import com.noldaga.domain.CommentDto;
import com.noldaga.domain.entity.Comment;
import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.User;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.Comment.CommentRepository;
import com.noldaga.repository.Feed.FeedRepository;
import com.noldaga.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

    public List<CommentDto> getFeedComments(Long id) {
        Pageable pageable = PageRequest.of(0,5);
        Page<Comment> commentPageList = commentRepository.FindWithFeed(id,pageable);

        List<CommentDto> commentDtoList = new ArrayList<>();

        commentPageList.getContent().forEach(comment->{
            CommentDto commentDto = CommentDto.fromEntity(comment);
            commentDtoList.add(commentDto);
        });
        return commentDtoList;
    }

    public CommentDto getOneComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow();
        return CommentDto.fromEntity(comment);
    }

    public CommentDto modifyComment(CommentModifyRequest request, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->{
            return new IllegalArgumentException("댓글 정보가 없습니다.");
        });

        //Comment modifiedComment = CommentDto.fromEntity(modifiedCommentDto);
        comment.change(request.getContent());
        Comment changedComment = commentRepository.save(comment);

        CommentDto changedCommentDto = CommentDto.fromEntity(changedComment);
        return changedCommentDto;
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

}
