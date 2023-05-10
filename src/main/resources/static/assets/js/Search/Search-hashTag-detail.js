function makeSearchMenu(){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const hashTagId = urlParams.get('tag_id');
    const hashTagName = urlParams.get('tag_name');

    setHashTagIndex(hashTagName);
    getHashTagFeeds(hashTagId);
}

makeSearchMenu();

function setHashTagIndex(hashTagName){
    let hashTagIndexBox = document.querySelector("#hashTag-index");
    hashTagIndexBox.innerHTML = `<h2 class="badge bg-danger bg-opacity-10 text-danger mb-2 fw-bold">#</h2>
                <h5>${hashTagName}</h5>`;
}


function getHashTagFeeds(hashTagId){
    var page = 0;

    $.ajax({
        type: "GET",
        url: "/api/feeds/hashTag/"+hashTagId+"/"+page,
        dataType: "json"
    }).done(function(resp){
        setHashTagFeeds(resp.result);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });

    $("#loadmore-button").on("click",()=>{
        loadmore(++page);
    });
}

function setHashTagFeeds(feeds){
    for(let i=0; i<feeds.length; i++){
        let feedBox = document.querySelector("#hashTag-feed-content");

        //카드 형식의 피드
        let feedCard = document.createElement("div");
        feedCard.className = "card bg-transparent border-0";
        feedCard.innerHTML = setHashTagFeedsContent(feeds[i]);

        let breakLine = document.createElement("hr");
        breakLine.className = "my-4";

        feedBox.append(feedCard);
        feedBox.append(breakLine);
    }
    let feedBox = document.querySelector("#hashTag-feed-content");
    let loadMoreBox = document.querySelector("#load-more-button");
    loadMoreBox.innerHTML = setLoadMoreButton();
    feedBox.append(loadMoreBox);
}

function setHashTagFeedsContent(feed) {
    return `<div class="row g-3">
                <div class="col-4">
                  <!-- Blog image -->
                  <img class="rounded" src="assets/images/post/4by3/03.jpg" alt="">
                </div>
                <div class="col-8">
                  <!-- Blog caption -->
                  <h5><a href="/feed?id=${feed.id}" class="btn-link stretched-link text-reset fw-bold">${feed.title}</a></h5>
                  <div class="d-none d-sm-inline-block">
                    <p class="mb-2">${feed.content}</p>
                    <!-- BLog date -->
                    <a class="small text-secondary" href="#!"> <i class="bi bi-calendar-date pe-1"></i> ${feed.modDate}</a>
                  </div>
                </div>
              </div>`;
}

function setLoadMoreButton(){
    return `<a href="#!" role="button" class="btn btn-sm btn-loader btn-primary-soft" data-bs-toggle="button" aria-pressed="true">
                <span class="load-text"> Load more connections </span>
                <div class="load-icon">
                  <div class="spinner-grow spinner-grow-sm" role="status">
                    <span class="visually-hidden">Loading...</span>
                  </div>
                </div>
              </a>`;
}