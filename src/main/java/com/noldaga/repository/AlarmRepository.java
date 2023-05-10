package com.noldaga.repository;


import com.noldaga.domain.entity.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

//    Page<Alarm> findAllByToUser(User user, Pageable pageable); // -> 알람에 유저로 인덱스 걸어주자

    Page<Alarm> findAllByToUserIdOrderByIdDesc(Long toUserId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("DELETE FROM Alarm a where a.id= :id")
    void deleteById(Long id);


}
