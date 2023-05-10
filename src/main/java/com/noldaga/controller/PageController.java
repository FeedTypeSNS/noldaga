package com.noldaga.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {

    @GetMapping("/login-form")
    public String login() {
        return "sign-in";
    }

    @GetMapping("/join-form")
    public String join() {
        return "sign-up";
    }


    @GetMapping("/find-username")
    public String findUsername() {
        return "find-username";
    }

    @GetMapping("/find-password")
    public String findPassword() {
        return "find-password";
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/feed")
    public String detail() {
        return "post-details";
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "my-profile";
    }

    @GetMapping("/save")
    public String mypageSave() {
        return "my-profile-save";
    }

    @GetMapping("/searchfeed")
    public String searchFeed() {
        return "search-feed";
    }

    @GetMapping("/searchhash")
    public String searchHashTag() {
        return "search-hashTag";
    }

    @GetMapping("/hashtag")
    public String hashtagFeeds() {
        return "search-hashTag-detail";
    }

    @GetMapping("/searchpeople")
    public String searchPeople() {
        return "search-people";
    }

    @GetMapping("/chat")
    public String chatMain() {
        return "messaging";
    }

    @GetMapping("/groups")
    public String groupList() {
        return "groups";
    }

    @GetMapping("/group")
    public String groupView() {
        return "group-details";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }

    @GetMapping("/editProfile")
    public String editProfile() {
        return "edit-profile-picture";
    }

    @GetMapping("/notifications")
    public String notifications(){
        return "notifications";
    }
}
