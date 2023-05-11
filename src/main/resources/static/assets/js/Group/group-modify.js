function updateGroup(groupId) {
    var formData = new FormData();
    formData.append('name', $('#name').val());
    var profileImage = $('#fileInput')[0].files[0];
    if (profileImage) {
        formData.append('profileUrl', profileImage);
    } else {
        formData.append('profileUrl', "");
    }
    formData.append('open', $('input[name="open"]:checked').val());
    formData.append('pw', $('#pw').val());
    formData.append('intro', $('#intro').val());

    $.ajax({
        type: 'PUT',
        url: '/api/group/'+groupId,
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            // API 응답을 성공적으로 받았을 때 실행되는 코드
            // 응답 데이터는 'response' 매개변수로 전달됨
            if (response.resultCode === 'SUCCESS') {
                // 삭제 성공 시 화면에서 그룹 삭제
                alert('수정 완료');
                location.reload();
            } else {
                alert('수정 실패');
            }
        },
        error: function(xhr, status, error) {
            console.log('API Error:', error);
            alert('그룹 수정에 실패하였습니다.');
        }
    });
    /*
    let data={
        name: $("#name").val(),
        open: $("input[name='open']:checked").val(),
        pw: $("#pw").val(),
        profile_url: $("#profile_url").val(),
        intro: $("#intro").val()
    };
    $.ajax({
        type: "PUT",
        url: "/api/group/" + groupId,
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(response) {
            // API 응답을 성공적으로 받았을 때 실행되는 코드
            // 응답 데이터는 'response' 매개변수로 전달됨
            if (response.resultCode === 'SUCCESS') {
                // 삭제 성공 시 화면에서 그룹 삭제
                alert('수정 완료');
                location.reload();
            } else {
                alert('수정 실패');
            }
        },
        error: function(xhr, status, error) {
            console.log('API Error:', error);
            alert('그룹 수정에 실패하였습니다.');
        }
    });*/

}
