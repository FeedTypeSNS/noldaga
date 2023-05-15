package com.noldaga.repository;

import com.noldaga.domain.entity.FeedTag;
import com.noldaga.domain.entity.HashTag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FeedTagRepository extends JpaRepository<FeedTag, Long> {

    @Query("select ft from FeedTag ft where ft.feed.id=:feedId")
    List<FeedTag> findByFeedId(Long feedId);

    @Transactional
    @Modifying
    @Query("delete from FeedTag ft where ft.feed.id=:feedId")
    void deleteByFeedId(Long feedId);

    List<FeedTag> findByHashTag(HashTag tag);

    @Query(nativeQuery = true, value = "select * from feed_tag f WHERE f.hashtag_id =:tag ORDER BY rand() limit :n")
    List<FeedTag> findAllByHashRand(Long tag, int n);

}
