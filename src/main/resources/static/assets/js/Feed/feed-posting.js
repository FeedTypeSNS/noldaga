let post = {
  init:function() {
    $("#posting-button").on("click",()=>{
      this.posting();
    }); //on(1,2) 1에는 어떤 이벤트인지 적어주고 2에 그 이벤트시 어떤일이 일어날지 적는다

    $("#posting-button-demo").on("click",()=>{
      this.posting_demo();
    });
  },

  posting:function(){

    let data={
      title: $("#title").val(),
      content: $("#content").val(),
      range: $("#open_range").val(),
      groupId: $("#group_id").val()
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
      location.href = "/";
    }).fail(function(error){
      alert('포스팅 실패');
      alert(JSON.stringify(error));
    });

  },

  posting_demo:function(){
    let data={
      title: $("#titledemo").val(),
      content: $("#contentdemo").val(),
      range: $("#open_range_demo").val(),
      groupId: $("#group_id_demo").val()
    };

    $.ajax({
      type: "POST",
      url: "/api/feed",
      data: JSON.stringify(data),
      contentType: "application/json; charset=utf-8",
      dataType: "json"
    }).done(function(resp){
      this.image_save(resp);
      location.href = "/";
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
  }


};

post.init();

