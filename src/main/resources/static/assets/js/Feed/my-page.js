function getUser() {

    $.ajax({
        type: "GET",
        url: "/api/feed/getuser",
        async: false
    }).done(function(loginUser){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        getMyPageOwnerData(loginUser);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });

    $("#loadmore-button").on("click",()=>{
        alert("버튼확인");
        loadmore(++page);
    });
}
getUser();

function getMyPageOwnerData(loginUser) {
    const queryString = window.location.search; //마이페이지 소유자 아이디
    const urlParams = new URLSearchParams(queryString);
    const myPageOwnerId = urlParams.get('user_id');

    $.ajax({
        type: "GET",
        url: "/api/users/"+myPageOwnerId+"/profile",
        dataType: "json"
    }).done(function(myPageOwner){
        setProfile(myPageOwner.result,loginUser);
        getFeeds();
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function getFeeds() {
    const queryString = window.location.search; //마이페이지 소유자
    var page = 0;

    $.ajax({
        type: "GET",
        url: "/api/feeds/mypage/"+page+queryString,
        dataType: "json"
    }).done(function(resp){
        insertPhotoCards(resp.result);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}


function setProfile(myPageOwner,loginUser){
    let profileCard = document.querySelector("#profileCard");
    if(myPageOwner.id == loginUser.id)
        profileCard.innerHTML = profileContentMine(myPageOwner); //저장됨 버튼이 보임
    else
        profileCard.innerHTML = profileContentOther(myPageOwner);//저장됨 버튼이 안보임
}

function profileContentMine(data) {
    var profileMessage = (data.profileMessage==="" || data.profileMessage==null)? "" : data.profileMessage;

    return `<div class="h-200px rounded-top" style="background-image:url(/assets/images/bg/05.jpg); background-position: center; background-size: cover; background-repeat: no-repeat;"></div>
            <!-- Card body START -->
            <div class="card-body py-0">
              <div class="d-sm-flex align-items-start text-center text-sm-start">
                <div>
                  <!-- Avatar -->
                  <div class="avatar avatar-xxl mt-n5 mb-3">
                    <img class="avatar-img rounded-circle border border-white border-3" src=${data.profileImageUrl} alt="">
                  </div>
                </div>
                <div class="ms-sm-4 mt-sm-3">
                  <!-- Info -->
                  <h1 class="mb-0 h5">${data.username} <i class="bi bi-patch-check-fill text-success small"></i></h1>
                  <p class="mb-0 h6">${data.totalFollower} 팔로워  ${data.totalFollowing} 팔로우</p>
                  <h1 class="mb-0 h5">${profileMessage}</h1>
                </div>
                <!-- Button -->
                <div class="d-flex mt-3 justify-content-center ms-sm-auto">
                  <a href="/editProfile" class="btn btn-danger-soft me-2" type="button"> <i class="bi bi-pencil-fill pe-1"></i> 프로필 수정 </a>
                  <div class="dropdown">
                    <!-- Card share action menu -->
                    <button class="icon-md btn btn-light" type="button" id="profileAction2" data-bs-toggle="dropdown" aria-expanded="false">
                      <i class="bi bi-three-dots"></i>
                    </button>
                    <!-- Card share action dropdown menu -->
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="profileAction2">
                      <li><a class="dropdown-item" href="#"> <i class="bi bi-bookmark fa-fw pe-2"></i>Share profile in a message</a></li>
                      <li><a class="dropdown-item" href="#"> <i class="bi bi-file-earmark-pdf fa-fw pe-2"></i>Save your profile to PDF</a></li>
                      <li><a class="dropdown-item" href="#"> <i class="bi bi-lock fa-fw pe-2"></i>Lock profile</a></li>
                      <li><hr class="dropdown-divider"></li>
                      <li><a class="dropdown-item" href="#"> <i class="bi bi-gear fa-fw pe-2"></i>Profile settings</a></li>
                    </ul>
                  </div>
                </div>
              </div>
              <!-- List profile -->
              <div id="favorGroups"></div>
              <!--<ul class="list-inline mb-0 text-center text-sm-start mt-3 mt-sm-0">
                <div class="flex-shrink-0 avatar avatar-xs me-2">
                   <a href=""><img class="avatar-img rounded-circle" src="/assets/images/avatar/01.jpg" alt=""/></a>
                </div>
              </ul>-->
            </div>
            <!-- Card body END -->
            <div class="card-footer mt-3 pt-2 pb-0">
              <!-- Nav profile pages -->
              <ul class="nav nav-bottom-line align-items-center justify-content-center justify-content-md-start mb-0 border-0">
                  <li class="nav-item"> <a class="nav-link active" href="/nol/mypage?user_id=${data.id}"> 게시물 </a> </li>
                  <li class="nav-item"> <a class="nav-link" href="/nol/save" > 저장됨</a> </li>
                  <li class="nav-item"> <a class="nav-link" href="/nol/like" > 좋아요한</a> </li>
                  <li class="nav-item"> <a class="nav-link" href="/nol/friend?user_id=${data.id}"> 친구목록 <span class="badge bg-success bg-opacity-10 text-success small">${data.totalFollower+data.totalFollowing}</span> </a> </li>
              </ul>
            </div>`;
}

function profileContentOther(data) {
    var profileMessage = (data.profileMessage==="" || data.profileMessage==null)? "" : data.profileMessage;

    return `<div class="h-200px rounded-top" style="background-image:url(/assets/images/bg/05.jpg); background-position: center; background-size: cover; background-repeat: no-repeat;"></div>
            <!-- Card body START -->
            <div class="card-body py-0">
              <div class="d-sm-flex align-items-start text-center text-sm-start">
                <div>
                  <!-- Avatar -->
                  <div class="avatar avatar-xxl mt-n5 mb-3">
                    <img class="avatar-img rounded-circle border border-white border-3" src=${data.profileImageUrl} alt="">
                  </div>
                </div>
                <div class="ms-sm-4 mt-sm-3">
                  <!-- Info -->
                  <h1 class="mb-0 h5">${data.username} <i class="bi bi-patch-check-fill text-success small"></i></h1>
                  <p class="mb-0 h6">${data.totalFollower} 팔로워  ${data.totalFollowing} 팔로우</p>
                  <h1 class="mb-0 h5">${profileMessage}</h1>
                </div>
                <!-- Button -->
                <div class="d-flex mt-3 justify-content-center ms-sm-auto">
                  <button class="btn btn-danger-soft btn-sm mb-0 me-2" onclick="follow(${data.id})"> 팔로우 </button>
                  <div class="dropdown">
                    <!-- Card share action menu -->
                    <button class="icon-md btn btn-light" type="button" id="profileAction2" data-bs-toggle="dropdown" aria-expanded="false">
                      <i class="bi bi-three-dots"></i>
                    </button>
                    <!-- Card share action dropdown menu -->
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="profileAction2">
                      <li><a class="dropdown-item" href="#"> <i class="bi bi-bookmark fa-fw pe-2"></i>Share profile in a message</a></li>
                      <li><a class="dropdown-item" href="#"> <i class="bi bi-file-earmark-pdf fa-fw pe-2"></i>Save your profile to PDF</a></li>
                      <li><a class="dropdown-item" href="#"> <i class="bi bi-lock fa-fw pe-2"></i>Lock profile</a></li>
                      <li><hr class="dropdown-divider"></li>
                      <li><a class="dropdown-item" href="#"> <i class="bi bi-gear fa-fw pe-2"></i>Profile settings</a></li>
                    </ul>
                  </div>
                </div>
              </div>
              <!-- List profile -->
              <ul class="list-inline mb-0 text-center text-sm-start mt-3 mt-sm-0">
                <div class="flex-shrink-0 avatar avatar-xs me-2">
                   <img class="avatar-img rounded-circle" src="/assets/images/avatar/01.jpg" alt=""/>
                </div>
                <div class="flex-shrink-0 avatar avatar-xs me-2">
                   <img class="avatar-img rounded-circle" src="/assets/images/avatar/02.jpg" alt=""/>
                </div>
                <div class="flex-shrink-0 avatar avatar-xs me-2">
                   <img class="avatar-img rounded-circle" src="/assets/images/avatar/03.jpg" alt=""/>
                </div>
                <div class="flex-shrink-0 avatar avatar-xs me-2">
                   <img class="avatar-img rounded-circle" src="/assets/images/avatar/04.jpg" alt=""/>
                </div>
                <div class="flex-shrink-0 avatar avatar-xs me-2">
                   <img class="avatar-img rounded-circle" src="/assets/images/avatar/05.jpg" alt=""/>
                </div>
                <div class="flex-shrink-0 avatar avatar-xs me-2">
                   <img class="avatar-img rounded-circle" src="/assets/images/avatar/06.jpg" alt=""/>
                </div>
                <div class="flex-shrink-0 avatar avatar-xs me-2">
                   <img class="avatar-img rounded-circle" src="/assets/images/avatar/07.jpg" alt=""/>
                </div>
                <div class="flex-shrink-0 avatar avatar-xs me-2">
                   <img class="avatar-img rounded-circle" src="/assets/images/avatar/08.jpg" alt=""/>
                </div>
                <div class="flex-shrink-0 avatar avatar-xs me-2">
                   <img class="avatar-img rounded-circle" src="/assets/images/avatar/09.jpg" alt=""/>
                </div>
                <div class="flex-shrink-0 avatar avatar-xs me-2">
                   <img class="avatar-img rounded-circle" src="/assets/images/avatar/10.jpg" alt=""/>
                </div>
              </ul>
            </div>
            <!-- Card body END -->
            <div class="card-footer mt-3 pt-2 pb-0">
              <!-- Nav profile pages -->
              <ul class="nav nav-bottom-line align-items-center justify-content-center justify-content-md-start mb-0 border-0">
                  <li class="nav-item"> <a class="nav-link active" href="/nol/mypage?user_id=${data.id}"> 게시물 </a> </li>
                  <li class="nav-item"> <a class="nav-link" href="/nol/friend?user_id=${data.id}"> 친구목록 <span class="badge bg-success bg-opacity-10 text-success small">${data.totalFollower+data.totalFollowing}</span> </a> </li>
              </ul>
            </div>`;
}

function insertPhotoCards(data){
    for(let i=0; i<data.length; i++) {
        let photoCard = document.querySelector("#photoCard");
        let photoDiv = document.createElement("div");
        photoDiv.className = "col-sm-6 col-md-4 col-lg-3";
        photoDiv.innerHTML = photoCardContent(data[i]);
        photoCard.append(photoDiv);
    }
}

function photoCardContent(data) {
    return `<a href="/nol/feed?id=${data.id}" data-gallery="image-popup" data-glightbox="description: .custom-desc2; descPosition: left;">
                              <img class="thumbnailimage" src=${data.imageDtoList[0].url} alt="">
                          </a>
                          <!-- likes -->
                          <ul class="nav nav-stack py-2 small">
                          ${data.title}
                          </ul>
                          <ul class="nav nav-stack py-2 small">
                              <li class="nav-item">
                                  <a class="nav-link" href="#!" onclick="like(${data.id})"><i class="bi bi-heart-fill text-danger pe-1"></i>${data.totalLike} </a>
                              </li>
                              <li class="nav-item">
                                  <a class="nav-link" href="#!"> <i class="bi bi-chat-left-text-fill pe-1"></i>${data.totalComment} </a>
                              </li>
                          </ul>
                          <!-- glightbox Albums left bar START -->
                          <div class="glightbox-desc custom-desc2">
                              <div class="d-flex align-items-center justify-content-between">
                                  <div class="d-flex align-items-center">
                                      <!-- Avatar -->
                                      <div class="avatar me-2">
                                          <img class="avatar-img rounded-circle" src="/assets/images/avatar/04.jpg" alt="">
                                      </div>
                                      <!-- Info -->
                                      <div>
                                          <div class="nav nav-divider">
                                              <h6 class="nav-item card-title mb-0">Lori Ferguson</h6>
                                              <span class="nav-item small"> 2hr</span>
                                          </div>
                                          <p class="mb-0 small">Web Developer at Webestica</p>
                                      </div>
                                  </div>
                                  <!-- Card feed action dropdown START -->
                                  <div class="dropdown">
                                      <a href="#" class="text-secondary btn btn-secondary-soft-hover py-1 px-2" id="cardFeedAction" data-bs-toggle="dropdown" aria-expanded="false">
                                          <i class="bi bi-three-dots"></i>
                                      </a>
                                      <!-- Card feed action dropdown menu -->
                                      <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="cardFeedAction">
                                          <li><a class="dropdown-item" href="#"> <i class="bi bi-bookmark fa-fw pe-2"></i>Save post</a></li>
                                          <li><a class="dropdown-item" href="#"> <i class="bi bi-person-x fa-fw pe-2"></i>Unfollow lori ferguson </a></li>
                                          <li><a class="dropdown-item" href="#"> <i class="bi bi-x-circle fa-fw pe-2"></i>Hide post</a></li>
                                          <li><a class="dropdown-item" href="#"> <i class="bi bi-slash-circle fa-fw pe-2"></i>Block</a></li>
                                          <li><hr class="dropdown-divider"></li>
                                          <li><a class="dropdown-item" href="#"> <i class="bi bi-flag fa-fw pe-2"></i>Report post</a></li>
                                      </ul>
                                  </div>
                                  <!-- Card feed action dropdown END -->
                              </div>
                              <p class="mt-3 mb-0">I'm so privileged to be involved in the @bootstrap hiring process! <a href="#">#internship #inclusivebusiness</a> <a href="#">#internship</a> <a href="#"> #hiring</a> <a href="#">#apply</a> </p>
                              <ul class="nav nav-stack py-3 small">
                                  <li class="nav-item">
                                      <a class="nav-link active" href="#!"> <i class="bi bi-hand-thumbs-up-fill pe-1"></i>Liked (56)</a>
                                  </li>
                                  <li class="nav-item">
                                      <a class="nav-link" href="#!"> <i class="bi bi-chat-fill pe-1"></i>Comments (12)</a>
                                  </li>
                                  <!-- Card share action START -->
                                  <li class="nav-item dropdown ms-auto">
                                      <a class="nav-link mb-0" href="#" id="cardShareAction" data-bs-toggle="dropdown" aria-expanded="false">
                                          <i class="bi bi-reply-fill fa-flip-horizontal pe-1"></i>Share (3)
                                      </a>
                                      <!-- Card share action dropdown menu -->
                                      <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="cardShareAction">
                                          <li><a class="dropdown-item" href="#"> <i class="bi bi-envelope fa-fw pe-2"></i>Send via Direct Message</a></li>
                                          <li><a class="dropdown-item" href="#"> <i class="bi bi-bookmark-check fa-fw pe-2"></i>Bookmark </a></li>
                                          <li><a class="dropdown-item" href="#"> <i class="bi bi-link fa-fw pe-2"></i>Copy link to post</a></li>
                                          <li><a class="dropdown-item" href="#"> <i class="bi bi-share fa-fw pe-2"></i>Share post via …</a></li>
                                          <li><hr class="dropdown-divider"></li>
                                          <li><a class="dropdown-item" href="#"> <i class="bi bi-pencil-square fa-fw pe-2"></i>Share to News Feed</a></li>
                                      </ul>
                                  </li>
                                  <!-- Card share action END -->
                              </ul>
                              <!-- Add comment -->
                              <div class="d-flex mb-3">
                                  <!-- Avatar -->
                                  <div class="avatar avatar-xs me-2">
                                      <img class="avatar-img rounded-circle" src="/assets/images/avatar/04.jpg" alt="">
                                  </div>
                                  <!-- Comment box  -->
                                  <form class="position-relative w-100">
                                      <textarea class="one form-control pe-4 bg-light" data-autoresize rows="1" placeholder="Add a comment..."></textarea>
                                      <!-- Emoji button -->
                                      <div class="position-absolute top-0 end-0">
                                          <button class="btn" type="button">🙂</button>
                                      </div>
                                  </form>
                              </div>
                              <!-- Comment wrap START -->
                              <ul class="comment-wrap list-unstyled ">
                                  <!-- Comment item START -->
                                  <li class="comment-item">
                                      <div class="d-flex">
                                          <!-- Avatar -->
                                          <div class="avatar avatar-xs">
                                              <img class="avatar-img rounded-circle" src="/assets/images/avatar/05.jpg" alt="">
                                          </div>
                                          <div class="ms-2">
                                              <!-- Comment by -->
                                              <div class="bg-light rounded-start-top-0 p-3 rounded">
                                                  <div class="d-flex justify-content-center">
                                                      <div class="me-2">
                                                          <h6 class="mb-1"> <a href="#!"> Frances Guerrero </a></h6>
                                                          <p class="small mb-0">Removed demands expense account in outward tedious do.</p>
                                                      </div>
                                                      <small>5hr</small>
                                                  </div>
                                              </div>
                                              <!-- Comment react -->
                                              <ul class="nav nav-divider py-2 small">
                                                  <li class="nav-item">
                                                      <a class="nav-link" href="#!"> Like (3)</a>
                                                  </li>
                                                  <li class="nav-item">
                                                      <a class="nav-link" href="#!"> Reply</a>
                                                  </li>
                                                  <li class="nav-item">
                                                      <a class="nav-link" href="#!"> View 5 replies</a>
                                                  </li>
                                              </ul>
                                          </div>
                                      </div>
                                  </li>
                              </ul>
                              <!-- Comment wrap END -->
                          </div>
                          <!-- glightbox Albums left bar END  -->`;
}

function loadmore(currentPage){

    $.ajax({
        type: "GET",
        url: "/api/feeds/"+currentPage,
        dataType: "json"
    }).done(function(resp){
        initMainPageSimpleModified(resp);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function like(data) {
    const url = window.location.href;

    $.ajax({
        type: "GET",
        url: "api/like/feed/"+data,
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
        url: "api/like/feed/"+data,
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
        url: "api/like/feed/"+data
    }).done(function(resp){
        window.location.href = url;
    }).fail(function(error){
        alert(JSON.stringify(error));
        window.location.href = url;
    });
}