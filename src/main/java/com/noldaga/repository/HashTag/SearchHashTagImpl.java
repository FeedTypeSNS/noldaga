package com.noldaga.repository.HashTag;

import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.HashTag;
import com.noldaga.domain.entity.QHashTag;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class SearchHashTagImpl extends QuerydslRepositorySupport implements SearchHashTag{
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    QHashTag hashTag = QHashTag.hashTag;

    public SearchHashTagImpl() {
        super(HashTag.class);
    }

    @Override
    public Page<HashTag> findAllBySearch(String q, Pageable pageable) {
        JPQLQuery<HashTag> query = jpaQueryFactory.selectFrom(hashTag)
                .where(hashTag.tagName.contains(q))
                .orderBy(hashTag.tagName.length().asc());
        this.getQuerydsl().applyPagination(pageable,query);

        List<HashTag> hashTagList = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(hashTagList,pageable,count);
    }
}
