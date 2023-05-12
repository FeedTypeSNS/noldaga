package com.noldaga.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@AllArgsConstructor//test 에서 사용
@Getter
@NoArgsConstructor
public class ChatSendRequest {

    //private MessageType type;
    //private Long roomId;
    private String msg;
    //private MultipartFile img;

    //private String sender;
}
