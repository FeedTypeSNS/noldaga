function makeSearchMenu(query){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const searchQuery = urlParams.get('q');

    $('#search-content-second').val(searchQuery);

    let cardFooterDiv = document.querySelector("#cardFooter");
    cardFooterDiv.innerHTML = searchMenuContent(queryString);
    getSearchUsers(searchQuery);

    $("#search-content-second").on("keydown",(e)=>{
        if (e.keyCode === 13) {
            let data= $("#search-content-second").val();
            window.location.href = "/nol/searchfeed?q="+data;
        }
    })
}

makeSearchMenu();

function searchMenuContent(queryString){
    return `<ul class="nav nav-bottom-line align-items-center justify-content-center mb-0 border-0">
              <li class="nav-item"> <a class="nav-link" href="/nol/searchfeed${queryString}"> 게시글 </a> </li>
              <li class="nav-item"> <a class="nav-link" href="/nol/searchhash${queryString}"> 해시태그 </a> </li>
              <li class="nav-item"> <a class="nav-link active" href="/nol/searchpeople${queryString}"> 사람 </a> </li>
              <li class="nav-item"> <a class="nav-link" href="/nol/searchgroup${queryString}"> 그룹 </a> </li>
            </ul>`;
}

function getSearchUsers(searchQuery){
    $.ajax({
        type: "GET",
        url: "/api/search/user/"+searchQuery,
        dataType: "json"
    }).done(function(resp){
        setSearchUsers(resp.result);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function setSearchUsers(users){
    for(let i=0; i<users.length; i++){
        let feedBox = document.querySelector("#search-user-content");

        //카드 형식의 피드
        let feedCard = document.createElement("div");
        feedCard.className = "d-md-flex align-items-center mb-4";
        feedCard.innerHTML = setFeedCardContent(users[i]);
        feedBox.append(feedCard);
    }
    let feedBox = document.querySelector("#search-user-content");
    let loadMoreBox = document.querySelector("#load-more-button");
    loadMoreBox.innerHTML = setLoadMoreButton();
    feedBox.append(loadMoreBox);
}

function setFeedCardContent(user) {
    var profileMessage = (user.profileMessage==="" || user.profileMessage==null)? "" : user.profileMessage;

    return `<div class="avatar me-3 mb-3 mb-md-0">
                <a href="/nol/mypage?user_id=${user.id}"> <img class="avatar-img rounded-circle" src="assets/images/avatar/01.jpg" alt=""> </a>
              </div>
              <!-- Info -->
              <div class="w-100">
                <div class="d-sm-flex align-items-start">
                  <h6 class="mb-0">아이디 : ${user.username}</h6>
                  <p class="small ms-sm-2 mb-0">닉네임 : ${user.nickname}</p>
                </div>
                <!-- Connections START -->
                <div class="d-sm-flex align-items-start">
                  <h6 class="small ms-sm-2 mb-0">${profileMessage}</h6 >
                </div>
                <div class="d-sm-flex align-items-start">
                  <h6 class="small mb-0">팔로워수 : ${user.totalFollower} 팔로잉수 : ${user.totalFollowing}</h6 >
                </div>
                <!-- Connections END -->
              </div>
              <!-- Button -->
              <div class="ms-md-auto d-flex">
                <button class="btn btn-danger-soft btn-sm mb-0 me-2" onclick="follow(${user.id})"> follow </button>
                <button class="btn btn-primary-soft btn-sm mb-0"> chatting </button>
              </div>`;
}

function setLoadMoreButton(){
    return `<a href="#!" role="button" class="btn btn-sm btn-loader btn-primary-soft" data-bs-toggle="button" aria-pressed="true">
                <span class="load-text"> 더보기 </span>
                <div class="load-icon">
                  <div class="spinner-grow spinner-grow-sm" role="status">
                    <span class="visually-hidden">Loading...</span>
                  </div>
                </div>
              </a>`;
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