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
function cwsOpen(){
    //alert("ws://"+location.host+"/chatroom/"+name);
    let name = document.querySelector("#getUsernameFromHeader").textContent;
    console.log(name+"님이 채팅창을 열었습니다.");
    if (name==null && name.trim()===''){
        name = $("#ws-username").val();
    }
    cws = new WebSocket("ws://"+location.host+"/chatroom/"+encodeURIComponent(name));
    cwsEvt();
}
function cwsEvt(){
    cws.onopen = function (data) {
    }
    cws.onmessage = function (data){
        var get = data.data;
        if(get!=null && get.trim()!==''){
           var dd = JSON.parse(get);
            console.log("메시지 타입: "+dd.result.type)
           if(dd.result.type==="ENTER"){
               $("#us-sessionId").val(dd.result.sessionId);
           }else if(dd.result.type==="NEWROOM"){
               getChatListFunc();
           }else if(dd.result.type==="NEWCHAT"){//알림 추가..
               getChatListFunc();
           }else if(dd.result.type==="DELETEROOM"){
               let uuid = document.querySelector("#ws-romeUuid").value;
               let roomId = document.querySelector("#ws-romeId").value;
               getChatListFunc();
               if(uuid === dd.result.roomInfo.uuid) {
                   getOneChatRoom(roomId, uuid);
                   //saveMsg(dd.result.roomInfo.id, dd.result.msg);
                   showChatMsg(dd.result.data.result.chat, "");
                   //document.querySelector("#chat-view-name").textContent = dd.result.data.result.chat.room.viewRoomName;
               }
           }else if(dd.result.type==="DELETEMSG"){
               let pRoomId = document.querySelector("#ws-romeId").value;
               console.log(dd.result.id+"번 메시지 삭제..")
               let roomId = dd.result.roomInfo.id;
               //let uuid = dd.result.uuid;
               let chatId = dd.result.roomInfo.chatId;
               getChatListFunc();
               if(pRoomId===roomId){
                   $("#chat-msg-id-"+chatId+" *").remove();
                   $("#chat-msg-id-"+chatId).remove();
               }
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
function wsOpen(uuid){
    let name = document.querySelector("#getUsernameFromHeader").textContent;
    console.log(name+"님이 "+uuid +" 채팅방을 열었습니다.");
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
                }
                else if(d.result.type==="TALK"){
                    //alert(d.result.chat.unread_count);
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
                }
                else {
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
        ws.close();
        console.log("채팅 방 소켓 종료");
    }
}
function wsSend(){
    let roomId = $("#ws-romeId").val();
    let msg = $("#chatting-msg").val();
    var imges = $('#ChatFileInput')[0].files[0];
    //console.log(msg);
    if(msg!=null&&msg.trim()!=='') { //msg가 빈공간일 수도 있음..
        console.log("메시지 전송");
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
    if (typeof imges === 'object' && imges !== null && imges !== undefined){
        console.log("이미지 전송");
        saveImg(roomId, imges)
    }
    $('#chatting-msg').val("");
}


function saveImg(roomId, file){
    /*let formData = new FormData();
    formData.append("img", file);*/
    let formData = new FormData();
    formData.append("img", file);

    $.ajax({
        type:'post',
        enctype:"multipart/form-data",  // 업로드를 위한 필수 파라미터
        url: "/api/chat/"+roomId+"/img",
        data: formData,
        processData: false,
        contentType: false
    }).done(function (resp){
        //alert(JSON.stringify(resp));
        ws.send(JSON.stringify(resp));
        readURL(null);
    }).fail(function (error){
        alert('채팅 전송 실패');
        alert(JSON.stringify(error));
        throw new Error(error);
    })
}

function saveMsg(roomId, message){
    //console.log("saveMSG");
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
        readURL(null);
    }).fail(function (error){
        alert('채팅 전송 실패');
        alert(JSON.stringify(error));
        throw new Error(error);
    });
}

function saveAdminMsg(roomId, userName){
    //console.log("saveMSG");
    //let name = document.querySelector("#ws-username").value;
    let message = userName+"님이 방을 나갔습니다.";
    let sendReq = {msg : message};
    $.ajax({
        type: "POST",
        url:"/api/chat/"+roomId+"/admin",
        data:JSON.stringify(sendReq),
        dataType: "json",
        contentType: "application/json; charset=utf-8"
    }).done(function (resp){
        //adminCwsOpen();
        cws.send(JSON.stringify(sendDelRoomAlarm(resp.result.chat.room.uuid, message, roomId, resp)));
        //adminCws.close();
    }).fail(function (error){
        alert('채팅 전송 실패');
        alert(JSON.stringify(error));
        throw new Error(error);
    });
}



function delChatShow(username, create, curId){
    let oldU = $("#pre-msg-username").val();
    let oldTime = $("#pre-msg-create").val();
    let curTime = forChatTimestamp(create);
    let preId = $("#pre-msg-id").val();

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