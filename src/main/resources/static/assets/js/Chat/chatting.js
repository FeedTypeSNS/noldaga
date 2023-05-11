function getChatPage(){
    $.ajax({
        type: "GET",
        url:"/chat"
    });
}

let chatting = {
    init:function (){
        $("#new-chatting-submit").on("click", (event)=>{
           //alert("채팅방 생성");
            event.preventDefault();// 이벤트 기본 동작 취소
            this.createChatting();
        });
    },
    createChatting:function (){ //채팅방 만들기
        let name = getJoinName();
        const joinUserList = {joinUserList: name};
        $.ajax({
            type: "POST",
            //async: true, // false 일 경우 동기 요청으로 변경
            url:"/api/chat",
            data: JSON.stringify(joinUserList), //json으로 변경해 보내야함
            dataType: "json",
            contentType: "application/json; charset=utf-8"
        }).done(function (resp){
            let sessionId = document.querySelector("#ws-sessionId")?.value; //세션 존재하는지 확인하기 위해
            if(sessionId){ //기존 세션이 존재한다면.. 닫고 새로운 세션을 열어야 함
                // sessionId가 undefined, null, 빈 문자열 등이 아닌 경우 실행될 코드
                wsClose();
            }else {
                //getChatListFunc();//채팅방 리스트 불러오기
                cws.send(JSON.stringify(resp));
                //리스트를 직접 호출하는대신 cws에 보내서 현재 들어와 있는 사람들 바로.. 켜지게게
                let username = document.querySelector("#ws-username").value;
                $("#ws-romeId").val(resp.result.roomInfo.id);
                $("#ws-romeUuid").val(resp.result.roomInfo.uuid);  //방 정보 표현해주기..
                //alert("채팅방 기본 뷰 생성 중..")
                enterRoom(resp.result);
                //alert("채팅방 기본 뷰 생성 완료")
                wsOpen(username, resp.result.roomInfo.uuid);
                //alert(JSON.stringify(resp));
            }
        }).fail(function (error){
            alert('채팅방 생성 실패');
            alert(JSON.stringify(error));
        })
    },
}

function getChatDetailRoom(element){ //chatlist에 설정된 아이디
    let id = $(element).attr("id").split("-")[1];
    let chatInfo = document.querySelector("#chatInfo-"+id);
    let uuid = chatInfo.querySelector("input[name='chat-uuid']").value;
    let roomId = chatInfo.querySelector("input[name='chat-roomId']").value;
    //let romeName = chatInfo.querySelector("input[name='chat-romeName']").value;
    //alert(uuid+", "+roomId+", "+romeName);

    $("#ws-romeUuid").val(uuid);
    $("#ws-romeId").val(roomId);
    getOneChatRoom(roomId, uuid);
}
function getOneChatRoom(roomId, uuid){
    $.ajax({
        type: "GET",
        url:"/api/chat/"+roomId,
        dataType: "json"
    }).done(function (resp){
        let sessionId = document.querySelector("#ws-sessionId").value; //세션 존재하는지 확인하기 위해
        if(sessionId!=null||sessionId.trim()!==""){ //기존 세션이 존재한다면.. 닫고 새로운 세션을 열어야 함
            wsClose();
        }
        //alert("방하나 불러오기\n"+JSON.stringify(resp));
        //getChatListFunc();
        let name = document.querySelector("#ws-username").value;
        enterRoom(resp.result);
        wsOpen(name, uuid);
        //console.log('채팅기본 뷰 완성');
    }).fail(function (error){
        console.log('채팅방 불러오기 실패');
        alert(JSON.stringify(error));
        throw new Error(error);
    });
}

function deleteChatRoom(event){
    let uuid = document.querySelector("#ws-romeUuid").value;
    event.preventDefault();
    let roomId = $("#ws-romeId").val();
    $.ajax({
        type: "DELETE",
        url:"/api/chat/"+roomId,
        dataType: "json"
    }).done(function (resp) {
        console.log(JSON.stringify(resp));
        getChatBody();
        cws.send(JSON.stringify(sendDelRoomAlarm(uuid)));
    }).fail(function (error){
        console.log('채팅방 삭제 실패');
        alert(JSON.stringify(error));
        throw new Error(error);
    });
}

