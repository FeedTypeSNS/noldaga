package com.noldaga.repository;


import com.noldaga.domain.entity.Code;
import com.noldaga.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long> {


    Optional<Code> findById(Integer id);


    @Modifying
    @Transactional
    @Query("delete from Code c where c.id=:id")
    void deleteById(Integer id);
}
