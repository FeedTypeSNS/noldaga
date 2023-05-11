package com.noldaga.domain.alarm;

import com.noldaga.domain.GroupDto;
import com.noldaga.domain.entity.Alarm;
import com.noldaga.domain.userdto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@Getter
@Slf4j
@AllArgsConstructor
public class AlarmDto {

    private Long id;

    private AlarmType alarmType;

    private Long toUserId;

    //누가(From) 무엇(Object)을 했는지 (어떤 유저가 피드에 무언가를 했다)
    private AlarmArgs alarmArgs; //확장의 가능성이 높은 AlarmArgs 는 json 으로 db에 저장하는것이 유리해보임

    private GroupDto fromGroupDto;
    private UserDto fromUserDto;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;




    public static AlarmDto fromEntity(Alarm alarm) {



        return new AlarmDto(
                alarm.getId(),
                alarm.getAlarmType(),
                alarm.getToUserId(),
                alarm.getAlarmArgs(),
                GroupDto.fromEntity(alarm.getFromGroup()),
                UserDto.fromEntity(alarm.getFromUser()),
                alarm.getCreatedAt(),
                alarm.getModifiedAt(),
                alarm.getDeletedAt()
        );


    }
}
