package com.noldaga.controller;

import com.noldaga.domain.entity.User;
import com.noldaga.domain.entity.chat.ChatRoom;
import com.noldaga.domain.entity.chat.ChatSession;
import com.noldaga.domain.entity.chat.JoinRoom;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.Chat.ChatRoomRepository;
import com.noldaga.repository.Chat.ChatSessionRepository;
import com.noldaga.repository.Chat.JoinRoomRepository;
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
public class SocketHandler extends TextWebSocketHandler {
    HashMap<String, WebSocketSession> sessionMap = new HashMap<>(); //웹소켓 세션을 담아둘 맵
    //WebSocketSession을 직접 jpa에 넣는것은 불가능 -> WebSocketSession은 HTTP 요청과 연결된 객체이므로 엔티티로 직접 저장불가능
    //하지만 for 문을 돌려 찾는 것은 비효율적 -> 세션아이디만 받아와서 repository를 생성하고 이를 세션의 수명에 맞게 같이 관리하면
    //jpa를 통해 빠른 조회로 map과 비교해 세션을 찾아 보내줄 수 있음

    private final ChatRoomRepository chatRoomRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final UserRepository userRepository;
    private final JoinRoomRepository joinRoomRepository;
    public SocketHandler(ChatRoomRepository chatRoomRepository, ChatSessionRepository chatSessionRepository, UserRepository userRepository, JoinRoomRepository joinRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatSessionRepository = chatSessionRepository;
        this.userRepository = userRepository;
        this.joinRoomRepository = joinRoomRepository;
    }//repository 사용을 위한 생성자 주입 -> @Autowired는 권장하지 않는 방법..


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        //메시지 발송
        String msg = message.getPayload();
        JSONObject obj = jsonToObjectParser(msg);
        String uuid = getUuidFromJson(obj);


        Optional<ChatRoom> room = chatRoomRepository.findByUuid(uuid);
        if (room.isPresent()){//방이 존재하면 해당 방에 들어가있는 모든 사람들에게 전송해야함..
            List<ChatSession> sessions = chatSessionRepository.findAllByChatRoom(room.get());//세션 모두 찾기

            //해당 방에 들어간 세션들만 찾아서 메시지 발송
            for(ChatSession chatSession : sessions) {
                WebSocketSession wss = sessionMap.get(chatSession.getSessionId()); //세션 가져오기
                if (wss != null) {
                    try {
                        wss.sendMessage(new TextMessage(obj.toJSONString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            throw new SnsApplicationException(ErrorCode.CAN_NOT_FIND_CHATROOM); //존재하지 않는 방
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //소켓 연결
        super.afterConnectionEstablished(session);

        String url = session.getUri().toString();
        String[] splitUrl = url.split("/");

        String uuid = splitUrl[4];
        String name = URLDecoder.decode(splitUrl[5], "UTF-8");
        //한글을 인코딩해 보냈으니 디코딩해 반환해줘야 한다.
        log.info("방번호: "+uuid+"\n회원: "+name);

        User user = userRepository.findByUsername(name).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", name)));
        ChatRoom room = chatRoomRepository.findByUuid(uuid).orElseThrow(()->
                new SnsApplicationException(ErrorCode.CAN_NOT_FIND_CHATROOM));

        Optional<JoinRoom> joinAuth = joinRoomRepository.findByUsersAndRoom(user, room);
        //만약 방에 존재하지 않는 회원이라면 -> 허락받지 못한 회원이니 에러 던져줘야함..
        //이렇게 확인을 해주면 만약 방에 참가하지 않은 사용자가 어떻게 해킹해 고유의 uuid를 찾아
        //특정 채팅방에 들어갈려해도, 초대되지 않은 방이다보니 방에 들어갈 수 없어서 보안성 향상
        if (joinAuth.isPresent()) {
            sessionMap.put(session.getId(), session);
            chatSessionRepository.save(ChatSession.of(session.getId(), room));
            //세션 저장
            JSONObject obj = getEntter(user, room);//JSON 객체 생성(응답..)

            session.sendMessage(new TextMessage(obj.toJSONString()));
        }else{
            throw new SnsApplicationException(ErrorCode.NOT_ALLOW_IN_ROOM);
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //소켓 종료 ->
        Optional<ChatSession> closeSession = chatSessionRepository.findBySessionId(session.getId());
        if (closeSession.isPresent()) {
            chatSessionRepository.delete(closeSession.get());//세션 수명과 같이 관리하기 위해 삭제
            sessionMap.remove(session.getId()); //해당 세션값 삭제
            super.afterConnectionClosed(session, status);
        }else {
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

    private static String getUuidFromJson(JSONObject obj){
        JSONObject result = (JSONObject) obj.get("result");
        JSONObject chat = (JSONObject) result.get("chat");
        JSONObject room = (JSONObject) chat.get("room");
        return (String) room.get("uuid");
    }

    //enter 응답 생성
    @SuppressWarnings("unchecked")
    private JSONObject getEntter(User user, ChatRoom room) {
        JSONObject obj = new JSONObject();
        obj.put("resultCode", "SUCCESS");
        JSONObject result = new JSONObject();
        result.put("type", "ENTER");
        result.put("username", user.getUsername());
        /*result.put("roomName", room.getRoomName());*/
        obj.put("result", result);
        return obj;
    }

}
/*
WebSocketSession의 경우, 연결 &종료가 빈번하고 메시지를 보내기위한 검색이 빈번하다.
하지만 WebSocketSession은 HTTP 요청과 연결된 객체이므로 주로 HashMap에 의해 관리되고 사용된다.

여기서 채팅의 경우 각 방에 들어와있는 사람들만 서로 연결되어 대화가 주고받을 수 있어야한다.
방을 구분하는 걸 List<HashMap>과 같이 외부에서 방들을 구분해주게 된다면 비효율적이다.
   -> List<HashMap>을 사용하는 경우 검색 속도가 느리며, 데이터 무결성을 보장하지 않음, 또한 객체 지향적인 코드 작성이 어렵고, 불필요한 코드 양이 많아질 수 있음.

따라서, 생성, 삭제, 조회가 자주 일어나는 WebSocketSession에서 각 방안에서 채팅하기 위해서는
고유의 값인 seesion.getId()를 이용해 JPA에 저장하여 각 crd가 일어날때 고유값을 통해
HashMap의 키값으로 WebSocketSession을 찾아와 사용하는 것이 더 효율적이다 생각해 위와 같은 코드로 작성했다.

-> JPA+HashMap을 통해 List<HashMap>에 비해
    1. 검색 속도: JPA는 내부적으로 캐싱과 인덱싱을 사용하여 검색 속도를 빠름
    2. 객체 지향적인 코드: JPA는 객체 지향적인 코드 작성을 촉진, 이는 코드 유지 보수성과 가독성을 향상
    3. 데이터 무결성: JPA는 데이터베이스 무결성 제약 조건을 강제 적용하므로 데이터 일관성과 무결성이 유지
    4. 불필요한 코드 감소: JPA는 CRUD 작업을 처리하는 데 필요한 코드 양을 줄임
    과 같은 장점으로 효율성과 무결성을 얻을 수 있다..
*/
