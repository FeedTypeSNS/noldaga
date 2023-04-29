package com.noldaga.controller;

import com.noldaga.controller.request.*;
import com.noldaga.controller.response.CodeIdResponse;
import com.noldaga.controller.response.Response;
import com.noldaga.controller.response.UserJoinResponse;
import com.noldaga.domain.CodeUserDto;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.service.AuthService;
import com.noldaga.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


@RestController
@RequestMapping("/api/anonymous")
@RequiredArgsConstructor
public class AnonymousController {

    private final UserService userService;
    private final AuthService authService;


    @PostMapping("/join/validate-username") //회원가입 1 : 아이디중복검사
    public Response<Void> login(@RequestBody UsernameRequest req) {
        userService.validateDuplication(req.getUsername());
        return Response.success();
    }

    @PostMapping("/join/send-code") //회원가입 2 : 이메일 인증 : 이메일 로 코드전송
    public Response<CodeIdResponse> email(@RequestBody MailAuthRequest req) throws MessagingException, UnsupportedEncodingException {

        Integer codeId = authService.sendCode(req.getEmailAddress());
        return Response.success(CodeIdResponse.of(codeId));
    }

    @PostMapping("/join/validate-code")//회원가입 3 : 코드 대조
    public Response<Void> validateCode(@RequestBody CodeRequest req) {

        authService.validateCode(req.getCodeId(), req.getCode());
        return Response.success();
    }

    @PostMapping("/join") //회원가입 4 : 회원가입
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest req) {
        UserDto userDto = userService.join(req.getUsername(), req.getPassword(), req.getNickname(), req.getEmail());
        UserJoinResponse userJoinResponse = UserJoinResponse.fromUserDto(userDto);

        return Response.success(userJoinResponse);
    }

    @PostMapping("/find-password/send-code")//비번찾기1 : 가입된 이메일로 코드전송
    public Response<CodeIdResponse> findPassword(@RequestBody UsernameRequest req) throws MessagingException, UnsupportedEncodingException {

        String emailAddress = userService.findEmail(req.getUsername());
        Integer codeId = authService.sendCode(emailAddress, req.getUsername());
        return Response.success(CodeIdResponse.of(codeId));
    }

    @Transactional
    @PostMapping("/find-password/init-password") //비번찾기2 : 코드 검증후 비밀번호 초기화
    public Response<Void> initPassword(@RequestBody CodeRequest req) throws MessagingException, UnsupportedEncodingException {

        CodeUserDto codeUserDto = authService.validateCodeForPassword(req.getCodeId(), req.getCode());

        String newPassword = userService.initPassword(codeUserDto.getUsername());
        authService.sendPassword(codeUserDto.getEmail(), newPassword);

        return Response.success();
    }

    @PostMapping("/find-username/send-username")// 아이디찾기: 가입된 이메일로 아이디전송
    public Response<Void> findUsername(@RequestBody MailAuthRequest req) throws MessagingException, UnsupportedEncodingException {
        UserDto userDto = userService.findUsernameByEmail(req.getEmailAddress());
        authService.sendUsername(req.getEmailAddress(), userDto.getUsername());

        return Response.success();
    }


    @PostMapping("/login")
    public Response<String> login(@RequestBody UserLoginRequest req) {
        String token = userService.login(req.getUsername(), req.getPassword());

        return Response.success(token);
    }
}
