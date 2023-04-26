package com.noldaga.controller;

import com.noldaga.controller.request.CommentCreateRequest;
import com.noldaga.domain.CommentDto;
import com.noldaga.domain.entity.User;
import com.noldaga.service.CommentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/api/comment")
    public CommentDto register(@RequestBody CommentCreateRequest request, Authentication authentication){
        return commentService.create(request, authentication.getName());
    }

}
