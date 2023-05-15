function init(){
    getRecFeedListFunc();
}


function loadmoreRec(currentPage){
    $.ajax({
        type: "GET",
        url: "/api/feeds/"+currentPage,
        dataType: "json"
    }).done(function(resp){
        if(resp.result.length === 0) alert("마지막 페이지입니다");
        alert(resp);
        setFeedsContent(resp.result);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function getRecFeedListFunc(){
    $.ajax({
        type:"GET",
        url:"/api/rec/0",
        //contentType: "application/json; charset=utf-8"
        dataType: "json"
    }).done(function (resp){ //dataType을 통해 이미 json 파일만 가져오게됨. -> 즉 응답만 가져옴
        //alert(JSON.stringify(resp));
        //console.log(resp);
        getMyFeedListHtml(resp.result);
    }).fail(function (error){
        alert("실패");
        alert(JSON.stringify(error));
        console.log(error);
        console.log(JSON.stringify(error));
    });
}

function getMyFeedListHtml(resp){
    //$(".preview-container *").remove();
    //let n = 0;
    let recContainer = document.querySelector(".recommend-info-for-user");

    for (let i=0; i<resp.length; i++){
        let data = resp[i];
        //alert(JSON.stringify(data));
        //console.log(recContainer);

        let recommendbox = document.createElement("div");
        recommendbox.className = "recommend-box";
        recommendbox.innerHTML = getMyRecList(data);
        console.log(recommendbox);
        recContainer.append(recommendbox);

        let tagInfo = document.querySelector("#TagRec"+data.id);
        let tagCard = document.createElement("li");
        tagCard.className = "list-inline-item m-0";
        let n = data.feedTagDtoList.length;
        if (n>2){n=2}
        for(let j=0; j<n; j++){
            tagCard.innerHTML += `<a class="btn btn-light btn-sm" href="/nol/hashtag?tag_id=${data.feedTagDtoList[j].hashTagDto.id}&tag_name=${data.feedTagDtoList[j].hashTagDto.tagName.substr(1)}">${data.feedTagDtoList[j].hashTagDto.tagName}</a>&nbsp`;
        }

        tagInfo.append(tagCard);
        //n = data.id;
    }
    recContainer.after(getdot());
}

function getMyRecList(data){
    let str = data.content;
    let result = str.substring(0, 10);
    let date = forAgoChatTimestamprec(data.date);

    return ` <div class="preview-item">
                  <div class = "basic img" id="">
                    <img style="width: 300px; height: 200px" src="${data.imageUrl}" alt="Video Preview 1">
                  </div>
                  <div class="text">
                    <h3>${data.title}</h3>
                    <p>${result}...</p>
                  </div>
                </div>
                <div class="recommend-Info">
                <div style="padding-left: 5px; padding-top: 10px" class="d-flex" >
                 <a href="/nol/mypage?user_id=${data.userSimpleDto.id}">
                  <img style="width:48px; height:48px; margin-right: 5px" class="avatar-img rounded-circle" src="${data.userSimpleDto.profileImageUrl}" alt="">
                  </a>
                  <div class="flex-grow-1 d-block">
                    <h6 class="mb-0 mt-1"><a href="/nol/feed?id=${data.id}">${data.userSimpleDto.username}</a></h6>
                    <div class="small text-secondary">${data.userSimpleDto.nickname}</div>
                  </div>
                </div>
                <br>
                        <a class="nav-link save" href="#!">
                        <i class="bi bi-calendar-event pe-1"></i>${data.modiDate} - ${date}</a>

                  <div style="padding-left: 10px; padding-bottom: 15px;">
                <ul class="nav nav-stack py-3 small">
                  <li class="nav-item">
                  <a
                      class="nav-link like"
                      href="#!"
                      data-bs-container="body"
                      data-bs-toggle="tooltip"
                      data-bs-placement="top"
                      data-bs-html="true"
                      data-bs-custom-class="tooltip-text-start"
                      data-bs-title="Frances Guerrero<br> Lori Stevens<br> Billy Vasquez<br> Judy Nguyen<br> Larry Lawson<br> Amanda Reed<br> Louis Crawford"
                      onclick="like(${data.id})"
                      id="like-button-zzzzz"
                    >
                    <a class="nav-link like" href="#!" data-bs-container="body" data-bs-toggle="tooltip" data-bs-placement="top" data-bs-html="true" data-bs-custom-class="tooltip-text-start" data-bs-title="Frances Guerrero<br> Lori Stevens<br> Billy Vasquez<br> Judy Nguyen<br> Larry Lawson<br> Amanda Reed<br> Louis Crawford" onclick="like(2)" id="like-button-zzzzz">
                      <i class="bi bi-hand-thumbs-up-fill pe-1"></i>좋아요(${data.totalLike})</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="/nol/mypage?user_id=${data.userSimpleDto.id}">
                      <i class="bi bi-chat-fill pe-1"></i>댓글(${data.totalComment})</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link save" href="#!">
                      <i class="bi bi-eye pe-1"></i>${data.totalView}</a>
                  </li>
                </ul>
                <div id="TagRec${data.id}"></div>
                  </div>
                  </div>
`;
}

function forAgoChatTimestamprec(timestamp){
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


function getdot(){
    return `div class="d-flex mb-1">
              <div class="flex-grow-1">
                <div class="w-100">
                  <div class="d-flex flex-column align-items-center">
                    <div class="bg-light text-secondary p-3 rounded-2">
                      <div class="typing d-flex align-items-center">
                        <div class="dot"></div>
                        <div class="dot"></div>
                        <div class="dot"></div>
                      </div>
                    </div>
                  </div>
              </div>
            </div>

          </div>`;
}

init();