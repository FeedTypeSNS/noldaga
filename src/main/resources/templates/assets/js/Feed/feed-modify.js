let post = {

  init:function() {

    $("#modify-button").on("click",()=>{
      this.modify();
    });

    $("#delete-button").on("click",()=>{
      this.delete();
    });

    $("#modify").on("click",()=>{
      this.show();
    });
  },

  modify:function(){
    const queryString = window.location.search;

    let data={
      title: $("#title").val(),
      content: $("#content").val(),
      range: $("#open_range").val(),
      groupId: $("#group_id").val()
    };

    alert(JSON.stringify(data));

    $.ajax({
      type: "PUT",
      url: "/api/feed"+queryString,
      data: JSON.stringify(data),
      contentType: "application/json; charset=utf-8",
      dataType: "json"
    }).done(function(resp){
      alert('수정 완료');
      location.href = "/";
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
      location.href = "/";
    }).fail(function(error){
      alert('삭제 실패');
      alert(JSON.stringify(error));
      location.href = "/";
    });

  },

  show:function() {

    const queryString = window.location.search;

    $.ajax({
      type: "GET",
      url: "/api/feed"+ queryString,
      dataType: "json"
    }).done(function(resp){//이렇게 받으면 이미 알아서 js객체로 바꿔줬기 때문에 JSON.parse(resp)하면 안됨
      setModifyModal(resp);
    }).fail(function(error){
      alert(JSON.stringify(error));
    });

    function setModifyModal(data){
      $('#title').val(data.title);
      $('#content').val(data.content);
      $('#id').val(data.id);
    }
  }

};

post.init();

