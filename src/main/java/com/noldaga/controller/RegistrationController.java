package com.noldaga.controller;

import com.noldaga.controller.request.*;
import com.noldaga.controller.response.CodeIdResponse;
import com.noldaga.controller.response.Response;
import com.noldaga.controller.response.UserJoinResponse;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;


@RestController
@RequestMapping("/api/anonymous")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    //todo 회원가입이라는 하나의 트랜잭션안에 여러번의 api를 통해 인증을 하는데, 서버쪽에서 상태를 유지해야함.
    @PostMapping("/join/validate-username") //회원가입 1 : 아이디중복검사
    public Response<Void> validateDuplication(@RequestBody UsernameRequest req) {

        registrationService.validateDuplication(req.getUsername());

        return Response.success();
    }


    @PostMapping("/join/send-code") //회원가입 2 : 이메일 인증 : 이메일 로 코드전송

    public Response<CodeIdResponse> sendCodeForJoin(@RequestBody MailRequest req) throws MessagingException, UnsupportedEncodingException {

        Integer codeId = registrationService.sendCodeForJoin(req.getEmail());

        return Response.success(CodeIdResponse.of(codeId));
    }

    @PostMapping("/join/validate-code")//회원가입 3 : 코드 대조
    public Response<Void> validateCode(@RequestBody CodeRequest req) {

        registrationService.validateCodeForJoin(req.getCodeId(), req.getCode());

        return Response.success();
    }

    @PostMapping("/join") //회원가입 4 : 회원가입
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest req) {


        UserDto userDto = registrationService.join(
                req.getCodeId(),
                req.getCode(),
                req.getEmail(),
                req.getUsername(),
                req.getPassword(),
                req.getNickname());

        UserJoinResponse userJoinResponse = UserJoinResponse.fromUserDto(userDto);

        return Response.success(userJoinResponse);
    }

    @PostMapping("/find-password/send-code")//비번찾기1 : 가입된 이메일로 코드전송
    public Response<CodeIdResponse> findPassword(@RequestBody UsernameRequest req) throws MessagingException, UnsupportedEncodingException {

        Integer codeId = registrationService.sendCodeToFindPassword(req.getUsername());

        return Response.success(CodeIdResponse.of(codeId));
    }

    @PostMapping("/find-password/init-password") //비번찾기2 : 코드 검증후 비밀번호 초기화
    public Response<Void> initPassword(@RequestBody CodeRequest req) throws MessagingException, UnsupportedEncodingException {

        registrationService.initPassword(req.getCodeId(), req.getCode());

        return Response.success();
    }

    @PostMapping("/find-username/send-username")// 아이디찾기: 가입된 이메일로 아이디전송
    public Response<Void> findUsername(@RequestBody MailRequest req) throws MessagingException, UnsupportedEncodingException {

        registrationService.findUsername(req.getEmail());

        return Response.success();
    }


    @PostMapping("/login")
    public Response<String> login(@RequestBody UserLoginRequest req, HttpServletResponse httpServletResponse) {

        String token = registrationService.login(req.getUsername(), req.getPassword());

        generateTokenCookie(httpServletResponse, token);
        return Response.success(token);
    }

    private void generateTokenCookie(HttpServletResponse response, String token) {
        final int A_MONTH = 60 * 60 * 24 * 30;
        Cookie tokenCookie = new Cookie("tokenCookie", token);
        tokenCookie.setMaxAge(A_MONTH);
        response.addCookie(tokenCookie);
        tokenCookie.setHttpOnly(true); //자바스크립트로 조작 금지
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);
    }
}