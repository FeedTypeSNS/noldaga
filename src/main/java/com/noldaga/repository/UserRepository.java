package com.noldaga.repository;


import com.noldaga.domain.userdto.UserRole;
import com.noldaga.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findFirstByRole(UserRole role);
    Optional<User> findByEmail(String email);
}
