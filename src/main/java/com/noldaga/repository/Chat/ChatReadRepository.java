package com.noldaga.repository.Chat;

import com.noldaga.domain.entity.chat.Chat;
import com.noldaga.domain.entity.chat.ChatRead;
import com.noldaga.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatReadRepository extends JpaRepository<ChatRead, Long> {
    Optional<ChatRead> findByChatAndReadUser(Chat chat, User user);
    List<ChatRead> findAllByChat(Chat chat);
    List<ChatRead> findAllByChatAndReadUser(Chat chat, User user);
}
