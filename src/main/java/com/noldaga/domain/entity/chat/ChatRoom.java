package com.noldaga.domain.entity.chat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

@EntityListeners(AuditingEntityListener.class) //자동으로 시간을 매핑하여 테이블에 넣어주는 jpa auditing 사용
@Getter //dto 생성시 사용
@ToString
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //채팅방 번호 - 서버에서 관리
    //uuid로 사용해도 되나, 사용자에게 보여질 방 번호를 uuid로 할 경우
    //가시적으로도 좋지 않고, 탈취당할 확률이 있어서 임시 번호 개념으로 넣어둠..

    @Column(updatable = false)
    private String uuid;
    //네트워크 상에서는 id를 관리하는 중앙관리자가 없어서 중복확인이 불가능하다.
    //따라서 네트워크 상에서 중복되지 않는 id를 가지려면
    //중복되는 확률이 0에가까운 id 필요 -> 소켓의 경우 네트워크를 통해 하니 추가..

    @Setter
    @Column
    private String roomName; //생성될때 채팅방 이름


    @Setter
    @Column
    private String viewRoomName; //채팅방 이름 (변경가능한..)

    @Column
    @Setter
    private int userNum; //방에 참가한 회원 수


    protected ChatRoom(){}

    public ChatRoom(Long id,String uuid, String name, String uname, int userNum){
        this.id = id;
        this.uuid = uuid;
        this.roomName = name;
        this.viewRoomName = uname;
        this.userNum = userNum;
    }

    public static ChatRoom of(String name,String uuid, String viewName, int userNum){
        return new ChatRoom(null,uuid, name, viewName, userNum);
    }

    public void alterRoomName(String roomName, String viewRoomName){
        this.roomName = roomName;
        this.viewRoomName = viewRoomName;
    }

    public static String getViewName(String roomName, String name){
        String[] names = roomName.split(", ");
        String viewRoomName = "";

        if (names[names.length - 1].equals(name)) {
            viewRoomName = roomName.replaceAll(", " + name, ""); //이름이 마지막인 경우 앞의 , 와 함께 제거
        } else {
            viewRoomName = roomName.replaceAll(name + ", ", ""); //이름이 마지막이 아닌 경우 뒤에 , 와 함께 제거

        }
        return viewRoomName;
    }

}

/* 다대다 연결 관계를 없애기 위해서 아래와 같이 설정
 메시지 - 채팅방 정보 - 채팅방에 참가한 사람들 정보
 chat  - chatRoom  - JoinRoom

*/
