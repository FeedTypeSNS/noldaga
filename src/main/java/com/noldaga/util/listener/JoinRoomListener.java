/*
package com.noldaga.util.listener;

import com.noldaga.domain.entity.User;
import com.noldaga.domain.entity.chat.ChatRoom;
import com.noldaga.domain.entity.chat.JoinRoom;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Configuration
@Component
public class JoinRoomListener {

    @PersistenceContext
    private EntityManager entityManager;

    @PreRemove
    public void preRemove(Object object) {
        if (object instanceof User) {
            User user = (User) object;
            List<JoinRoom> joinRooms = entityManager.createQuery("SELECT jr FROM JoinRoom jr WHERE jr.users = :user", JoinRoom.class)
                    .setParameter("user", user)
                    .getResultList();

            for (JoinRoom joinRoom : joinRooms) {
                ChatRoom chatRoom = joinRoom.getRoom();
                if (chatRoom != null) {
                    User me = joinRoom.getUsers();
                    String roomName = chatRoom.getRoomName();
                    String viewName = chatRoom.getViewRoomName();
                    if (chatRoom.getUserNum() > 1 && roomName.equals(viewName)) { //한 명 이상이면 그룹방에서 방을 나간거니 나간사람 이름 제외시켜줘야함
                        String newName = ChatRoom.getViewName(roomName, me.getUsername());
                        chatRoom.setRoomName(newName);
                        chatRoom.setViewRoomName(newName);
                    } else {//두 명인 방에서 한명이 탈퇴하면 대화상대를 알 수 없음..
                        chatRoom.setViewRoomName("(알 수 없음)");
                    }
                    entityManager.persist(chatRoom);//변경된 방이름 저장
                    entityManager.flush();
                }
                entityManager.remove(joinRoom); //변경된 joinRoom은 삭제
            }
        }
    } //회원이 탈퇴하는 경우 방이름을 변경 해줌


    @PostUpdate
    @PostRemove
    public void updateUserNum(JoinRoom joinRoom) {
        ChatRoom chatRoom = joinRoom.getRoom();
        if (chatRoom!=null) {
            int userNum = entityManager.createQuery("SELECT COUNT(jr) FROM JoinRoom jr WHERE jr.room.id = :room", Integer.class)
                    .setParameter("room", chatRoom.getId())
                    .getSingleResult();
            chatRoom.setUserNum(userNum);
            entityManager.persist(chatRoom);
            if (chatRoom.getUserNum() == 0) {
                entityManager.remove(chatRoom);
            }//만약 joinroom에 존재하는 회원이 없다면 해당 방도 삭제
        }
    }
    //방에 참가한 인원 수 자동 업데이트
    */
/*JoinRoom 엔티티의 생성/제거 시점에 ChatRoom 엔티티의 UserNum 값을 업데이트
      EntityManager를 사용하여 JoinRoom 엔티티를 기반으로 ChatRoom 엔티티의 userNum 값을 조회한 후,
      ChatRoom 엔티티의 userNum 값을 업데이트하고, EntityManager를 사용하여 변경사항을 저장*//*



}
//JPA Entity에서 발생하는 이벤트를 처리할 수 있는 리스너 클래스
//이벤트 처리 메서드를 포함하고 있으며, 이 메서드는 JPA Entity에서 이벤트가 발생할 때 자동으로 호출


//전체 흐름 순서
*/
/*1. 유저가 삭제될 때 해당 유저를 가지고 있는 모든 JoinRoom을 조회하고,
     각 JoinRoom이 속한 ChatRoom의 이름을 변경한 후에 변경된 ChatRoom을 저장한다.
     그 후 joinRoom을 삭제 해야하니 updateUserNum이 실행되고 회원수가 0인경우에는
     해당 chatRoom도 삭제한 후 마지막으로 해당 JoinRoom을 삭제합니다.

     따라서 유저가 삭제되면 해당 유저를 가지고 있는 모든 JoinRoom의 ChatRoom의 정보가 업데이트되고, JoinRoom도 삭제된다.

*/

