





function addMyHashTag(){
    const hashTags = document.getElementById("tag-content").value;


    const json = JSON.stringify({
        hashTags: hashTags,
    })

    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/users/me/hashtag',true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onreadystatechange=function(){
        if (this.readyState === XMLHttpRequest.DONE) {
            const response = JSON.parse(this.responseText);
            if(response.resultCode==='SUCCESS'){
                alert('관심사가 등록 되었습니다.');
            }
            else{
                alert('실패');
            }

        }
    }
    xhr.send(json);
}

