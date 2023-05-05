function createGroup() {
    var formData = new FormData();
    formData.append('name', $('#name').val());
    formData.append('profile_url', $('#avatarUpload')[0].files[0]);
    formData.append('open', $('input[name="open"]:checked').val());
    formData.append('pw', $('#pw').val());
    formData.append('intro', $('#intro').val());
    console.log(document.getElementById('avatarUpload').files[0]);
    // FormData 객체를 JSON으로 변환
    var groupData = {};
    for (var [key, value] of formData.entries()) {
        groupData[key] = value;
    }

    // JSON 형식으로 데이터를 전송
    $.ajax({
        type: 'POST',
        url: '/api/group',
        data: JSON.stringify(groupData),
        contentType: 'application/json; charset=utf-8',
        success: function(response) {
            console.log(response.result.id);
            // API 응답을 성공적으로 받았을 때 실행되는 코드
            // 응답 데이터는 'response' 매개변수로 전달됨
            if (response.resultCode === 'SUCCESS') {
                // 삭제 성공 시 화면에서 그룹 삭제
                alert('생성 완료');
                location.reload();
            } else {
                alert('생성 실패');
            }
        },
        error: function(error) {
            console.log('API Error:', error);
            alert('그룹 생성에 실패하였습니다.');
        }
    });
}