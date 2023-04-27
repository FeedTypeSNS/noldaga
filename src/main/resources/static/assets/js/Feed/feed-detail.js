function init() {

    const queryString = window.location.search;
    var comment_page = 0;
    let responseData;

    $.ajax({
        type: "GET",
        url: "/api/feed"+ queryString,
        dataType: "json"
    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        responseData = resp.result;
        initDetailPage(resp.result,comment_page);//여기서 넘길 때 로그인한 회원도 받아서 올 수 있다면
    }).fail(function(error){
        alert(JSON.stringify(error));
    });

    $("#comment-loadmore-button").on("click",()=>{
        this.loadmoreComments(responseData,++comment_page);
    });
}

init();

function loadmoreComments(data,comment_page){

    let page = Math.ceil(data.commentList.length/10);
    if(comment_page < page) { //아직 보여줄 댓글이 남음
        for (let i = comment_page*10; i < comment_page*10+10; i++) {
            let ReplyBox = document.querySelector("#FeedReplycontent");
            let cardBox2 = document.createElement("div");
            //if(commentList[i].userDto.id == resp.userDto.name)
            //cardBox2.innerHTML = getDetailPage_comment_others(data.commentList[i]);
            //else(~~~~면)
            cardBox2.innerHTML = getDetailPage_comment_mine(data.commentList[i]);
            ReplyBox.append(cardBox2);
        }
    }
    else{
        //load more버튼을 없애는게 목표
    }
}

function initDetailPage(data,comment_page){
    let feedBox = document.querySelector("#FeedDetailcontent");
    let cardBox = document.createElement("div");

    let replySubmitBox = document.querySelector("#replyFormBox");
    let replyBox = document.createElement("form");
    replyBox.className = "nav nav-item w-100 position-relative";

    cardBox.innerHTML = getDetailPage_Feed(data);
    feedBox.append(cardBox);

    replyBox.innerHTML = reply_submit_form(data);
    replySubmitBox.append(replyBox);

    let page = Math.ceil(data.commentList.length/5);
    if(comment_page < page) { //아직 보여줄 댓글이 남음
        for (let i = comment_page*10; i < comment_page*10+10; i++) {
            let ReplyBox = document.querySelector("#FeedReplycontent");
            let cardBox2 = document.createElement("div");
            //if(~~~~면) resp.user.name == resp.userDto.name 이런식으로 비교해서 내꺼 남의꺼 구분하기
            cardBox2.innerHTML = getDetailPage_comment_others(data.commentList[i]);
            //else(~~~~면)
            //cardBox2.innerHTML = getDetailPage_comment_mine(data.commentList[i]);
            ReplyBox.append(cardBox2);
        }
    }
    else{
        //load more버튼을 없애는게 목표
    }
}

function getDetailPage_Feed(data){
    return `<!-- Fees images --><!-- 함수에 넣을 부분 시작 -->
                            <img class="card-img rounded" src="assets/images/post/16by9/big/01.jpg" alt="">
                            <!-- Feed meta START -->
                            <div class="d-flex align-items-center justify-content-between my-3">
                                <div class="d-flex align-items-center">
                                    <!-- Avatar -->
                                    <div class="avatar avatar-story me-2">
                                        <a href="#!"> <img class="avatar-img rounded-circle" src="assets/images/avatar/04.jpg" alt=""> </a>
                                    </div>
                                    <!-- Info -->
                                    <div>
                                      <div class="nav nav-divider">
                                        <h6 class="nav-item card-title mb-0"> <a href="#!"> ${data.userResponse.username} </a></h6>
                                        <span class="nav-item small">  </span>
                                      </div>
                                      <p class="mb-0 medium">${data.modDate}</p>
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
                            <!-- Feed meta Info -->
                            <h1 class="h4">${data.title}</h1>
                            <p>${data.content} </p>
                            <!-- 함수에 넣을 부분 끝 -->`;
}

function reply_submit_form(data){
    return `<textarea
                            data-autoresize
                            class="form-control pe-5 bg-light"
                            rows="1"
                            placeholder="댓글을 입력하세요"
                            id="replyContent"
                    ></textarea>
                            <input type ="hidden" id="feedId" value="${data.id}"/>
                            <button
                                    class="nav-link bg-transparent px-3 position-absolute top-50 end-0 translate-middle-y border-0"
                                    type="button" id="reply-button" onclick="reply()"
                            >
                                <i class="bi bi-send-fill"> </i>
                            </button>`;
}

