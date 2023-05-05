package com.noldaga.controller;


import com.noldaga.controller.request.MailRequest;
import com.noldaga.controller.request.UserInfoModifyRequest;
import com.noldaga.controller.request.UserPasswordModifyRequest;
import com.noldaga.controller.request.UserProfileModifyRequest;
import com.noldaga.controller.response.Response;
import com.noldaga.controller.response.UserInfoResponse;
import com.noldaga.controller.response.UserResponse;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.service.UserService;
import com.noldaga.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Positive;
import java.io.IOException;
import java.time.LocalDate;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    //유저 프로필 조회(약력 정보)
    @GetMapping("/{userId}/profile")
    public Response<UserResponse> getUserProfile(@PathVariable @Positive Long userId) {
        UserDto userDto = userService.searchUserById(userId);
        return Response.success(UserResponse.fromUserDto(userDto));
    }


    //내 프로필 조회(약력 정보)
    @GetMapping("/me/profile")
    public Response<UserResponse> getMyUserProfile(Authentication authentication) {
        UserDto userDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to UserDto class failed"));
        return Response.success(UserResponse.fromUserDto(userDto));
    }


    //내 프로필 수정
    @PostMapping(value = "/me/profile"/*, consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}*/)
    public Response<UserResponse> modifyMyUserProfile(
            @RequestPart(value = "file", required = false) MultipartFile multipartFile,
            @Validated @RequestPart(value = "json") UserProfileModifyRequest req,
            Authentication authentication
    ) throws IOException {

        System.out.println("multipartFile = " + multipartFile);
        System.out.println("req = " + req);

        UserDto userDto = userService.modifyMyProfile(multipartFile,
                req.getNickname(), req.getProfileMessage(), req.getProfileImageUrl(),
                authentication.getName());

        return Response.success(UserResponse.fromUserDto(userDto));
    }


    //내 유저정보 조회
    @GetMapping("/me/info")
    public Response<UserInfoResponse> getMyUserInfo(Authentication authentication) {
        UserDto userDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to UserDto class failed"));

        return Response.success(UserInfoResponse.fromUserDto(userDto));
    }

    //내 유저정보 수정
    @PostMapping("/me/info")
    public Response<UserInfoResponse> modifyMyUserInfo(@Validated @RequestBody UserInfoModifyRequest req, Authentication authentication) {

        UserDto userDto = userService.modifyMyUserinfo(req.getNickname(), req.getGender(), req.getBirthday(), authentication.getName());
        return Response.success(UserInfoResponse.fromUserDto(userDto));
    }

    @PostMapping("/me/email")
    public Response<UserInfoResponse> modifyMyEmail(@Validated @RequestBody MailRequest req,Authentication authentication) {
        //인증된 이메일인지 확인 해야함
        UserDto userDto = userService.modifyMyEmail(req.getEmail(), authentication.getName());
        return Response.success(UserInfoResponse.fromUserDto(userDto));
    }

    @PostMapping("/me/password")
    public Response<UserInfoResponse> modifyMyPassword
            (@Validated @RequestBody UserPasswordModifyRequest req, Authentication authentication){
        UserDto userDto =userService.modifyPassword(req.getCurrentPassword(),req.getNewPassword(),authentication.getName());
        return Response.success(UserInfoResponse.fromUserDto(userDto));
    }
}
