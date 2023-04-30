package com.noldaga.repository;

import com.noldaga.domain.entity.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

    @Query("select count(fl) from FeedLike fl where fl.feed.id=:feedId and fl.user.id=:userId")
    int findByFeedIdAndUsername(Long feedId, Long userId);

    @Modifying
    @Transactional
    @Query("delete from FeedLike fl where fl.feed.id=:feedId and fl.user.id=:userId")
    void deleteByFeedIdAndUserId(Long feedId, Long userId);
}
