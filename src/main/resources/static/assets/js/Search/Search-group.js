function makeSearchMenu(){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const searchQuery = urlParams.get('q');

    $('#search-content-second').val(searchQuery);

    let cardFooterDiv = document.querySelector("#cardFooter");
    cardFooterDiv.innerHTML = searchMenuContent(queryString);
    getSearchGroups(searchQuery);

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
              <li class="nav-item"> <a class="nav-link" href="/nol/searchpeople${queryString}"> 사람 </a> </li>
              <li class="nav-item"> <a class="nav-link active" href="/nol/searchgroup${queryString}"> 그룹 </a> </li>
            </ul>`;
}

function getSearchGroups(searchQuery){

    $.ajax({
        type: "GET",
        url: "/api/search/group/"+searchQuery,
        dataType: "json"
    }).done(function(resp){
        setSearchGroups(resp.result);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function setSearchGroups(groups){
    for(let i=0; i<groups.length; i++){
        let groupBox = document.querySelector("#search-group-content");

        //카드 형식의 피드
        let groupCard = document.createElement("div");
        groupCard.className = "col-sm-6 col-lg-4";
        groupCard.innerHTML = setGroupCardContent(groups[i]);
        groupBox.append(groupCard);
    }
    let feedBox = document.querySelector("#search-group-content");
    let loadMoreBox = document.querySelector("#load-more-button");
    loadMoreBox.innerHTML = setLoadMoreButton();
    feedBox.append(loadMoreBox);
}

function setGroupCardContent(group) {
    var groupOpen = (group.open === 0)? "비밀그룹" : "공개그룹";

    return `<div class="card">
        <div class="h-40px rounded-top"></div>
        <div class="card-body text-center pt-0">
          <div class="avatar avatar-lg mt-n5 mb-3">
            <img class="avatar-img rounded-circle border border-white border-3 bg-white" src="${group.profile_url}" alt="">
          </div>
          <h5 class="mb-0"><a href="group?id=${group.id}">${group.name}</a></h5>
          <small><i class="bi bi-lock pe-1"></i>${groupOpen}</small>
          <div class="hstack gap-2 gap-xl-3 justify-content-center mt-3">
            <div>
              <small class="mb-0">${group.intro}</small>
            </div>
          </div>
        </div>
        <div class="card-footer text-center">
          <button class="btn btn-sm btn-loader btn-primary-soft" onclick="registerCheck(${group.pw})">그룹 가입</button>
        </div>
      </div>
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