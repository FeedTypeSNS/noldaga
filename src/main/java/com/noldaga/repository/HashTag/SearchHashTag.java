package com.noldaga.repository.HashTag;

import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.HashTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchHashTag {
    Page<HashTag> findAllBySearch(String query, Pageable pageable);
}
