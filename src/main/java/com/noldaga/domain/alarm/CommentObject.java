package com.noldaga.domain.alarm;

import com.noldaga.domain.entity.Comment;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class CommentObject implements AlarmObject{

    private Long id;
    private String comment;

    private String feedTitle;
    private final String OBJECT_TYPE = "COMMENT";



    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getNameOnAlarm() {
        String f = AlarmObject.shortenString(feedTitle);
        String c = AlarmObject.shortenString(comment);
        return String.format("피드 : %s      댓글 : %s", f, c);
    }



    @Override
    public String getObjectType() {
        return this.OBJECT_TYPE;
    }

    public static AlarmObject from(Comment comment,String feedTitle){
        return new CommentObject(comment.getId(), comment.getContent(),feedTitle);

    }
}
