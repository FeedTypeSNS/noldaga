$.ajax({
    url: "/api/groups/member", // API 엔드포인트 URL
    method: "GET", // HTTP 요청 메서드
    dataType: "json", // 응답 데이터 타입
    success: function(response) { // 성공 콜백 함수
        // API 응답을 성공적으로 받았을 때 실행되는 코드
        // 응답 데이터는 'response' 매개변수로 전달됨

        // 그룹 목록을 렌더링할 HTML 문자열 생성
        var groupsHtml2 = '';
        $.each(response.result, function(index, group) {
            groupsHtml2 += '<div class="col-sm-6 col-lg-4">';
            groupsHtml2 += '  <div class="card">';
            groupsHtml2 += '    <div class="h-40px rounded-top"></div>';
            groupsHtml2 += '    <div class="card-body text-center pt-0">';
            groupsHtml2 += '      <div class="avatar avatar-lg mt-n5 mb-3">';
            groupsHtml2 += '        <img class="avatar-img rounded-circle border border-white border-3 bg-white" src="' + group.profile_url + '" alt="">';
            groupsHtml2 += '      </div>';
            groupsHtml2 += '      <h5 class="mb-0"><a href="group?id='+group.id+'">' + group.name + '</a></h5>';
            if (group.open === 0) {
                groupsHtml2 += '      <small><i class="bi bi-lock pe-1"></i>비밀그룹</small>';
            } else {
                groupsHtml2 += '      <small><i class="bi bi-globe pe-1"></i>공개그룹</small>';
            }
            groupsHtml2 += '      <div class="hstack gap-2 gap-xl-3 justify-content-center mt-3">';
            groupsHtml2 += '        <div>';
            groupsHtml2 += '          <small class="mb-0">' + group.intro + '</small>';
            groupsHtml2 += '        </div>';
            groupsHtml2 += '      </div>';
            groupsHtml2 += '    </div>';
            groupsHtml2 += '    <div class="card-footer text-center">';
            groupsHtml2 += '      <button class="btn btn-danger-soft btn-sm" onclick="unregisterGroup('+group.id+')">그룹 탈퇴</button>';
            groupsHtml2 += '    </div>';
            groupsHtml2 += '  </div>';
            groupsHtml2 += '</div>';
        });

        // 그룹 목록을 렌더링할 DOM 요소 선택 후 HTML 문자열 삽입
        $('#group-list2').html(groupsHtml2);
    },
    error: function(xhr, status, error) { // 오류 콜백 함수
        // API 요청이 실패하거나 응답이 오류인 경우 실행되는 코드
        console.log('API Error:', error);
    }
});

