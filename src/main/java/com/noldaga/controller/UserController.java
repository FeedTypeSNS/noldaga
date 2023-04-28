package com.noldaga.controller;


import com.noldaga.controller.request.*;
import com.noldaga.controller.response.CodeIdResponse;
import com.noldaga.controller.response.Response;
import com.noldaga.controller.response.UserJoinResponse;
import com.noldaga.domain.CodeDto;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.module.CodeValidator;
import com.noldaga.service.MailSendService;
import com.noldaga.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final MailSendService mailSendService;

    private final CodeValidator codeValidator;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest req) {
        UserDto userDto = userService.join(req.getUsername(), req.getPassword(), req.getNickname(), req.getEmail());
        UserJoinResponse userJoinResponse = UserJoinResponse.fromUserDto(userDto);

        return Response.success(userJoinResponse); //프론트에서 api의 반환값을 파싱하기 쉽도록 실패하든 성공하든 획일화된 포맷팅으로 보낸다.
    }

    @PostMapping("/login")
    public Response<String> login(@RequestBody UserLoginRequest userLoginRequest) {
        String token = userService.login(userLoginRequest.getUsername(), userLoginRequest.getPassword());

        return Response.success(token);
    }

    @PostMapping("/check-duplicated")
    public Response<Void> login(@RequestBody UsernameRequest request) {
        userService.check(request.getUsername()).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USERNAME, String.format("%s is duplicated", it.getUsername()));
        });

        return Response.success();
    }

    @PostMapping("/email")
    public Response<CodeIdResponse> email(@RequestBody MailAuthRequest req) throws MessagingException, UnsupportedEncodingException {

        CodeDto codeDto= codeValidator.generateCode();
        mailSendService.sendEmail(req.getEmailAddress(), codeDto.getCode());

        return Response.success(CodeIdResponse.fromCodeDto(codeDto));
    }

    @PostMapping("/check-code")
    public Response<Void> checkCode(@RequestBody CodeRequest req, HttpServletRequest httpRequest) {

        codeValidator.validateCode(req.getCodeId(), req.getCode());

        return Response.success();
    }


}
