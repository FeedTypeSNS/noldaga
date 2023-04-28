package com.noldaga.controller;

import com.noldaga.controller.request.CommentCreateRequest;
import com.noldaga.controller.request.CommentModifyRequest;
import com.noldaga.controller.response.CommentResponse;
import com.noldaga.controller.response.FeedResponse;
import com.noldaga.controller.response.Response;
import com.noldaga.domain.CommentDto;
import com.noldaga.domain.entity.User;
import com.noldaga.service.CommentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/api/comment")
    public Response<CommentResponse> register(@RequestBody CommentCreateRequest request, Authentication authentication){
        CommentDto commentDto = commentService.create(request, authentication.getName());
        return Response.success(CommentResponse.fromCommentDto(commentDto));
    }

    @GetMapping("/api/comment/{id}")
    public Response<CommentResponse> getOne(@PathVariable Long id, Authentication authentication){
        CommentDto commentDto = commentService.getOneComment(id,authentication.getName());
        return Response.success(CommentResponse.fromCommentDto(commentDto));
    }

    @PutMapping("/api/comment/{id}")
    public Response<CommentResponse> modify(@RequestBody CommentModifyRequest request, @PathVariable Long id, Authentication authentication){
        CommentDto commentDto = commentService.modifyComment(request,id,authentication.getName());
        return Response.success(CommentResponse.fromCommentDto(commentDto));
    }

    @DeleteMapping("/api/comment/{id}")
    public void delete(@PathVariable Long id, Authentication authentication){
        commentService.deleteComment(id, authentication.getName());
    }

}
