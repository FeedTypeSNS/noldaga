package com.noldaga.repository;

import com.noldaga.domain.entity.StoreFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface StoreFeedRepository extends JpaRepository<StoreFeed, Long> {

    @Query("select count(sf) from StoreFeed sf where sf.feed.id=:feedId and sf.user.id=:userId")
    int findByFeedIdAndUsername(Long feedId, Long userId);

    @Modifying
    @Transactional
    @Query("delete from StoreFeed sf where sf.feed.id=:feedId and sf.user.id=:userId")
    void deleteByFeedIdAndUserId(Long feedId, Long userId);
}
