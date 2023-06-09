var fpage = 0;

function makeSearchMenu(query){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const searchQuery = urlParams.get('q');

    $('#search-content-second').val(searchQuery);

    let cardFooterDiv = document.querySelector("#cardFooter");
    cardFooterDiv.innerHTML = searchMenuContent(queryString);
    getSearchHashTags(searchQuery);

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
              <li class="nav-item"> <a class="nav-link active" href="/nol/searchhash${queryString}"> 해시태그 </a> </li>
              <li class="nav-item"> <a class="nav-link" href="/nol/searchpeople${queryString}"> 사람 </a> </li>
              <li class="nav-item"> <a class="nav-link" href="/nol/searchgroup${queryString}"> 그룹 </a> </li>
            </ul>`;
}

function getSearchHashTags(searchQuery){
    $.ajax({
        type: "GET",
        url: "/api/search/hashTag/"+searchQuery+"/"+fpage,
        dataType: "json"
    }).done(function(resp){
        setSearchHashTags(resp.result);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function setSearchHashTags(hashTags){
    for(let i=0; i<hashTags.length; i++){
        let feedBox = document.querySelector("#search-hashTag-content");

        //카드 형식의 피드
        let feedCard = document.createElement("div");
        feedCard.className = "card bg-transparent border-0";
        feedCard.innerHTML = setHashTagCardContent(hashTags[i]);

        let breakLine = document.createElement("hr");
        breakLine.className = "my-4";

        feedBox.append(feedCard);
        feedBox.append(breakLine);
    }
    let feedBox = document.querySelector("#search-hashTag-content");
    let loadMoreBox = document.querySelector("#load-more-button");
    loadMoreBox.innerHTML = setLoadMoreButton();
    feedBox.append(loadMoreBox);
}

function setHashTagCardContent(hashTag) {
    return `<div class="row g-3">
                <div class="col-8">
                  <!-- Blog caption -->
                  <h2 class="badge bg-danger bg-opacity-10 text-danger mb-2 fw-bold">#</h2>
                  <h5><a href="/nol/hashtag?tag_id=${hashTag.id}&tag_name=${hashTag.tagName.substr(1)}" class="btn-link stretched-link text-reset fw-bold">${hashTag.tagName.substr(1)}</a></h5>
                </div>
              </div>`;
}

function setLoadMoreButton(){
    return `<a href="#!" role="button" class="btn btn-sm btn-loader btn-primary-soft" data-bs-toggle="button" aria-pressed="true" onclick="loadmoreSearchHashTag(++fpage)">
                <span class="load-text"> 더보기 </span>
                <div class="load-icon">
                  <div class="spinner-grow spinner-grow-sm" role="status">
                    <span class="visually-hidden">Loading...</span>
                  </div>
                </div>
              </a>`;
}

function loadmoreSearchHashTag(changedPage){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const searchQuery = urlParams.get('q');

    $.ajax({
        type: "GET",
        url: "/api/search/hashTag/"+searchQuery+"/"+changedPage,
        dataType: "json"
    }).done(function(resp){
        setSearchHashTags(resp.result);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}
