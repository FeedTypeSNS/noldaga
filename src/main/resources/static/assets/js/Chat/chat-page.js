/*import {forAgoChatTimestamp} from './chatting';*/
/*let socket = new SockJS("/ws/chat");
let ws = Stomp.over(socket);
let reconnect = 0;*/
function setBasic(){
    getChatBody();

    getChatListFunc();

    //outPage();
}
/*function outPage(){
    if (!(window.location.pathname.startsWith('/chat'))) {
        localStorage.removeItem('username');
    }
}*/
function getChatListFunc(){
    $.ajax({
        type:"GET",
        url:"/api/chat/list",
        //contentType: "application/json; charset=utf-8"
        dataType: "json"
    }).done(function (resp){ //dataType을 통해 이미 json 파일만 가져오게됨. -> 즉 응답만 가져옴
        //alert(JSON.stringify(resp));
        //console.log(resp.result);
        getMyChatListHtml(resp.result);

        let name = document.querySelector("#ws-username").value;
        cwsOpen(name);
    }).fail(function (error){
        alert("실패");
        alert(JSON.stringify(error));
        console.log(error);
        console.log(JSON.stringify(error));
    });
}


function getChatBody(){
    $("#chatting-tool *").remove();
    $("#chatting-tool").remove();
    let chatBody = document.querySelector("#chat-tool-body");
    let body = document.createElement("div");
    body.id = "chatting-tool";
    body.className="card-body h-100";
    body.innerHTML = getBody();
    chatBody.append(body);
}

function getMyChatListHtml(result){
    $(".myChatList *").remove();
    $(".myChatList").remove();

    let active = document.querySelector("#active-chat-count");
    active.textContent = '';
    let num = 0;
    for (let j=0; j<result.length; j++){
       num += result[j].unreadCount;
    }
    active.append(num);
    for(let i=0; i<result.length; i++){
        let data = result[i];
        let chatList = document.querySelector("#chatList");

        let myList = document.createElement("div");
        myList.className = "myChatList";
        myList.id = "room-"+data.roomInfo.id;
        myList.innerHTML = getMyChatList(data);
        chatList.append(myList); //mylist 밑에 붙이기 = 왼쪽 채팅방 리스트
        //내 치팅방 리스트 반환 해줌..

        let roomId = data.roomInfo.id;
        let joinCount = Number(data.joinPeoples.length)-1; //회원 참가수에 따라 회원 이미지 변화..
        let joinImg = document.querySelector("#joinListImg"+roomId);
        let thisJoinImg = document.createElement("div");
        thisJoinImg.className = "joinPeopleImgList"+roomId;
        switch (joinCount) {
            case 1 : thisJoinImg.innerHTML = getOneImg(data);break;
            case 2 : thisJoinImg.innerHTML = getTwoImg(data);break;
            case 3 : thisJoinImg.innerHTML = getThreeImg(data);break;
            default : thisJoinImg.innerHTML = getFourImg(data);break;

        }
        joinImg.append(thisJoinImg);
        //코드로 조건에 맞게 하고 싶었는데 안되서 하드 코딩으로 바꿈..

        checkUnread(data);

    }
}

function getMyChatList(data){
    let ago;
    if (data.recentChat.sender.role==="ADMIN"){
        ago = "";
    }else {
        ago = forAgoChatTimestamp(data.recentChat.createAt);
    }
    return`<li data-bs-dismiss="offcanvas">
            <!-- Chat user tab item -->
            <form class="chat-open" id="chatInfo-${data.roomInfo.id}">
            <input type="hidden" name="chat-uuid" value="${data.roomInfo.uuid}">
            <input type="hidden" name="chat-roomId" value="${data.roomInfo.id}">
            <input type="hidden" name="chat-romeName" value="${data.roomInfo.roomName}">
            </form>
            <a class="nav-link" id="chat-${data.roomInfo.id}-tap" data-bs-toggle="pill" role="tab" onclick="getChatDetailRoom(this)">
                <div class="d-flex">
                    <div  class="flex-shrink-0 avatar me-2" id="joinListImg${data.roomInfo.id}">
                        <!--<img class="avatar-img rounded-circle" src="/assets/images/avatar/10.jpg" alt="">--><!--img url 넣어줘야함 회원의-->
                    </div>    
                    <div  class="flex-grow-1 d-block" >
                        <h6 class="mb-0 mt-1">${data.roomInfo.viewRoomName}</h6>
                        <div class="small text-secondary">${data.recentChat.sender.username}: ${data.recentChat.msg} · ${ago}
                          <div class="position-relative" style="margin-left: 3px;" id="unread-${data.roomInfo.id}"></div>
                        </div>
                    </div>
                </div>
               </a>
        </li>
`;
}

