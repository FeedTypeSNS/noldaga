package com.noldaga.controller;

import com.noldaga.controller.request.ChatRoomCreateRequest;
import com.noldaga.controller.request.ChatSendRequest;
import com.noldaga.controller.response.ChatRoomListResponse;
import com.noldaga.controller.response.ChatRoomResponse;
import com.noldaga.controller.response.ChatSendResponse;
import com.noldaga.controller.response.Response;
import com.noldaga.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RestController
//@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    //기본 chat service test
    //json으로 반환하기 위해 추가
    @GetMapping("/api/chat/list")
    public Response<List<ChatRoomListResponse>> getMyChatRoomList(Authentication authentication){
        List<ChatRoomListResponse> result = chatService.findAllMyChatRoom(authentication.getName());
        return Response.success(result);
    }//내 채팅방 리스트 불러오기

    @GetMapping("/api/chat/{roomId}")
    public Response<ChatRoomResponse> getOneChatRoom(Authentication authentication, @PathVariable Long roomId){
        ChatRoomResponse result = chatService.findOneChatRoom(authentication.getName(), roomId);
        return Response.success(result);
    }//채팅방 상세보기? (한개 채팅방 들어가기)

    @PostMapping("/api/chat")
    public Response<ChatRoomResponse> createChatRoom(Authentication authentication, @RequestBody ChatRoomCreateRequest request){
        ChatRoomResponse result = chatService.createChatRoom(authentication.getName(), request);
        return Response.success(result);
    }//채팅방 생성하기

    @PostMapping("/api/chat/{roomId}/message")
    public Response<ChatSendResponse> saveMessage(Authentication authentication, @RequestBody ChatSendRequest request, @PathVariable Long roomId){
        ChatSendResponse result = chatService.saveChat(authentication.getName(), request, roomId);
        return Response.success(result);
    }//메시지 보내기 = 보내자마자 저장되야함..

    @DeleteMapping("/api/chat/{roomId}/message/{chatId}")
    public Response<String> deleteMessage(Authentication authentication, @PathVariable Long chatId){
        String result = chatService.deleteChat(authentication.getName(), chatId);
        return Response.success(result);
    }//메시지 삭제

    @DeleteMapping("/api/chat/{roomId}")
    public Response<String> deleteChatRoom(Authentication authentication, @PathVariable Long roomId){
        String result = chatService.deleteChatRoom(authentication.getName(), roomId);
        return Response.success(result);
    }//채팅방 삭제





}
