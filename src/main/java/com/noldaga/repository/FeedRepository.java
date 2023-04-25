package com.noldaga.repository;


import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed,Long> {

    public abstract Page<Feed> findAllByUser(User user, Pageable pageable);
    //findAllById 는 pk 기반 select 여서 인덱싱이 되어있어서 성능상 문제가없음.
    //findAllByUser 는 인덱싱이 안되어있어서 성능상 문제가 있음 -> 인덱싱 해줘야함 (인덱싱은 성능고려에서 굉장히 중요한 이슈임) : mysql 에서는 fk 도 자동으로 인덱스생성
    //엔티티의 @Table 어노테이션에 인덱싱 정보를 넣어줌

}
