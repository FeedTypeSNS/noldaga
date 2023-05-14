function init() {

    $.ajax({
        type: "GET",
        url: "/api/feed/getuser",
        async: false
    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        makeHeader(resp);
        makeLeftNav(resp);
        headerGetGroups();
    }).fail(function(error){
        alert(JSON.stringify(error));
    });

    $("#search-content").on("keydown",(e)=>{
        if (e.keyCode === 13) {
            let data= $("#search-content").val();
            window.location.href = "/nol/searchfeed?q="+data;
        }
    })
}

init();

function makeHeader(data){
    let headerBox = document.querySelector("#header");
    headerBox.innerHTML = headerContent(data);
}

function headerContent(data) {
    return `<a class="btn text-secondary py-0 me-sm-3 sidebar-start-toggle" onclick="sidebarToggleStart()">
        <i class="bi bi-justify-left fs-3 lh-1"></i>
      </a>
      <a class="navbar-brand" href="/nol">
        <img
                class="light-mode-item navbar-brand-item"
                src="/assets/images/noldagaMain.png"
                alt="logo"
        />
        <img
                class="dark-mode-item navbar-brand-item"
                src="/assets/images/noldagaMain.png"
                alt="logo"
        />
      </a>
      <!-- Logo END -->

      <!-- Responsive navbar toggler -->
      <button
              class="navbar-toggler ms-auto icon-md btn btn-light p-0"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#navbarCollapse"
              aria-controls="navbarCollapse"
              aria-expanded="false"
              aria-label="Toggle navigation"
      >
            <span class="navbar-toggler-animation">
              <span></span>
              <span></span>
              <span></span>
            </span>
      </button>

      <!-- Main navbar START -->
      <div class="collapse navbar-collapse" id="navbarCollapse">
        <!-- Nav Search START -->
        <div
                class="nav mt-3 mt-lg-0 flex-nowrap align-items-center px-4 px-lg-0"
        >
          <div class="nav-item w-100">
              <button
                      class="btn bg-transparent px-2 py-1 position-absolute"
                      type="button"
              >
                <i class="bi bi-search fs-5"> </i>
              </button>
              <input
                      class="form-control ps-5 bg-light"
                      type="search"
                      placeholder="검색할 내용을 입력해주세요"
                      aria-label="Search"
                      id="search-content"
              />
          </div>
        </div>
        <!-- Nav Search END -->

        <ul class="navbar-nav navbar-nav-scroll ms-auto">
          <!-- Nav item 1 Demos -->
          <!-- Nav item 4 Mega menu -->

        </ul>
      </div>
      <!-- Main navbar END -->

      <!-- Nav right START -->
      <ul class="nav flex-nowrap align-items-center ms-sm-3 list-unstyled">
        <li class="nav-item ms-2">
          <a
                  class="nav-link icon-md btn btn-light p-0"
                  href="/nol/chat"
          >
            <i class="bi bi-chat-left-text-fill fs-6"> </i>
          </a>
        </li>
        <li class="nav-item ms-2">
          <a
                  class="nav-link icon-md btn btn-light p-0"
                  href="/nol/settings"
          >
            <i class="bi bi-gear-fill fs-6"> </i>
          </a>
        </li>
        
        <!-- Notification dropdown END -->

        <li class="nav-item ms-2 dropdown">
          <a
                  class="nav-link btn icon-md p-0"
                  href="#"
                  id="profileDropdown"
                  role="button"
                  data-bs-auto-close="outside"
                  data-bs-display="static"
                  data-bs-toggle="dropdown"
                  aria-expanded="false"
          >
            <img
                    class="avatar-img rounded-2"
                    src=${data.profileImageUrl}
                    alt=""
            />
          </a>
          <ul
                  class="dropdown-menu dropdown-animation dropdown-menu-end pt-3 small me-md-n3"
                  aria-labelledby="profileDropdown"
          >
            <!-- Profile info -->
            <li class="px-3">
              <div class="d-flex align-items-center position-relative">
                <!-- Avatar -->
                <div class="avatar me-3">
                  <img
                          class="avatar-img rounded-circle"
                          src=${data.profileImageUrl}
                          alt="avatar"
                  />
                </div>
                <div>
                  <a class="h6 stretched-link" id="getUsernameFromHeader" href="#">${data.username}</a>
                  <p class="small m-0">${data.nickname}</p>
                </div>
              </div>
              <a
                      class="dropdown-item btn btn-primary-soft btn-sm my-2 text-center"
                      href="/nol/editProfile?user_id=${data.id}"
              >View profile</a
              >
            </li>
            <!-- Links -->
            <li>
              <a class="dropdown-item" href="/nol/settings"
              ><i class="bi bi-gear fa-fw me-2"></i>내 정보 & 알림 범위 수정</a
              >
            </li>
            <li class="dropdown-divider"></li>
            <li>
              <a
                      class="dropdown-item bg-danger-soft-hover"
                      href="/nol/logout"
              ><i class="bi bi-power fa-fw me-2"></i>로그아웃</a
              >
            </li>
            <li><hr class="dropdown-divider" /></li>
            <!-- Dark mode options START -->
            <li>
              <div
                      class="modeswitch-item theme-icon-active d-flex justify-content-center gap-3 align-items-center p-2 pb-0"
              >
                <span>Mode:</span>
                <button
                        type="button"
                        class="btn btn-modeswitch nav-link text-primary-hover mb-0"
                        data-bs-theme-value="light"
                        data-bs-toggle="tooltip"
                        data-bs-placement="top"
                        data-bs-title="Light"
                >
                  <svg
                          xmlns="http://www.w3.org/2000/svg"
                          width="16"
                          height="16"
                          fill="currentColor"
                          class="bi bi-sun fa-fw mode-switch"
                          viewBox="0 0 16 16"
                  >
                    <path
                            d="M8 11a3 3 0 1 1 0-6 3 3 0 0 1 0 6zm0 1a4 4 0 1 0 0-8 4 4 0 0 0 0 8zM8 0a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-1 0v-2A.5.5 0 0 1 8 0zm0 13a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-1 0v-2A.5.5 0 0 1 8 13zm8-5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1 0-1h2a.5.5 0 0 1 .5.5zM3 8a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1 0-1h2A.5.5 0 0 1 3 8zm10.657-5.657a.5.5 0 0 1 0 .707l-1.414 1.415a.5.5 0 1 1-.707-.708l1.414-1.414a.5.5 0 0 1 .707 0zm-9.193 9.193a.5.5 0 0 1 0 .707L3.05 13.657a.5.5 0 0 1-.707-.707l1.414-1.414a.5.5 0 0 1 .707 0zm9.193 2.121a.5.5 0 0 1-.707 0l-1.414-1.414a.5.5 0 0 1 .707-.707l1.414 1.414a.5.5 0 0 1 0 .707zM4.464 4.465a.5.5 0 0 1-.707 0L2.343 3.05a.5.5 0 1 1 .707-.707l1.414 1.414a.5.5 0 0 1 0 .708z"
                    />
                    <use href="#"></use>
                  </svg>
                </button>
                <button
                        type="button"
                        class="btn btn-modeswitch nav-link text-primary-hover mb-0"
                        data-bs-theme-value="dark"
                        data-bs-toggle="tooltip"
                        data-bs-placement="top"
                        data-bs-title="Dark"
                >
                  <svg
                          xmlns="http://www.w3.org/2000/svg"
                          width="16"
                          height="16"
                          fill="currentColor"
                          class="bi bi-moon-stars fa-fw mode-switch"
                          viewBox="0 0 16 16"
                  >
                    <path
                            d="M6 .278a.768.768 0 0 1 .08.858 7.208 7.208 0 0 0-.878 3.46c0 4.021 3.278 7.277 7.318 7.277.527 0 1.04-.055 1.533-.16a.787.787 0 0 1 .81.316.733.733 0 0 1-.031.893A8.349 8.349 0 0 1 8.344 16C3.734 16 0 12.286 0 7.71 0 4.266 2.114 1.312 5.124.06A.752.752 0 0 1 6 .278zM4.858 1.311A7.269 7.269 0 0 0 1.025 7.71c0 4.02 3.279 7.276 7.319 7.276a7.316 7.316 0 0 0 5.205-2.162c-.337.042-.68.063-1.029.063-4.61 0-8.343-3.714-8.343-8.29 0-1.167.242-2.278.681-3.286z"
                    />
                    <path
                            d="M10.794 3.148a.217.217 0 0 1 .412 0l.387 1.162c.173.518.579.924 1.097 1.097l1.162.387a.217.217 0 0 1 0 .412l-1.162.387a1.734 1.734 0 0 0-1.097 1.097l-.387 1.162a.217.217 0 0 1-.412 0l-.387-1.162A1.734 1.734 0 0 0 9.31 6.593l-1.162-.387a.217.217 0 0 1 0-.412l1.162-.387a1.734 1.734 0 0 0 1.097-1.097l.387-1.162zM13.863.099a.145.145 0 0 1 .274 0l.258.774c.115.346.386.617.732.732l.774.258a.145.145 0 0 1 0 .274l-.774.258a1.156 1.156 0 0 0-.732.732l-.258.774a.145.145 0 0 1-.274 0l-.258-.774a1.156 1.156 0 0 0-.732-.732l-.774-.258a.145.145 0 0 1 0-.274l.774-.258c.346-.115.617-.386.732-.732L13.863.1z"
                    />
                    <use href="#"></use>
                  </svg>
                </button>
                <button
                        type="button"
                        class="btn btn-modeswitch nav-link text-primary-hover mb-0 active"
                        data-bs-theme-value="auto"
                        data-bs-toggle="tooltip"
                        data-bs-placement="top"
                        data-bs-title="Auto"
                >
                  <svg
                          xmlns="http://www.w3.org/2000/svg"
                          width="16"
                          height="16"
                          fill="currentColor"
                          class="bi bi-circle-half fa-fw mode-switch"
                          viewBox="0 0 16 16"
                  >
                    <path
                            d="M8 15A7 7 0 1 0 8 1v14zm0 1A8 8 0 1 1 8 0a8 8 0 0 1 0 16z"
                    />
                    <use href="#"></use>
                  </svg>
                </button>
              </div>
            </li>
            <!-- Dark mode options END-->
          </ul>
        </li>
        <!-- Profile START -->
      </ul>`;
}


