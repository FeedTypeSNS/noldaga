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
        init(user, resp.result);
    }).fail(function(error){
        init(user, null);
    });
}




function init(user, groupMember) {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const groupId = urlParams.get('id');
    $.ajax({
        type: "GET",
        url: "/api/group/"+ groupId,
        dataType: "json"
    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        initDetailPage(resp.result, user, groupMember);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}


function initDetailPage(group, user, groupMember) {
    let feedBox = document.querySelector("#group-info");
    let cardBox = document.createElement("div");
    console.log(group);
    cardBox.innerHTML = getDetailPage_Feed(group, user, groupMember);
    feedBox.append(cardBox);
}

function getDetailPage_Feed(group, user, groupMember) {
    console.log(groupMember);

    const groupType = group.open === 0 ? "<i class=\"bi bi-lock pe-1\"></i> 비밀 그룹" : "<i class='bi bi-globe pe-1'></i> 공개 그룹";
    const userType = user.id === group.userDto.id ? "<a href=\"#\" data-bs-toggle=\"modal\" data-bs-target=\"#modalCreateGroup\"><i class=\"bi bi-gear-fill fs-6\"> </i></a>" : "";
    let hiddenType = "";
    let hiddenType2 = "hidden";
    if(groupMember != null) {
        hiddenType = groupMember.userDto.id === user.id ? "hidden" : "";
        hiddenType2 = groupMember.userDto.id === user.id ? "" : "hidden";
    }

    return `<div class="d-md-flex flex-wrap align-items-start text-center text-md-start">
    <div class="mb-2">
        <!-- Avatar -->
        <div class="avatar avatar-xl">
            <img class="avatar-img border-0" src="assets/images/logo/13.svg" alt="">
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
        <button onclick="registerCheck(${group.pw})" class="btn btn-sm btn-primary-soft me-2" type="button" ${hiddenType}><i
            class="bi bi-person-plus-fill pe-1"></i> Join
        </button>
        <button onclick="registerCheck(${group.pw})" class="btn btn-sm btn-primary-soft me-2" type="button" ${hiddenType2}><i
            class="bi bi-person-check-fill pe-1"></i> Joined
        </button>
        <button class="btn btn-sm btn-success me-2" type="button"><i class="fa-solid fa-plus pe-1"></i> Invite
        </button>
        
        ${userType}        
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
        <form>
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
                <!-- Avatar edit -->
                <div class="avatar-edit">
                  <input name="profile_url" type='file' id="avatarUpload" accept=".png, .jpg, .jpeg" />
                  <label for="avatarUpload"></label>
                </div>
                <!-- Avatar preview -->
                <div class="avatar avatar-xl position-relative">
                  <img id="avatar-preview" class="avatar-img rounded-circle border border-white border-3 shadow" src="assets/images/avatar/placeholder.jpg" alt="">
                </div>
              </div>
              <!-- Avatar remove button -->
              <div class="avatar-remove">
                <button type="button" id="avatar-reset-img" class="btn btn-light">사진 삭제</button>
              </div>
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
        </form>
        <!-- Form END -->
      </div>
      <!-- Modal footer -->

    </div>
  </div>
</div>
<!-- Modal modify group END -->`;
}
