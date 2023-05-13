package com.noldaga.repository.HashTag;

import com.noldaga.domain.entity.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserTagRepository extends JpaRepository<UserTag, Long> {

    @Query("select ut from UserTag ut where ut.user.id=:userId")
    List<UserTag> findByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("delete from UserTag ut where ut.user.id=:userId")
    void deleteByUserId(Long userId);
}
