function registerCheck(GroupPw) {
    if (GroupPw !== "" && GroupPw !== null) {
        $('#passwordModal').modal('show');
        $('#passwordSubmit').on('click', function() {
            const insertPw = $('#passwordInput').val();
            $('#passwordModal').modal('hide');
            if (insertPw == GroupPw) {
                register();
            } else {
                alert("비밀번호가 일치하지 않습니다.");
            }
        });
    } else {
        register();
    }
}



function register() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const groupId = urlParams.get('id');
    $.ajax({
        url: "/api/group/member/" + groupId,
        method: "POST",
        dataType: "json",
        success: function(response) {
            // API 응답을 성공적으로 받았을 때 실행되는 코드
            // 응답 데이터는 'response' 매개변수로 전달됨
            if (response.resultCode === 'SUCCESS') {
                // 삭제 성공 시 화면에서 그룹 삭제
                alert('가입 완료');
                location.reload();
            } else {
                alert('가입 실패');
            }
        },
        error: function(xhr, status, error) {
            console.log('API Error:', error);
            alert('그룹 가입에 실패하였습니다.');
        }
    });
}