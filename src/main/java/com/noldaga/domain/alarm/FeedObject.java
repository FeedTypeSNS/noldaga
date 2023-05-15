package com.noldaga.domain.alarm;


import com.noldaga.domain.entity.Feed;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FeedObject implements AlarmObject {

    private Long id;
    private String title;

    private final String OBJECT_TYPE = "feed";

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getNameOnAlarm() {

        String f =AlarmObject.shortenString(this.title);
        return String.format("피드 : %s", f);
    }


    @Override
    public String getObjectType() {
        return this.OBJECT_TYPE;
    }

    public static FeedObject from(Feed feed){
        return new FeedObject(feed.getId(), feed.getTitle());
    }
}
