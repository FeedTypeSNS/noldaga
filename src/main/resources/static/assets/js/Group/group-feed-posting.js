let imageList = [];
let post = {
    getGroup:function () {
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const groupId = urlParams.get('id');
        const self = this;
        $.ajax({
            type: "GET",
            url: "/api/group/"+ groupId,
            dataType: "json"
        }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
            console.log(resp.result);
            self.init(resp.result);
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },

    init:function(group) {
        const self = this;
        $("#posting-button").on("click",()=>{
            self.posting(group);
        }); //on(1,2) 1에는 어떤 이벤트인지 적어주고 2에 그 이벤트시 어떤일이 일어날지 적는다

        $("#enter-content-input").on("keydown",(e)=>{
            if (e.keyCode === 13) {
                self.enterkey(group);
            }
        });

        $("#uploadFile").on("change",(e)=>{
            this.image_save();
        });
    },

    posting:function(group){
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const group_id = urlParams.get('id');
        let range
        if(group.open == 1) {
            range = 0;
        } else if(group.open == 0) {
            range = 1;
        }
        let data={
            title: $("#title").val(),
            content: $("#content").val(),
            range: range,
            groupId: group_id,
            urls : imageList
        };

        if(data.title.length>20) {
            alert('제목이 너무 길어요. 20자 이하로 입력해주세요');
            return;
        }
        if(data.title.length==0){
            alert('제목을 입력해주세요');
            return;
        }
        if(data.content.length==0){
            alert('내용을 입력해주세요');
            return;
        }

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

    /*posting_demo:function(){
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
    },*/

    image_save:function(){
        const formImageData = new FormData();

        var images = $("#uploadFile")[0];
        for(let i=0; i<images.files.length; i++) {
            formImageData.append("images", images.files[i]);
        }

        $.ajax({
            type:'post',
            enctype:"multipart/form-data",  // 업로드를 위한 필수 파라미터
            url: '/api/feed/imgs',
            data: formImageData,
            processData: false,
            contentType: false
        }).done((resp)=>{
            imageList = resp.result;
            this.showUploadFile(resp.result);
        }).fail(function(error){
            alert('업로드실패');
            alert(JSON.stringify(error));
        });
    },

    enterkey:function(group){
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const group_id = urlParams.get('id');
        let range
        if(group.open == 1) {
            range = 0;
        } else if(group.open == 0) {
            range = 1;
        }
        let data={
            content: $("#enter-content-input").val(),
            title: $("#enter-content-input").val().substring(0, 10),
            range: range,
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

    },

    showUploadFile:function(images) {
        for (let i = 0; i < images.length; i++) {
            const uploadResult = document.querySelector(".uploadResult");
            const str = `<div class="card col-4">
            <div class="card-header d-flex justify-content-center">
                <button type="button" onclick="remove.removeFile('${images[i]}',this)">삭제</button>
            </div>
            <div class="card-body">
                 <img src=${images[i]}>
            </div>
        </div><!-- card -->`

            uploadResult.innerHTML += str
        }
    }

};

post.getGroup();


let remove = {
    removeFile:function(url,obj){
        const targetDiv = obj.closest(".card");

        //배열에서 삭제
        var index = imageList.indexOf(url);
        imageList.splice(index, 1);
        //서버에서 삭제
        this.removeFileFromServer(url);
        //모달창에서 삭제
        targetDiv.remove();
    },

    removeFileFromServer:function(url){
        $.ajax({
            type: "DELETE",
            url: "/api/feed/img?url="+url,
            contentType: "application/json; charset=utf-8"
        }).done(function(resp){
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    }
}

