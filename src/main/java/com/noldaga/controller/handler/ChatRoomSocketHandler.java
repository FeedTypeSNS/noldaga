package com.noldaga.controller.handler;

import com.noldaga.domain.entity.User;
import com.noldaga.domain.entity.chat.ChatRoom;
import com.noldaga.domain.entity.chat.JoinRoom;
import com.noldaga.domain.entity.chat.UserSession;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.Chat.ChatRoomRepository;
import com.noldaga.repository.Chat.JoinRoomRepository;
import com.noldaga.repository.Chat.UserSessionRepository;
import com.noldaga.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Log4j2
@Component
public class ChatRoomSocketHandler extends TextWebSocketHandler {
    HashMap<String, WebSocketSession> sessionMap = new HashMap<>();

    private final ChatRoomRepository chatRoomRepository;
    private final UserSessionRepository userSessionRepository;
    private final UserRepository userRepository;
    private final JoinRoomRepository joinRoomRepository;

    public ChatRoomSocketHandler(ChatRoomRepository chatRoomRepository, UserSessionRepository userSessionRepository, UserRepository userRepository, JoinRoomRepository joinRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.userSessionRepository = userSessionRepository;
        this.userRepository = userRepository;
        this.joinRoomRepository = joinRoomRepository;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        //메시지 발송
        String msg = message.getPayload();
        JSONObject obj = jsonToObjectParser(msg);
        JSONObject result = (JSONObject) obj.get("result");
        String type = (String) result.get("type");
        log.info("메시지: " + msg);
        log.info("타입: " + type);
        JSONObject rooms = (JSONObject) result.get("roomInfo");
        String uuid =  rooms.get("uuid").toString();

        Optional<ChatRoom> room = chatRoomRepository.findByUuid(uuid);

        if (room.isPresent()) {//방이 존재하면 해당 방에 들어가있는 현재 접속 중인 모든 사람들에게 전송해야함..
            List<JoinRoom> users = joinRoomRepository.findAllByRoom(room.get());//세션 모두 찾기
            //해당 방에 들어간 세션들만 찾아서 메시지 발송
            for (int i = 0; i < users.size(); i++) {
                Optional<UserSession> us = userSessionRepository.findByUser(users.get(i).getUsers());
                if (us.isPresent()) {
                    WebSocketSession wss = sessionMap.get(us.get().getSessionId());
                    if (wss != null) {
                        try {
                            wss.sendMessage(new TextMessage(obj.toJSONString()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            throw new SnsApplicationException(ErrorCode.CAN_NOT_FIND_CHATROOM); //존재하지 않는 방
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //소켓 연결
        super.afterConnectionEstablished(session);

        String url = session.getUri().toString();
        String[] splitUrl = url.split("/");
        String name = URLDecoder.decode(splitUrl[4], "UTF-8");
        log.info("확인 이름 : " + name);
        //한글을 인코딩해 보냈으니 디코딩해 반환해줘야 한다.

        log.info("채팅창 입성: " + name);
        User user = userRepository.findByUsername(name).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", name)));

        sessionMap.put(session.getId(), session);
        userSessionRepository.save(UserSession.of(session.getId(), user));
        //세션 저장
        JSONObject obj = getEntter(user, session);//JSON 객체 생성(응답..)
        log.info("확인 이름 : " + obj);
        session.sendMessage(new TextMessage(obj.toJSONString()));
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //소켓 종료 ->
        Optional<UserSession> closeSession = userSessionRepository.findBySessionId(session.getId());
        if (closeSession.isPresent()) {
            userSessionRepository.delete(closeSession.get());//세션 수명과 같이 관리하기 위해 삭제
            sessionMap.remove(session.getId()); //해당 세션값 삭제
            super.afterConnectionClosed(session, status);
        } else {
            throw new SnsApplicationException(ErrorCode.ENDED_SESSION); //이미 종료된 세션일 경우 오류 발생 필요..
        }
    }

    //json 변환
    private static JSONObject jsonToObjectParser(String jsonStr) {
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(jsonStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return obj;
    }

   /* private static String getUuidFromJson(JSONObject obj){
        JSONObject result = (JSONObject) obj.get("result");
        //JSONObject chat = (JSONObject) result.get("chat");
        JSONObject room = (JSONObject) result.get("roomInfo");
        return (String) room.get("uuid");
    }*/

    //enter 응답 생성 -> 입장
    @SuppressWarnings("unchecked")
    private JSONObject getEntter(User user, WebSocketSession session) {
        JSONObject obj = new JSONObject();
        obj.put("resultCode", "SUCCESS");
        JSONObject result = new JSONObject();
        result.put("type", "ENTER");
        result.put("username", user.getUsername());
        result.put("sessionId", session.getId());
        obj.put("result", result);
        return obj;
    }
}
