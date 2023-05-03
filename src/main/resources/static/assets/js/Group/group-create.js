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
            url: "/api/group",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(response) {
                $.ajax({
                    url: "/api/group/member/" + response.result.id,
                    method: "POST",
                    dataType: "json",
                    success: function(resp) {
                        // API 응답을 성공적으로 받았을 때 실행되는 코드
                        // 응답 데이터는 'response' 매개변수로 전달됨
                        if (resp.resultCode === 'SUCCESS') {
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
                })
            },
            error: function(xhr, status, error) {
                console.log('API Error:', error);
                alert('그룹 생성에 실패하였습니다.');
            }
        });

    }



};

post.init();

