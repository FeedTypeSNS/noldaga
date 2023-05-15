package com.noldaga.repository;

import com.noldaga.domain.entity.recommend.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
    Optional<UserInterest> findByUserId(Long userId);
}
