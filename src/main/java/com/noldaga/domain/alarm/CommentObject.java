package com.noldaga.domain.alarm;

import com.noldaga.domain.entity.Comment;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class CommentObject implements AlarmObject{

    private Long id;
    private String content;

    private String feedTitle;
    private final String OBJECT_TYPE = "COMMENT";



    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getNameOnAlarm() {
        return feedTitle + "  -  " + content;
    }


    @Override
    public String getObjectType() {
        return this.OBJECT_TYPE;
    }

    public static AlarmObject from(Comment comment,String feedTitle){
        return new CommentObject(comment.getId(), comment.getContent(),feedTitle);

    }
}
