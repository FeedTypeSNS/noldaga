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

    @GetMapping("/mypage")
    public String mypage(){
        return "my-profile";
    }

    @GetMapping("/save")
    public String mypageSave(){
        return "my-profile-save";
    }

    @GetMapping("/chat")
    public String chatMain(){
        return "messaging";
    }

    @GetMapping("/groups")
    public String groupList(){
        return "groups";
    }

    @GetMapping("/group")
    public String groupView() { return "group-details"; }

    @GetMapping("/settings")
    public String settings(){
        return "settings";
    }

    @GetMapping("/editProfile")
    public String editProfile(){
        return "test";
    }
}
