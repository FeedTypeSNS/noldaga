package com.noldaga.repository;

import com.noldaga.domain.entity.CommentLike;
import com.noldaga.domain.entity.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    @Query("select count(cl) from CommentLike cl where cl.comment.id=:commentId and cl.user.id=:userId")
    int findByFeedIdAndUsername(Long commentId, Long userId);

    @Modifying
    @Transactional
    @Query("delete from CommentLike cl where cl.comment.id=:commentId and cl.user.id=:userId")
    void deleteByFeedIdAndUserId(Long commentId, Long userId);
}
