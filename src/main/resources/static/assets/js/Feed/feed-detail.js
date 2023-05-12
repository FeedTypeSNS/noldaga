let imageLength = null;

function getUser() {

    $.ajax({
        type: "GET",
        url: "/api/feed/getuser",
        async: false
    }).done(function(loginUser){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        getFeedData(loginUser);
        getGroups();
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}
getUser();


function getGroups() {

    $.ajax({
        type: "GET",
        url: "/api/groups/member",
        dataType: "json"
    }).done(function(resp){
        setModalGroupSelectBox(resp.result);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}


function setModalGroupSelectBox(data){
    for(let i=0; i<data.length; i++){
        let modifyGroupSelectBox = document.querySelector("#modify_group_id");

        let selectOption = document.createElement("option"); //<option></option>
        selectOption.value = `${data[i].id}`;
        selectOption.innerHTML = `${data[i].name}`;
        modifyGroupSelectBox.append(selectOption);
    }
}

function getFeedData(loginUser) {
    const queryString = window.location.search;
    var comment_page = 0;
    let responseData;

    $.ajax({
        type: "GET",
        url: "/api/feed"+ queryString,
        dataType: "json"
    }).done(function(feedData){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        responseData = feedData.result;
        setDetailPage(feedData.result,comment_page,loginUser);//여기서 넘길 때 로그인한 회원도 받아서 올 수 있다면
    }).fail(function(error){
        alert(JSON.stringify(error));
    });

    $("#comment-loadmore-button").on("click",()=>{
        loadmoreComments(responseData,++comment_page,loginUser);
    });
}


function loadmoreComments(feedData,comment_page,loginUser){
    let page = Math.ceil(feedData.totalComment/10);

    if(comment_page < page) { //아직 보여줄 댓글이 남음

        for (let i = comment_page*10; i < comment_page*10+10; i++) {
            let replyBox = document.querySelector("#FeedReplycontent");

            let replyCard = document.createElement("div");
            if(feedData.commentList[i].userResponse.username == loginUser.username)
                replyCard.innerHTML = getDetailPage_comment_mine(feedData.commentList[i]); //내가 쓴 댓글에는 수정/삭제버튼 보임
            else
                replyCard.innerHTML = getDetailPage_comment_others(feedData.commentList[i]); //남이 쓴 댓글은 안보임
            replyBox.append(replyCard);
        }
    }
    else{
        //load more버튼을 없애는게 목표
    }
}

function setDetailPage(feedData,comment_page,loginUser){
    //피드 사진
    let imageBox = document.querySelector("#FeedImages");

    //피드 내용 들어가는 부분
    let feedBox = document.querySelector("#FeedDetailcontent");
    let feedCard = document.createElement("div");

    //좋아요 버튼, 댓글, 수정, 삭제 버튼 라인
    let feedReactBox = document.querySelector("#FeedReact");
    let feedReactCard = document.createElement("div");

    //댓글 입력창
    let replySubmitBox = document.querySelector("#replyFormBox");
    let replySubmitForm = document.createElement("form");
    replySubmitForm.className = "nav nav-item w-100 position-relative";

    imageBox.innerHTML = getFeedImages(feedData.imageDtoList);

    feedCard.innerHTML = getDetailPage_Feed(feedData);
    feedBox.append(feedCard);

    if(feedData.userResponse.id == loginUser.id) {
        if(feedData.delDate == null)
            feedReactCard.innerHTML = getReactButtonsMine(feedData); //내가 쓴 글은 수정 삭제 보임
        else
            feedReactCard.innerHTML = getReactButtonsOthers(feedData);
    }
    else
        feedReactCard.innerHTML = getReactButtonsOthers(feedData); //남이 쓴 글은 수정 삭제 버튼 없음

    feedReactBox.append(feedReactCard);

    replySubmitForm.innerHTML = reply_submit_form(feedData);
    replySubmitBox.append(replySubmitForm);

    let page = Math.ceil(feedData.totalComment/10);

    if(comment_page < page) { //아직 보여줄 댓글이 남음

        for (let i = comment_page*10; i < comment_page*10+10; i++) {
            let replyBox = document.querySelector("#FeedReplycontent");

            let replyCard = document.createElement("div");
            if(feedData.commentList[i].userResponse.username == loginUser.username) {
                replyCard.innerHTML = getDetailPage_comment_mine(feedData.commentList[i]); //내가 쓴 댓글에는 수정/삭제버튼 보임
            }
            else
                replyCard.innerHTML = getDetailPage_comment_others(feedData.commentList[i]); //남이 쓴 댓글은 안보임
            replyBox.append(replyCard);
        }
    }
    else{
        //load more버튼을 없애는게 목표
    }
}

function getReactButtonsMine(data){
    return `<ul class="nav nav-stack flex-wrap small mb-3">
                            <li class="nav-item">
                                <a class="nav-link" href="#!" onclick="feedLike(${data.id})"> <i class="bi bi-hand-thumbs-up-fill pe-1"></i>(${data.totalLike})</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="#!"> <i class="bi bi-chat-fill pe-1"></i>(${data.totalComment})</a>
                            </li>
                            <li class="nav-item">
                                <a
                                        href="#"
                                        class="nav-link bg-light py-1 px-2 mb-0"
                                        data-bs-toggle="modal"
                                        data-bs-target="#ModifyModal"
                                        id="modify"
                                        onclick="show()"
                                >
                                    <i class="bi bi-brush pe-2"></i
                                    >수정
                                </a>
                            </li>
                            <li class="nav-item">
                                <a
                                        href="#"
                                        class="nav-link bg-light py-1 px-2 mb-0"
                                        data-bs-toggle="modal"
                                        data-bs-target="#DeleteAlertModal"
                                >
                                    <i class="bi bi-trash3 pe-2"></i
                                    >삭제
                                </a>
                            </li>

                            <!-- Card share action END -->
                        </ul>`;
}

function getReactButtonsOthers(data){
    return `<ul class="nav nav-stack flex-wrap small mb-3">
                            <li class="nav-item">
                                <a class="nav-link" href="#!" onclick="feedLike(${data.id})"> <i class="bi bi-hand-thumbs-up-fill pe-1"></i>(${data.totalLike})</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="#!"> <i class="bi bi-chat-fill pe-1"></i>(${data.totalComment})</a>
                            </li>
                            <!-- Card share action START -->
                            <li class="nav-item dropdown ms-sm-auto">
                                <a class="nav-link mb-0" href="#" id="cardShareAction" data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="bi bi-reply-fill flip-horizontal ps-1"></i>(3)
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
                        </ul>`;
}

function getFeedImages(images){
    let result = "";
    imageLength = images.length-1;
    for(let i=0; i<images.length; i++) {
        result += `<img class="feedimage" src=${images[i].url} alt="">`;
    }
    return result;
}

function getDetailPage_Feed(data){
    return `<div class="d-flex align-items-center justify-content-between my-3">
                                <div class="d-flex align-items-center">
                                    <!-- Avatar -->
                                    <div class="avatar avatar-story me-2">
                                        <a href="/nol/mypage?user_id=${data.userResponse.id}"> <img class="avatar-img rounded-circle" src=${data.userResponse.profileImageUrl} alt=""> </a>
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
                                        <a href="#!"><img class="avatar-img rounded-circle" src="/assets/images/avatar/05.jpg" alt=""></a>
                                    </div>
                                    <!-- Comment by -->
                                    <div class="ms-2">
                                        <div class="bg-light p-3 rounded">
                                            <div class="d-flex justify-content-between">
                                                <h6 class="mb-1"> <a href="#!"> ${data.userResponse.username} </a> </h6>
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
                                                  onclick="commentLike(${data.id})"
                                                >
                                                  <i class="bi bi-hand-thumbs-up-fill pe-1"></i>Liked
                                                  (${data.totalLike})</a
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
                                        <a href="#!"><img class="avatar-img rounded-circle" src="/assets/images/avatar/05.jpg" alt=""></a>
                                    </div>
                                    <!-- Comment by -->
                                    <div class="ms-2">
                                        <div class="bg-light p-3 rounded">
                                            <div class="d-flex justify-content-between">
                                                <h6 class="mb-1"> <a href="#!"> ${data.userResponse.username} </a> </h6>
                                                <small class="ms-2">${data.modDate}</small>
                                            </div>
                                            <p class="small mb-0">${data.content}</p>
                                        </div>
                                        <!-- Comment react -->
                                        <ul class="nav nav-divider pt-2 small">
                                            <li class="nav-item">
                                                <a class="nav-link" href="#!" onclick="commentLike(${data.id})"> Like (${data.totalLike})</a>
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

function feedLike(data) {
    $.ajax({
        type: "GET",
        url: "api/like/feed/"+data,
        dataType: "json"
    }).done(function(resp){
        if(resp) feed_like_delete(data);
        else feed_like_register(data);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function feed_like_register(data) {
    const queryString = window.location.search;

    $.ajax({
        type: "POST",
        url: "api/like/feed/"+data,
        data: JSON.stringify(content),
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function(resp){
        window.location.href = "/feed"+ queryString;
    }).fail(function(error){
        alert(JSON.stringify(error));
        window.location.href = "/feed"+ queryString;
    });
}

function feed_like_delete(data) {
    const queryString = window.location.search;

    $.ajax({
        type: "DELETE",
        url: "api/like/feed/"+data
    }).done(function(resp){
        window.location.href = "/feed"+ queryString;
    }).fail(function(error){
        alert(JSON.stringify(error));
        window.location.href = "/feed"+ queryString;
    });
}


function commentLike(data) {

    $.ajax({
        type: "GET",
        url: "api/like/comment/"+data,
        dataType: "json"
    }).done(function(resp){
        if(resp) comment_like_delete(data);
        else comment_like_register(data);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

function comment_like_register(data) {
    const queryString = window.location.search;
    $.ajax({
        type: "POST",
        url: "api/like/comment/"+data,
        data: JSON.stringify(content),
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function(resp){
        window.location.href = "/feed"+ queryString;
    }).fail(function(error){
        alert(JSON.stringify(error));
        window.location.href = "/feed"+ queryString;
    });
}

function comment_like_delete(data) {
    const queryString = window.location.search;
    $.ajax({
        type: "DELETE",
        url: "api/like/comment/"+data
    }).done(function(resp){
        window.location.href = "/feed"+ queryString;
    }).fail(function(error){
        alert(JSON.stringify(error));
        window.location.href = "/feed"+ queryString;
    });
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
        $('#commentContent').val(data.result.content);
        $('#commentId').val(data.result.id);
    }
}

function setDeleteComment(id){
    $('#deleteId').val(id);
}

function show() {

    const queryString = window.location.search;

    $.ajax({
        type: "GET",
        url: "/api/feed"+ queryString,
        dataType: "json"
    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        setModifyModal(resp);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });

    function setModifyModal(data){
        $('#modify_title').val(data.result.title);
        $('#modify_content').val(data.result.content);
        $('#modify_id').val(data.result.id);
        $("#modify_open_range").val(data.result.range).prop("selected", true);
        $("#modify_group_id").val(data.result.groupId).prop("selected", true);
    }
}


////????
//function modify(){
//    const queryString = window.location.search;
//
//    let data={
//        title: $("#title").val(),
//        content: $("#content").val(),
//        range: $("#open_range").val(),
//        groupId: $("#group_id").val()
//    };
//
//    alert(JSON.stringify(data));
//
//    $.ajax({
//        type: "PUT",
//        url: "/api/feed"+queryString,
//        data: JSON.stringify(data),
//        contentType: "application/json; charset=utf-8",
//        dataType: "json"
//    }).done(function(resp){
//        alert('수정 완료');
//        location.href = "/";
//    }).fail(function(error){
//        alert('수정 실패');
//        alert(JSON.stringify(error));
//    });
//
//}