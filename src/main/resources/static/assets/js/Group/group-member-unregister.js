function unregisterGroup(groupId) {
    if (confirm('정말로 그룹을 탈퇴하시겠습니까?')) {
        $.ajax({
            url: "/api/group/member/" + groupId,
            method: "DELETE",
            dataType: "json",
            success: function(response) {
                // API 응답을 성공적으로 받았을 때 실행되는 코드
                // 응답 데이터는 'response' 매개변수로 전달됨
                if (response.resultCode === 'SUCCESS') {
                    // 삭제 성공 시 화면에서 그룹 삭제
                    alert('탈퇴 완료');
                    location.href = "/groups";
                } else {
                    alert('탈퇴 실패');
                }
            },
            error: function(xhr, status, error) {
                console.log('API Error:', error);
                alert('그룹 탈퇴에 실패하였습니다.');
            }
        });
    }
}