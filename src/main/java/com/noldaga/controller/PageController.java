package com.noldaga.controller;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class PageController {

    @GetMapping("/")
    public String root(){
        return "redirect:/login-form";
    }

    @GetMapping("/login-form")
    public String login(Authentication authentication) {

        if(authentication!=null){
            return "home";
        }
        return "sign-in";
    }

    @GetMapping("/join-form")
    public String join(Authentication authentication) {
        if(authentication!=null){
            return "home";
        }
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
        return "home";
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

    @GetMapping("/nol/logout")
    public String logOut(HttpServletResponse response) throws IOException {

        System.out.println("===========================UserController.logOut===============");
        Cookie tokenCookie = new Cookie("tokenCookie", null);
        tokenCookie.setMaxAge(0);
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);

        return "sign-in";
    }

}