function getBody(){
    return `<div class="position-absolute top-50 start-50 translate-middle">
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <svg aria-label="Direct" class="msgImg" color="rgb(0, 0, 0)" fill="rgb(0, 0, 0)" height="96"
                                 role="img" viewBox="0 0 96 96" width="96"><title>Direct</title>
                                <circle cx="48" cy="48" fill="none" r="47" stroke="currentColor" stroke-linecap="round"
                                        stroke-linejoin="round" stroke-width="2"></circle>
                                <line fill="none" stroke="currentColor" stroke-linejoin="round" stroke-width="2" x1="69.286" x2="41.447"
                                      y1="33.21" y2="48.804"></line>
                                <polygon fill="none" points="47.254 73.123 71.376 31.998 24.546 32.002 41.448 48.805 47.254 73.123"
                                         stroke="currentColor" stroke-linejoin="round" stroke-width="2"></polygon>
                            </svg>
                            <p/>
                            <h1 class="h5 mb-0">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                내 메시지</h1><p/>
                            <div class="small text-secondary">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                친구나 그룹에 <br>
                                &nbsp;&nbsp;메시지를 보내보세요.
                            </div>
                            <p/>
                            <div class="dropend position-relative">
                                <div class="nav">
                                    <a id="myFollowList2"  class="btn btn-primary toast-btn" data-target="chatToast"> <i class="bi bi-pencil-square">&nbsp;메시지 보내기</i> </a>
                                </div>
                            </div>
                        </div>

                        <div class="size-fix-box" style=" padding-bottom: 78%; width: 100%"></div>`;
}

function getOneImg(data){
    return`<img class="avatar-img rounded-circle" src="/assets/images/avatar/01.jpg" alt=""><p style="display: none">${data.joinPeoples[0].username}</p>`;
    //img url 걸어줘야함. 아직 없으니 임시 대체
}
//한사람일 경우 img 태그만 필요..

function getTwoImg(data){
    return`<ul class="avatar-group avatar-group-two">
                        <li class="avatar avatar-xs">
                            <img class="avatar-img rounded-circle" src="/assets/images/avatar/02.jpg" alt="avatar">
                            <p style="display: none">${data.joinPeoples[0].username}</p>
                        </li>
                        <li class="avatar avatar-xs">
                            <img class="avatar-img rounded-circle" src="/assets/images/avatar/03.jpg" alt="avatar">
                            <p style="display: none">${data.joinPeoples[1].username}</p>
                        </li>
                    </ul>
`;
    //img url 걸어줘야함. 아직 없으니 임시 대체
}
function getThreeImg(data){
    return`<ul class="avatar-group avatar-group-three">
                        <li class="avatar avatar-xs">
                            <img class="avatar-img rounded-circle" src="/assets/images/avatar/03.jpg" alt="avatar">
                            <p style="display: none">${data.joinPeoples[0].username}</p>
                        </li>
                        <li class="avatar avatar-xs">
                            <img class="avatar-img rounded-circle" src="/assets/images/avatar/04.jpg" alt="avatar">
                            <p style="display: none">${data.joinPeoples[1].username}</p>
                        </li>
                        <li class="avatar avatar-xs">
                            <img class="avatar-img rounded-circle" src="/assets/images/avatar/05.jpg" alt="avatar">
                            <p style="display: none">${data.joinPeoples[2].username}</p>
                        </li>
                    </ul>
`;
    //img url 걸어줘야함. 아직 없으니 임시 대체
}
function getFourImg(data){
    return `<ul class="avatar-group avatar-group-four">
                        <li class="avatar avatar-xxs">
                            <img class="avatar-img rounded-circle" src="/assets/images/avatar/06.jpg" alt="avatar">
                             <p style="display: none">${data.joinPeoples[0].username}</p>
                        </li>
                        <li class="avatar avatar-xxs">
                            <img class="avatar-img rounded-circle" src="/assets/images/avatar/07.jpg" alt="avatar">
                             <p style="display: none">${data.joinPeoples[1].username}</p>
                        </li>
                        <li class="avatar avatar-xxs">
                            <img class="avatar-img rounded-circle" src="/assets/images/avatar/08.jpg" alt="avatar">
                            <p style="display: none">${data.joinPeoples[2].username}</p>
                        </li>
                        <li class="avatar avatar-xxs">
                            <img class="avatar-img rounded-circle" src="/assets/images/avatar/placeholder.jpg" alt="avatar">
                            <p style="display: none">${data.joinPeoples[3].username}</p>
                        </li>
                    </ul>`;
}//src에 이미지 태그 변경 필요..

function forAgoChatTimestamp(timestamp){
    let date = new Date(timestamp);
    let diffMs = new Date() - date;

// Calculate the time difference in minutes
    let diffMins = Math.floor(diffMs / (1000 * 60));

    if (diffMins < 60) {
        // Less than an hour ago
        return diffMins === 1 ? '1분 전' : `${diffMins}분 전`;
    } else if (diffMins < 1440) {
        // Within a day
        const diffHours = Math.floor(diffMins / 60);
        return diffHours === 1 ? '1시간 전' : `${diffHours}시간 전`;
    } else if (diffMins < 10080) {
        // Within a week
        const diffDays = Math.floor(diffMins / 1440);
        return diffDays === 1 ? '1일 전' : `${diffDays}일 전`;
    } else {
        // More than a week ago
        const diffWeeks = Math.floor(diffMins / 10080);
        return diffWeeks === 1 ? '1주 전' : `${diffWeeks}주 전`;
    }

} //날짜 포맷, 온지 얼마나 됬는지

let unread = 0;
function checkUnread(data) {
    unread += data.unreadCount;
    console.log("총안읽은 메시지 수 :"+unread);
    if (unread !== 0) {
        $("#unread-" + data.roomInfo.id + " *").remove();
        let unr = document.querySelector("#unread-" + data.roomInfo.id)
        let un = document.createElement("span");
        un.className = "position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger";
        un.innerHTML = `${unread}`;
        unr.append(un);
    }else if (unread===0){
        $("#unread" + data.roomInfo.id + " *").remove();
    }
}

init();