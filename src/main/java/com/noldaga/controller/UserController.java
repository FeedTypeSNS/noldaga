package com.noldaga.controller;


import com.noldaga.service.MailAuthService;
import com.noldaga.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final MailAuthService mailAuthService;



}
