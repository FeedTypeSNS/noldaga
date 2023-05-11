package com.noldaga.repository;


import com.noldaga.domain.userdto.UserRole;
import com.noldaga.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
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


}
