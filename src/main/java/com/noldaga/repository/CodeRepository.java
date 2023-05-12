package com.noldaga.repository;


import com.noldaga.domain.entity.Code;
import com.noldaga.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long> {


    Optional<Code> findById(Integer id);
}
