package com.noldaga.repository.Comment;


import com.noldaga.domain.entity.Comment;
import com.noldaga.domain.entity.QComment;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class SearchCommentImpl extends QuerydslRepositorySupport implements  SearchComment{
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    QComment comment= QComment.comment;

    public SearchCommentImpl(){
        super(Comment.class);
    }

    public Page<Comment> FindWithFeed(Long id, Pageable pageable){
        JPQLQuery<Comment> query = jpaQueryFactory.selectFrom(comment)
                .where(comment.feed.id.eq(id))
                .orderBy(comment.modDate.desc());
        List<Comment> commentList = query.fetch();
        long count = query.fetchCount();
        return new PageImpl<>(commentList,pageable,count);
    }
}
