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
        getFollower(myPageOwner.result.username);
        getFollowing(myPageOwner.result.username);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}


function getFollower(myPageOwnerUsername){
    var page = 0;

    $.ajax({
        type: "GET",
        url: "/api/follower/"+myPageOwnerUsername, //나를 팔로우한 사람들 목록
        dataType: "json"
    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        insertPhotoCardFollowers(resp.result);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function getFollowing(myPageOwnerUsername){
    var page = 0;

    $.ajax({
        type: "GET",
        url: "/api/following/"+myPageOwnerUsername, //내가 팔로우한 사람들 목록
        dataType: "json"
    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        insertPhotoCardFollowings(resp.result);
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

    return `<div class="h-200px rounded-top" style="background-image:url(assets/images/bg/05.jpg); background-position: center; background-size: cover; background-repeat: no-repeat;"></div>
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
                  <li class="nav-item"> <a class="nav-link" href="/mypage?user_id=${data.id}"> 게시물 </a> </li>
                  <li class="nav-item"> <a class="nav-link" href="/save" > 저장됨</a> </li>
                  <li class="nav-item"> <a class="nav-link" href="/like" > 좋아요한</a> </li>
                  <li class="nav-item"> <a class="nav-link" href="#"> 태그됨</a> </li>
                  <li class="nav-item"> <a class="nav-link active" href="/friend?user_id=${data.id}"> 친구목록 <span class="badge bg-success bg-opacity-10 text-success small">${data.totalFollower+data.totalFollowing}</span> </a> </li>
              </ul>
            </div>`;
}

function profileContentOther(data) {
    var profileMessage = (data.profileMessage==="" || data.profileMessage==null)? "" : data.profileMessage;

    return `<div class="h-200px rounded-top" style="background-image:url(assets/images/bg/05.jpg); background-position: center; background-size: cover; background-repeat: no-repeat;"></div>
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
                  <li class="nav-item"> <a class="nav-link" href="/mypage?user_id=${data.id}"> 게시물 </a> </li>
                  <li class="nav-item"> <a class="nav-link" href="#"> 태그됨</a> </li>
                  <li class="nav-item"> <a class="nav-link active" href="/friend?user_id=${data.id}"> 친구목록 <span class="badge bg-success bg-opacity-10 text-success small">${data.totalFollower+data.totalFollowing}</span> </a> </li>
              </ul>
            </div>`;
}


function insertPhotoCardFollowers(data){ //마이페이지 소유자를 팔로우하는 사람들 목록
    for(let i=0; i<data.length; i++) {
        let photoCard = document.querySelector("#photoCardFollower");
        let photoDiv = document.createElement("div");
        photoDiv.className = "card-body";
        photoDiv.innerHTML = photoCardContent(data[i]);
        photoCard.append(photoDiv);
    }
}

function insertPhotoCardFollowings(data){ //마이페이지 소유자가 팔로우하는 사람들 목록
    for(let i=0; i<data.length; i++) {
        let photoCard = document.querySelector("#photoCardFollowing");
        let photoDiv = document.createElement("div");
        photoDiv.className = "card-body";
        photoDiv.innerHTML = photoCardContent(data[i]);
        photoCard.append(photoDiv);
    }
}

function photoCardContent(data) {
    var profileMessage = (data.profileMessage==="" || data.profileMessage==null)? "" : data.profileMessage;
    return `<div class="d-md-flex align-items-center mb-4">
                <!-- Avatar -->
                <div class="avatar me-3 mb-3 mb-md-0">
                  <a href="#!"> <img class="avatar-img rounded-circle" src="assets/images/avatar/01.jpg" alt=""> </a>
                </div>
                <!-- Info -->
                <div class="w-100">
                  <div class="d-sm-flex align-items-start">
                    <h6 class="mb-0"><a href="#!">아이디 : ${data.username} </a></h6>
                    <p class="small ms-sm-2 mb-0">닉네임 : ${data.nickname}</p>
                </div>
                <!-- Connections START -->
                <div class="d-sm-flex align-items-start">
                  <h6 class="small ms-sm-2 mb-0">${profileMessage}</h6 >
                </div>
                <div class="d-sm-flex align-items-start">
                  <h6 class="small mb-0">팔로워수 :  팔로잉수 : </h6 >
                </div>
                <!-- Connections END -->
              </div>
              <!-- Button -->
              <div class="ms-md-auto d-flex">
                <button class="btn btn-danger-soft btn-sm mb-0 me-2" onclick="follow(${data.id})"> Follow </button>
                <button class="btn btn-primary-soft btn-sm mb-0"> Chatting </button>
              </div>`;
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


function follow(userId){
    const url = window.location.href;

    $.ajax({
        type: "POST",
        url: "/api/follow/"+userId,
        dataType: "json"
    }).done(function(resp){
        window.location.href = url;
    }).fail(function(error){
        alert(JSON.stringify(error));
        window.location.href = url;
    });
}