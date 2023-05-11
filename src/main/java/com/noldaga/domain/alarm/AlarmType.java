package com.noldaga.domain.alarm;


import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public enum AlarmType {

    //실제 알람의 내용은 db에 저장하고 있지않도록하고, 해당 이넘타입에서 어떤 메시지가 띄어질지는 서버나 client 쪽에서 정하도록 하자
    //client 에서 이런 메시지를 컨트롤하게 된다면, 이넘타입의 메시지가 바뀔때마다 앱을 업데이트 해주어야하니까 웹서버에서 하는것이 좋을듯?
    NEW_COMMENT_ON_FEED("피드에 댓글이 달렸습니다."),
    //'A가 B게시물에 댓글을 작성했습니다' : A,B 는 가변 값이기 때문에 이 메시지 자체를 db에 저장하지말고 가변값을 제외하고 db에 저장,
    // 가변값들은 서버에서 컨트롤을 하자 혹은 클라이언트에서 컨트롤하자( 앱인 경우 업데이트 이슈를 고려하면 서버에서 가변값들을 컨트롤하는것이 좋음)
    NEW_LIKE_ON_FEED("피드에 좋아요가 눌렸습니다."),

    NEW_LIKE_ON_COMMENT("댓글에 좋아요가 눌렸습니다."),

    BANNED("신화그룹에서 강퇴 당했습니다"),

    NEW_FOLLOWER("A가 당신을 팔로우 했습니다"),

    NEW_MEMBER("A가 신화그룹에 가입 했습니다"),
    ;

    private final String alarmText;

}
