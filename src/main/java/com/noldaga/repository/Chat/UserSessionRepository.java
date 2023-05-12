package com.noldaga.repository.Chat;

import com.noldaga.domain.entity.User;
import com.noldaga.domain.entity.chat.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, String> {

    Optional<UserSession> findByUser(User user);
    Optional<UserSession> findBySessionId(String id);
}
