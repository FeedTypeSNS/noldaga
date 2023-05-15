package com.noldaga.repository.Comment;

import com.noldaga.domain.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchComment {
    Page<Comment> FindWithFeed(Long id, Pageable pageable);

}
