package com.noldaga.repository.HashTag;

import com.noldaga.domain.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long>, SearchHashTag {

    @Query("select h from HashTag h where h.tagName=:hashTagName")
    Optional<HashTag> findByHashTagName(String hashTagName);

    //랜덤 해시 태그 종류 빼기
    @Query(value = "SELECT * FROM hash_tag ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<HashTag> findRandomOne();
}
