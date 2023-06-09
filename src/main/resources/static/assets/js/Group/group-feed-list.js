function iinit() {

    $.ajax({
        type: "GET",
        url: "/api/feed/getuser",
        async: false
    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        initProfile(resp);
        init();
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}
iinit();

function init() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const groupId = urlParams.get('id');
    var page = 0;

    $.ajax({
        type: "GET",
        url: "/api/feeds/group/"+groupId+"/"+page,
        dataType: "json"
    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        setFeedsContent(resp.result);
        //initLoadMoreButton();
    }).fail(function(error){
        alert(JSON.stringify(error));
    });

    $("#loadmore-button").on("click",()=>{
        loadmore(++page, groupId);
    });
}




function loadmore(currentPage, groupId){

    $.ajax({
        type: "GET",
        url: "/api/feeds/group/"+groupId+"/"+currentPage,
        dataType: "json"
    }).done(function(resp){
        if(resp.result.length == 0) alert("마지막 페이지입니다");
        setFeedsContent(resp.result);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function initProfile(data){
    let profileDiv = document.querySelector("#profileDiv");
    let profileBox = document.createElement("a");
    profileBox.href = "/mypage?user_id="+data.id;
    profileBox.innerHTML = profileContent(data);
    profileDiv.append(profileBox);
}

function profileContent(data) {
    return `<img
                        class="avatar-img rounded-circle"
                        src="${data.profileImageUrl}"
                        alt=""
                />`;
}


function setFeedsContent(data) {
    if(data.length == 0) {
        let GroupNoFeedBox = document.querySelector("#noFeed");
        GroupNoFeedBox.style.display = "";
    }
    for(let i=0; i<data.length; i++){
        let feedBox = document.querySelector("#feed");

        //카드박스 body
        let cardBox = document.createElement("div"); //<div></div>
        cardBox.className = "card"; //<div class="card"></div>
        cardBox.innerHTML = feedContent(data[i]);
        //카드박스 해쉬태그
        let tagBox = document.createElement("li");
        tagBox.className = "list-inline-item m-0";
        for(let j=0; j<data[i].feedTagDtoList.length; j++){
            tagBox.innerHTML += `<a class="btn btn-light btn-sm" href="#">${data[i].feedTagDtoList[j].hashTagDto.tagName}</a>&nbsp`;
        }
        //해쉬태그 밑에 공백을 만들고싶어서..
        let blankBox = document.createElement("div");
        blankBox.innerHTML=`&nbsp`;

        cardBox.append(tagBox);
        cardBox.append(blankBox);
        feedBox.append(cardBox); //그걸 feedBox에 붙임
    }
}

function feedContent(data) {
    return `<div class="card-header border-0 pb-0">
                <div class="d-flex align-items-center justify-content-between">
                  <div class="d-flex align-items-center">
                    <!-- Avatar -->
                    <div class="avatar avatar-story me-2">
                      <a href="/nol/mypage?user_id=${data.userResponse.id}">
                        <img
                          class="avatar-img rounded-circle"
                          src="${data.userResponse.profileImageUrl}"
                          alt=""
                        />
                      </a>
                    </div>
                    <!-- Info -->
                    <div>
                      <div class="nav nav-divider">
                        <h6 class="nav-item card-title mb-0">
                          <a href="/nol/mypage?user_id=${data.userResponse.id}">${data.userResponse.username} </a>
                        </h6>
                        <span class="nav-item small"> ${data.modDate} </span>
                      </div>
                      <h6 class="nav-item card-title mb-0">
                          <a href="/nol/feed?id=${data.id}">${data.title}  </a>
                        </h6>
                    </div>
                  </div>
                  <!-- Card feed action dropdown START -->
                  <div class="dropdown">
                    <a
                      href="#"
                      class="text-secondary btn btn-secondary-soft-hover py-1 px-2"
                      id="cardFeedAction"
                      data-bs-toggle="dropdown"
                      aria-expanded="false"
                    >
                      <i class="bi bi-three-dots"></i>
                    </a>
                    <!-- Card feed action dropdown menu -->
                
                    <ul
                      class="dropdown-menu dropdown-menu-end"
                      aria-labelledby="cardFeedAction"
                    >
                      <li>
                        <a class="dropdown-item" href="#">
                          <i class="bi bi-bookmark fa-fw pe-2"></i>Save post</a
                        >
                      </li>
                      <li>
                        <a class="dropdown-item" href="#">
                          <i class="bi bi-person-x fa-fw pe-2"></i>Unfollow lori
                          ferguson
                        </a>
                      </li>
                      <li>
                        <a class="dropdown-item" href="#">
                          <i class="bi bi-x-circle fa-fw pe-2"></i>Hide post</a
                        >
                      </li>
                      <li>
                        <a class="dropdown-item" href="#">
                          <i class="bi bi-slash-circle fa-fw pe-2"></i>Block</a
                        >
                      </li>
                      <li><hr class="dropdown-divider" /></li>
                      <li>
                        <a class="dropdown-item" href="#">
                          <i class="bi bi-flag fa-fw pe-2"></i>Report post</a
                        >
                      </li>
                    </ul>
                  </div>
                  <!-- Card feed action dropdown END -->
                </div>
             </div>
              <!-- Card header END -->
              <!-- Card body START -->
              <div class="card-body">
                <p>
                  ${data.content} 
                </p>
                <!-- Card img -->
                <div class="mainthumbnailimageContainer">
                    <img
                      class="mainthumbnailimage"
                      src=${data.imageDtoList[0].url} 
                      alt=""
                    />
                </div>
                <!-- Feed react START -->
                <ul class="nav nav-stack py-3 small">
                  <li class="nav-item">
                    <a
                      class="nav-link active"
                      href="#!"
                      data-bs-container="body"
                      data-bs-toggle="tooltip"
                      data-bs-placement="top"
                      data-bs-html="true"
                      data-bs-custom-class="tooltip-text-start"
                      data-bs-title="Frances Guerrero<br> Lori Stevens<br> Billy Vasquez<br> Judy Nguyen<br> Larry Lawson<br> Amanda Reed<br> Louis Crawford"
                      onclick="like(${data.id})"
                    >
                      <i class="bi bi-hand-thumbs-up-fill pe-1"></i>좋아요(${data.totalLike})</a
                    >
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="#!">
                      <i class="bi bi-chat-fill pe-1"></i>댓글(${data.totalComment})</a
                    >
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="#!" onclick="save(${data.id})">
                      <i class="bi bi-bookmark-check-fill pe-1"></i>저장하기</a
                    >
                  </li>
                  <li class="nav-item">
                    <a class="nav-link save" href="#!">
                      <i class="bi bi-eye pe-1"></i>${data.totalView}</a>
                  </li>
                  <!-- Card share action END -->
                </ul>
                <!-- Feed react END -->

                <!-- Add comment -->
                
                <!-- Comment wrap START -->
                
                <!-- Comment wrap END -->
              </div>
              <!-- Card body END -->
              <!-- Card footer START -->
              <div class="card-footer border-0 pt-0>
               </div>`;
}


function reply() {
    let content={
        content: $("#replyContent").val(),
        feedId: $("#feedId").val()
    };
    alert(content.content+'  '+content.feedId);
    $.ajax({
        type: "POST",
        url: "/api/comment",
        data: JSON.stringify(content),
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function(resp){
        alert('댓글 등록 완료');
        window.location.href = "/test";
    }).fail(function(error){
        alert('댓글 등록 실패');
        alert(JSON.stringify(error));
        window.location.href = "/test";
    });
}

function like(data) {
    const url = window.location.href;

    $.ajax({
        type: "GET",
        url: "/api/like/feed/"+data,
        dataType: "json"
    }).done(function(resp){
        if(resp) like_delete(data,url);
        else like_register(data,url);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function like_register(data,url) {

    $.ajax({
        type: "POST",
        url: "/api/like/feed/"+data,
        data: JSON.stringify(content),
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function(resp){
        window.location.href = url;
    }).fail(function(error){
        alert(JSON.stringify(error));
        window.location.href = url;
    });
}

function like_delete(data,url) {

    $.ajax({
        type: "DELETE",
        url: "/api/like/feed/"+data
    }).done(function(resp){
        window.location.href = url;
    }).fail(function(error){
        alert(JSON.stringify(error));
        window.location.href = url;
    });
}

function save(data) {
    const url = window.location.href;

    $.ajax({
        type: "GET",
        url: "/api/feed/store/"+data,
        dataType: "json"
    }).done(function(resp){
        if(resp) save_delete(data,url);
        else save_register(data,url);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function save_register(data,url) {

    $.ajax({
        type: "POST",
        url: "/api/feed/store/"+data,
        data: JSON.stringify(content),
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function(resp){
        window.location.href = url;
    }).fail(function(error){
        alert(JSON.stringify(error));
        window.location.href = url;
    });
}

function save_delete(data,url) {

    $.ajax({
        type: "DELETE",
        url: "/api/feed/store/"+data
    }).done(function(resp){
        window.location.href = url;
    }).fail(function(error){
        alert(JSON.stringify(error));
        window.location.href = url;
    });
}