package com.noldaga.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    DUPLICATED_USER_ID(HttpStatus.CONFLICT,"User Id is duplicated"), //회원가입시 이미 회원가입된 username
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"), //로그인시 회원가입이 안된 username
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid"), //로그인시 틀린 password

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid"),//토큰필터개발에서 추가됨

    FEED_NOT_FOUND(HttpStatus.NOT_FOUND, "Feed not founded"),//피드수정개발할때 추가됨
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "Permission is invalid"),//피드수정개발할때 추가됨.

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),//런타임 예외등(UserService 회원가입 트랜잭션 런타임예외 하면서 추가한듯.?)

    ALREADY_LIKED(HttpStatus.CONFLICT, "User has already liked the Feed"), //feedLike 개발하면서 추가

    ;

    private HttpStatus status;
    private String message;
}
