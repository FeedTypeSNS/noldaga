package com.noldaga.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor//test 에서 사용
@Getter
@NoArgsConstructor
public class ChatSendRequest {
    public enum MessageType{
        ENTER, TALK
    }
    private MessageType type;
    //private Long roomId;
    private String msg;
    //private String sender;
}
