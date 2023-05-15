function createGroup() {
    var formData = new FormData();
    formData.append('name', $('#name').val());
    var profileImage = $('#fileInput')[0].files[0];
    if (profileImage) {
        formData.append('profileUrl', profileImage);
    } else {
        formData.append('profileUrl', "");
    }
    formData.append('open', $('input[name="open"]:checked').val());
    formData.append('pw', $('#group-pw').val());
    formData.append('intro', $('#intro').val());

    $.ajax({
        type: 'POST',
        url: '/api/group',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            if (response.resultCode === 'SUCCESS') {
                alert('그룹 생성이 완료되었습니다.');
                location.reload();
            } else {
                alert('그룹 생성에 실패했습니다.');
            }
        },
        error: function(error) {
            console.log('API Error:', error);
            alert('그룹 생성에 실패하였습니다.');
        }
    });
}