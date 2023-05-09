var fpage = 0;

function makeSearchMenu(query){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const searchQuery = urlParams.get('q');

    $('#search-content-second').val(searchQuery);

    let cardFooterDiv = document.querySelector("#cardFooter");
    cardFooterDiv.innerHTML = searchMenuContent(queryString);
    getSearchHashTags(searchQuery);
}

makeSearchMenu();

function searchMenuContent(query){
    return `<ul class="nav nav-bottom-line align-items-center justify-content-center mb-0 border-0">
              <li class="nav-item"> <a class="nav-link" href="/searchfeed${query}"> 게시글 </a> </li>
              <li class="nav-item"> <a class="nav-link active" href="/searchhash${query}"> 해시태그 </a> </li>
              <li class="nav-item"> <a class="nav-link" href="/searchpeople${query}"> 사람 </a> </li>
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
                  <h5><a href="/hashtag?tag_id=${hashTag.id}&tag_name=${hashTag.tagName.substr(1)}" class="btn-link stretched-link text-reset fw-bold">${hashTag.tagName.substr(1)}</a></h5>
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
