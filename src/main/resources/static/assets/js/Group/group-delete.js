$(document).ready(function() {
    // 삭제 버튼 클릭 이벤트 처리
    $('#delete-group-button').click(function(e) {
        e.preventDefault(); // 기본 동작 막기

        // 경고창 표시
        if (confirm('정말로 그룹을 삭제하시겠습니까?')) {
            // AJAX 요청 보내기
            var group_id = $('#group-id').val();
            $.ajax({
                type: 'DELETE',
                url: '/api/groups/' + group_id,
                success: function(response) {
                    // 성공한 경우에 처리할 내용
                    alert('그룹이 삭제되었습니다.');
                    window.location.href = '/groups'; // 그룹 목록 페이지로 이동
                },
                error: function(xhr, status, error) {
                    // 실패한 경우에 처리할 내용
                    alert('그룹 삭제에 실패했습니다: ' + error);
                }
            });
        }
    });
});

