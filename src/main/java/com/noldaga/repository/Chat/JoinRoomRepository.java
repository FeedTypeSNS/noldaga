package com.noldaga.repository.Chat;

import com.noldaga.domain.entity.User;
import com.noldaga.domain.entity.chat.JoinRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JoinRoomRepository extends JpaRepository<JoinRoom, Long> {

    List<JoinRoom> findAllByUsers(User user);

    List<JoinRoom> findAllByUuid(String uuid);

    Optional<JoinRoom> findByUsers(User user);

    Optional<JoinRoom> findByUsersAndUuid(User user, String uuid);
}
