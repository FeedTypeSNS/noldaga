function getUser() {

    $.ajax({
        type: "GET",
        url: "/api/group/getuser",
        async: false
    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        getGroupMember(resp);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}
getUser();

function getGroupMember(user) {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const groupId = urlParams.get('id');
    $.ajax({
        type: "GET",
        url: "/api/group/member/"+ groupId,
        dataType: "json"
    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        init2(user, resp.result);
    }).fail(function(error){
        init2(user, null);
    });
}




function init2(user, groupMember) {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const groupId = urlParams.get('id');
    $.ajax({
        type: "GET",
        url: "/api/group/"+ groupId,
        dataType: "json"
    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        initDetailGroupPage(resp.result, user, groupMember);
        initDetailGroupPostPage(resp.result, user, groupMember);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}


function initDetailGroupPage(group, user, groupMember) {
    let feedBox = document.querySelector("#group-info");
    let cardBox = document.createElement("div");
    console.log(group);
    cardBox.innerHTML = getDetailGroupPage(group, user, groupMember);
    feedBox.append(cardBox);
}

function getDetailGroupPage(group, user, groupMember) {
    console.log(groupMember);

    const groupType = group.open === 0 ? "<i class=\"bi bi-lock pe-1\"></i> 비밀 그룹" : "<i class='bi bi-globe pe-1'></i> 공개 그룹";
    const userType = user.id === group.userDto.id ? "<a href=\"#\" data-bs-toggle=\"modal\" data-bs-target=\"#modalCreateGroup\"><i class=\"bi bi-gear-fill fs-6\"> </i></a>" : "";
    let hiddenType = "";
    let hiddenType2 = "hidden";
    const hiddenType3 = user.id === group.userDto.id ? "hidden" : "";
    let hiddenType4 = "";
    let hiddenType5 = "hidden";

    if(groupMember != null) {
        hiddenType = groupMember.userDto.id === user.id ? "hidden" : "";
        hiddenType2 = groupMember.userDto.id === user.id ? "" : "hidden";
        hiddenType4 = groupMember.favor === 1 ? "" : "hidden";
        hiddenType5 = groupMember.favor === 1 ? "hidden" : "";
    }

    let profile = "";
    if(group.profile_url != "") {

    } else {
        profile = "/assets/images/avatar/placeholder.jpg";
    }

    return `<div class="d-md-flex flex-wrap align-items-start text-center text-md-start">
    <div class="mb-2">
        <!-- Avatar -->
        <div class="avatar avatar-xl">
            <img class="avatar-img rounded-circle border border-white border-3 bg-white" src="${group.profile_url}${profile}" alt="">
        </div>
    </div>
    <div class="ms-md-4 mt-3">
        <h1 class="h5 mb-0">${group.name}</h1>
        <ul class="nav nav-divider justify-content-center justify-content-md-start">
            <li class="nav-item">${groupType}</li>
        </ul>
        <small class="mb-0">${group.intro}</small>
    </div>
    <!-- Button -->
    <div class="d-flex justify-content-center justify-content-md-start align-items-center mt-3 ms-lg-auto">
        <button onclick="favorGroup(${group.id})" class="btn btn-sm btn-primary-soft me-2" type="button" ${hiddenType2} ${hiddenType5}>
        <i class="bi bi-bookmark-star"></i>
        </button>
        <button onclick="unfavorGroup(${group.id})" class="btn btn-sm btn-primary-soft me-2" type="button" ${hiddenType2} ${hiddenType4}>
        <i class="bi bi-bookmark-star-fill"></i>
        </button>
        <button onclick="registerCheck(${group.pw})" class="btn btn-sm btn-primary-soft me-2" type="button" ${hiddenType} ${hiddenType3}><i
            class="bi bi-person-plus-fill pe-1"></i> 가입
        </button>
        <button onclick="unregisterGroup(${group.id})" class="btn btn-sm btn-primary-soft me-2" type="button" ${hiddenType2} ${hiddenType3}><i
            class="bi bi-person-check-fill pe-1"></i> 가입됨
        </button>
        ${userType} 
        <!--<button class="btn btn-sm btn-success me-2" type="button"><i class="fa-solid fa-plus pe-1"></i> Invite
        </button>-->
        
    </div>
</div>

<!-- Modal modify group START -->
<div class="modal fade" id="modalCreateGroup" tabindex="-1" aria-labelledby="modalLabelCreateGroup" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <!-- Title -->
      <div class="modal-header">
        <h5 class="modal-title" id="modalLabelCreateGroup">그룹 수정하기</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <!-- Form START -->
        
          <!-- Group name -->
          <div class="mb-3">
            <label class="form-label">그룹 이름</label>
            <input id="name" name="name" type="text" class="form-control" placeholder="그룹 이름을 입력해주세요." value="${group.name}">
          </div>
          <!-- Group picture -->
          <div class="mb-3">
            <label class="form-label">그룹 사진</label>
            <!-- Avatar upload START -->
            <div class="d-flex align-items-center">
              <div class="avatar-uploader me-3">
                <div class="mb-3">
                  <img id="groupImg" alt="" src="${profile}${group.profile_url}" class="rounded-circle" style="width: 100px; height: 100px;">
                </div>
                <div>
                  <input type="file" accept=".png, .jpg, .jpeg" name="profile_url" id="fileInput">
                </div>
              </div>
              <!-- Avatar remove button -->
              <!--<div class="avatar-remove">
                <button type="button" id="avatar-reset-img" class="btn btn-light">사진 삭제</button>
              </div>-->
            </div>
            <!-- Avatar upload END -->
          </div>
          <!-- Select audience -->
          <div class="mb-3">
            <label class="form-label d-block">공개 여부</label>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" name="open" id="public" value="1" checked="checked" onchange="togglePassword()">
              <label class="form-check-label" for="public">공개</label>
            </div>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" name="open" id="private" value="0" onchange="togglePassword()">
              <label class="form-check-label" for="private">비공개</label>
            </div>
          </div>
          <!-- Invite friend -->
          <div id="passwordDiv" class="mb-3" style="display: none;">
            <label class="form-label">그룹 비밀번호 </label>
            <input id="pw" name = "pw" type="password" class="form-control" placeholder="그룹 비밀번호를 입력해주세요.">
          </div>
          <!-- Group description -->
          <div class="mb-3">
            <label class="form-label">그룹 소개 </label>
            <textarea id="intro" name = "intro" class="form-control" rows="3" placeholder="그룹 소개를 입력해주세요.">${group.intro}</textarea>
          </div>
          <input type="hidden" id="id" name="id" value="${group.id}"/>

          <div class="modal-footer">
            <button onclick="updateGroup(${group.id})" class="btn btn-success-soft">그룹 수정</button>
          </div>
        
        <!-- Form END -->
      </div>
      <!-- Modal footer -->

    </div>
  </div>
</div>
<script>
  imageViewer();
  function imageViewer() {
    const fileInput = document.getElementById('fileInput');
    const imgPreview = document.getElementById('groupImg');

    fileInput.addEventListener('change', (event) => {
      const file = event.target.files[0];
      const reader = new FileReader();

      reader.onload = (e) => {
        imgPreview.src = e.target.result;
      };

      reader.readAsDataURL(file);
    });
  }
</script>
<!-- Modal modify group END -->`;
}

/* ----------------------------------------------------------------- */
function initDetailGroupPostPage(group, user, groupMember) {
    let GroupPostBox = document.querySelector("#groupPostBox");
    let GroupFeedBox = document.querySelector("#feed");
    let GroupNoMemberFeedBox = document.querySelector("#noMemberFeed");

    if(user.id === group.userDto.id) {
        GroupPostBox.style.display = "";
    } else if(groupMember != null ){
        if(groupMember.userDto.id === user.id) {
            GroupPostBox.style.display = "";
        }
    } else {
        GroupPostBox.style.display = "none";
        if(group.open == 0) {
            GroupFeedBox.style.display = "none";
            GroupNoMemberFeedBox.style.display = "";
        }

    }
}

function favorGroup(groupId) {
    $.ajax({
        url: "/api/group/member/favor/" + groupId,
        method: "GET",
        dataType: "json",
        success: function(response) {
            // API 응답을 성공적으로 받았을 때 실행되는 코드
            // 응답 데이터는 'response' 매개변수로 전달됨
            if (response.resultCode === 'SUCCESS') {
                // 삭제 성공 시 화면에서 그룹 삭제
                location.reload();
            } else {
                alert('즐겨찾기 실패');
            }
        },
        error: function(xhr, status, error) {
            console.log('API Error:', error);
            alert('그룹 즐거찾기에 실패하였습니다.');
        }
    });
}

function unfavorGroup(groupId) {
    $.ajax({
        url: "/api/group/member/unfavor/" + groupId,
        method: "GET",
        dataType: "json",
        success: function(response) {
            // API 응답을 성공적으로 받았을 때 실행되는 코드
            // 응답 데이터는 'response' 매개변수로 전달됨
            if (response.resultCode === 'SUCCESS') {
                // 삭제 성공 시 화면에서 그룹 삭제
                location.reload();
            } else {
                alert('즐겨찾기삭제 실패');
            }
        },
        error: function(xhr, status, error) {
            console.log('API Error:', error);
            alert('그룹 즐거찾기삭제에 실패하였습니다.');
        }
    });
}

