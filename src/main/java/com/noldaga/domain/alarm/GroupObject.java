package com.noldaga.domain.alarm;


import com.noldaga.domain.entity.Group;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GroupObject implements AlarmObject{

    private Long id;
    private String name;
    private final String OBJECT_TYPE = "GROUP";

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getNameOnAlarm() {
        return this.name;
    }

    @Override
    public String getObjectType() {
        return this.OBJECT_TYPE;
    }

    public static GroupObject from(Group group){
        return new GroupObject(group.getId(), group.getName());
    }

}
