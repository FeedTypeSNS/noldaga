package com.noldaga.controller;


import com.noldaga.controller.request.*;
import com.noldaga.controller.response.CodeIdResponse;
import com.noldaga.controller.response.Response;
import com.noldaga.controller.response.UserInfoResponse;
import com.noldaga.controller.response.UserResponse;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.service.MailAuthService;
import com.noldaga.service.UserService;
import com.noldaga.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final MailAuthService mailAuthService;

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

    //비밀번호 수정
    @PostMapping("/me/password")
    public Response<UserInfoResponse> modifyMyPassword
            (@Validated @RequestBody UserPasswordModifyRequest req, Authentication authentication) {
        UserDto loginUserDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to UserDto class failed"));

        userService.matchPassword(req.getCurrentPassword(),loginUserDto.getPassword());

        UserDto userDto = userService.modifyPassword(req.getNewPassword(), authentication.getName());
        return Response.success(UserInfoResponse.fromUserDto(userDto));
    }




    //이메일 수정시 비밀번호 확인
    //todo 회원정보수정에서이메일 수정시 비번 검증하고 이메일 수정해야함
    @PostMapping("/me/validate-password")//비밀번호를 아는 상태면 코드를 보낼수 있음 : 비밀번호를 몰라도 되지만 다음단계에서 비밀번호 필요해서 미리 검증해줌
    public Response<CodeIdResponse> sendCodeForEmail(@Validated @RequestBody UserEmailCodeRequest req, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        UserDto userDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to UserDto class failed"));

        userService.matchPassword(req.getPassword(),userDto.getPassword());

        Integer codeId =mailAuthService.sendCodeForEmail(req.getEmail());//code_email

        return Response.success(CodeIdResponse.of(codeId));
    }


    //이메일 수정
    @PostMapping("/me/email")//비밀번호를 아는 상태에서 코드를 맞춰야 이메일 변경가능
    public Response<UserInfoResponse> modifyMyEmail(@Validated @RequestBody UserMailModifyRequest req, Authentication authentication) {

        UserDto loginUserDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to UserDto class failed"));

        userService.matchPassword(req.getPassword(),loginUserDto.getPassword());

        String email= mailAuthService.validateCodeForEmail(req.getCodeId(),req.getCode()); //코드대조후 코드를 전송했던 이메일을 꺼내옴

        UserDto resultUserDto =userService.modifyMyEmail(email, authentication.getName());


        return Response.success(UserInfoResponse.fromUserDto(resultUserDto));
    }

}
