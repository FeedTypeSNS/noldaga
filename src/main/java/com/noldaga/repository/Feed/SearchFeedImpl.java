package com.noldaga.repository.Feed;

import com.noldaga.domain.entity.*;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.security.core.parameters.P;

import java.util.List;

public class SearchFeedImpl extends QuerydslRepositorySupport implements SearchFeed {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    QFeed feed = QFeed.feed;
    QFollow follow = QFollow.follow;
    QStoreFeed storeFeed = QStoreFeed.storeFeed;

    public SearchFeedImpl() {
        super(Feed.class);
    }

    @Override
    public Page<Feed> MainFeedWithFollow(long id, Pageable pageable) {
        JPQLQuery<Feed> query = jpaQueryFactory.selectFrom(feed)
                .join(follow).on(feed.user.id.eq(follow.follower.id))
                .where(follow.following.id.eq(id))
                .where(feed.groupId.eq(0L))
                .orderBy(feed.modDate.desc());

        this.getQuerydsl().applyPagination(pageable,query);
        List<Feed> feedList = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(feedList,pageable,count);
    }

    @Override
    public Page<Feed> MyPageFeed(long userId, Pageable pageable) {
        JPQLQuery<Feed> query = jpaQueryFactory.selectFrom(feed)
                .where(feed.user.id.eq(userId))
                .where(feed.groupId.eq(0L))
                .orderBy(feed.modDate.desc());

        this.getQuerydsl().applyPagination(pageable,query);
        List<Feed> feedList = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(feedList,pageable,count);
    }

    @Override
    public Page<Feed> MyPageFeedOnlyPublic(long userId, Pageable pageable) {
        JPQLQuery<Feed> query = jpaQueryFactory.selectFrom(feed)
                .where(feed.user.id.eq(userId))
                .where(feed.groupId.eq(0L))
                .where(feed.range.eq(0))
                .orderBy(feed.modDate.desc());

        this.getQuerydsl().applyPagination(pageable,query);
        List<Feed> feedList = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(feedList,pageable,count);
    }

    @Override
    public Page<Feed> GroupPageFeed(long id, Pageable pageable) {
        JPQLQuery<Feed> query = jpaQueryFactory.selectFrom(feed)
                .where(feed.groupId.eq(id))
                .orderBy(feed.modDate.desc());

        this.getQuerydsl().applyPagination(pageable,query);
        List<Feed> feedList = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(feedList,pageable,count);
    }

    @Override
    public Page<Feed> ExplorePageFeed(Pageable pageable) {
        JPQLQuery<Feed> query = jpaQueryFactory.selectFrom(feed)
                .where(feed.range.eq(0))
                .orderBy(feed.modDate.desc());

        this.getQuerydsl().applyPagination(pageable,query);
        List<Feed> feedList = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(feedList,pageable,count);
    }

    @Override
    public Page<Feed> MyStoredFeed(Long userId, Pageable pageable) {
        JPQLQuery<Feed> query = jpaQueryFactory.selectFrom(feed)
                .join(storeFeed).on(feed.id.eq(storeFeed.feed.id))
                .where(storeFeed.user.id.eq(userId))
                .orderBy(storeFeed.regDate.desc());

        this.getQuerydsl().applyPagination(pageable,query);
        List<Feed> feedList = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(feedList,pageable,count);
    }

}
