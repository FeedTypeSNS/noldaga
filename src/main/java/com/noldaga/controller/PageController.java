package com.noldaga.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login-form")
    public String login(){
        return "sign-in";
    }

    @GetMapping("/join-form")
    public String join(){
        return "sign-up";
    }


    @GetMapping("/find-username")
    public String findUsername(){
        return "find-username";
    }

    @GetMapping("/find-password")
    public String findPassword(){
        return "find-password";
    }

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/feed")
    public String detail(){
        return "post-details";
    }


}
