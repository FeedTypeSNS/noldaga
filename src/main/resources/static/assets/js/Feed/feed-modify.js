let post = {

  init:function() {

    //피드 수정 모달창 - 수정버튼
    $("#modify-button").on("click",()=>{
      this.modify();
    });

    //피드 삭제 모달창 - 삭제버튼
    $("#delete-button").on("click",()=>{
      this.delete();
    });

    //????
    $("#modify").on("click",()=>{
      this.show();
    });

    //댓글 수정 모달창 - 수정버튼
    $("#comment-modify-button").on("click",()=>{
      this.modifyComment();
    });

    //댓글 삭제 모달창 - 삭제버튼
    $("#comment-delete-button").on("click",()=>{
      this.deleteComment();
    });

  },

  modify:function(){
    const queryString = window.location.search;
    const url = window.location.href;

    let data={
      title: $("#modify_title").val(),
      content: $("#modify_content").val(),
      range: $("#modify_open_range").val(),
      groupId: $("#modify_group_id").val(),
      urls : imageList
    };

    $.ajax({
      type: "PUT",
      url: "/api/feed"+queryString,
      data: JSON.stringify(data),
      contentType: "application/json; charset=utf-8",
      dataType: "json"
    }).done(function(resp){
      alert(JSON.stringify(data.urls));
      alert('수정 완료');
      location.href = url;
    }).fail(function(error){
      alert('수정 실패');
      alert(JSON.stringify(error));
    });

  },

  delete:function(){

    const queryString = window.location.search;

    $.ajax({
      type: "DELETE",
      url: "/api/feed" + queryString,
      contentType: "application/json; charset=utf-8"
    }).done(function(resp){
      alert('삭제 완료');
      location.href = "/nol";
    }).fail(function(error){
      alert('삭제 실패');
      alert(JSON.stringify(error));
      location.href = "/nol";
    });

  },

//  show:function() {
//
//    const queryString = window.location.search;
//
//    $.ajax({
//      type: "GET",
//      url: "/api/feed"+ queryString,
//      dataType: "json"
//    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
//      setModifyModal(resp);
//    }).fail(function(error){
//      alert(JSON.stringify(error));
//    });
//
//    function setModifyModal(data){
//      $('#title').val(data.result.title);
//      $('#content').val(data.result.content);
//      $('#id').val(data.result.id);
//    }
//  },

  modifyComment:function(){

    const queryString = window.location.search;
    let data={
      id: $("#commentId").val(),
      content: $("#commentContent").val()
    };

    $.ajax({
      type: "PUT",
      url: "/api/comment/"+data.id,
      data: JSON.stringify(data),
      contentType: "application/json; charset=utf-8",
      dataType: "json"
    }).done(function(resp){
      alert('수정 완료');
      location.href = "/nol/feed"+queryString;
    }).fail(function(error){
      alert('수정 실패');
      alert(JSON.stringify(error));
    });

  },

  deleteComment:function(){

    const queryString = window.location.search;

    let data={
      id: $("#deleteId").val()
    };

    $.ajax({
      type: "DELETE",
      url: "/api/comment/" + data.id,
      contentType: "application/json; charset=utf-8"
    }).done(function(resp){
      alert('삭제 완료');
      location.href = "/nol/feed"+queryString;
    }).fail(function(error){
      alert('삭제 실패');
      alert(JSON.stringify(error));
      location.href = "/nol/feed"+queryString;
    });

  }

};

post.init();

