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

    @GetMapping("/nol")
    public String home() {
        return "index";
    }

    @GetMapping("/nol/feed")
    public String detail() {
        return "post-details";
    }

    @GetMapping("/nol/mypage")
    public String mypage() {
        return "my-profile";
    }

    @GetMapping("/nol/save")
    public String mypageSave() {
        return "my-profile-save";
    }

    @GetMapping("/nol/like")
    public String mypageLiked(){
        return "my-profile-like";
    }

    @GetMapping("/nol/friend")
    public String mypageFriend(){
        return "my-profile-friends";
    }

    @GetMapping("/nol/searchfeed")
    public String searchFeed() {
        return "search-feed";
    }

    @GetMapping("/nol/searchhash")
    public String searchHashTag() {
        return "search-hashTag";
    }

    @GetMapping("/nol/hashtag")
    public String hashtagFeeds() {
        return "search-hashTag-detail";
    }

    @GetMapping("/nol/searchpeople")
    public String searchPeople() {
        return "search-people";
    }

    @GetMapping("/nol/searchgroup")
    public String searchGroup(){
        return "search-group";
    }

    @GetMapping("/nol/chat")
    public String chatMain() {
        return "messaging";
    }

    @GetMapping("/nol/groups")
    public String groupList() {
        return "groups";
    }

    @GetMapping("/nol/group")
    public String groupView() {
        return "group-details";
    }

    @GetMapping("/nol/settings")
    public String settings() {
        return "settings";
    }

    @GetMapping("/nol/editProfile")
    public String editProfile() {
        return "edit-profile-picture";
    }

    @GetMapping("/nol/notifications")
    public String notifications(){
        return "notifications";
    }
}
