package com.noldaga.repository.Feed;


import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.HashTag;
import com.noldaga.domain.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedRepository extends JpaRepository<Feed,Long>, SearchFeed {

    //public abstract Page<Feed> findAllByUser(User user, Pageable pageable);
    //findAllById 는 pk 기반 select 여서 인덱싱이 되어있어서 성능상 문제가없음.
    //findAllByUser 는 인덱싱이 안되어있어서 성능상 문제가 있음 -> 인덱싱 해줘야함 (인덱싱은 성능고려에서 굉장히 중요한 이슈임) : mysql 에서는 fk 도 자동으로 인덱스생성
    //엔티티의 @Table 어노테이션에 인덱싱 정보를 넣어줌
    @EntityGraph(attributePaths = {"comment"})
    @Query("select f from Feed f where f.id=:id")
    Optional<Feed> findByIdWithComment(Long id);


    //JPA, JPQL 에서 limit이 사용안됨. 사용하기 위해 Query annotation의 nativeQuery 옵션을 true 변경 후 사용가능
    //좋아요 순  n개 가져오기
    @Query(nativeQuery = true, value = "select * from feed f ORDER BY f.total_like Desc LIMIT :n")
    List<Feed> findTopByNOrderByLikeCountDesc(int n);

    //많이본 순으로 n개 가져오기
    @Query(nativeQuery = true, value = "select * from feed f ORDER BY f.total_view Desc LIMIT :n")
    List<Feed> findTopNOrderByTotalViewDesc(int n);

    //랜덤 두 개 가져오기
    @Query(value = "SELECT * FROM feed ORDER BY RAND() LIMIT :n", nativeQuery = true)
    List<Feed> findRandom(int n);

    //가장 최근 데이터 한개
    List<Feed> findFirstByOrderByModDateDesc();

    //작성한 사람의 첫번재 글 가져오기
    List<Feed> findFirstByUserOrderByModDateDesc(User user);



}
