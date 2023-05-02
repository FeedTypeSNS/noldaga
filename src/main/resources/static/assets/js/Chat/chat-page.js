function init(){
    $.ajax({
        type:"GET",
        url:"/api/chat/list",
        //contentType: "application/json; charset=utf-8"
        dataType: "json"
    }).done(function (resp){ //dataType을 통해 이미 json 파일만 가져오게됨. -> 즉 응답만 가져옴
        alert(JSON.stringify(resp));
        alert("성공");
        //console.log(resp.result);
        initChatPageSimpleModified(resp.result);
    }).fail(function (error){
        alert("실패");
        alert(JSON.stringify(error));
        console.log(error);
        console.log(JSON.stringify(error));
    });
}

init();

function initChatPageSimpleModified(result){
    for(let i=0; i<result.length; i++){
        let data = result[i];
        let chatList = document.querySelector("#chatList");

        let myList = document.createElement("div");
        myList.className = "myChatList";

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


    }
}

function getMyChatList(data){
    return`<li data-bs-dismiss="offcanvas">
            <!-- Chat user tab item -->
            <input type="hidden" name="chat-uuid" value="${data.roomInfo.uuid}">
            <input type="hidden" name="chat-romeName" value="${data.roomInfo.roomName}">
            <a href="#chat-${data.roomInfo.id}" class="nav-link" id="chat-${data.roomInfo.id}-tab" data-bs-toggle="pill" role="tab">
                <div class="d-flex">
                    <div style="display: inline-block" class="flex-shrink-0 avatar me-2" id="joinListImg${data.roomInfo.id}">
                        <!--<img class="avatar-img rounded-circle" src="assets/images/avatar/10.jpg" alt="">--><!--img url 넣어줘야함 회원의-->
                    </div>
                    </div>        
                    <div style="display: inline-block" class="flex-grow-1 d-block" >
                        <h6 class="mb-0 mt-1">${data.roomInfo.viewRoomName}</h6>
                        <div class="small text-secondary">${data.recentChat.sender.username}: ${data.recentChat.msg}</div>
                    </div>
                </div>
            </a>
        </li>
`;
}

function getOneImg(data){
    return`<img class="avatar-img rounded-circle" src="assets/images/avatar/01.jpg" alt=""><p style="display: none">${data.joinPeoples[0].username}</p>`;
    //img url 걸어줘야함. 아직 없으니 임시 대체
}
//한사람일 경우 img 태그만 필요..

function getTwoImg(data){
    return`<ul class="avatar-group avatar-group-two">
                        <li class="avatar avatar-xs">
                            <img class="avatar-img rounded-circle" src="assets/images/avatar/02.jpg" alt="avatar">
                            <p style="display: none">${data.joinPeoples[0].username}</p>
                        </li>
                        <li class="avatar avatar-xs">
                            <img class="avatar-img rounded-circle" src="assets/images/avatar/03.jpg" alt="avatar">
                            <p style="display: none">${data.joinPeoples[1].username}</p>
                        </li>
                    </ul>
`;
    //img url 걸어줘야함. 아직 없으니 임시 대체
}
function getThreeImg(data){
    return`<ul class="avatar-group avatar-group-three">
                        <li class="avatar avatar-xs">
                            <img class="avatar-img rounded-circle" src="assets/images/avatar/03.jpg" alt="avatar">
                            <p style="display: none">${data.joinPeoples[0].username}</p>
                        </li>
                        <li class="avatar avatar-xs">
                            <img class="avatar-img rounded-circle" src="assets/images/avatar/04.jpg" alt="avatar">
                            <p style="display: none">${data.joinPeoples[1].username}</p>
                        </li>
                        <li class="avatar avatar-xs">
                            <img class="avatar-img rounded-circle" src="assets/images/avatar/05.jpg" alt="avatar">
                            <p style="display: none">${data.joinPeoples[2].username}</p>
                        </li>
                    </ul>
`;
    //img url 걸어줘야함. 아직 없으니 임시 대체
}
function getFourImg(data){
    return `<ul class="avatar-group avatar-group-four">
                        <li class="avatar avatar-xxs">
                            <img class="avatar-img rounded-circle" src="assets/images/avatar/06.jpg" alt="avatar">
                             <p style="display: none">${data.joinPeoples[0].username}</p>
                        </li>
                        <li class="avatar avatar-xxs">
                            <img class="avatar-img rounded-circle" src="assets/images/avatar/07.jpg" alt="avatar">
                             <p style="display: none">${data.joinPeoples[1].username}</p>
                        </li>
                        <li class="avatar avatar-xxs">
                            <img class="avatar-img rounded-circle" src="assets/images/avatar/08.jpg" alt="avatar">
                            <p style="display: none">${data.joinPeoples[2].username}</p>
                        </li>
                        <li class="avatar avatar-xxs">
                            <img class="avatar-img rounded-circle" src="assets/images/avatar/placeholder.jpg" alt="avatar">
                            <p style="display: none">${data.joinPeoples[3].username}</p>
                        </li>
                    </ul>`;
}//src에 이미지 태그 변경 필요..




/*
function getPeopleListImg(joinCount, data, myList) {
    let thisJoinImg = document.createElement("div");
    thisJoinImg.className = "joinPeopleImgList";
    if (joinCount <= 1) {
        thisJoinImg.innerHTML = getOneImg(data);

    } else {
        let num;
        switch (joinCount) {
            case 2 :
                num = "two";
                break;
            case 3 :
                num = "three";
                break;
            default :
                num = "four";
                joinCount = 4;
        }
        let ulImgList = document.createElement("ul");
        ulImgList.className = "avatar-group avatar-group-" + num;
        for (let j = 0; j < joinCount; j++) {
            let dataImg = data.joinPeoples[j];
            if (joinCount > 3) {
                ulImgList.innerHTML = getFourOverImg(dataImg);
            } else {
                ulImgList.innerHTML = getOneOverImg(dataImg);
            }
        }

    }

    thisJoinImg.append(myList);
    return thisJoinImg;
} //회원 참가 채팅 리스트 반환에서 이미지 부분 너무 길어서 밖에 배치*/