function makeLeftNav(data){
    let NavBox = document.querySelector("#leftNav");
    NavBox.innerHTML = leftNavContent(data);
}

function leftNavContent(data) {
    return `<div class="nav-sidenav p-4 bg-mode h-100 custom-scrollbar">
          <!-- Card START -->
          <!-- Side Nav START -->
          <ul class="nav nav-link-secondary flex-column fw-bold gap-2">
            <!--<li class="nav-item">
              <a class="nav-link" href="my-profile-origin.html">
                <i class="bi bi-search nav-icon"></i>
                <span class="nav-text">검색 </span></a
              >
            </li>-->
            <li class="nav-item">
              <a class="nav-link" href="my-profile-origin.html">
                <i class="bi bi-card-checklist nav-icon"></i>
                <span class="nav-text">탐색 </span></a
              >
            </li>
            <li class="nav-item">
              <a class="nav-link" href="/nol/groups">
                <i class="bi bi-people nav-icon"></i>
                <span class="nav-text">그룹 </span></a
              >
            </li>
            <li class="nav-item">
              <a class="nav-link" href="/nol/chat">
                <i class="bi bi-chat-left-text nav-icon"></i>
                <span class="nav-text">채팅 </span></a
              >
            </li>
            <li class="nav-item">
              <a class="nav-link" href="/nol/notifications">
                <i class="bi bi-bell-fill nav-icon"></i>
                <span class="nav-text">알림 </span></a
              >
            </li>
            <li class="nav-item">
              <a
                      class="nav-link"
                      href="#!"
                      data-bs-toggle="modal"
                      data-bs-target="#modalCreateFeed"
              >
                <i class="bi bi-pencil-square nav-icon"></i>
                <span class="nav-text">포스팅 </span></a
              >
            </li>
            <li class="nav-item">
              <a class="nav-link" href="/nol/mypage?user_id=${data.id}">
                <i class="bi bi nav-icon">
                  <!-- Avatar -->
                  <div class="avatar avatar-xxs me">
                    <img
                            class="avatar-img rounded-circle"
                            src=${data.profileImageUrl}
                            alt=""
                    />
                  </div>
                </i>
                <span class="nav-text">MyPage </span></a
              >
            </li>
            
          </ul>
          <!-- Side Nav END -->
          <!-- Card body END -->
        </div>`;
}

