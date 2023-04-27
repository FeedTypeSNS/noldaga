package com.noldaga.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@RequiredArgsConstructor
@Controller
public class ChatController {
    @GetMapping("/chat")
    public String chatMain(){
        return "messaging";
    }
}
