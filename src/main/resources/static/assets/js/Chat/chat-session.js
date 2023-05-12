function init(){
    setMyinfo();
}
function setMyinfo(){
    $.ajax({
        type:"GET",
        url:"/api/chat/me",
        dataType: "json"
    }).done(function (resp){
        $("#ws-username").val(resp.result.username);
    }).fail(function (error){
        alert(JSON.stringify(error));
    });

} //내 정보 저장이 가장 먼저 이뤄져야함..

var cws;
function cwsOpen(name){
    //alert("ws://"+location.host+"/chatroom/"+name);
    if (name==null && name.trim()===''){
        name = $("#ws-username").val();
    }
    name = $()
    cws = new WebSocket("ws://"+location.host+"/chatroom/"+encodeURIComponent(name));
    cwsEvt();
}
function cwsEvt(){
    cws.onopen = function (data) {
    }
    cws.onmessage = function (data){
        var get = data.data;
        console.log("받은 데이터: "+JSON.stringify(get))
        if(get!=null && get.trim()!==''){
           var dd = JSON.parse(get);
           if(dd.result.type==="ENTER"){
               $("#us-sessionId").val(dd.result.sessionId);
           }else if(dd.result.type==="NEWROOM"){
               getChatListFunc();
           }else if(dd.result.type==="NEWCHAT"){
               getChatListFunc();
           }else if(dd.result.type==="DELETEROOM"){
               getChatListFunc();
               saveMsg(roomId, msg);
           }
        }
    }

}
function cwsClose(){
    if (cws && cws.readyState === WebSocket.OPEN) {
        console.log("채팅 페이지 소켓 종료");
        cws.close();
    }
}
//채팅페이지에서 필요한 웹 소켓 세션
function cwsSend(){

}




var ws;
function wsOpen(name, uuid){
    ws = new WebSocket("ws://"+location.host+"/chatting/"+uuid+"/"+encodeURIComponent(name));
    //웹 소켓 전송 시 config에 맞춰, 방 uuid와 name을 같이 보내줌
    //웹소켓 연결 URL에서 파라미터 값을 전달할 때, 한글 등의 문자열을 전송하면 URL 인코딩이 필요
    //자바스크립트에서는 encodeURIComponent 함수를 사용하여 URL 인코딩 필요
    wsEvt();
}
function wsEvt(){
    ws.onopen = function (data){
        //console.log("소켓 연결");
        //소켓열리면 초기화 세팅
    }
    ws.onmessage = function (data){
        console.log("메시지 응답");
        var get = data.data; //JSON 데이터의 문자열 표현이 저장
        if (get != null){
            if(get.trim() !== ''){ //받아진 데이터가 있을 때
                var d = JSON.parse(get); //JSON 문자열을 파싱하여 자바스크립트 객체로 변환 후 속성에 접근 가능
                //console.log(d);
                if (d.result.type==="ENTER"){ //입장한거라면
                    $("#ws-sessionId").val(d.result.sessionId);
                    console.log(d.result.username+" 사용자가 입장했습니다.");
                }else if(d.result.type==="TALK"){
                    alert(d.result.chat.unread_count);
                    if(d.result.chat.unread_count===0){
                        $(".readData-me-send *").remove();
                        $(".readData-me-send").remove();
                    }
                    //읽음 수 체크 삭제..

                    let inName = d.result.chat.sender.username;
                    let name = document.querySelector("#ws-username").value;
                    if(inName === name){ //보낸 세션 id가 같다면 내가 보낸거..
                        showChatMsg(d.result.chat, name); //내가 보낸거
                    }else {
                        showChatMsg(d.result.chat, ""); //남이 보낸거
                        console.log(JSON.stringify(sendMsgAlarm()));
                        cws.send(JSON.stringify(sendMsgAlarm()));
                    }
                    //alert(item.sender.username+" "+item.sender.username+" "+item.id);
                    console.log(d.result.chat.createAt)

                    delChatShow(inName, d.result.chat.createAt, d.result.chat.id);

                    const chatDisplayBody = document.getElementById('chat-display-body');
                    chatDisplayBody.scrollTop = chatDisplayBody.scrollHeight; //스크롤바 하단 배치

                    $("#pre-msg-username").val(d.result.chat.sender.username);
                    $("#pre-msg-create").val(forChatTimestamp(d.result.chat.createAt));
                    $("#pre-msg-id").val(d.result.chat.id);
                }else {
                    console.warn("unknown type!");
                }
            }
        }
    }
    document.getElementById("chatting-msg").addEventListener("keypress", function (e){
        if(e.key === "Enter"){
            wsSend();
        }
    })
}
function wsClose(){
    if (ws && ws.readyState === WebSocket.OPEN) {
        console.log("채팅 방 소켓 종료");
        ws.close();
    }
}
function wsSend(){
    /*let me = document.querySelector("#username-forChat");
    let name = me.querySelector("input[name='chat-username-me']").value;*/
    let msg = $("#chatting-msg").val();
    //console.log(msg);
    if(msg!=null&&msg.trim()!=='') { //msg가 빈공간일 수도 있음..
        let roomId = $("#ws-romeId").val();
        //console.log("보낼 방 id :"+roomId);
        let name = $("#ws-username").val();
        //console.log("보내는 사람 이름 :"+name);
        saveMsg(roomId, msg);

        /*var option = {
            type:"message",
            sessionId: $("#sessionId").val(),
            username : $("#ws-username").val(),
            msg: $("#chatting-msg").val(),
            roomId: $("#ws-romeId").val()
        }*/
        //showChatMsg(msg, name);
        /*ws.send(JSON.stringify(option));*/
    }
    $('#chatting-msg').val("");
}
function saveMsg(roomId, message){
    console.log("saveMSG");
    //let name = document.querySelector("#ws-username").value;
    let sendReq = {msg : message};
    $.ajax({
        type: "POST",
        url:"/api/chat/"+roomId+"/message",
        data:JSON.stringify(sendReq),
        dataType: "json",
        contentType: "application/json; charset=utf-8"
    }).done(function (resp){
        //alert(JSON.stringify(resp));
        ws.send(JSON.stringify(resp));
    }).fail(function (error){
        alert('채팅 전송 실패');
        alert(JSON.stringify(error));
        throw new Error(error);
    })
}


function delChatShow(username, create, curId){
    let oldU = $("#pre-msg-username").val();
    let oldTime = $("#pre-msg-create").val();
    let curTime = forChatTimestamp(create);
    let preId = $("#pre-msg-id").val();
    console.log("비교 시작!!!")
    console.log(oldTime+"\n"+curTime);
    console.log(oldU+"\n"+username);
    console.log(curId+"\n"+preId);

    if(oldU===username){
        if(oldTime===curTime) {
            $("#senderImg-" + curId + " *").remove();
            $("#senderImg-" + curId).remove();
            $("#sender-" + curId + " *").remove();
            $("#sender-" + curId).remove();
            $("#createDate-" + preId + " *").remove();
            $("#createDate-" + preId).remove();
        }
    }
}
// 채팅을 위한 웹 소켓 세션 관련



init();