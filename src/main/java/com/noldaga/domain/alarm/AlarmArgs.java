package com.noldaga.domain.alarm;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor //이거 있어야 json 으로 바꿀수 있음
@AllArgsConstructor
public class AlarmArgs {

    private String fromType;
    private Long fromId;//알람 보낸 사람
    private String fromNameOnAlarm;

    private String targetType; // 클릭했을때 이동될 object(피드 , 유저, 그룹 , 피드:댓글 )
    private Long targetObjectId; // 10번유저가 2번피드(targetObject) 에 무언가(좋아요, 댓글 등)을 했다
    private String targetNameOnAlarm;



    public static AlarmArgs of(AlarmObject fromObject,AlarmObject targetObject){

        return new AlarmArgs(

                fromObject.getObjectType(),fromObject.getId(), fromObject.getNameOnAlarm(),
                targetObject.getObjectType(), targetObject.getId(), targetObject.getNameOnAlarm());

    }


//    mysql 에서는 없어도 되고 h2에서는 있어야하고
//    db에서 json 데이터가 넘어오면 자바객체로 바꿀때 필요함 ( 스프링MVC 쪽에는 이게 자동으로 적용되는듯?)
//    public AlarmArgs(String json) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        AlarmArgs alarmArgs = objectMapper.readValue(json, AlarmArgs.class);
//        this.fromUserId = alarmArgs.getFromUserId();
//        this.targetObjectId = alarmArgs.getTargetObjectId();
//        this.dummy=alarmArgs.getDummy();
//    }


}


//댓글에 좋아요가 눌리는 알람을 고려한다면 : fromUserId(누가) targetObjectId(댓글을) 뿐아니라, 어떤 게시글의 댓글인지 에 대한 정보로서 필드가 추가되어야함..
//여러 상황에 대한 알람을 하나의 AlarmArgs 로 퉁칠려고 하니, 여러 필드들이 있을 수 있고, 이러한 필드들은 각 상황에따라 null 인 값들도 존재하게됨
//머 어쨋든 이런 정보들을 null 로 db에 넣게 되면 굉장히 보기안좋고 비효율적임 -> db에 저장할때 json 으로 저장해주자
