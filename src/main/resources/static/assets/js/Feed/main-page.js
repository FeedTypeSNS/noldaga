function init() {

    var page = 0;

    $.ajax({
        type: "GET",
        url: "/api/feeds/"+page,
        dataType: "json"
    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        initMainPageOriginal(resp);
        //initLoadMoreButton();
    }).fail(function(error){
        alert(JSON.stringify(error));
    });

    $("#loadmore-button").on("click",()=>{
        alert("버튼확인");
        this.loadmore(++page);
    });
}

init();


function loadmore(currentPage){

    $.ajax({
        type: "GET",
        url: "/api/feeds/"+currentPage,
        dataType: "json"
    }).done(function(resp){
        initMainPageOriginal(resp);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function initMainPageSimpleModified(data) {
    for(let i=0; i<data.length; i++){
        let feedBox = document.querySelector("#feed");
        let cardBox = document.createElement("div"); //<div></div>
        cardBox.className = "card"; //<div class="card"></div>

        cardBox.innerHTML = getFeedBoxContent(data[i]);
        feedBox.append(cardBox); //그걸 feedBox에 붙임
    }
}

function initMainPageOriginal(data) {
    for(let i=0; i<data.length; i++){
        let feedBox = document.querySelector("#feed");
        let cardBox = document.createElement("div"); //<div></div>
        cardBox.className = "card"; //<div class="card"></div>

        cardBox.innerHTML = getFeedBoxContent(data[i]); //내부에 html 넣어주기 <div class="outgoing_msg"></div>안에 넣는다.
        feedBox.append(cardBox); //그걸 feedBox에 붙임
    }
}

//안씀
function initMainPage(data) {
    for(let i=0; i<data.length; i++){
        let feedBox = document.querySelector("#feed");
        //피드 카드하나 만들기
        let feedOneDiv = document.createElement("div");
        feedOneDiv.className = "card";
        //헤더div만들기
        let feedHeaderDiv = document.createElement("div"); //<div></div>
        feedHeaderDiv.className = "card-header border-0 pb-0";
        //헤더내용 넣기
        feedHeaderDiv.innerHTML = getHeaderContent(data[i]);
        //바디div만들기
        let feedBodyDiv = document.createElement("div"); //<div></div>
        feedBodyDiv.className = "card-body";
        //바디내용 넣기
        feedBodyDiv.innerHTML = getBodyContentContent(data[i]);
        feedBodyDiv.innerHTML = getBodyContentImage(data[i]);

        feedOneDiv.append(feedHeaderDiv); //그걸 feedBox에 붙임
        feedOneDiv.append(feedBodyDiv); //그걸 feedBox에 붙임
        feedBox.append(feedOneDiv); //그걸 feedBox에 붙임
    }
}

//안씀
function initLoadMoreButton() {
    let feedBox = document.querySelector("#feedBox");
    let cardBox = document.createElement("a");
    cardBox.href = "#";
    cardBox.role = "button";
    cardBox.className = "btn btn-loader btn-primary-soft";
    cardBox.ariaPressed = "true";
    cardBox.innerHTML = getLoadMoreButton();

    feedBox.append(cardBox);
}

//안씀
function getLoadMoreButton(){
    return `<span className="load-text"> Load more </span>
        <div className="load-icon">
            <div className="spinner-grow spinner-grow-sm" role="status">
                <span className="visually-hidden">Loading...</span>
            </div>
        </div>`;

}
//안씀
function getBodyContentImage(data) {
    return `<img
                  class="card-img"
                  src="assets/images/post/3by2/01.jpg"
                  alt="Post"
           />`;
}
//안씀
function getBodyContentContent(data) {
    return `<p>
              ${data.content}
            </p>`;
}

//안씀
function getHeaderContent(data) {
    return `<div class="d-flex align-items-center justify-content-between">
                  <div class="d-flex align-items-center">
                    <!-- Avatar -->
                    <div class="avatar avatar-story me-2">
                      <a href="#!">
                        <img
                          class="avatar-img rounded-circle"
                          src="assets/images/avatar/04.jpg"
                          alt=""
                        />
                      </a>
                    </div>
                    <!-- Info -->
                    <div>
                      <div class="nav nav-divider">
                        <h6 class="nav-item card-title mb-0">
                          <a href="#!"> ${data.userName}</a>
                        </h6>
                        <span class="nav-item small"> ${data.modDate}</span>
                      </div>
                      <h6 class="nav-item card-title mb-0">
                          <a href="/feed?id=${data.id}"> ${data.title}</a>
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
                </div>`;
}


function getFeedBoxContent(data) {
    return `<div class="card-header border-0 pb-0">
                <div class="d-flex align-items-center justify-content-between">
                  <div class="d-flex align-items-center">
                    <!-- Avatar -->
                    <div class="avatar avatar-story me-2">
                      <a href="#!">
                        <img
                          class="avatar-img rounded-circle"
                          src="assets/images/avatar/04.jpg"
                          alt=""
                        />
                      </a>
                    </div>
                    <!-- Info -->
                    <div>
                      <div class="nav nav-divider">
                        <h6 class="nav-item card-title mb-0">
                          <a href="#">${data.userDto.name} </a>
                        </h6>
                        <span class="nav-item small">${data.modDate} </span>
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
                  src="assets/images/post/3by2/01.jpg"
                  alt="Post"
                />
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
                      onclick="like()"
                    >
                      <i class="bi bi-hand-thumbs-up-fill pe-1"></i>Liked
                      (56)</a
                    >
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="#!">
                      <i class="bi bi-chat-fill pe-1"></i>Comments (12)</a
                    >
                  </li>
                  <!-- Card share action START -->
                  <li class="nav-item dropdown ms-sm-auto">
                    <a
                      class="nav-link mb-0"
                      href="#"
                      id="cardShareAction"
                      data-bs-toggle="dropdown"
                      aria-expanded="false"
                    >
                      <i class="bi bi-reply-fill flip-horizontal ps-1"></i>Share
                      (3)
                    </a>
                    <!-- Card share action dropdown menu -->
                    <ul
                      class="dropdown-menu dropdown-menu-end"
                      aria-labelledby="cardShareAction"
                    >
                      <li>
                        <a class="dropdown-item" href="#">
                          <i class="bi bi-envelope fa-fw pe-2"></i>Send via
                          Direct Message</a
                        >
                      </li>
                      <li>
                        <a class="dropdown-item" href="#">
                          <i class="bi bi-bookmark-check fa-fw pe-2"></i
                          >Bookmark
                        </a>
                      </li>
                      <li>
                        <a class="dropdown-item" href="#">
                          <i class="bi bi-link fa-fw pe-2"></i>Copy link to
                          post</a
                        >
                      </li>
                      <li>
                        <a class="dropdown-item" href="#">
                          <i class="bi bi-share fa-fw pe-2"></i>Share post via
                          …</a
                        >
                      </li>
                      <li><hr class="dropdown-divider" /></li>
                      <li>
                        <a class="dropdown-item" href="#">
                          <i class="bi bi-pencil-square fa-fw pe-2"></i>Share to
                          News Feed</a
                        >
                      </li>
                    </ul>
                  </li>
                  <!-- Card share action END -->
                </ul>
                <!-- Feed react END -->

                <!-- Add comment -->
                <div class="d-flex mb-3">
                  <!-- Avatar -->
                  <div class="avatar avatar-xs me-2">
                    <a href="#!">
                      <img
                        class="avatar-img rounded-circle"
                        src="assets/images/avatar/12.jpg"
                        alt=""
                      />
                    </a>
                  </div>
                  <!-- Comment box  -->
                  <form class="nav nav-item w-100 position-relative">
                    <textarea
                      data-autoresize
                      class="form-control pe-5 bg-light"
                      rows="1"
                      placeholder="Add a comment..."
                      id="replyContent"
                    ></textarea>
                    <input type ="hidden" id="feedId" />
                    <button
                      class="nav-link bg-transparent px-3 position-absolute top-50 end-0 translate-middle-y border-0"
                      type="submit" id="reply-button" onclick="reply()"
                    >
                      <i class="bi bi-send-fill"> </i>
                    </button>
                  </form>
                </div>
                <!-- Comment wrap START -->
                <ul class="comment-wrap list-unstyled">
                  <!-- Comment item START -->
                  <li class="comment-item">
                    <div class="d-flex">
                      <!-- Avatar -->
                      <div class="avatar avatar-xs">
                        <a href="#!"
                          ><img
                            class="avatar-img rounded-circle"
                            src="assets/images/avatar/05.jpg"
                            alt=""
                        /></a>
                      </div>
                      <!-- Comment by -->
                      <div class="ms-2">
                        <div class="bg-light p-3 rounded">
                          <div class="d-flex justify-content-between">
                            <h6 class="mb-1">
                              <a href="#!"> Frances Guerrero </a>
                            </h6>
                            <small class="ms-2">4min</small>
                          </div>
                          <p class="small mb-0">
                            Removed demands expense account in outward tedious
                            do. Particular way thoroughly unaffected projection.
                          </p>
                        </div>
                        <!-- Comment react -->
                        <ul class="nav nav-divider pt-2 small">
                          <li class="nav-item">
                            <a class="nav-link" href="#!"> Like (1)</a>
                          </li>
                          <li class="nav-item">
                            <a class="nav-link" href="#!"> Reply</a>
                          </li>
                          <li class="nav-item">
                            <a class="nav-link" href="#!"> View 6 replies</a>
                          </li>
                        </ul>
                      </div>
                    </div>
                  </li>
                  <!-- Comment item END -->
                </ul>
                <!-- Comment wrap END -->
              </div>
              <!-- Card body END -->
              <!-- Card footer START -->
              <div class="card-footer border-0 pt-0">
                <!-- Load more comments -->
                <a
                  href="#!"
                  role="button"
                  class="btn btn-link btn-link-loader btn-sm text-secondary d-flex align-items-center"
                  data-bs-toggle="button"
                  aria-pressed="true"
                >
                  <div class="spinner-dots me-2">
                    <span class="spinner-dot"></span>
                    <span class="spinner-dot"></span>
                    <span class="spinner-dot"></span>
                  </div>
                  Load more comments
                </a>
               </div>`;
}

function modi() {
    $("#deletetest").on("click", () => {
        alert("수정하기 버튼 클릭");
    });
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

function like() {
    alert('피드 좋아요');
}