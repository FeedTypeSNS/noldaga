package com.noldaga.domain.alarm;

import com.noldaga.domain.entity.Alarm;
import com.noldaga.domain.userdto.UserDto;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@Getter
@Slf4j
@ToString
public class AlarmDto {

    private Long id;

    private AlarmType alarmType;

    private Long toUserId;

    //누가(From) 무엇(Object)을 했는지 (어떤 유저가 피드에 무언가를 했다)
    private AlarmArgs alarmArgs; //확장의 가능성이 높은 AlarmArgs 는 json 으로 db에 저장하는것이 유리해보임

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;


    private AlarmDto(Long id, AlarmType alarmType, Long toUserId, AlarmArgs alarmArgs, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.alarmType = alarmType;
        this.toUserId = toUserId;
        this.alarmArgs = alarmArgs;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;
    }

    public static AlarmDto fromEntity(Alarm alarm) {
        log.info("================Call fromEntity================");
        return new AlarmDto(
                alarm.getId(),
                alarm.getAlarmType(),
                alarm.getToUserId(),
                alarm.getAlarmArgs(),
                alarm.getCreatedAt(),
                alarm.getModifiedAt(),
                alarm.getDeletedAt()

        );


    }
}
