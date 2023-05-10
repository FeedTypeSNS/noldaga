package com.noldaga.controller.response;


import com.noldaga.domain.alarm.AlarmArgs;
import com.noldaga.domain.alarm.AlarmDto;
import com.noldaga.domain.alarm.AlarmType;
import com.noldaga.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AlarmResponse {

    private Long id;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;

    private String alarmText;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;




    public static AlarmResponse fromAlarmDto(AlarmDto alarmDto) {
        return new AlarmResponse(
                alarmDto.getId(),
                alarmDto.getAlarmType(),
                alarmDto.getAlarmArgs(),
                alarmDto.getAlarmType().getAlarmText(),
                alarmDto.getCreatedAt(),
                alarmDto.getModifiedAt(),
                alarmDto.getDeletedAt()

        );
    }
}
