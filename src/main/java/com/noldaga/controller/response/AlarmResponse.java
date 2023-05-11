package com.noldaga.controller.response;


import com.noldaga.domain.GroupDto;
import com.noldaga.domain.alarm.AlarmArgs;
import com.noldaga.domain.alarm.AlarmDto;
import com.noldaga.domain.alarm.AlarmType;
import com.noldaga.domain.entity.Group;
import com.noldaga.domain.entity.User;
import com.noldaga.domain.userdto.UserDto;
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

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;




    public static AlarmResponse fromAlarmDto(AlarmDto alarmDto) {

        GroupDto groupDto = alarmDto.getFromGroupDto();
        UserDto userDto = alarmDto.getFromUserDto();
        String imageUrl=null;
        if(groupDto!=null){
            imageUrl = groupDto.getProfile_url();
        }
        if(userDto!=null){
            imageUrl = userDto.getProfileImageUrl();
        }

        return new AlarmResponse(
                alarmDto.getId(),
                alarmDto.getAlarmType(),
                alarmDto.getAlarmArgs(),
                alarmDto.getAlarmType().getAlarmText(),
                imageUrl,
                alarmDto.getCreatedAt(),
                alarmDto.getModifiedAt(),
                alarmDto.getDeletedAt()

        );
    }
}
