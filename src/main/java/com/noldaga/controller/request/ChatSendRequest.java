package com.noldaga.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor//test 에서 사용
@Getter //serialize 에 사용
@NoArgsConstructor //@RequestBody 매커니즘 의 Json 데이터 -> 자바객체   과정에서 사용됨 by jackson
public class ChatSendRequest {
    public enum MessageType{
        ENTER, TALK
    }
    private MessageType type;
    //private Long roomId;
    private String msg;
    //private String sender;
}
