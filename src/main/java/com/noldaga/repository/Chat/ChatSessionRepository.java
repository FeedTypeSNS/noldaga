package com.noldaga.repository.Chat;

import com.noldaga.domain.entity.chat.ChatRoom;
import com.noldaga.domain.entity.chat.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatSessionRepository extends JpaRepository<ChatSession, String> {

    List<ChatSession> findAllByChatRoom(ChatRoom chatRoom);

    Optional<ChatSession> findBySessionId(String s);

}
