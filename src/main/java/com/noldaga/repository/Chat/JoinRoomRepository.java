package com.noldaga.repository.Chat;

import com.noldaga.domain.entity.chat.ChatRoom;
import com.noldaga.domain.entity.chat.JoinRoom;
import com.noldaga.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JoinRoomRepository extends JpaRepository<JoinRoom, Long> {

    List<JoinRoom> findAllByUsers(User user);

    List<JoinRoom> findAllByRoom(ChatRoom room);

    Optional<JoinRoom> findByUsers(User user);

    Optional<JoinRoom> findByUsersAndRoom(User user, ChatRoom room);
}