function enterRoom(resp){
    $("#chatting-tool *").remove();
    $("#chatting-tool").remove();
    $(".card-footer *").remove();
    $(".card-footer").remove();
    let me = document.querySelector("#ws-username").value;
    console.log(me);
    //let name = me.querySelector("input[name='chat-username-me']").value;
    let roomId = resp.roomInfo.id;

    let url = window.location.href; //현재 페이지 url
    let newUrl = url + "/" + roomId;
    //history.pushState(null, null, newUrl); -> 이미지 작업 연결후 설정해주기

    initChattingPage(resp, me);

}

function getJoinName(){
    const name = document.querySelectorAll('input[name="joinUserList"]:checked');

    let nameList = [];
    name.forEach((i)=>{
        nameList.push(i.value);
    });

    return nameList;
}


function initChattingPage(resp, name){
    console.log("기본페이지 생성 시작");
    let chatBody = document.querySelector("#chat-tool-body");
    let body = document.createElement("div");
    body.id = "chatting-tool";
    body.className="card-body h-100";
    body.innerHTML = initChatBody(resp, name);
    chatBody.append(body);

    let footer = document.createElement("div");
    footer.className="card-footer flex-shrink-0";
    footer.innerHTML = cardFooter();
    body.after(footer);

    let roomId = resp.roomInfo.id;
    let joinCount = Number(resp.joinPeoples.length)-1; //회원 참가수에 따라 회원 이미지 변화..
    let joinImg = document.querySelector("#get-chat-userImg-"+roomId);
    let thisImg = document.createElement("div");
    thisImg.className = "joinPeopleImgList"+roomId;
    switch (joinCount) {
        case 1 : thisImg.innerHTML = getOneImg(resp);break;
        case 2 : thisImg.innerHTML = getTwoImg(resp);break;
        case 3 : thisImg.innerHTML = getThreeImg(resp);break;
        default : thisImg.innerHTML = getFourImg(resp);break;

    }
    joinImg.append(thisImg);
    //alert(resp.pastChat); //확인
    //console.log("pastChat"+"\n"+JSON.stringify(resp.pastChat));
    let data = resp.pastChat;
    let rmId = -1;
    for(let i=0; i<data.length; i++) {
        showChatMsg(data[i], name);
        if (rmId === data[i].id){
            $("#senderImg-" + rmId + " *").remove();
            $("#senderImg-" + rmId).remove();
            $("#sender-" + rmId + " *").remove();
            $("#sender-" + rmId).remove();
        }
        if(i!==data.length-1){
            rmId = delSameTimeChat(data[i], data[i+1]);
        }
    }
    const chatDisplayBody = document.getElementById('chat-display-body');
    chatDisplayBody.scrollTop = chatDisplayBody.scrollHeight; //스크롤바 하단 배치

}

function showChatMsg(data, name){
        let chatList = document.querySelector("#chat-display-body");
        let chat;
        //console.log(data.sender.username === name);
        if (data.sender.username === name){
            chat = document.createElement("div");
            chat.className = "d-flex justify-content-end text-end mb-1";
            chat.innerHTML= chatSent_m(data);
        }else {
            chat = document.createElement("div");
            chat.className = "d-flex mb-1";

            chat.innerHTML= chatSent_o(data);
        }


        chatList.append(chat);

    $("i.fa-solid.fa-check").each(function() {
        if ($(this).text().trim() === "0") {
            $(this).remove();
        }
    });//읽음 수 체크 삭제..
    //기존 채팅 내용 붙이기
}

