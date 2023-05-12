package com.noldaga.service;

import com.noldaga.controller.request.ChatRoomCreateRequest;
import com.noldaga.controller.request.ChatSendRequest;
import com.noldaga.controller.response.ChatRoomListResponse;
import com.noldaga.controller.response.ChatRoomResponse;
import com.noldaga.controller.response.ChatSendResponse;
import com.noldaga.domain.UserSimpleDto;
import com.noldaga.domain.chatdto.ChatDto;
import com.noldaga.domain.chatdto.ChatReadDto;
import com.noldaga.domain.chatdto.ChatRoomDto;
import com.noldaga.domain.entity.User;
import com.noldaga.domain.entity.chat.Chat;
import com.noldaga.domain.entity.chat.ChatRead;
import com.noldaga.domain.entity.chat.ChatRoom;
import com.noldaga.domain.entity.chat.JoinRoom;
import com.noldaga.domain.userdto.UserRole;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.Chat.ChatReadRepository;
import com.noldaga.repository.Chat.ChatRepository;
import com.noldaga.repository.Chat.ChatRoomRepository;
import com.noldaga.repository.Chat.JoinRoomRepository;
import com.noldaga.repository.UserRepository;
import com.noldaga.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.mail.Multipart;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final UserRepository userRepository;
    private final JoinRoomRepository joinRoomRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatReadRepository chatReadRepository;
    private final S3Uploader s3Uploader;
    private Map<Long, ChatRoom> chatRooms;


    public UserSimpleDto getMyData(String me){
        User user = userRepository.findByUsername(me).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", me)));

        return UserSimpleDto.fromEntity(user);
    }

    //내 채팅방들 불러오기(왼쪽) -> 즉 회원이 chat에 들어오면 반환해줄 정보
    public List<ChatRoomListResponse> findAllMyChatRoom(String me) {
        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(me).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", me)));

        List<ChatRoomListResponse> roomInfoList = new ArrayList<>(); //response에 사용..
        List<JoinRoom> myRoom = joinRoomRepository.findAllByUsers(user);/*.orElseThrow(() ->
                new SnsApplicationException(ErrorCode.CAN_NOT_FIND_CHATROOM, String.format("There is no room where \"%s\" joined ", me))); //내가 참가한 방들 리스트 반환
*/
        if (myRoom.size()>0) {
            for (int i = 0; i < myRoom.size(); i++) {
                ChatRoom chatRoom = myRoom.get(i).getRoom();    //자기가 참가된 i번째 방의
                if (chatRoom.getViewRoomName().equals(chatRoom.getRoomName())) {
                    String userViewName = ChatRoom.getViewName(chatRoom.getRoomName(), me);
                    chatRoom.setViewRoomName(userViewName);
                }//만약 따로 이름 설정을 하지 않았으면 이름은 회원별로 다르게 보여줘야함
                 //영수, 영희, 사랑 -> 영수가 볼땐 "영희, 사랑" & 영희가 볼땐 "영수, 사랑"으로 반환..

                int unread = getUnreadCount(chatRoom, user);
                log.info(user.getUsername()+"내가 안읽은 수 : "+unread);
                List<UserSimpleDto> joinPeoples; //각 방에 참가한 사람 리스트들 반환
                ChatDto recentChat;

                if (chatRoom != null) {
                    joinPeoples = getJpeoples(chatRoom);
                    recentChat = getRChat(chatRoom, joinPeoples.size());
                    roomInfoList.add(ChatRoomListResponse.returnResponse(ChatRoomDto.fromEntity(chatRoom), joinPeoples, recentChat,unread, joinPeoples.size()));
                } else {
                    //채팅방이 존재한다고 찾아졌는데 방정보가 없는건 중간에 실수로 제거된거거나, 연결된 부분들이 제거가 안된것
                    //joinRoom(me)에서나, chatRoom(roomId)에서 사라진 것이니.. 이걸찾아서 제거하는 로직이 필요할듯
                    throw new SnsApplicationException(ErrorCode.CAN_NOT_FIND_CHATROOM);
                }
            }


            Collections.sort(roomInfoList, Comparator.comparing((ChatRoomListResponse crr) -> crr.getRecentChat().getCreateAt()).reversed());
            //최근 채팅의 create at기준으로 내림차순 정렬
        }
            return roomInfoList;
    }




    //선택한 채팅방 하나 불러오기(채팅방 리스트에서 하나 클릭시 반환해줄 정보)
    @SuppressWarnings("unchecked")
    public ChatRoomResponse findOneChatRoom(String me, Long id) {
        User user = userRepository.findByUsername(me).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", me)));
        ChatRoom room = chatRoomRepository.findById(id).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.CAN_NOT_FIND_CHATROOM));
        userReadCheck(user, room); //불러와지니 즉 읽혀져야함
        List<UserSimpleDto> joinList = getJpeoples(room);
        Object[] result = getPastChatAndReader(room);
        List<ChatDto> pastChat = (List<ChatDto>) result[0];
        List<ChatReadDto> reader = (List<ChatReadDto>) result[1];
        String type = "EXISTROOM";

        if (room.getViewRoomName().equals(room.getRoomName())) { //만약 따로 이름 설정을 하지 않았으면 이름은 회원별로 다르게 보여줘야함
            String userViewName = ChatRoom.getViewName(room.getRoomName(), me);
            room.setViewRoomName(userViewName);
        }

        return ChatRoomResponse.returnResponse(ChatRoomDto.fromEntity(room), joinList, pastChat, reader, type);
    }


    //채팅방 생성하기
    @SuppressWarnings("unchecked")
    @Transactional
    public ChatRoomResponse createChatRoom(String me, ChatRoomCreateRequest request) {
        User user = userRepository.findByUsername(me).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", me)));

        List<String> nameList = request.getJoinUserList(); //사용자들..
        nameList.add(me); //나도 넣어줘야함
        Collections.sort(nameList); //정렬해서 저장해줘야 중복 값 없을 수 있음..(순서가 달라질 수도 선택에 따라)
        log.info("채팅방 이름 생성..\n"+ nameList);
        StringBuilder rn = new StringBuilder(); //초기 채팅방 이름 생성을 위한..
        for (int i = 0; i < nameList.size(); i++) {
            rn.append(nameList.get(i)).append(", ");
        }
        String roomName = rn.substring(0, rn.length() - 2); //마지막 ", "는 제거

        Optional<ChatRoom> chatRoom = chatRoomRepository.findByRoomName(roomName);
        ChatRoom newRoom;

        String type;
        ChatRoomDto chatRoomInfo;
        List<UserSimpleDto> joinPeople = new ArrayList<>();
        List<ChatDto> pastChat = new ArrayList<>();
        List<ChatReadDto> chatReads = new ArrayList<>();

        if (chatRoom.isPresent()) { //기존에 존재하는 채팅방을 불러올 경우
            newRoom = chatRoom.get(); //채팅방 정보
            String userViewName = ChatRoom.getViewName(roomName, me); //사람마다 보여주는 이름 달라져야함..
            log.info("기존에 존재하는 방: "+userViewName);
            newRoom.setViewRoomName(userViewName);
            userReadCheck(user, newRoom); //바로 들어가지니 나는 모두 읽었다 체크함
            joinPeople = getJpeoples(newRoom); //참가한 사람리스트 반환
            Object[] result = getPastChatAndReader(newRoom);
            pastChat = (List<ChatDto>) result[0];
            chatReads = (List<ChatReadDto>) result[1];
            chatRoomInfo = ChatRoomDto.fromEntity(newRoom);
            type = "EXISTROOM";
        } else { //처음 채팅방을 생성할 경우
            type = "NEWROOM";
            String[] names = roomName.split(", ");

            newRoom = chatRoomRepository.save(ChatRoom.of(roomName, roomName, nameList.size())); //채팅방 저장

            for (int i = 0; i < names.length; i++) {
                User join = userRepository.findByUsername(names[i]).orElseThrow(() ->
                        new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", me)));;
                joinRoomRepository.save(JoinRoom.of(newRoom, join)); //초대된 회원 joinRoom에 저장
                UserSimpleDto u = UserSimpleDto.fromEntity(join);
                joinPeople.add(u);
            } //채팅방 참가 회원 저장
            if (newRoom.getViewRoomName().equals(roomName)) { //만약 따로 이름 설정을 하지 않았으면 이름은 회원별로 다르게 보여줘야함
                String userViewName = ChatRoom.getViewName(roomName, me);
                log.info("새로저장된 방: "+userViewName);
                //newRoom.setViewRoomName(userViewName);
                chatRoomInfo = ChatRoomDto.fromEntity(newRoom);
                chatRoomInfo.setViewRoomName(userViewName);
            }else {
                chatRoomInfo = ChatRoomDto.fromEntity(newRoom);
            }
        }

        return ChatRoomResponse.returnResponse(chatRoomInfo, joinPeople, pastChat, chatReads, type);
    }


    //메시지 저장하기
    @Transactional
    public ChatSendResponse saveChat(String me, ChatSendRequest request, Long id) throws IOException {
            ChatRoom room = chatRoomRepository.findById(id).orElseThrow(() ->
                    new SnsApplicationException(ErrorCode.CAN_NOT_FIND_CHATROOM));

             if (request.getMsg().contains("/")){
            String[] parts = request.getMsg().split("/");
            String admin = parts[parts.length-1];
            if (admin.equals("ADMINSENDDELETE")){
                String msg = "/ADMINSENDDELETE";
                User user = userRepository.findFirstByRole(UserRole.valueOf("ADMIN")).get();
                Chat chat = chatRepository.save(Chat.of(room, user, request.getMsg().replace(msg, ""), room.getUserNum()));
                log.info("채팅: "+chat.getMsg());
                return ChatSendResponse.returnResponse(ChatDto.fromEntity(chat));
            }
        }//삭제시 메시지 저장하는거 따로 빼는게 날듯..

            User user = userRepository.findByUsername(me).orElseThrow(() ->
                    new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", me)));

            String msg = request.getMsg();

            Chat chat = chatRepository.save(Chat.of(room, user, msg, room.getUserNum() - 1)); //채팅 저장(자신은 읽은 거니 -1)
            //chatReadRepository.save(ChatRead.of(user, chat)); //보낸 사람은 읽음 처리 해줘야함
            log.info("채팅: "+chat.getMsg());
            return ChatSendResponse.returnResponse(ChatDto.fromEntity(chat)/*, receivers*/);
    }

    @Transactional
    public ChatSendResponse saveChatImg(String me, MultipartFile img, Long id) throws IOException {
        ChatRoom room = chatRoomRepository.findById(id).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.CAN_NOT_FIND_CHATROOM));

        User user = userRepository.findByUsername(me).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", me)));

        String imgurl = "CHATIMG|";
        imgurl += s3Uploader.upload(img, "/chat/img");

        Chat chat = chatRepository.save(Chat.of(room, user, imgurl, room.getUserNum() - 1)); //채팅 저장(자신은 읽은 거니 -1)
        //chatReadRepository.save(ChatRead.of(user, chat)); //보낸 사람은 읽음 처리 해줘야함
        log.info("채팅: "+chat.getMsg());
        return ChatSendResponse.returnResponse(ChatDto.fromEntity(chat)/*, receivers*/);
    }

    //메시지 삭제
    @Transactional
    public String deleteChat(String me, Long id) throws UnsupportedEncodingException {
        Chat chat = chatRepository.findById(id).orElseThrow(()->
                new SnsApplicationException(ErrorCode.CHAT_NOT_FIND)); //지울 채팅 찾기
        List<ChatRead> read = chatReadRepository.findAllByChat(chat); //읽은 내역도 삭제되야함


        if (chat.getMsg().contains("CHATIMG|")&&chat.getMsg().contains("https://kr.object.ncloudstorage.com/noldaga-s3/chat/img")){
            String msg = chat.getMsg().replace("CHATIMG|", "");
            s3Uploader.deleteImage(msg);
            log.info(msg);
        }

        chatReadRepository.deleteAllInBatch(read); //우선 읽은 내역 전체 삭제, casecade로 안함
        chatRepository.delete(chat); //이후 채팅 삭제
        //참조 무결성의 법칙으로, 만약 chat을 지울때 이를 참조하고 있는 테이블이 있으면 삭제가 안됨.. = 그래서 참조하는 chatRead 먼저 삭제

        //고민인게 소켓이면 실시간으로 사라지나? 아니면 사라지고 채팅리스트를 돌려줘야하나 확인해야함 프론트에서
        return " \" "+chat.getMsg()+" \" 가 성공적으로 삭제됬습니다.";
        //cascade, orphanRemovel은 회원 -> 회원상세처럼 완전히 개인 소유가 가능할 때 사용하는 옵션
        //따라서 지금과 같은 채팅의 경우, 온전한 본인의 것이 아니라 채팅방에 참가한 여러사람이 공유하기에..
        //따로 그냥 코드로 삭제하는게 좋을 것 같아 따로 지우는 처리를 해줌
    }


    //채팅방 삭제
    @Transactional
    public String deleteChatRoom(String me, Long id){
        User user = userRepository.findByUsername(me).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", me)));
        ChatRoom room = chatRoomRepository.findById(id).orElseThrow(()->
                new SnsApplicationException(ErrorCode.CAN_NOT_FIND_CHATROOM));
        userReadCheck(user, room); //삭제전 우선 모두 읽음 처리 시키기

        List<JoinRoom> people = joinRoomRepository.findAllByRoom(room);
        JoinRoom meJoin = joinRoomRepository.findByUsersAndRoom(user, room).orElseThrow(()->
                new SnsApplicationException(ErrorCode.ALREADY_OUT_ROOM));
        log.info("방에 참가한 사람들"+String.valueOf(people));

        List<Chat> chatList = chatRepository.findAllByRoom(room); //방안의 채팅 내역
        log.info("방에 채팅내역"+String.valueOf(chatList));
        if (room.getUserNum()==0) { throw new SnsApplicationException(ErrorCode.CAN_NOT_FIND_CHATROOM, "This room id already delete");
        }else {
            if (people.size() == 1 ) { //내가 마지막 남은 사람이면 내가 사라지면 전부 삭제해야함
                log.info("내가 마지막 사람");
                for (int i = 0; i < chatList.size(); i++) {
                    List<ChatRead> pRead = chatReadRepository.findAllByChat(chatList.get(i));
                    chatReadRepository.deleteAllInBatch(pRead);
                    //그냥 deleteAll은  delete 쿼리가 하나씩 날아감,
                    //deleteAllInBatch 한번에 실행되서 성능이 훨 좋음
                } //읽음 정보 모두 삭제
                chatRepository.deleteAllInBatch(chatList);
                joinRoomRepository.deleteAllInBatch(people);
                log.info("방 완전 삭제");
                chatRoomRepository.delete(room);
            } else { //아니라면 내 join 정보와 읽음 여부 정보 삭제 해줌 됨!, 이름 변경..
                log.info("나 말고 더 있음");
                for (int i = 0; i < chatList.size(); i++) {
                    List<ChatRead> meRead = chatReadRepository.findAllByChatAndReadUser(chatList.get(i), user);
                    chatReadRepository.deleteAllInBatch(meRead);
                    log.info("읽은 목록"+meRead);
                }//읽음 여부  내 부분 삭제
                log.info("나 조인 삭제: "+meJoin.getId());
                joinRoomRepository.delete(meJoin);
            }
        }

        return user.getUsername()+"님이 "+room.getViewRoomName()+" 방에서 나갔습니다.";
    } //join room에 모두가 나가야 삭제됨, 나간사람이 채팅한 거는 남겨두기로.. 추후에 삭제하든 정보를 null로 바꿔두든 할예정
      //내용만 바꾸게.. 초대는 일단 불가능하게 인스타처럼 설정

    @Transactional
    public void delChatRoomUserNum(int num){
        List<ChatRoom> delRoom = chatRoomRepository.findAllByUserNum(num);
        chatRoomRepository.deleteAllInBatch(delRoom);
    }
    @Transactional
    public void reSettingRoom(String me, Long id){
        //방이름을 통해 찾으니 함께 변경해줘야함
        Optional<ChatRoom> rooms = chatRoomRepository.findById(id);
        if(rooms.isPresent()) {
            ChatRoom room = rooms.get();
            List<JoinRoom> people = joinRoomRepository.findAllByRoom(room);


            String roomName = ChatRoom.getViewName(room.getRoomName(), me);
            String viewName = roomName;

            if (!roomName.contains(",")) {
                if (people.size() == 1 && people.get(0).getUsers().getUsername().equals(me)) {
                    viewName = "(알 수 없음)";
                } //회원이 탈퇴해서 사라졌을때
                else {
                    viewName = "대화상대 없음";
                }//회원이 방을 모두 나가서 나만 남았을 떄
            }
            room.alterRoomName(roomName, viewName);
            int roomNum = room.getUserNum();
            room.setUserNum(roomNum - 1);
            chatRoomRepository.save(room);
        }
    }

    //읽었는지 체크
    public void userReadCheck(User user, ChatRoom room) {
        List<Chat> chatlist = chatRepository.findAllByRoom(room);

        for (int i = 0; i < chatlist.size(); i++) {
            Chat chat = chatlist.get(i);
            readC(user, chat);
        }
    }

    public void userOneRead(User user, Long chatId){
        Chat chat = chatRepository.findById(chatId).get();
        readC(user, chat);
    }

    @Transactional
    public void readC(User user, Chat chat){
        if(!(chat.getSender().getId().equals(user.getId()))){ //내가 쓴글은 애초에 읽음 체크 안함..
            Optional<ChatRead> readCheck = chatReadRepository.findByChatAndReadUser(chat, user); //내가 읽었는지 체크
            int count = chat.getUnread();
            if (readCheck.isEmpty()) { //내이름과, 채팅정보로 읽은 존재가 없다 = 나는 읽은적 없다
                if (count > 0) {
                    chat.setUnread(chat.getUnread() - 1);
                    count = chatRepository.save(chat).getUnread();    //채팅방에 존재하는 메시지들의 읽음 수 -1

                    if (count > 0) { //만약 모두 읽지 않았다면, 내가 읽은 정보 저장해야함..
                        chatReadRepository.save(ChatRead.of(user, chat));
                    } else {//만약 모두 읽은 거라면 읽음 정보 모두 삭제, 내가 읽은게 마지막으로 읽음..
                        List<ChatRead> deleteData = chatReadRepository.findAllByChat(chat);
                        chatReadRepository.deleteAllInBatch(deleteData);
                    }
                }
            }
        }
    }

    //가장 최근 채팅이 없는 경우 관리자가 생성해 전달해줌..
    private Chat getChatFromAdmin(ChatRoom chatRoom, int jps) {
        Chat recentChat;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String rt = now.format(formatter); //현재 시간 포멧에 맞게 변화

        User admin = userRepository.findFirstByRole(UserRole.ADMIN).get();

        recentChat = Chat.of(chatRoom, admin, "채팅을 시작해보세요", jps);
        recentChat.setCreatedAt(LocalDateTime.parse(rt));
        //최근 메시지가 없는 경우 관리자가 임의의 메시지를 보내줘야, 정렬이 가능함..
        return recentChat;
    }

    //해당 채팅방의  참여한 사람 리스트 반환
    private List<UserSimpleDto> getJpeoples(ChatRoom chatRoom) {
        List<UserSimpleDto> joinPeoples = new ArrayList<>(); //각 방에 참가한 사람 리스트들 반환
        List<JoinRoom> jp = joinRoomRepository.findAllByRoom(chatRoom); //참가한 사람들
        for (int j = 0; j < jp.size(); j++) {
            joinPeoples.add(UserSimpleDto.fromEntity(jp.get(j).getUsers())); //참가한 사람 정보
        }
        return joinPeoples;
    }

    //해당 채팅방의  가장 최근 채팅 반환
    private ChatDto getRChat(ChatRoom chatRoom, int i) {
        Optional<Chat> rc = chatRepository.findFirstByRoomOrderByCreatedAtDesc(chatRoom);//방안의 가장 최근 채팅
        Chat recentChat = null;

        if(rc.isEmpty()){
            recentChat = getChatFromAdmin(chatRoom, i);
        }else {
            recentChat = rc.get();
        }

        return ChatDto.fromEntity(recentChat);
    }

    //과거 채팅 내역  + 채팅 읽은 사람 리스트 반환
    private Object[] getPastChatAndReader(ChatRoom chatRoom) {
        List<ChatDto> pastChat = new ArrayList<>();
        List<ChatReadDto> chatReads = new ArrayList<>();
        List<Chat> pc = chatRepository.findAllByRoomWithSort(chatRoom.getId()); //과거 채팅 내역 반환
        for (int i=0; i<pc.size(); i++){
            pastChat.add(ChatDto.fromEntity(pc.get(i)));
            List<ChatRead> cr = chatReadRepository.findAllByChat(pc.get(i)); //하나의 채팅에 대해 읽은 사람 반환
            for (int j=0; j<cr.size(); j++){
                chatReads.add(ChatReadDto.fromEntity(cr.get(j))); //그 안에 하나하나 넣어줌
            }
        }
        Object[] result = new Object[2];
        result[0] = pastChat;
        result[1] = chatReads;
        return result;
    }

    /*private String getViewName(String roomName, String name){
        String[] names = roomName.split(", ");
        String viewName = "";

        if (names[names.length - 1].equals(name)) {
            viewName = roomName.replaceAll(", " + name, ""); //이름이 마지막인 경우 앞의 , 와 함께 제거
        } else {
            viewName = roomName.replaceAll(name + ", ", ""); //이름이 마지막이 아닌 경우 뒤에 , 와 함께 제거

        }
        return viewName;
    }//me로 들어오*/

    public ChatRoomDto getChatRoomFromId(String me,Long id){
        ChatRoom room = chatRoomRepository.findById(id).orElseThrow(()->
                new SnsApplicationException(ErrorCode.CAN_NOT_FIND_CHATROOM));

        if (room.getViewRoomName().equals(room.getRoomName())) { //만약 따로 이름 설정을 하지 않았으면 이름은 회원별로 다르게 보여줘야함
            String userViewName = ChatRoom.getViewName(room.getRoomName(), me);
            room.setViewRoomName(userViewName);
        }

        return ChatRoomDto.fromEntity(room);
    }//ChatRoom정보 반환해주는 메소드..


    private int getUnreadCount(ChatRoom chatRoom, User user) {
        int c = 0;

        List<Chat> chatList = chatRepository.findAllByRoom(chatRoom); //모든 채팅 리스트 반환
        for(int i=0; i<chatList.size(); i++){
            if(chatList.get(i).getUnread()!=0){
                log.info("일치 여부: "+!(chatList.get(i).getSender().getId().equals(user.getId())));
                log.info("작성자: "+chatList.get(i).getSender().getId());
                if (!(chatList.get(i).getSender().getId().equals(user.getId()))){
                    Optional<ChatRead> cr = chatReadRepository.findByChatAndReadUser(chatList.get(i), user);
                    if (cr.isEmpty()){
                        c++;
                    }
                }
            }
        }

        return c;
    }//안읽은 채팅 수 반환

    /*public Boolean confirmInvitation(String me, Long id){
        User user = userRepository.findByUsername(me).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", me)));
        ChatRoom room = chatRoomRepository.findById(id).orElseThrow(()->
                new SnsApplicationException(ErrorCode.CAN_NOT_FIND_CHATROOM));
        Optional<JoinRoom> invite = joinRoomRepository.findByUsersAndRoom(user, room);
        if (invite.isPresent()){
            return true; //존재한다면 초대되어 있음..
        }else {
            return false;
        }
    }*/
    //chatRoom에 초대되어 있는 사람인지 확인하기..
}
