package com.noldaga.controller;


import com.noldaga.controller.request.UserProfileModifyRequest;
import com.noldaga.controller.response.Response;
import com.noldaga.controller.response.UserResponse;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    //유저 프로필 조회
    @GetMapping("/{userId}/profile")
    public Response<UserResponse> getUserProfile(@PathVariable Long userId){
        UserDto userDto = userService.searchUserById(userId);
        return Response.success(UserResponse.fromUserDto(userDto));
    }


    //내 프로필 조회
    @GetMapping("/me/profile")
    public Response<UserResponse> getMyUserProfile(Authentication authentication) {
        UserDto userDto = userService.searchUserByUsername(authentication.getName());
        return Response.success(UserResponse.fromUserDto(userDto));
    }



    //내 프로필 수정
    @PostMapping(value="/me/profile"/*, consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}*/)
    public Response<UserResponse> modifyMyUserProfile(
            @RequestPart(value="file", required = false)MultipartFile multipartFile,
            @RequestPart(value="json")UserProfileModifyRequest req,
            Authentication authentication
            ) throws IOException {

        System.out.println("multipartFile = " + multipartFile);
        System.out.println("req = " + req);

        UserDto userDto = userService.modifyMyProfile(multipartFile,
                req.getNickname(),req.getProfileMessage(),req.getProfileImageUrl(),
                authentication.getName());


        return Response.success(UserResponse.fromUserDto(userDto));
    }



}
