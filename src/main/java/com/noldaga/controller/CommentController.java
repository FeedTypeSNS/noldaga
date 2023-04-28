package com.noldaga.controller;

import com.noldaga.controller.request.CommentCreateRequest;
import com.noldaga.controller.request.CommentModifyRequest;
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
    public CommentDto register(@RequestBody CommentCreateRequest request, Authentication authentication){
        return commentService.create(request, authentication.getName());
    }

    @GetMapping("/api/comment/{id}")
    public CommentDto getOne(@PathVariable Long id, Authentication authentication){
        CommentDto commentDto = commentService.getOneComment(id,authentication.getName());
        return commentDto;
    }

    @PutMapping("/api/comment/{id}")
    public CommentDto modify(@RequestBody CommentModifyRequest request, @PathVariable Long id, Authentication authentication){
        CommentDto commentDto = commentService.modifyComment(request,id,authentication.getName());
        return commentDto;
    }

    @DeleteMapping("/api/comment/{id}")
    public void delete(@PathVariable Long id, Authentication authentication){
        commentService.deleteComment(id, authentication.getName());
    }

}
