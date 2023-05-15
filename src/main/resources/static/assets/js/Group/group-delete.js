function deleteGroup(groupId) {
    if (confirm('정말로 그룹을 삭제하시겠습니까?')) {
        $.ajax({
            url: "/api/group/" + groupId,
            method: "DELETE",
            dataType: "json",
            success: function(response) {
                // API 응답을 성공적으로 받았을 때 실행되는 코드
                // 응답 데이터는 'response' 매개변수로 전달됨
                if (response.resultCode === 'SUCCESS') {
                    // 삭제 성공 시 화면에서 그룹 삭제
                    alert('삭제 완료');
                    location.reload();
                } else {
                    alert('삭제 실패');
                }
            },
            error: function(xhr, status, error) {
                console.log('API Error:', error);
                alert('그룹 삭제에 실패하였습니다.');
            }
        });
    }
}