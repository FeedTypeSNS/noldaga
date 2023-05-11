package com.noldaga.domain.entity;


import com.noldaga.domain.alarm.AlarmArgs;
import com.noldaga.domain.alarm.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="alarm"/*, indexes = {
        @Index(name = "user_id_idx", columnList = "user_id")
}*/
)
//@TypeDef(name= "jsonb", typeClass= JsonBinaryType.class)
@TypeDef(name= "json", typeClass= JsonType.class)
@SQLDelete(sql= "update alarm set deleted_at = now() where alarm_id=?")
@Where(clause = "deleted_at is NULL")
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="alarm_id")
    private Long id;

    //알람 타입별로 뭉쳐서 알림을 보냄 ( '1번 게시글을 영수 외 10명이 좋아합니다' 라고 1번만 알람이 감)
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

//    @ManyToOne(fetch = FetchType.LAZY)//EAGER : 디폴트 : 즉시로딩
//    @JoinColumn(name="user_id")
//    private User toUser; //알람 받는사람


    @JoinColumn(name="user_id")
    private Long toUserId;

    //누가(From) 무엇(Object)을 했는지 (어떤 유저가 피드에 무언   가를 했다)

//    @Type(type="json")
    @Type(type="json") //jsonb 타입에만 인덱스를 걸 수 있고, json 타입에는 인덱스 못 걸음
    @Column(columnDefinition = "json")//db에 json 타입으로 집어넣음 : db에서 지원하지않은 데이터타입도 이런식으로 넣을 수 있음 : DB에서 테이블이 따로 있지않은 도메인을 자바객체에서 필드로 가질때 이런식으로 할 수 있음
    private AlarmArgs alarmArgs; //확장의 가능성이 높은 AlarmArgs 는 json 으로 db에 저장하는것이 유리해보임
    //from, object 라는 정보를 갖고있음
    //AlarmType 의 필드들이 나중에 바뀔수도 있지만 , json 이라는 문자열 형태로 db에 저장되면 db 컬럼들은 변경될 필요가 없음



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="group_id")
    private Group fromGroup;

    @ManyToOne(fetch =FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User fromUser;


    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false , updatable = false)
    private LocalDateTime createdAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deletedAt;

    protected Alarm(){}


    private Alarm(Long id, AlarmType alarmType, Long toUserId, AlarmArgs alarmArgs,Group fromGroup,User fromUser) {
        this.id = id;
        this.alarmType = alarmType;
        this.toUserId = toUserId;
        this.alarmArgs = alarmArgs;
        this.fromGroup = fromGroup;
        this.fromUser = fromUser;
    }

    public static Alarm of(Long toUserId, AlarmType alarmType, AlarmArgs alarmArgs,Group fromGroup){
        return new Alarm(null, alarmType, toUserId, alarmArgs,fromGroup,null);
    }

    public static Alarm of(Long toUserId, AlarmType alarmType, AlarmArgs alarmArgs,User fromUser){
        return new Alarm(null, alarmType, toUserId, alarmArgs,null,fromUser);
    }

    public String getFromType(){
        return alarmArgs.getFromType();
    }

}
