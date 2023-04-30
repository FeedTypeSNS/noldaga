package com.noldaga.repository.Chat;

import com.noldaga.domain.entity.Chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findById(Long id);
    Optional<ChatRoom> findByRoomName(String name);
}
