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
    DUPLICATED_USERNAME(HttpStatus.CONFLICT,"User Id is duplicated"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid"),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid"),

    FEED_NOT_FOUND(HttpStatus.NOT_FOUND, "Feed not founded"),
    INVALID_PERMISSION(HttpStatus.FORBIDDEN, "Permission is invalid"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),

    //feedLike
    ALREADY_LIKED(HttpStatus.CONFLICT, "User has already liked the Feed"),
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

    INVALID_DATA_VALUE(HttpStatus.BAD_REQUEST,"Invalid data value"),


    ENDED_SESSION(HttpStatus.NOT_FOUND, "This Session is already close"),

    //Group
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "Group not founded"),


    //File
    CONVERT_MULTIPART_FILE_FAIL(HttpStatus.CONFLICT, "Conversion of multipart file failed."),
    FILE_NOT_EXIST(HttpStatus.BAD_REQUEST, "This file does not exist."),
    FILE_FORMAT_INVALID(HttpStatus.BAD_REQUEST, "Wrong image format. (Must be jpg,jpeg,png format)"),

    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "No Account associated with this email"),

    EMAIL_LIMIT_EXCEEDED(HttpStatus.CONFLICT,"This Email has exceeded the limit"),

    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "Alarm not founded"),
    INVALID_GROUP_ID(HttpStatus.UNAUTHORIZED,"Invalid Group Id"),
    ALREADY_JOINED(HttpStatus.CONFLICT,"Already joined" );

    private HttpStatus status;
    private String message;
}
