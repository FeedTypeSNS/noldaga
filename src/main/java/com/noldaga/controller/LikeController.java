package com.noldaga.controller;

import com.noldaga.controller.response.CommentLikeResponse;
import com.noldaga.controller.response.FeedLikeResponse;
import com.noldaga.controller.response.Response;
import com.noldaga.domain.CommentLikeDto;
import com.noldaga.domain.FeedLikeDto;
import com.noldaga.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/api/like/feed/{feedId}")
    public int feedLikeCheck(@PathVariable Long feedId, Authentication authentication) {
        return likeService.feedLikeCheck(feedId, authentication.getName());
    }

    @GetMapping("/api/like/comment/{commentId}")
    public int commentLikeCheck(@PathVariable Long commentId, Authentication authentication) {
        return likeService.commentLikeCheck(commentId, authentication.getName());
    }

    @PostMapping("/api/like/feed/{feedId}")
    public Response<FeedLikeResponse> feedLikecreate(@PathVariable Long feedId, Authentication authentication) {
        FeedLikeDto feedLikeDto = likeService.feedLikeRegister(feedId, authentication.getName());
        //authentication.getName()을 까보면 principal.getName() -> AbstractAuthenticationToken.getName() 참고하면 UserDetails 구현해주어야함

        return Response.success(FeedLikeResponse.fromFeedLikeDto(feedLikeDto));
    }

    @PostMapping("/api/like/comment/{commentId}")
    public Response<CommentLikeResponse> commentLikecreate(@PathVariable Long commentId, Authentication authentication) {
        CommentLikeDto commentLikeDto = likeService.commentLikeRegister(commentId, authentication.getName());
        //authentication.getName()을 까보면 principal.getName() -> AbstractAuthenticationToken.getName() 참고하면 UserDetails 구현해주어야함

        return Response.success(CommentLikeResponse.fromCommentLikeDto(commentLikeDto));
    }

    @DeleteMapping("/api/like/feed/{feedId}")
    public Response<Void> feedLikeDelete(@PathVariable Long feedId, Authentication authentication) {
        likeService.feedLikeDelete(feedId, authentication.getName());
        //authentication.getName()을 까보면 principal.getName() -> AbstractAuthenticationToken.getName() 참고하면 UserDetails 구현해주어야함
        return Response.success();
    }

    @DeleteMapping("/api/like/comment/{commentId}")
    public Response<Void> commentLikeDelete(@PathVariable Long commentId, Authentication authentication) {
        likeService.commentLikeDelete(commentId, authentication.getName());
        //authentication.getName()을 까보면 principal.getName() -> AbstractAuthenticationToken.getName() 참고하면 UserDetails 구현해주어야함
        return Response.success();
    }
}
