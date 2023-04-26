package com.noldaga.controller;

import com.noldaga.controller.response.FollowResponse;
import com.noldaga.controller.response.Response;
import com.noldaga.domain.UserDto;
import com.noldaga.domain.UserSimpleDto;
import com.noldaga.domain.entity.User;
import com.noldaga.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FollowController {
    private final FollowService followService;

    @PostMapping("/follow/{userId}")
    public Response<FollowResponse> doFollow(Authentication authentication, @PathVariable Long userId){
        FollowResponse msg = followService.doFollow(authentication.getName(), userId);
        return Response.success(msg);
    }//팔로우 하기

    @PostMapping("/unfollow/{userId}")
    public Response<FollowResponse> unFollow(Authentication authentication, @PathVariable Long userId){
        FollowResponse msg = followService.unFollow(authentication.getName(), userId);
        return Response.success(msg);
    }//언팔로우 하기

    @GetMapping("/follower/my")
    public Response<List<UserSimpleDto>> getMyFollower(Authentication authentication){
        List<UserSimpleDto> myFollower = followService.getFollowerList(authentication.getName());
        return Response.success(myFollower);
    }//내 팔로워 리스트 반환

    @GetMapping("/following/my")
    public Response<List<UserSimpleDto>> getMyFollowing(Authentication authentication){
        List<UserSimpleDto> myFollowing = followService.getFollowingList(authentication.getName());
        return Response.success(myFollowing);
    }//내 팔로잉 리스트반환


    @GetMapping("/follower/{userName}")
    public Response<List<UserSimpleDto>> getFollower(@PathVariable String userName){
        List<UserSimpleDto> follower = followService.getFollowerList(userName);
        return Response.success(follower);
    }//다른 사람 팔로워 리스트 반환

    @GetMapping("/following/{userName}")
    public Response<List<UserSimpleDto>> getFollowing(@PathVariable String userName){
        List<UserSimpleDto> following = followService.getFollowingList(userName);
        return Response.success(following);
    }//다른 사람 팔로잉 리스트반환
}