function getDetailPage_comment_others(data){
    return `<ul class="comment-wrap list-unstyled">
                            <!-- Comment item START -->
                            <li class="comment-item">
                                <div class="d-flex">
                                    <!-- Avatar -->
                                    <div class="avatar avatar-xs">
                                        <a href="#!"><img class="avatar-img rounded-circle" src="assets/images/avatar/05.jpg" alt=""></a>
                                    </div>
                                    <!-- Comment by -->
                                    <div class="ms-2">
                                        <div class="bg-light p-3 rounded">
                                            <div class="d-flex justify-content-between">
                                                <h6 class="mb-1"> <a href="#!"> ${data.userDto.username} </a> </h6>
                                                <small class="ms-2">${data.modDate}</small>
                                            </div>
                                            <p class="small mb-0">${data.content}</p>
                                        </div>
                                        <!-- Comment react -->
                                        <ul class="nav nav-divider pt-2 small">
                                            <li class="nav-item">
                                                <a
                                                  class="nav-link"
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
                                        </ul>
                                    </div>
                                </div>
                            </li>
                            <!-- Comment item END -->
                        </ul>`;
}

function getDetailPage_comment_mine(data){
    return `<ul class="comment-wrap list-unstyled">
                            <!-- Comment item START -->
                            <li class="comment-item">
                                <div class="d-flex">
                                    <!-- Avatar -->
                                    <div class="avatar avatar-xs">
                                        <a href="#!"><img class="avatar-img rounded-circle" src="assets/images/avatar/05.jpg" alt=""></a>
                                    </div>
                                    <!-- Comment by -->
                                    <div class="ms-2">
                                        <div class="bg-light p-3 rounded">
                                            <div class="d-flex justify-content-between">
                                                <h6 class="mb-1"> <a href="#!"> ${data.userDto.username} </a> </h6>
                                                <small class="ms-2">${data.modDate}</small>
                                            </div>
                                            <p class="small mb-0">${data.content}</p>
                                        </div>
                                        <!-- Comment react -->
                                        <ul class="nav nav-divider pt-2 small">
                                            <li class="nav-item">
                                                <a class="nav-link" href="#!"> Like (1)</a>
                                            </li>
                                            <li class="nav-item">
                                                <a
                                                        href="#"
                                                        class="nav-link py-1 px-2 mb-0"
                                                        data-bs-toggle="modal"
                                                        data-bs-target="#CommentModifyModal"
                                                        onclick="showComment(${data.id})"
                                                >
                                                <i class="bi bi-brush pe-2"></i>수정
                                                </a>
                                            </li>
                                            <li class="nav-item">
                                                <a
                                                        href="#"
                                                        class="nav-link py-1 px-2 mb-0"
                                                        data-bs-toggle="modal"
                                                        data-bs-target="#CommentDeleteModal"
                                                        onclick="setDeleteComment(${data.id})"
                                                >
                                                    <i class="bi bi-trash3 pe-2"></i
                                                    >삭제
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </li>
                            <!-- Comment item END -->
                        </ul>`;
}

function reply() {
    const queryString = window.location.search;
    let content={
        content: $("#replyContent").val(),
        feedId: $("#feedId").val()
    };
    $.ajax({
        type: "POST",
        url: "/api/comment",
        data: JSON.stringify(content),
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function(resp){
        alert('댓글 등록 완료');
        window.location.href = "/feed"+queryString;
    }).fail(function(error){
        alert('댓글 등록 실패');
        alert(JSON.stringify(error));
    });
}

function like() {
    alert('댓글 좋아요 누르면 파란색으로 바꿔주세용');
}

function showComment(id) {

    $.ajax({
        type: "GET",
        url: "/api/comment/"+ id,
        dataType: "json"
    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        setCommentModifyModal(resp);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });

    function setCommentModifyModal(data){
        $('#commentContent').val(data.content);
        $('#commentId').val(data.id);
    }
}

function setDeleteComment(id){
    $('#deleteId').val(id);
}