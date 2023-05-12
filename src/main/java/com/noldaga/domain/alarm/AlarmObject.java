package com.noldaga.domain.alarm;

public interface AlarmObject {

    int stringLength=10;
    Long getId();

    String getNameOnAlarm();

    String getObjectType();

    static String shortenString(String source){
        if(source.length()<=stringLength){
            return source;
        }
        return source.substring(0,stringLength)+"...";
    }

}
