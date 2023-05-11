package com.noldaga.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_CODE(HttpStatus.UNAUTHORIZED,"Code is invalid"),
    INVALID_CODE_ID(HttpStatus.UNAUTHORIZED, "CodeId is invalid"),
    INVALID_EMAIL(HttpStatus.UNAUTHORIZED, "Email is not authenticated"),
    DUPLICATED_USERNAME(HttpStatus.CONFLICT,"User Id is duplicated"), //회원가입시 이미 회원가입된 username
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"), //로그인시 회원가입이 안된 username
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid"), //로그인시 틀린 password

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid"),//토큰필터개발에서 추가됨

    FEED_NOT_FOUND(HttpStatus.NOT_FOUND, "Feed not founded"),//피드수정개발할때 추가됨
    INVALID_PERMISSION(HttpStatus.FORBIDDEN, "Permission is invalid"),//피드수정개발할때 추가됨.

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),//런타임 예외등(UserService 회원가입 트랜잭션 런타임예외 하면서 추가한듯.?)

    //feedLike
    ALREADY_LIKED(HttpStatus.CONFLICT, "User has already liked the Feed"), //feedLike 개발하면서 추가
    FEEDLIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "FeedLike not founded"),
    COMMENTLIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "CommentLike not founded"),

    //follow
    CAN_NOT_FOLLOW_SELF(HttpStatus.CONFLICT, "User cannot follow yourself"),
    CAN_NOT_UNFOLLOW_SELF(HttpStatus.CONFLICT, "User cannot unfollow yourself"),
    ALREADY_FOLLOW(HttpStatus.CONFLICT, "User has already follow"),
    ALREADY_UNFOLLOW(HttpStatus.CONFLICT, "User has already unfollow"),

    //comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment not founded"),

    //Chat
    CAN_NOT_FIND_CHATROOM(HttpStatus.NOT_FOUND, "This chat room does not exist"),
    CHAT_NOT_FIND(HttpStatus.NOT_FOUND, "This chat des not exist"),
    ALREADY_OUT_ROOM(HttpStatus.CONFLICT, "User has already out this room"),
    NOT_ALLOW_IN_ROOM(HttpStatus.UNAUTHORIZED, "User do not have permission to enter the room"),


    UPLOAD_SIZE_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE,"File size too large"),

    INVALID_DATA_VALUE(HttpStatus.BAD_REQUEST,"Invalid data value"), //@Validated 실패시


    ENDED_SESSION(HttpStatus.NOT_FOUND, "This Session is already close"),

    //Group
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "Group not founded"),//피드수정개발할때 추가됨

    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "No Account associated with this email"),

    EMAIL_LIMIT_EXCEEDED(HttpStatus.CONFLICT,"This Email has exceeded the limit"),

    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "Alarm not founded"),
    ;

    private HttpStatus status;
    private String message;
}