function initChatBody(resp, name){
    const me = resp.joinPeoples.findIndex(obj => obj.username === name);
    if(me === -1){
        throw new Error('자신의 정보를 찾을 수 없습니다.');
    }

    return `<div class="tab-content py-0 mb-0 h-100" id="chatTabsContent">
                            <!-- Conversation item START -->
                            <div class="fade tab-pane show active h-100" id="chat-${resp.roomInfo.id}" role="tabpanel" aria-labelledby="chat-${resp.roomInfo.id}-tab">
                                <!-- Top avatar and status START -->
                                <input type="hidden" name="currentRoom-uuid" value="${resp.roomInfo.uuid}">
                                <div class="d-sm-flex justify-content-between align-items-center">
                                    <div class="d-flex mb-2 mb-sm-0">
                                        <div class="flex-shrink-0 avatar me-2" id="get-chat-userImg-${resp.roomInfo.id}">
                                             
                                        </div>
                                        <div class="d-block flex-grow-1">
                                            <h6 class="mb-0 mt-1">${resp.roomInfo.viewRoomName}</h6>
                                            <div class="small text-secondary"><i class="fa-solid fa-circle text-success me-1"></i>Online- 실시간 처리해서 켜 있음 온라인</div>
                                        </div>
                                    </div>
                                    <div class="d-flex align-items-center">
                                        <!-- Call button -->
                                        <a href="#!" class="icon-md rounded-circle btn btn-primary-soft me-2 px-2" data-bs-toggle="tooltip" data-bs-placement="top" title="Audio call"><i class="bi bi-telephone-fill"></i></a>
                                        <a href="#!" class="icon-md rounded-circle btn btn-primary-soft me-2 px-2" data-bs-toggle="tooltip" data-bs-placement="top" title="Video call"><i class="bi bi-camera-video-fill"></i></a>
                                        <!-- Card action START -->
                                        <div class="dropdown">
                                            <a class="icon-md rounded-circle btn btn-primary-soft me-2 px-2" href="#" id="chatcoversationDropdown" role="button" data-bs-toggle="dropdown" data-bs-auto-close="outside" aria-expanded="false"><i class="bi bi-three-dots-vertical"></i></a>
                                            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="chatcoversationDropdown">
                                                <li><a class="dropdown-item" href="#"><i class="bi bi-check-lg me-2 fw-icon"></i>모두 읽음 표시</a></li>
                                                <li><a class="dropdown-item" href="#"><i class="bi bi-mic-mute me-2 fw-icon"></i>대화 음소거</a></li>
                                                <li><a class="dropdown-item" href="#"><i class="bi bi-person-check me-2 fw-icon"></i>상대 프로필 페이지 이동</a></li>
                                                <li><a class="dropdown-item" onclick="deleteChatRoom(event)"><i class="bi bi-trash me-2 fw-icon"></i>채팅방 나가기</a></li>
                                                <li class="dropdown-divider"></li>
                                                <li><a class="dropdown-item" onclick="getChatPage()" "><i class="bi bi-archive me-2 fw-icon"></i>채팅 목록으로 이동</a></li>
                                            </ul>
                                        </div>
                                        <!-- Card action END -->
                                    </div>
                                </div>
                                <!-- Top avatar and status END -->
                                <hr>
                                <!-- Chat conversation START -->
                                <input type="hidden" id="pre-msg-username" value="">
                                <input type="hidden" id="pre-msg-create" value="">
                                <input type="hidden" id="pre-msg-id" value="">
                                <div class="chat-conversation-content" style="overflow: auto" id="chat-display-body">
                               
                                </div>
                        </div>
                    </div>`;
}

function cardFooter(){
    return `<div class="d-sm-flex align-items-end">
                            <textarea id="chatting-msg" class="form-control mb-sm-0 mb-3" data-autoresize placeholder="메시지를 입력하세요" rows="1"></textarea>
                            <button class="btn btn-sm btn-danger-soft ms-sm-2"><i class="fa-solid fa-face-smile fs-6"></i></button>
                            <button class="btn btn-sm btn-secondary-soft ms-2"><i class="fa-solid fa-paperclip fs-6"></i></button>
                            <button class="btn btn-sm btn-primary ms-2" onclick="wsSend()"><i class="fa-solid fa-paper-plane fs-6"></i></button>
                    </div>`;
}


const readData = (data) => {
    let html = '';
    //alert(data.unread);
    if(Number(data.unread)!==0){
        html += `<i class="fa-solid fa-check" style="color: #0a58ca"><br> ${data.unread_count}</i>`;
    }else {
        $(".readData-me-send *").remove();
        $(".readData-me-send").remove();
    }
    return html;
}//읽은 수에 따라 다르게 표시

function delSameTimeChat(one, two){
    let oneT = forChatTimestamp(one.createAt);
    let twoT = forChatTimestamp(two.createAt);
    if(one.sender.username===two.sender.username){
        if(oneT===twoT) {
            $("#createDate-" + one.id + " *").remove();
            $("#createDate-" + one.id).remove();
            return two.id;
        }
    }
}//만약 둘이 같은 시간에 보내면 화면 표시 달라짐

