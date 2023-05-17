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

    Page<Alarm> findAllByToUserId(Long toUserId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("delete Alarm a where a.id= :id") //영속성컨텐츠 동기화 없이 바로 쿼리 날라감
    void deleteById(Long id);
    //그렇지 않으면 조회를한후에 delete 를 하든 soft delete 를 하게됨


    long countByToUserIdAndUnRead(Long toUserid,boolean unRead);

    boolean existsByToUserIdAndUnRead(Long toUserId, boolean unRead);


}
