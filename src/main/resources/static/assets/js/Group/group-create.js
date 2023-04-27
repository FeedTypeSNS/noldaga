let post = {
    init:function() {
        $("#create-button").on("click",()=>{
            this.creating();
        });
    },

    creating:function(){

        let data={
            name: $("#name").val(),
            open: $("input[name='open']:checked").val(),
            pw: $("#pw").val(),
            profile_url: $("#profile_url").val(),
            intro: $("#intro").val()
        };

        $.ajax({
            type: "POST",
            url: "/api/groups",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(response) {
                // API 응답을 성공적으로 받았을 때 실행되는 코드
                // 응답 데이터는 'response' 매개변수로 전달됨
                if (response.resultCode === 'SUCCESS') {
                    // 삭제 성공 시 화면에서 그룹 삭제
                    alert('생성 완료');
                    location.href = "/groups";
                } else {
                    alert('생성 실패');
                }
            },
            error: function(xhr, status, error) {
                console.log('API Error:', error);
                alert('그룹 삭제에 실패하였습니다.');
            }
        });

    }



};

post.init();

