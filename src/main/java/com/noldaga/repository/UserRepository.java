package com.noldaga.repository;


import com.noldaga.domain.userdto.UserRole;
import com.noldaga.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findFirstByRole(UserRole role);
    Optional<User> findByEmail(String email);

    @Query("SELECT u.username from User u where u.email= :email") //users 가 아닌 User로 해줘야함 (엔티티 클래스 이름으로 해줘야함)
    List<String> findAllUsernameByEmail(String email);

    @Query("SELECT COUNT(*) FROM User u where u.email= :email")
    int countByEmail(String email);

    @Query("SELECT u from User u where u.username like %?1%")
    List<User> findAllBySearch(String q);

    //가장 팔로워 수 많은 가입자
    @Query(nativeQuery = true, value = "select * from users u order by u.total_follower DESC LIMIT :n, 1")
    Optional<User> findNthByOrderByTotalFollowerDesc(Long n);
    //가장 팔로잉 수 많은 가입자
    @Query(nativeQuery = true, value = "select * from users u order by u.total_follower DESC LIMIT :n, 1")
    Optional<User> findNthByOrderByTotalFollowingDesc(Long n);

    //Optional<User> findFirstByOrderByCreatedAtDesc();
}
