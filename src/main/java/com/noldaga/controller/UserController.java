package com.noldaga.controller;


import com.noldaga.controller.request.*;
import com.noldaga.controller.response.*;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.service.UserService;
import com.noldaga.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    //유저 프로필 조회(약력 정보)
    @GetMapping("/{userId}/profile")//완료
    public Response<UserResponse> getUserProfile(@PathVariable @Positive Long userId) {

        UserDto userDto = userService.searchUserById(userId);

        return Response.success(UserResponse.fromUserDto(userDto));
    }


    //내 프로필 조회(약력 정보)
    @GetMapping("/me/profile")//완료
    public Response<UserResponse> getMyUserProfile(Authentication authentication) {

        UserDto userDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to UserDto class failed"));

        return Response.success(UserResponse.fromUserDto(userDto));
    }


    //내 프로필 수정 : 완료
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


    //내 유저정보 조회 :완료
    @GetMapping("/me/info")
    public Response<UserInfoResponse> getMyUserInfo(Authentication authentication) {

        UserDto userDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to UserDto class failed"));

        return Response.success(UserInfoResponse.fromUserDto(userDto));
    }


    //내 유저정보 수정 :완료
    @PostMapping("/me/info")
    public Response<UserInfoResponse> modifyMyUserInfo(@Validated @RequestBody UserInfoModifyRequest req, Authentication authentication) {

        UserDto userDto = userService.modifyMyUserinfo(req.getNickname(), req.getGender(), req.getBirthday(), authentication.getName());
        return Response.success(UserInfoResponse.fromUserDto(userDto));
    }

    //비밀번호 수정 : 완료
    @PostMapping("/me/password")
    public Response<UserInfoResponse> modifyMyPassword
    (@Validated @RequestBody UserPasswordModifyRequest req, Authentication authentication) {

        UserDto loginUserDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to UserDto class failed"));

        UserDto userDto = userService.modifyPassword(req.getNewPassword(), req.getCurrentPassword(), loginUserDto);
        return Response.success(UserInfoResponse.fromUserDto(userDto));
    }


    //이메일 수정시 비밀번호 확인 : 완료
    //todo 회원정보수정에서이메일 수정시 비번 검증하고 이메일 수정해야함
    @PostMapping("/me/validate-password")//비밀번호를 아는 상태면 코드를 보낼수 있음 : 비밀번호를 몰라도 되지만 다음단계에서 비밀번호 필요해서 미리 검증해줌
    public Response<CodeIdResponse> sendCodeForEmail(@Validated @RequestBody UserEmailCodeRequest req, Authentication authentication) throws MessagingException, UnsupportedEncodingException {

        UserDto loginUserDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to UserDto class failed"));

        Integer codeId = userService.sendCodeToModifyEmail(req.getPassword(), loginUserDto, req.getEmail());

        return Response.success(CodeIdResponse.of(codeId));
    }


    //이메일 수정 : 완료
    @PostMapping("/me/email")//비밀번호를 아는 상태에서 코드를 맞춰야 이메일 변경가능
    public Response<UserInfoResponse> modifyMyEmail(@Validated @RequestBody UserMailModifyRequest req, Authentication authentication) {

        UserDto loginUserDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to UserDto class failed"));


        UserDto resultUserDto = userService.modifyMyEmail(req.getCodeId(), req.getCode(), req.getPassword(), loginUserDto);


        return Response.success(UserInfoResponse.fromUserDto(resultUserDto));
    }


    //    db에 쌓여있는 알람 가져오기
    @GetMapping("/me/alarm")
    public Response<Page<AlarmResponse>> getAlarms(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Authentication authentication) {

        UserDto loginUserDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to UserDto class failed"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("unRead").descending().and(Sort.by("id").descending()));
        Page<AlarmResponse> alarmResponsePage = userService.getAlarms(loginUserDto.getId(), pageable).map(AlarmResponse::fromAlarmDto);
        return Response.success(alarmResponsePage);
    }


    //알림 삭제
    @DeleteMapping("/me/alarm/{alarmId}")
    public Response<Void> deleteAlarm(@PathVariable Long alarmId, Authentication authentication) {
        UserDto loginUserDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to UserDto class failed"));

        userService.deleteAlarm(alarmId, loginUserDto);

        return Response.success();
    }


    //알림 read로 처리
    @PostMapping("/me/alarm/{alarmId}")
    public Response<Void> readAlarm(@PathVariable Long alarmId, Authentication authentication) {
        UserDto loginUserDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to UserDto class failed"));


        userService.readAlarm(loginUserDto.getId(), alarmId);
        return Response.success();
    }

    //안읽은 알림 있으면 true , 안읽은 알림 없으면 false
    @GetMapping("/me/alarm/check")
    public Response<Boolean> existUnReadAlarm(Authentication authentication){
        UserDto loginUserDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to UserDto class failed"));

        boolean existUnRead = userService.existUnReadAlarm(loginUserDto.getId());

        return Response.success(existUnRead);
    }

    //안읽은 알람 몇개 인지 개수 반환
    @GetMapping("/me/alarm/un-read")
    public Response<Long> countUnReadAlarm(Authentication authentication){
        UserDto loginUserDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to UserDto class failed"));

        Long cntUnReadAlarm = userService.countUnReadAlarm(loginUserDto.getId());

        return Response.success(cntUnReadAlarm);
    }

}