function headerGetGroups() {

    $.ajax({
        type: "GET",
        url: "/api/groups/member",
        dataType: "json"
    }).done(function(resp){
        setHeaderModalGroupSelectBox(resp.result);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}


function setHeaderModalGroupSelectBox(data){
    for(let i=0; i<data.length; i++){
        let groupSelectBox = document.querySelector("#group_id");

        let selectOption = document.createElement("option"); //<option></option>
        selectOption.value = `${data[i].id}`;
        selectOption.innerHTML = `${data[i].name}`;
        groupSelectBox.append(selectOption);
    }
}

function sidebarToggleStart() {
    var sidebar = e.select('.sidebar-start-toggle');
    if (e.isVariableDefined(sidebar)) {
        var sb = e.select('.sidebar-start-toggle');
        var mode = document.getElementsByTagName("BODY")[0];
        sb.addEventListener("click", function(){
            mode.classList.toggle("sidebar-start-enabled");
        });
    }
}
// END: Sidebar Toggle

// START: 09 Sidebar Toggle end
function sidebarToggleEnd() {
    var sidebar = e.select('.sidebar-end-toggle');
    if (e.isVariableDefined(sidebar)) {
        var sb = e.select('.sidebar-end-toggle');
        var mode = document.getElementsByTagName("BODY")[0];
        sb.addEventListener("click", function(){
            mode.classList.toggle("sidebar-end-enabled");
        });
    }
}