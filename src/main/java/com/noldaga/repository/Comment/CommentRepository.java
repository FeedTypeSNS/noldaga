package com.noldaga.repository.Comment;

import com.noldaga.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>,SearchComment {
}
