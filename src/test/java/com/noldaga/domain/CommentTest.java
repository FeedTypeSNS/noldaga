package com.noldaga.domain;

import com.noldaga.domain.entity.Comment;
import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest
public class CommentTest {

    @Test
    public void CommentBuildTest(){
        User commentuser = User.of("username", "1234");
        User user = User.of("username2", "1111");
        Feed feed = Feed.of("title", "content", 0, 1, user);
        Comment comment = Comment.of("댓글 내용", feed, commentuser);
        log.info(comment.getContent());
        log.info(comment.getFeed().getTitle());
        log.info(comment.getUser().getUsername());


        //dtoTest
        CommentDto commentDto = CommentDto.fromEntity(comment);
        log.info(commentDto);
    }
}
