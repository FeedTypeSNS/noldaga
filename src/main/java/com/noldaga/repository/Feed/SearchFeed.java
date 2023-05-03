package com.noldaga.repository.Feed;

import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.StoreFeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchFeed {
    Page<Feed> MainFeedWithFollow(long id, Pageable pageable); //메인페이지(매개변수는 내 id) /팔로우기반, 공개범위 신경 안써도됨, 그룹아이디 0
    Page<Feed> MyPageFeed(long id, Pageable pageable); //마이페이지 회원 id기반 /그룹아이디 0
    Page<Feed> MyPageFeedOnlyPublic(long userId, Pageable pageable);
    Page<Feed> GroupPageFeed(long id, Pageable pageable);
    Page<Feed> ExplorePageFeed(Pageable pageable);
    Page<Feed> MyStoredFeed(Long userId, Pageable pageable);
}
