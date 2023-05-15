package com.noldaga.repository;

import com.noldaga.domain.entity.FeedLike;
import com.noldaga.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Modifying
    @Transactional
    @Query("delete from Image i where i.url=:url")
    void deleteByUrl(String url);

    @Modifying
    @Transactional
    @Query("delete from Image i where i.feed.id=:feedId")
    void deleteByFeedId(Long feedId);
}
