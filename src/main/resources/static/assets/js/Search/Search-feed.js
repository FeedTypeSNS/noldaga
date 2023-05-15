var page = 0;

function makeSearchMenu(query){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const searchQuery = urlParams.get('q');

    $('#search-content-second').val(searchQuery);

    let cardFooterDiv = document.querySelector("#cardFooter");
    cardFooterDiv.innerHTML = searchMenuContent(queryString);
    getSearchFeeds(searchQuery);

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
              <li class="nav-item"> <a class="nav-link active" href="/nol/searchfeed${queryString}"> 게시글 </a> </li>
              <li class="nav-item"> <a class="nav-link" href="/nol/searchhash${queryString}"> 해시태그 </a> </li>
              <li class="nav-item"> <a class="nav-link" href="/nol/searchpeople${queryString}"> 사람 </a> </li>
              <li class="nav-item"> <a class="nav-link" href="/nol/searchgroup${queryString}"> 그룹 </a> </li>
            </ul>`;
}

function getSearchFeeds(searchQuery){

    $.ajax({
        type: "GET",
        url: "/api/search/feed/"+searchQuery+"/"+page,
        dataType: "json"
    }).done(function(resp){
        setSearchFeeds(resp.result,page);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}


function setSearchFeeds(feeds,page){
    for(let i=0; i<feeds.length; i++){
        let feedBox = document.querySelector("#search-feed-content");

        //카드 형식의 피드
        let feedCard = document.createElement("div");
        feedCard.className = "card bg-transparent border-0";
        feedCard.innerHTML = setFeedCardContent(feeds[i]);

        let breakLine = document.createElement("hr");
        breakLine.className = "my-4";

        feedBox.append(feedCard);
        feedBox.append(breakLine);
    }
    let feedBox = document.querySelector("#search-feed-content");
    let loadMoreBox = document.querySelector("#load-more-button");
    loadMoreBox.innerHTML = setLoadMoreButton();
    feedBox.append(loadMoreBox);
}

function setFeedCardContent(feed) {
    return `<div class="row g-3">
                <div class="col-4">
                  <!-- Blog image -->
                  <img class="rounded" src="/assets/images/post/4by3/03.jpg" alt="">
                </div>
                <div class="col-8">
                  <!-- Blog caption -->
                  <h5><a href="/nol/feed?id=${feed.id}" class="btn-link stretched-link text-reset fw-bold">${feed.title}</a></h5>
                  <div class="d-none d-sm-inline-block">
                    <p class="mb-2">${feed.content}</p>
                    <p class="mb-2">${feed.userResponse.username}</p>
                    <!-- BLog date -->
                    <a class="small text-secondary" href="#!"> <i class="bi bi-calendar-date pe-1"></i> ${feed.modDate}</a>
                  </div>
                </div>
              </div>`;
}

function setLoadMoreButton(){
    return `<a href="#!" role="button" class="btn btn-sm btn-loader btn-primary-soft" data-bs-toggle="button" aria-pressed="true" onclick="loadmoreSearchFeed(++page)">
                <span class="load-text"> 더보기 </span>
                <div class="load-icon">
                  <div class="spinner-grow spinner-grow-sm" role="status">
                    <span class="visually-hidden">Loading...</span>
                  </div>
                </div>
              </a>`;
}

function loadmoreSearchFeed(page){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const searchQuery = urlParams.get('q');

    $.ajax({
        type: "GET",
        url: "/api/search/feed/"+searchQuery+"/"+page,
        dataType: "json"
    }).done(function(resp){
        setSearchFeeds(resp.result,page);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}