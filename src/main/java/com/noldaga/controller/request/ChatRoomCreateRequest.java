package com.noldaga.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor//test 에서 사용
@Getter
@NoArgsConstructor
public class ChatRoomCreateRequest {
    private List<String> joinUserList;  //참가할 사람들 리스트
}
