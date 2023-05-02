package com.noldaga.repository;

import com.noldaga.domain.entity.Group;
import com.noldaga.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAllByUser(User user, Sort sort);
}
