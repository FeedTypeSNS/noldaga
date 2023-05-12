let post = {
    init:function() {
        $("#posting-button").on("click",()=>{
            this.posting();
        }); //on(1,2) 1에는 어떤 이벤트인지 적어주고 2에 그 이벤트시 어떤일이 일어날지 적는다

        $("#enter-content-input").on("keydown",(e)=>{
            if (e.keyCode === 13) {
                this.enterkey();
            }
        });
    },

    posting:function(){
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const group_id = urlParams.get('id');
        let data={
            title: $("#title").val(),
            content: $("#content").val(),
            range: $("#open_range").val(),
            groupId: group_id
        };

        if(data.title.length>20) alert('제목이 너무 길어요. 20자 이하로 입력해주세요');
        if(data.title.length<=0) alert('제목을 입력해주세요');
        if(data.content.length<=0) alert('내용을 입력해주세요');

        $.ajax({
            type: "POST",
            url: "/api/feed",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp){
            alert('포스팅 완료');
            location.reload();
        }).fail(function(error){
            alert('포스팅 실패');
            alert(JSON.stringify(error));
        });

    },

    posting_demo:function(){
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const group_id = urlParams.get('id');
        let data={
            title: $("#titledemo").val(),
            content: $("#contentdemo").val(),
            range: $("#open_range_demo").val(),
            groupId: group_id
        };

        $.ajax({
            type: "POST",
            url: "/api/feed",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp){
            this.image_save(resp);
            location.reload();
        }).fail(function(error){
            alert('포스팅 실패');
            alert(JSON.stringify(error));
        });
    },

    image_save:function(data){
        alert("여기 작동 확인");
        var myDropzone = Dropzone.forElement("#images");
        myDropzone.processQueue();
        myDropzone.on("success", function(file, response) {
            var url = response.url;
            var fileName = response.fileName;
        });
    },

    enterkey:function(){
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const group_id = urlParams.get('id');
        let data={
            content: $("#enter-content-input").val(),
            title: $("#enter-content-input").val().substring(0, 10),
            range: "0",
            groupId: group_id
        };

        $.ajax({
            type: "POST",
            url: "/api/feed",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp){
            alert('포스팅 완료');
            location.reload();
        }).fail(function(error){
            alert('포스팅 실패');
            alert(JSON.stringify(error));
        });

    }

};

post.init();

