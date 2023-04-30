package com.noldaga.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {

    @GetMapping("/login-form")
    public String login(){
        System.out.println("PageController.login");
        return "sign-in";
    }

    @GetMapping("/join-form")
    public String join(){
        System.out.println("PageController.join");
        return "sign-up";
    }

    @GetMapping("/chat")
    public String chatMain(){
        return "messaging";
    }
}
