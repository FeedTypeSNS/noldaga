package com.noldaga.controller;


import com.noldaga.controller.request.UserJoinRequest;
import com.noldaga.controller.request.UserLoginRequest;
import com.noldaga.controller.request.UsernameRequest;
import com.noldaga.controller.response.Response;
import com.noldaga.controller.response.UserJoinResponse;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest req) {
        UserDto userDto = userService.join(req.getUsername(), req.getPassword(),req.getNickname(),req.getEmail());
        UserJoinResponse userJoinResponse = UserJoinResponse.fromUserDto(userDto);

        return Response.success(userJoinResponse); //프론트에서 api의 반환값을 파싱하기 쉽도록 실패하든 성공하든 획일화된 포맷팅으로 보낸다.
    }

    @PostMapping("/login")
    public Response<String> login(@RequestBody UserLoginRequest userLoginRequest) {
        String token = userService.login(userLoginRequest.getUsername(), userLoginRequest.getPassword());

        return Response.success(token);
    }

    @PostMapping("/check")
    public Response<Void> login(@RequestBody UsernameRequest request){
        userService.check(request.getUsername()).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USERNAME, String.format("%s is duplicated", it.getUsername()));
        });

        return Response.success();
    }



}