function chatSent_m(resp){
    let sendData = forChatTimestamp(resp.createAt);
    //console.log(sendData);
    return `<div class="w-100">
                      <div class="d-flex flex-column align-items-end">                  
                        <div class="bg-primary text-white p-2 px-3 rounded-2">${resp.msg}</div>
                        <!-- Images -->
                        <div class="d-flex my-2" id="createDate-${resp.id}">
                          <div class="small ms-2 readData-me-send">${readData(resp)}</div>                         
                          <div class="small text-secondary">${sendData}</div>
                         
                        </div>
                      </div>
                    </div>`;
}//내가 보낸거
function chatSent_o(resp){

    let sendData = forChatTimestamp(resp.createAt);

    return `<div class="flex-shrink-0 avatar avatar-xs me-2">
                      <img id="senderImg-${resp.id}" class="avatar-img rounded-circle" src="/assets/images/avatar/10.jpg" alt=""><!--${resp.sender.profileImageUrl}-->
                    </div>
                    <div class="flex-grow-1">
                      <div class="w-100">
                        <div class="d-flex flex-column align-items-start" >
                                <div id="sender-${resp.id}">
                                    <h6 class="mb-0 mt-1">${resp.sender.username}</h6>
                                </div>                        
                            <div class="bg-light text-secondary p-2 px-3 rounded-2">${resp.msg}</div>
                            <div class="d-flex my-2" id="createDate-${resp.id}">
                            <div class="small text-secondary">${sendData} </div>
                            <div class="small ms-2">${readData(resp)}</div>
                          </div>
                        </div>
                        </div>
                      </div>
                    </div>`;
}//다른 사람이 보낸거

function forChatTimestamp(timestamp){
    const date = new Date(timestamp);
    const currentDate = new Date();
    const diffMs = currentDate - date;
    const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));

    if (diffDays >= 365) {
        return date.toLocaleString("ko-KR", {
            year: "numeric",
            month: "short",
            day: "numeric",
            hour: "numeric",
            minute: "numeric",
            hour12: true,
        });
    } else if (diffDays > 7) {
        return date.toLocaleString("ko-KR", {
            weekday: "long",
            month: "short",
            day: "numeric",
            hour: "numeric",
            minute: "numeric",
            hour12: true,
        });
    } else if (diffDays > 0) {
        if (diffDays === 1) {
            return "어제, " + date.toLocaleString("ko-KR", {
                hour: "numeric",
                minute: "numeric",
                hour12: true,
            });
        } else {
            return date.toLocaleString("ko-KR", {
                weekday: "long",
                hour: "numeric",
                minute: "numeric",
                hour12: true,
            });
        }
    } else {
        return date.toLocaleString("ko-KR", {
            hour: "numeric",
            minute: "numeric",
            hour12: true,
        });
    }
} //날짜 포맷, 언제 도착했는지

chatting.init();
/*
connect();*/





/*import{ Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

//const Stomp = require('stompjs'); //-> 스크립트에서 require사용 못함
//let uuid = document.querySelector("input[name='currentRoom-uuid']").value;
let socket = new SockJS('http://localhost:8080/ws/chat');
let client = new Client({
    webSocketFactory: () => Stomp.over(socket),
    onConnect: () => {
        client.subscribe('/topic/test01', message => {
            console.log(`Received: ${message.body}`);
            if (message.body === 'First Message') {
                console.log('firstMessage:', message.body);
            }
        });
        client.publish({ destination: '/topic/test01', body: 'First Message' });
    },
});
client.activate();
let reconnect = 0;

function connect() {
    let uuid = document.querySelector("input[name='currentRoom-uuid']").value;
    let name = getJoinName();
    const path = window.location.pathname; // returns "/chat/18"
    const parts = path.split('/');
    const id = parseInt(parts[2]);

    stompClient.connect(
        {},
        function (frame) {
            stompClient.subscribe(`/topic/chat/room/${uuid}`, function (message) {
                let rcData = JSON.parse(message.body);
                alert(rcData);
            });

            stompClient.send(`/app/chat/${id}/message`, {}, JSON.stringify({ type: 'ENTER', msg: '' }));
        },
        function (error) {
            if (reconnect++ <= 5) {
                setTimeout(function () {
                    console.log('connection reconnect');
                    socket = new SockJS('/ws/chat');
                    stompClient = new Client({
                        brokerURL: '/ws/chat',
                        connectHeaders: {},
                        debug: function (str) {
                            console.log(str);
                        },
                        reconnectDelay: 5000,
                        heartbeatIncoming: 4000,
                        heartbeatOutgoing: 4000,
                    });
                    connect();
                }, 10 * 1000);
            }
        }
    );
}*/
//sockjs 오류로 다른 방식으로 변경..