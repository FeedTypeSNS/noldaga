function getMaster() {

    $.ajax({
        type: "GET",
        url: "/api/group/getuser",
        async: false
    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        init_group(resp);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}

getMaster();

function init_group(master) {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const groupId = urlParams.get('id');
    $.ajax({
        type: "GET",
        url: "/api/group/"+ groupId,
        dataType: "json"
    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
        initDetailGroupMasterPage(resp.result);
        getDetailGroupMemberPage(resp.result, master);
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}



function initDetailGroupMasterPage(group) {
    let feedBox = document.querySelector("#group-master-info");
    let cardBox = document.createElement("div");
    console.log(group);
    cardBox.innerHTML = getDetailGroupMasterPage(group);
    feedBox.append(cardBox);
}

function getDetailGroupMasterPage(group) {

    return `<div class="d-md-flex align-items-center">
                <!-- Avatar -->
                <div class="avatar me-3 mb-3 mb-md-0">
                  <a href="/nol/mypage?user_id=${group.userDto.id}"> <img class="avatar-img rounded-circle" src="${group.userDto.profileImageUrl}" alt=""> </a>
                </div>
                <!-- Info -->
                <div class="w-80">
                  <div class="d-sm-flex align-items-start">
                    <h6 class="mb-0"><a href="/nol/mypage?user_id=${group.userDto.id}">${group.userDto.name} </a></h6>
                    <p class="small ms-sm-2 mb-0">그룹장</p>
                  </div>

                </div>
              </div>`;
}


function getDetailGroupMemberPage(group, master) {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const groupId = urlParams.get('id');
    $.ajax({
        url: "/api/members/group/"+groupId, // API 엔드포인트 URL
        method: "GET", // HTTP 요청 메서드
        dataType: "json", // 응답 데이터 타입
        success: function(response) { // 성공 콜백 함수
            // API 응답을 성공적으로 받았을 때 실행되는 코드
            // 응답 데이터는 'response' 매개변수로 전달됨
            // 그룹 목록을 렌더링할 HTML 문자열 생성
            if(response.result == 0) {
                let GroupNoMember = document.querySelector("#noMember");
                let GroupMember = document.querySelector("#group-member-info");
                GroupMember.style.display = "none";
                GroupNoMember.style.display = "";
            }
            var groupsHtml = '';
            $.each(response.result, function(index, user) {
                groupsHtml += '<div class="d-md-flex align-items-center mb-4">';
                groupsHtml += '  <div class="avatar me-3 mb-3 mb-md-0">';
                groupsHtml += '    <a href="/nol/mypage?user_id='+user.id+'"> <img class="avatar-img rounded-circle" src="'+user.profileImageUrl+'" alt=""> </a>';
                groupsHtml += '    </div>';
                groupsHtml += '      <div class="w-80">';
                groupsHtml += '        <div class="d-sm-flex align-items-start">';
                groupsHtml += '          <h6 class="mb-0"><a href="/nol/mypage?user_id='+user.id+'">'+user.username+'</a></h6>';
                groupsHtml += '          <p class="small ms-sm-2 mb-0">그룹원</p>';
                groupsHtml += '        </div>';
                groupsHtml += '    </div>';
                groupsHtml += '      <div class="ms-md-auto d-flex">';
                if (group.userDto.id == master.id) {
                    groupsHtml += '        <button class="btn btn-danger-soft btn-sm" onclick="expelGroup('+group.id+','+user.id+')">추방하기</button>';
                } else {

                }
                groupsHtml += '          </div>';
                groupsHtml += '        </div>';

            });

            // 그룹 목록을 렌더링할 DOM 요소 선택 후 HTML 문자열 삽입
            $('#group-member-info').html(groupsHtml);
        },
        error: function(xhr, status, error) { // 오류 콜백 함수
            // API 요청이 실패하거나 응답이 오류인 경우 실행되는 코드
            console.log('API Error:', error);
        }
    });
}



