package com.noldaga.controller.handler;

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
import com.noldaga.service.ChatService;
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
public class ChattingSocketHandler extends TextWebSocketHandler {
    HashMap<String, WebSocketSession> sessionMap = new HashMap<>(); // 현재 연결된 WebSocketSession 관리
    // WebSocketSession은 영속화할 수 없는 객체이므로 메모리에서 관리한다.
    // 대신 sessionId를 ChatSession 엔티티에 저장하여
    // 사용자, 채팅방, 접속 세션의 관계를 관리한다.
    // 메시지 전송 시에는 ChatSession으로 대상 사용자를 조회한 뒤,
    // sessionId를 통해 실제 WebSocketSession을 찾아 전송한다.

    private final ChatRoomRepository chatRoomRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final UserRepository userRepository;
    private final JoinRoomRepository joinRoomRepository;
    private final ChatService chatService;
    public ChattingSocketHandler(ChatRoomRepository chatRoomRepository, ChatSessionRepository chatSessionRepository, UserRepository userRepository, JoinRoomRepository joinRoomRepository, ChatService chatService) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatSessionRepository = chatSessionRepository;
        this.userRepository = userRepository;
        this.joinRoomRepository = joinRoomRepository;
        this.chatService = chatService;
    }//repository 사용을 위한 생성자 주입 -> @Autowired는 권장하지 않는 방법..


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        //메시지 발송
        String msg = message.getPayload();
        JSONObject obj = jsonToObjectParser(msg);

        JSONObject result = (JSONObject) obj.get("result");
        JSONObject chat = (JSONObject) result.get("chat");
        JSONObject rooms = (JSONObject) chat.get("room");

        Long chatId = (Long)chat.get("id");
        String uuid = (String) rooms.get("uuid");


        Optional<ChatRoom> room = chatRoomRepository.findByUuid(uuid);
        if (room.isPresent()){//방이 존재하면 해당 방에 들어가있는 모든 사람들에게 전송해야함..
            List<ChatSession> sessions = chatSessionRepository.findAllByChatRoom(room.get());//세션 모두 찾기
            //해당 방에 들어간 세션들만 찾아서 메시지 발송
            for(ChatSession chatSession : sessions) {
                chatService.userOneRead(chatSession.getUser(), chatId);
                log.info("보내는 데이터(읽음 수 확인):\n"+obj.toJSONString());
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


        Optional<JoinRoom> joinAuth = joinRoomRepository.findByUsersAndUuid(user, room.getUuid());
        //만약 방에 존재하지 않는 회원이라면 -> 허락받지 못한 회원이니 에러 던져줘야함..
        //이렇게 확인을 해주면 만약 방에 참가하지 않은 사용자가 어떻게 해킹해 고유의 uuid를 찾아
        //특정 채팅방에 들어갈려해도, 초대되지 않은 방이다보니 방에 들어갈 수 없어서 보안성 향상
        if (joinAuth.isPresent()) {
            sessionMap.put(session.getId(), session);
            chatSessionRepository.save(ChatSession.of(session.getId(), room, user));
            //세션 저장
            JSONObject obj = getEntter(user, room, session);//JSON 객체 생성(응답..)

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

    //enter 응답 생성
    @SuppressWarnings("unchecked")
    private JSONObject getEntter(User user, ChatRoom room, WebSocketSession session) {
        JSONObject obj = new JSONObject();
        obj.put("resultCode", "SUCCESS");
        JSONObject result = new JSONObject();
        result.put("type", "ENTER");
        result.put("username", user.getUsername());
        result.put("sessionId", session.getId());
        /*result.put("roomName", room.getRoomName());*/
        obj.put("result", result);
        return obj;
    }

}
/*
WebSocketSession은 영속화할 수 없는 객체이므로 HashMap에서 관리한다.

다만 채팅 서비스에서는 단순히 세션만 관리하는 것이 아니라
현재 어떤 사용자가 어떤 채팅방에 접속 중인지 알아야 한다.

이를 위해 sessionId, User, ChatRoom 정보를
ChatSession 엔티티로 관리하였다.

HashMap은 실제 WebSocketSession 조회를 담당하고,
ChatSession은 채팅방 참여자 조회 및 읽음 처리와 같은
비즈니스 로직을 수행하기 위한 연결 정보를 제공한다.
*/

