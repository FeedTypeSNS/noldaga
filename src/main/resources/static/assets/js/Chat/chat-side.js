let side = {
    init:function (){
        $("#myFollowList").on("click",()=>{
           this.getMyFollowList();
        });
        $("#myFollowList2").on("click",()=>{
            this.getMyFollowList();
        }); //팔로잉 리스트 반환

    },

    getMyFollowList:function (){
        //const queryString = window.location.search;
        $.ajax({
            type: "GET",
            url:"/api/following/my",
            contentType:"application/json; charset=utf-8"
        }).done(function (resp){
            alert(JSON.stringify(resp));
            followlist(resp);
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    }

}



function followlist(data){
    $(".followlist *").remove(); //기존 내용 삭제 후 append
    $(".followlist").remove();
    for(let i=0; i<data.result.length; i++) {
        let followlistBox = document.querySelector('#sendCheckList');
        let databox = document.createElement("div");
        databox.className = "followlist";
        databox.innerHTML = getFollowlistContent(data.result[i]);
        followlistBox.append(databox);
    }
}
function getFollowlistContent(data) {
    return `                <li  style="list-style: none; padding-left: 0px;">
                                    <div class="d-flex">
                                        <div class="avatar">
                                            <img style="width:48px; height:48px;" class="avatar-img rounded-circle" src="${data.profileImageUrl}" alt=""> 
                                        </div>
                                        <div class="flex-grow-1 d-block">
                                            <input style="margin: 0.4rem;zoom: 1.5; float: right" class="sendUserCheck" type="checkbox" name="joinUserList" value="${data.username}" onclick="getJoinName()"/>

                                            <h6 style="padding-left: 5px" class="mb-0 mt-1">\"${data.username}\" </h6>
                                            <div class="small text-secondary">&nbsp;&nbsp;${data.nickname}</div>
                                            <br>
                                        </div>
                                    </div>
                            </li>`;

    /*`<li style="list-style: none; padding-left: 0px">${data.id} 번째  ${data.username} 회원
    <input style="margin: 0.4rem;" id="sendUserCheck" type="checkbox" name="joinUserList" value="${data.username}"/></li>`;*/

}

side.init(); //초기화
