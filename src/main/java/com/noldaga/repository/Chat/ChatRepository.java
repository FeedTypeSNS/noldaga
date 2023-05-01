package com.noldaga.repository.Chat;

import com.noldaga.domain.entity.chat.Chat;
import com.noldaga.domain.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findFirstByRoomOrderByCreatedAtDesc(ChatRoom room); //방에서 가장 최근 문자

    @Query("SELECT c FROM Chat c WHERE c.room.id = :id ORDER BY c.createdAt ASC")
    List<Chat> findAllByRoomWithSort(Long id);  //여기 수정해야할 수 도 있음 조심!,채팅룸 기준으로 보낸시간으로 정렬해서 반환받기

    List<Chat> findAllByRoom(ChatRoom room);

}
