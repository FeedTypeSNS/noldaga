function getUser() {
    $.ajax({
        type: "GET",
        url: "/api/feed/getuser",
        async: false
    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        setProfile(resp);
        getFeeds();
    }).fail(function(error){
        alert(JSON.stringify(error));
    });

}

getUser();

function getGroups() {

    $.ajax({
        type: "GET",
        url: "/api/groups/member",
        dataType: "json"
    }).done(function(resp){
        setModalGroupSelectBox(resp.result);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function getFeeds() {
    var page = 0;

    $.ajax({
        type: "GET",
        url: "/api/feeds/"+page,
        dataType: "json"
    }).done(function(resp){
        setFeedsContent(resp.result);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });

    $("#loadmore-button").on("click",()=>{
        loadmore(++page);
    });
}


function loadmore(currentPage){
    $.ajax({
        type: "GET",
        url: "/api/feeds/"+currentPage,
        dataType: "json"
    }).done(function(resp){
        setFeedsContent(resp.result);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function setModalGroupSelectBox(data){
    for(let i=0; i<data.length; i++){
        let groupSelectBox = document.querySelector("#group_id");

        //카드 형식의 피드
        let selectOption = document.createElement("option"); //<option></option>
        selectOption.value = `${data[i].id}`;
        selectOption.innerHTML = `${data[i].name}`;
        groupSelectBox.append(selectOption);
    }
}

function setProfile(data){
    let profileBox = document.querySelector("#profileDiv");

    let profileCard = document.createElement("a");
    profileCard.href = "/mypage?user_id="+data.id;
    profileCard.innerHTML = profileContent(data);

    profileBox.append(profileCard);
}

function profileContent(data) {
    return `<img
                        class="avatar-img rounded-circle"
                        src="/assets/images/albums/07.jpg"
                        alt=""
                />`;
}

function setFeedsContent(data) {

    for(let i=0; i<data.length; i++){
        let feedsBox = document.querySelector("#feed");

        //카드 형식의 피드
        let feedCard = document.createElement("div"); //<div></div>
        feedCard.className = "card"; //<div class="card"></div>
        feedCard.innerHTML = feedContent(data[i]);

        //내용 밑에 들어가는 해시태그
        let tagCard = document.createElement("li");
        tagCard.className = "list-inline-item m-0";
        for(let j=0; j<data[i].feedTagDtoList.length; j++){
            tagCard.innerHTML += `<a class="btn btn-light btn-sm" href="#">${data[i].feedTagDtoList[j].hashTagDto.tagName}</a>&nbsp`;
        }

        //해쉬태그 밑에 공백을 만들고싶어서..
        let blankBox = document.createElement("div");
        blankBox.innerHTML=`&nbsp`;

        feedCard.append(tagCard);
        feedCard.append(blankBox);
        feedsBox.append(feedCard); //그걸 feedsBox에 붙임
    }
}

function feedContent(data) {
    return `<div class="card-header border-0 pb-0">
                <div class="d-flex align-items-center justify-content-between">
                  <div class="d-flex align-items-center">
                    <!-- Avatar -->
                    <div class="avatar avatar-story me-2">
                      <a href="/mypage?user_id=${data.userResponse.id}">
                        <img
                          class="avatar-img rounded-circle"
                          src="/assets/images/avatar/04.jpg"
                          alt=""
                        />
                      </a>
                    </div>
                    <!-- Info -->
                    <div>
                      <div class="nav nav-divider">
                        <h6 class="nav-item card-title mb-0">
                          ${data.userResponse.username}
                        </h6>
                        <span class="nav-item small"> ${data.modDate} </span>
                      </div>
                      <h6 class="nav-item card-title mb-0">
                          <a href="/feed?id=${data.id}">${data.title}  </a>
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
                <img
                  class="card-img"
                  src="/assets/images/post/3by2/01.jpg"
                  alt="Post"
                />
                <!-- Feed react START -->
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
                      <i class="bi bi-hand-thumbs-up-fill pe-1"></i>좋아요(${data.totalLike})</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="#!">
                      <i class="bi bi-chat-fill pe-1"></i>댓글(${data.totalComment})</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link save" href="#!" onclick="save(${data.id})">
                      <i class="bi bi-bookmark-check-fill pe-1"></i>저장하기</a>
                  </li>
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

function like2(data) {
    let result = "";

    $.ajax({
        type: "GET",
        url: "api/like/feed/"+data,
        dataType: "json",
        async: false
    }).done(function(resp){
        if(resp) result = "true";
        else result = "false";
        return result;
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function like(data) {

    $.ajax({
        type: "GET",
        url: "api/like/feed/"+data,
        dataType: "json"
    }).done(function(resp){
        if(resp){
            like_delete(data);
            make_like_inactive();
        }
        else{
            like_register(data);
            make_like_active();
        }
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function like_register(data) {

    $.ajax({
        type: "POST",
        url: "api/like/feed/"+data,
        data: JSON.stringify(content),
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function(resp){
        window.location.href = "/";
    }).fail(function(error){
        alert(JSON.stringify(error));
        window.location.href = "/";
    });
}

function make_like_active() {
    //$("#like-button-zzzzz").classList.remove("nav-link", "like");
    //$("#like-button-zzzzz").classList.add("nav-link", "active");
    //const navLinkLike = document.querySelector(".nav-link.like");
    //navLinkLike.classList.remove("nav-link", "like");
    //navLinkLike.classList.add("nav-link", "active");
    const navLinkLike = document.querySelector('#like-button-zzzzz');
    navLinkLike.classList.remove("nav-link", "like");
    navLinkLike.classList.add("nav-link", "active");
}

function like_delete(data) {

    $.ajax({
        type: "DELETE",
        url: "api/like/feed/"+data
    }).done(function(resp){
        window.location.href = "/";
    }).fail(function(error){
        alert(JSON.stringify(error));
        window.location.href = "/";
    });
}

function make_like_inactive() {
    const navLinkLike = document.querySelector('.nav-link.like');
    const cList =  navLinkLike.classList;
    navLinkLike.classList.remove("nav-link", "like");
    navLinkLike.classList.add("nav-link", "active");

}

function save(data) {

    $.ajax({
        type: "GET",
        url: "/api/feed/store/"+data,
        dataType: "json"
    }).done(function(resp){
        if(resp) save_delete(data);
        else save_register(data);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function save_register(data) {

    $.ajax({
        type: "POST",
        url: "api/feed/store/"+data,
        data: JSON.stringify(content),
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function(resp){
        window.location.href = "/";
    }).fail(function(error){
        alert(JSON.stringify(error));
        window.location.href = "/";
    });
}

function save_delete(data) {

    $.ajax({
        type: "DELETE",
        url: "api/feed/store/"+data
    }).done(function(resp){
        window.location.href = "/";
    }).fail(function(error){
        alert(JSON.stringify(error));
        window.location.href = "/";
    });
}