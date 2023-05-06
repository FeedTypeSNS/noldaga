package com.noldaga.controller;

import com.noldaga.controller.request.*;
import com.noldaga.controller.response.CodeIdResponse;
import com.noldaga.controller.response.Response;
import com.noldaga.controller.response.UserJoinResponse;
import com.noldaga.domain.CodeUserDto;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.service.MailAuthService;
import com.noldaga.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/anonymous")
@RequiredArgsConstructor
public class AnonymousController {

    private final UserService userService;
    private final MailAuthService mailAuthService;
    private final int MAX_ACCOUNTS_PER_EMAIL=50;

    //todo 회원가입이라는 하나의 트랜잭션안에 여러번의 api를 통해 인증을 하는데, 서버쪽에서 상태를 유지해야함.
    @PostMapping("/join/validate-username") //회원가입 1 : 아이디중복검사
    public Response<Void> login(@RequestBody UsernameRequest req) {
        userService.validateDuplication(req.getUsername());
        return Response.success();
    }

    @PostMapping("/join/send-code") //회원가입 2 : 이메일 인증 : 이메일 로 코드전송
    public Response<CodeIdResponse> email(@RequestBody MailRequest req) throws MessagingException, UnsupportedEncodingException {
        if (MAX_ACCOUNTS_PER_EMAIL <= userService.countByEmail(req.getEmail())) {
            throw new SnsApplicationException(ErrorCode.EMAIL_LIMIT_EXCEEDED);
        }
        Integer codeId = mailAuthService.sendCode(req.getEmail());
        return Response.success(CodeIdResponse.of(codeId));
    }

    @PostMapping("/join/validate-code")//회원가입 3 : 코드 대조
    public Response<Void> validateCode(@RequestBody CodeRequest req) {

        mailAuthService.validateCode(req.getCodeId(), req.getCode());
        return Response.success();
    }

    @PostMapping("/join") //회원가입 4 : 회원가입
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest req) {
        //todo 포스트맨으로 회원가입 되도록 주석처리해놓음
//                mailAuthService.validateAuthenticatedEmail(req.getCodeId(), req.getEmail());

        //회원가입시 횟수 넘으면 이메일 코드조차 보낼수 없어서 이 로직은 무조건 false 이긴한데 안전빵으로 넣어줌
        if(userService.countByEmail(req.getEmail())>=MAX_ACCOUNTS_PER_EMAIL){
            throw new SnsApplicationException(ErrorCode.EMAIL_LIMIT_EXCEEDED);
        }

        UserDto userDto = userService.join(req.getUsername(), req.getPassword(), req.getNickname(), req.getEmail());
        UserJoinResponse userJoinResponse = UserJoinResponse.fromUserDto(userDto);

        return Response.success(userJoinResponse);
    }

    @PostMapping("/find-password/send-code")//비번찾기1 : 가입된 이메일로 코드전송
    public Response<CodeIdResponse> findPassword(@RequestBody UsernameRequest req) throws MessagingException, UnsupportedEncodingException {

        String emailAddress = userService.searchEmail(req.getUsername());
        Integer codeId = mailAuthService.sendCodeForPassword(emailAddress, req.getUsername());
        return Response.success(CodeIdResponse.of(codeId));
    }

    @Transactional
    @PostMapping("/find-password/init-password") //비번찾기2 : 코드 검증후 비밀번호 초기화
    public Response<Void> initPassword(@RequestBody CodeRequest req) throws MessagingException, UnsupportedEncodingException {

        CodeUserDto codeUserDto = mailAuthService.validateCodeForPassword(req.getCodeId(), req.getCode());

        String newPassword = userService.initPassword(codeUserDto.getUsername());
        mailAuthService.sendPassword(codeUserDto.getEmail(), newPassword);

        return Response.success();
    }

    @PostMapping("/find-username/send-username")// 아이디찾기: 가입된 이메일로 아이디전송
    public Response<Void> findUsername(@RequestBody MailRequest req) throws MessagingException, UnsupportedEncodingException {
        List<String> usernameList = userService.searchUsernameListByEmail(req.getEmail());
        if(usernameList.isEmpty()){
            throw new SnsApplicationException(ErrorCode.ACCOUNT_NOT_FOUND, String.format("No Account associated withe email: %s", req.getEmail()));
        }

        String usernames = usernameList.stream().collect(Collectors.joining(" , "));
        mailAuthService.sendUsernames(req.getEmail(), usernames);

        return Response.success();
    }


    @PostMapping("/login")
    public Response<String> login(@RequestBody UserLoginRequest req, HttpServletResponse httpServletResponse) {
        String token = userService.login(req.getUsername(), req.getPassword(), httpServletResponse);

        return Response.success(token);
    }
}